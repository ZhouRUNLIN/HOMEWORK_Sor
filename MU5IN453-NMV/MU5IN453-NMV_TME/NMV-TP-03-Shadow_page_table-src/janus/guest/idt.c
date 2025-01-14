#include <idt.h>
#include <types.h>
#include <printk.h>
#include <x86.h>


#define PIC_MASTER_COMMAND_PORT    0x20
#define PIC_MASTER_DATA_PORT       0x21
#define PIC_SLAVE_COMMAND_PORT     0xa0
#define PIC_SLAVE_DATA_PORT        0xa1
#define PIC_DATA_DISABLE           0xff


#define INTERRUPT_GATE_TYPE        0xee00
#define TRAP_GATE_TYPE             0xef00


extern vaddr_t trap_vector[];

static struct {
	uint16_t off0;
	uint16_t sel;
	uint16_t flags;
	uint16_t off1;
	uint32_t off2;
	uint32_t _ign0;
} __attribute__((packed)) idt64[INTERRUPT_VECTOR_SIZE];

struct {
	uint16_t limit;
	uint64_t base;
} __attribute__((packed)) idtr64;


interrupt_handler_t  interrupt_vector[INTERRUPT_VECTOR_SIZE];

static void default_interrupt(struct interrupt_context *ctx)
{
	uint64_t cr2;

	printk("Interrupt %lu (err = %#lx)\n", ctx->itnum, ctx->errcode);

	if (ctx->itnum == INT_PF) {
		cr2 = store_cr2();
		printk("  CR2 = %p\n", cr2);
	}

	asm volatile ("hlt");
}

void trap(struct interrupt_context *ctx)
{
	interrupt_handler_t handler = interrupt_vector[ctx->itnum];

	if (handler == NULL)
		default_interrupt(ctx);
	else
		handler(ctx);
}


void disable_pic(void)
{
	out8(PIC_MASTER_DATA_PORT, PIC_DATA_DISABLE);
	out8(PIC_SLAVE_DATA_PORT, PIC_DATA_DISABLE);
}

void setup_interrupts(void)
{
	size_t i;

	idtr64.limit = sizeof (idt64) - 1;
	idtr64.base = (uint64_t) &idt64;

	for (i = 0; i < INTERRUPT_VECTOR_SIZE; i++) {
		idt64[i].off0 = ((trap_vector[i] >> 00) &     0xffff);
		idt64[i].off1 = ((trap_vector[i] >> 16) &     0xffff);
		idt64[i].off2 = ((trap_vector[i] >> 32) & 0xffffffff);
		idt64[i].sel = KERNEL_CODE_SELECTOR;

		if (i == INT_USER_SYSCALL) {
			idt64[i].flags = TRAP_GATE_TYPE;
		} else {
			idt64[i].flags = INTERRUPT_GATE_TYPE;
		}
	}

	asm volatile ("lidt idtr64");
}
