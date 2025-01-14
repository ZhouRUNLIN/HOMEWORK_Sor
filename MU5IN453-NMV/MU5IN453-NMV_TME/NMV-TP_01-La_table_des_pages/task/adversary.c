#include <string.h>
#include <syscall.h>


extern char __task_start;
extern char __task_end;
extern char __bss_end;


char zero_variable = 0;
char zero_zone[8000];

void entry(void)
{
	size_t i;
	char *addr = (char *) 0x1fffff3000;

	syscall_print("  ==> Adversary Task\n");

	for (i = 0; i < 0x1000; i++)
		addr[i] = i & 0xff;

	syscall_munmap((vaddr_t) addr);

	for (i = 0; i < 0x1000; i++)
		if (addr[i] != 0) {
			syscall_print("  --> Adversary result: failure\n");
			syscall_exit();
		}

	syscall_yield();
	syscall_print("  --> Adversary result: success\n");
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
