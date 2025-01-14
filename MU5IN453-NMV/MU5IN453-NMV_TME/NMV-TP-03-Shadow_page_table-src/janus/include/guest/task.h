#ifndef _INCLUDE_TASK_H_
#define _INCLUDE_TASK_H_


#include <idt.h>


struct task
{
	paddr_t                   pgt;                   /* page table paddr */
	paddr_t                   load_paddr;      /* paddr of the task code */
	paddr_t                   load_end_paddr;    /* paddr following code */
	vaddr_t                   load_vaddr;        /* vaddr for load_paddr */
	vaddr_t                   bss_end_vaddr;      /* vaddr following bss */
	struct interrupt_context  context;       /* task registers save area */
};


/*
 * Documentation for 64-bits Task State Segment can be found in
 *   AMD64 Architecture Programmer's Manual, Volume 2: System Programming
 *   Section 12.2: Task-Management Resources
 */

struct task_state_segment
{
	uint32_t pad;
	uint64_t rsp0;
	uint64_t rsp1;
	uint64_t rsp2;
	uint64_t pad1;
	uint64_t ist1;
	uint64_t ist2;
	uint64_t ist3;
	uint64_t ist4;
	uint64_t ist5;
	uint64_t ist6;
	uint64_t ist7;
	uint64_t pad2;
	uint16_t pad3;
	uint16_t iopb_off;
	uint64_t iopb;
} __attribute__((packed));

extern struct task_state_segment tss;


void setup_tss(void);                              /* Setup user mode switch */

void load_tasks(const void *mb2);        /* Load tasks from multiboot 2 info */

struct task *current(void);                          /* Get the current task */

void next_task(struct interrupt_context *ctx);        /* Go to the next task */

void exit_task(struct interrupt_context *ctx);      /* Exit the current task */

void fork_task(struct interrupt_context *ctx);      /* Fork the current task */

void run_tasks(void);                          /* Start to execute the tasks */


#endif
