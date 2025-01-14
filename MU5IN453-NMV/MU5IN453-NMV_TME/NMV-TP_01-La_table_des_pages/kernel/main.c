#include <idt.h>                            /* see there for interrupt names */
#include <memory.h>                               /* physical page allocator */
#include <printk.h>                      /* provides printk() and snprintk() */
#include <string.h>                                     /* provides memset() */
#include <syscall.h>                         /* setup system calls for tasks */
#include <task.h>                             /* load the task from mb2 info */
#include <types.h>              /* provides stdint and general purpose types */
#include <vga.h>                                         /* provides clear() */
#include <x86.h>                                    /* access to cr3 and cr2 */

#define ADRESSE_MASK 0xFFFFFFFFFFFF000
#define HUGE_PAGE_MASK 0x080
#define GET_ADRESSE(p) (ADRESSE_MASK & p)
#define PAGE_PRESENT 0x01
#define IS_HUGE_PAGE(p) (p & HUGE_PAGE_MASK)

__attribute__((noreturn))
void die(void)
{
	/* Stop fetching instructions and go low power mode */
	asm volatile ("hlt");

	/* This while loop is dead code, but it makes gcc happy */
	while (1)
		;
}



//EXO1
void print_pgt(paddr_t pml, uint8_t lvl){
    uint64_t *p;
    int i;
    p = (uint64_t *)pml;
    if(lvl == 4)
        printk("CR3: 0x%x\n", pml);
    if(lvl < 1)
        return;
    for(i = 0; i< 512; i++){
        if(p[i] & PAGE_PRESENT){//p[i] est une entree dans PML = 64 bit
            printk("PML%d %d: 0x%x\n", lvl, i, GET_ADRESSE(pml));
            if(!IS_HUGE_PAGE(p[i])){
                print_pgt((paddr_t) GET_ADRESSE(p[i]),lvl-1);
            }
        }
    }
}
__attribute__((noreturn))
void main_multiboot2(void *mb2)
{
	clear();                                     /* clear the VGA screen */
	printk("Rackdoll OS\n-----------\n\n");                 /* greetings */

	setup_interrupts();                           /* setup a 64-bits IDT */
	setup_tss();                                  /* setup a 64-bits TSS */
	interrupt_vector[INT_PF] = pgfault;      /* setup page fault handler */

	remap_pic();               /* remap PIC to avoid spurious interrupts */
	disable_pic();                         /* disable anoying legacy PIC */
	sti();                                          /* enable interrupts */

    //EXO1
    //print_pgt(store_cr3(),4);
    //EXO2
    /*
    struct task fake;
    paddr_t new;
    fake.pgt = store_cr3();
    new = alloc_page();
    map_page(&fake, 0x201000, new);
*/

	load_tasks(mb2);                         /* load the tasks in memory */
	run_tasks();                                 /* run the loaded tasks */

	printk("\nGoodbye!\n");                                 /* fairewell */
	die();                        /* the work is done, we can die now... */
}
