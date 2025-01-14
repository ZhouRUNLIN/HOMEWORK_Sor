#ifndef _INCLUDE_MEMORY_H_
#define _INCLUDE_MEMORY_H_


#include <stdint.h>
#include <stdlib.h>


typedef uint64_t  paddr_t;
typedef uint64_t  vaddr_t;


void protect_low_memory(void);                    /* protect trapping memory */

void unprotect_low_memory(void);                /* unprotect trapping memory */

void display_mapping(void);                       /* display virtual mapping */


void allocate_physical_memory(size_t ram);  /* allocate guest memory backend */


paddr_t alloc_page(void);              /* allocate 1 page from guest backend */

void free_page(paddr_t addr);                   /* free a guest backend page */

void map_page(vaddr_t vaddr, paddr_t paddr, size_t len); /* map a guest page */

void unmap_page(vaddr_t vaddr, size_t len);            /* unmap a guest page */

void read_physical(void *dest, size_t size, paddr_t paddr);

void write_physical(const void *dest, size_t size, paddr_t paddr);


#endif
