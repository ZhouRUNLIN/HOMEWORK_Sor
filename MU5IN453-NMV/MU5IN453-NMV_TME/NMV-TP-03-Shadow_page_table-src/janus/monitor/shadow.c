#include "shadow.h"

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/mman.h>

#include "memory.h"
#include "state.h"
#include "vector.h"
#define GUEST_MIN_LOW 0x100000
#define GUEST_MAX_LOW 0x100000000

#define GUEST_MIN_HIGHT 0x100000000
#define GUEST_MAX_HIGHT 0x700000000000
void set_flat_mapping(size_t ram)
{
	if(ram < GUEST_MAX_LOW - GUEST_MIN_LOW){
		map_page(GUEST_MIN_LOW,GUEST_MIN_LOW,ram);
	}else{
		map_page(GUEST_MIN_LOW, GUEST_MIN_LOW, GUEST_MAX_LOW - GUEST_MIN_LOW);
		map_page(GUEST_MIN_HIGHT,GUEST_MIN_HIGHT, ram - (GUEST_MAX_LOW - GUEST_MIN_LOW));
	}

}

//TODO
struct shadow_page_table{
	paddr_t cr3;
	struct {
		vaddr_t vaddr;
		paddr_t paddr;
		size_t size;
	}mappings[64];
}shadow_page_table[10];
#define PGT_VALID_MASK 0x1
#define PGT_ADRESS_MASK 0xFFFFFFFFFF000
#define PGT_HUGEPAGE_MASK 0x80
#define PGT_IS_VALID(p) (p & PGT_VALID_MASK)
#define PGT_IS_HUGEPAGE(p) (p & PGT_HUGEPAGE_MASK)
#define PGT_ADDRESS(p) (p & PGT_ADRESS_MASK)
void parse_pml_level(paddr_t pml, vaddr_t prefix, uint8_t lvl){
	uint64_t *p = malloc(4096);
	vaddr_t new_prefix;
	int i;

	read_physical(p,4096, pml);
	
	for(i = 0; i < 512; i++){

		if(PGT_IS_VALID(p[i])){
			new_prefix = prefix + (i<<(12*(lvl-1)*9));
			if(lvl == 1 || PGT_IS_HUGEPAGE(p[i])){
				printf("huge page\n");
			}else{
				parse_pml_level(PGT_ADDRESS(p[i]),new_prefix,lvl-1);
			}
		}
	}
	free(p);
}


void set_page_table(void)
{
	paddr_t cr3 = mov_from_control(3);
}

int trap_read(vaddr_t addr, size_t size, uint64_t *val)
{
	display_mapping();
	fprintf(stderr, "trap_read unimplemented at %lx\n", addr);
	display_vga();
	exit(EXIT_FAILURE);
}

#define CGA_BEGIN 0xb8006
int trap_write(vaddr_t addr, size_t size, uint64_t val)
{
	display_mapping();
	
	if(addr >= CGA_BEGIN && addr < CGA_BEGIN + 4096){
		write_vga((addr- CGA_BEGIN) /2, (uint16_t)val);// divise 2 car 2 octet par case
	}else{
		return 0;
	}
	return 1;
}
