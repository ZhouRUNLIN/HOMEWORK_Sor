#ifndef _INCLUDE_MULTIBOOT2_H_
#define _INCLUDE_MULTIBOOT2_H_


#include <stdint.h>


struct multiboot2_load_info
{
	uint32_t  header_off;
	uint32_t  header_addr;
	uint32_t  load_addr;
	uint32_t  load_end_addr;
	uint32_t  bss_end_addr;
	uint32_t  entry_addr;
};


int parse_multiboot2(struct multiboot2_load_info *info, const char *path);

int load_multiboot2(const struct multiboot2_load_info *info, const char *path);


#endif
