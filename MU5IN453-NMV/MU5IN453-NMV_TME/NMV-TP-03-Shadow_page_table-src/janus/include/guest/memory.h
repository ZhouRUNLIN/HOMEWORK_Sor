#ifndef _INCLUDE_MEMORY_H_
#define _INCLUDE_MEMORY_H_


#include <idt.h>
#include <task.h>
#include <types.h>


paddr_t alloc_page(void);        /* Allocate a physical page identity mapped */

void free_page(paddr_t addr);  /* Release a page allocated with alloc_page() */


void map_page(vaddr_t vaddr, paddr_t paddr);

paddr_t unmap_page(vaddr_t vaddr);

void duplicate_task(struct task *ctx);

void mmap(vaddr_t vaddr);

void munmap(vaddr_t vaddr);

void pgfault(struct interrupt_context *ctx);


#endif
