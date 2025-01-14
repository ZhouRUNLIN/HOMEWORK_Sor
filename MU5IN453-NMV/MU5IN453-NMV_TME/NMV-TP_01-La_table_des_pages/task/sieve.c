#include <string.h>
#include <syscall.h>


#define PAGE_SIZE    4096
#define MAX_SEARCH   4096


extern char __task_start;
extern char __task_end;
extern char __bss_end;


vaddr_t heap = (vaddr_t) &__bss_end;


static unsigned long *number_array(unsigned long max, int init)
{
	size_t i, n = max * 8;
	unsigned long *ret = (unsigned long *) heap;

	if (n & (PAGE_SIZE - 1))
		n = (n + PAGE_SIZE) & ~(PAGE_SIZE - 1);

	while (n > 0) {
		syscall_mmap(heap);
		heap += PAGE_SIZE;
		n -= PAGE_SIZE;
	}

	if (init == 0)
		return ret;

	for (i = 0; i < max; i++)
		ret[i] = i;

	return ret;
}

static void realloc_array(unsigned long *arr, unsigned long max)
{
	size_t n = max * 8;
	vaddr_t addr = (vaddr_t) arr;

	if (n & (PAGE_SIZE - 1))
		n = (n + PAGE_SIZE) & ~(PAGE_SIZE - 1);

	while (n > 0) {
		syscall_munmap(addr);
		syscall_mmap(addr);
		addr += PAGE_SIZE;
		n -= PAGE_SIZE;
	}
}

static size_t filter(unsigned long *to, const unsigned long *from,
		     size_t index, size_t len)
{
	size_t i, j, n;

	for (i = 0; i < index; i++)
		to[i] = from[i];

	n = index;

	for (i = index; i < len; i++) {
		for (j = 2; j < index; j++)
			if ((from[i] % from[j]) == 0)
				break;
		if (j >= index)
			to[n++] = from[i];
	}

	return n;
}


void entry(void)
{
	unsigned long *flip, *flop, *temp;
	unsigned long index = 0, len = MAX_SEARCH;
	size_t i, num = 0;

	syscall_print("  ==> Sieve Task\n");

	flip = number_array(MAX_SEARCH, 1);
	flop = number_array(MAX_SEARCH, 0);

	while (index < len) {
		len = filter(flop, flip, index, len);
		realloc_array(flip, MAX_SEARCH);

		temp = flip;
		flip = flop;
		flop = temp;

		index++;

		if ((index % 4) == 0)
			syscall_yield();
	}

	for (i = 0; i < MAX_SEARCH; i++)
		if (flip[i] != 0)
			num++;

	if (num == 565)
		syscall_print("  --> Sieve result: success\n");
	else
		syscall_print("  --> Sieve result: failure\n");

	syscall_exit();
}


struct task_header header __attribute__((section(".header"))) = {
	.magic = TASK_HEADER_MAGIC,
	.load_addr = (vaddr_t) &__task_start,
	.load_end_addr = (vaddr_t) &__task_end,
	.bss_end_addr = (vaddr_t) &__bss_end,
	.header_addr = (vaddr_t) &header,
	.entry_addr = (vaddr_t) &entry
};
