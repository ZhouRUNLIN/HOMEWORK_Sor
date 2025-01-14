#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>

#include "intercept.h"
#include "memory.h"
#include "multiboot2.h"
#include "shadow.h"
#include "state.h"


#define PHYSICAL_MEMORY   (3ul << 30)


static const char *progname = "janus";

static void error(const char *format, ...)
{
	va_list ap;

	fprintf(stderr, "Error: ");

	va_start(ap, format);
	vfprintf(stderr, format, ap);
	va_end(ap);

	fprintf(stderr, "\nUsage: %s <kernel>\n", progname);
	exit(EXIT_FAILURE);
}

int main(int argc, const char **argv)
{
	struct multiboot2_load_info info;
	void *entry;

	progname = argv[0];

	if (argc != 2)
		error("missing kernel operand");

	allocate_physical_memory(PHYSICAL_MEMORY);
	set_flat_mapping(PHYSICAL_MEMORY);

 	uint16_t val = 'A';
	trap_write((paddr_t)0xb8006, sizeof(uint16_t), val);


	if (parse_multiboot2(&info, argv[1]) != 0)
		error("invalid kernel operand");

	if (load_multiboot2(&info, argv[1]) != 0)
		error("unable to load kernel");

	setup_state();
	setup_interception();

	entry = (void *) ((uint64_t) info.entry_addr);
	protect_low_memory();
	goto *entry;

	/* dead code */
	return EXIT_SUCCESS;
}
