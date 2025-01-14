#include <string.h>
#include <syscall.h>


#define HASH_ROUND       512
#define BUFFER_LENGTH    16


extern char __task_start;
extern char __task_end;
extern char __bss_end;


static char expected[] = {126, 64, 218, 208, 114, 16, 110, 33, 198, 241, 22,
			  210, 117, 211, 243, 161};

static void hash_n(char *out, size_t n)
{
	char buffer[BUFFER_LENGTH];
	size_t i;

	if (n == 0)
		return;

	for (i = 1; i < BUFFER_LENGTH; i++)
		buffer[i] = out[i - 1];
	buffer[0] = out[i - 1];

	for (i = 0; i < BUFFER_LENGTH; i++)
		buffer[i] += buffer[(i + BUFFER_LENGTH / 2) % BUFFER_LENGTH];

	syscall_yield();
	hash_n(buffer, n - 1);

 	for (i = 0; i < BUFFER_LENGTH; i++)
		buffer[i] += buffer[(i + BUFFER_LENGTH / 4) % BUFFER_LENGTH];

	for (i = 0; i < BUFFER_LENGTH; i++)
		out[i] = buffer[i];
}

static void hash(char *out, const char *in, size_t len)
{
	size_t i;

	for (i = 0; i < len; i++)
		out[i] = in[i % len];

	hash_n(out, HASH_ROUND);
}


void entry(void)
{
	char output[BUFFER_LENGTH];
	size_t i;

	syscall_print("  ==> Hash Task\n");

	hash(output, "Keep It Simple and Stupid", 25);

	if (output[0] != 126) {
		syscall_print("  --> Hash result: failure\n");
	} else {
		for (i = 0; i < BUFFER_LENGTH; i++)
			if (output[i] != expected[i]) {
				syscall_print("  --> Hash result: failure\n");
				break;
			}
		if (i == BUFFER_LENGTH)
			syscall_print("  --> Hash result: success\n");
	}

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
