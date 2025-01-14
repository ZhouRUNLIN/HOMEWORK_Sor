#ifndef _INCLUDE_MEMORY_H_
#define _INCLUDE_MEMORY_H_


#include <idt.h>
#include <task.h>
#include <types.h>


paddr_t alloc_page(void);        /* Allocate a physical page identity mapped */

void free_page(paddr_t addr);  /* Release a page allocated with alloc_page() */


void map_page(struct task *ctx, vaddr_t vaddr, paddr_t paddr);

void load_task(struct task *ctx);

void set_task(struct task *ctx);

void duplicate_task(struct task *ctx);

void mmap(struct task *ctx, vaddr_t vaddr);

void munmap(struct task *ctx, vaddr_t vaddr);

void pgfault(struct interrupt_context *ctx);


#endif
