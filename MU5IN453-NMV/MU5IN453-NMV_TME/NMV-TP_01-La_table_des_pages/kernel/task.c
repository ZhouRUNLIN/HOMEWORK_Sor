#include <memory.h>
#include <printk.h>
#include <string.h>
#include <syscall.h>
#include <task.h>
#include <types.h>
#include <x86.h>


#define MB2_TAG_END       0
#define MB2_TAG_CMDLINE   1
#define MB2_TAG_NAME      2
#define MB2_TAG_MODULE    3
#define MB2_TAG_MEMORY    4
#define MB2_TAG_BIOS      5
#define MB2_TAG_MMAP      6
#define MB2_TAG_VBE       7
#define MB2_TAG_FRAMEBUF  8
#define MB2_TAG_ELF       9
#define MB2_TAG_APM       10

#define TASK_FIFO_LEN     32


static struct interrupt_context save;         /* kernel before running tasks */
static struct task fifo[TASK_FIFO_LEN];           /* fifo of available tasks */
static size_t fifo_size = 0;                   /* amount of task in the fifo */
static size_t fifo_run = 0;                      /* current task in the fifo */


struct mb2_info
{
	uint32_t  total_size;
	uint32_t  reserved;
} __attribute__((packed));

struct mb2_tag
{
	uint32_t  type;
	uint32_t  size;
} __attribute__((packed));

struct mb2_tag_module
{
	uint32_t  type;
	uint32_t  size;
	uint32_t  mod_start;
	uint32_t  mod_end;
	uint8_t   string[];
} __attribute__((packed));


struct task_state_segment tss __attribute__((aligned(0x1000)));


/*
 * Symbol defined in entry.S
 * Points to a slot of the current GDT.
 */
extern uint64_t tss64[2];

/* Util functions to set up the TSS descriptor. */
#define TSS_ADDR_LOW(addr)			\
	( ((((addr) >>  0) & 0x0000ffff) << 16)	\
	| ((((addr) >> 16) & 0x000000ff) << 32)	\
	| ((((addr) >> 16) & 0x0000ff00) << 48)	\
	)

#define TSS_ADDR_HIGH(addr)			\
	( ((((addr) >> 32) & 0xffffffff) <<  0)	\
	)

#define TSS_SIZE(size)					\
	( (((((size) - 1) >>  0) & 0xffff) <<  0)	\
	| (((((size) - 1) >> 16) & 0x000f) << 48)	\
	)

/* Util functions to set up the TSS. */
#define OFFSETOF(type, field)   ((size_t) &(((type *) 0)->field))


void setup_tss(void)
{
	paddr_t tss_addr = (paddr_t) &tss;

	memset(&tss, 0, sizeof (tss));
	tss.rsp0 = store_rsp();
	tss.iopb_off = OFFSETOF(struct task_state_segment, iopb);
	tss.iopb = 0xffffffffffffffff;

	tss64[0] |= TSS_ADDR_LOW(tss_addr);
	tss64[0] |= TSS_SIZE(sizeof (tss));
	tss64[1] |= TSS_ADDR_HIGH(tss_addr);

	load_tr(TSS_SELECTOR);
}


static void parse_task(const struct mb2_tag_module *tag)
{
	const uint64_t *ptr = (const uint64_t *) ((uint64_t) tag->mod_start);
	const uint64_t *end = (const uint64_t *) ((uint64_t) tag->mod_end);
	const struct task_header *header;
	struct task *task;
	size_t pvdiff;

	if (fifo_size == TASK_FIFO_LEN)
		return;

	while (ptr < end) {
		if (*ptr == TASK_HEADER_MAGIC)
			break;
		ptr++;
	}

	if (ptr == end)
		return;

	task = fifo + fifo_size;
	memset(task, 0, sizeof (*task));
	fifo_size++;

	header = (const struct task_header *) ptr;
	pvdiff = header->header_addr - ((paddr_t) ptr);
	task->load_paddr = header->load_addr - pvdiff;
	task->load_end_paddr = header->load_end_addr - pvdiff;
	task->load_vaddr = header->load_addr;
	task->bss_end_vaddr = header->bss_end_addr;

	task->context.rip = header->entry_addr;
	task->context.cs = USER_CODE_SELECTOR | 0x3;
	task->context.ss = USER_DATA_SELECTOR | 0x3;
	task->context.rsp = 0x2000000000;
	task->context.rflags = RFLAGS_IF;

	load_task(task);
}

static void syscall_handler(struct interrupt_context *ctx)
{
	uint64_t arg0 = ctx->rsi;

	switch (ctx->rdi) {
	case SYSCALL_PRINT:
		printk("%s", arg0);
		break;
	case SYSCALL_PRINTNUM:
		printk("%lu", arg0);
		break;
	case SYSCALL_MMAP:
		mmap(fifo + fifo_run, arg0);
		break;
	case SYSCALL_MUNMAP:
		munmap(fifo + fifo_run, arg0);
		break;
	case SYSCALL_YIELD:
		next_task(ctx);
		break;
	case SYSCALL_EXIT:
		exit_task(ctx);
		break;
	case SYSCALL_FORK:
		fork_task(ctx);
		break;
	}
}

static void enter_handler(struct interrupt_context *ctx)
{
	struct task *task = (struct task *) ctx->rdi;

	save = *ctx;
	set_task(task);

	*ctx = task->context;
}

static void enter_task(struct task *task)
{
	asm volatile ("int %0" : : "i" (INT_USER_ENTER_TASKS), "D" (task));
}


void load_tasks(const void *mb2)
{
	const struct mb2_info *info = (const struct mb2_info *) mb2;
	const struct mb2_tag *tag;
	vaddr_t ptr = (vaddr_t) mb2;
	vaddr_t end = ptr + info->total_size;

	ptr += (sizeof (*info) + 7) & ~0x7;
	while (ptr < end) {
		tag = (const struct mb2_tag *) ptr;
		if (tag->type == MB2_TAG_MODULE)
			parse_task(((const struct mb2_tag_module *) tag));
		ptr = (ptr + tag->size + 7) & ~0x7;
	}

	interrupt_vector[INT_USER_SYSCALL] = syscall_handler;
	interrupt_vector[INT_USER_ENTER_TASKS] = enter_handler;
}


struct task *current(void)
{
	if (fifo_run >= fifo_size)
		return NULL;
	return fifo + fifo_run;
}

void next_task(struct interrupt_context *ctx)
{
	fifo[fifo_run].context = *ctx;

	fifo_run++;
	if (fifo_run >= fifo_size)
		fifo_run = 0;

	set_task(fifo + fifo_run);
	*ctx = fifo[fifo_run].context;
}

void exit_task(struct interrupt_context *ctx)
{
	if (fifo_run == (fifo_size - 1)) {
		fifo_size--;
		fifo_run = 0;
	} else {
		fifo_size--;
		fifo[fifo_run] = fifo[fifo_size];
	}

	if (fifo_size == 0) {
		*ctx = save;
	} else {
		set_task(fifo + fifo_run);
		*ctx = fifo[fifo_run].context;
	}
}

void fork_task(struct interrupt_context *ctx)
{
	struct task *task;
	uint64_t ret = -1;

	if (fifo_size == TASK_FIFO_LEN)
		goto out;

	task = fifo + fifo_size;
	fifo_size++;

	*task = *current();
	task->context = *ctx;
	task->context.rax = 1;
	duplicate_task(task);

	ret = 0;
 out:
	ctx->rax = ret;
}

void run_tasks(void)
{
	if (fifo_size == 0)
		return;

	fifo_run++;
	if (fifo_run >= fifo_size)
		fifo_run = 0;

	enter_task(fifo + fifo_run);
}
