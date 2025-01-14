#include <idt.h>
#include <memory.h>
#include <printk.h>
#include <string.h>
#include <types.h>
#include <vga.h>
#include <x86.h>

#define MAP_VADDR_LOW_0     0x200000
#define MAP_VADDR_LOW_1     0x201000
#define MAP_VADDR_HIGH_0    0x100000000
#define MAP_VADDR_HIGH_1    0x100001000

#define PAGE_SIZE           0x1000


__attribute__((noreturn))
void die(void)
{
	/* Stop fetching instructions and go low power mode */
	asm volatile ("hlt");

	/* This while loop is dead code, but it makes gcc happy */
	while (1)
		;
}

static void do_mmap_munmap(void)
{
	size_t i;
	uint8_t *ptr;

	mmap(MAP_VADDR_LOW_0);
	ptr = (uint8_t *) MAP_VADDR_LOW_0;
	for (i = 0; i < PAGE_SIZE; i++)
		ptr[i] = (uint8_t) i;

	printk("  %p => %u\n", ptr + 1, ptr[1]);

	munmap(MAP_VADDR_LOW_0);
	mmap(MAP_VADDR_LOW_0);

	printk("  %p => %u\n", ptr + 1, ptr[1]);

	munmap(MAP_VADDR_LOW_0);
}

static void do_double_map(void)
{
	paddr_t pa0;
	volatile uint8_t *ptr0, *ptr1;

	pa0 = alloc_page();

	map_page(MAP_VADDR_LOW_0, pa0);
	map_page(MAP_VADDR_LOW_1, pa0);

	ptr0 = (uint8_t *) MAP_VADDR_LOW_0;
	ptr1 = (uint8_t *) MAP_VADDR_LOW_1;

	*ptr0 = 42;
	printk("  %p := %u --> %p = %u\n", ptr0, *ptr0, ptr1, *ptr1);

	*ptr1 = 18;
	printk("  %p := %u --> %p = %u\n", ptr1, *ptr1, ptr0, *ptr0);

	unmap_page(MAP_VADDR_LOW_0);
	unmap_page(MAP_VADDR_LOW_1);

	free_page(pa0);
}

static void do_zero_copy(void)
{
	paddr_t pa0, pa1;
	size_t i;
	volatile uint8_t *ptr0, *ptr1;

	pa0 = alloc_page();
	pa1 = alloc_page();

	map_page(MAP_VADDR_LOW_0, pa0);
	ptr0 = (uint8_t *) MAP_VADDR_LOW_0;
	for (i = 0; i < PAGE_SIZE; i++)
		ptr0[i] = (uint8_t) i;

	map_page(MAP_VADDR_LOW_1, pa1);
	ptr1 = (uint8_t *) MAP_VADDR_LOW_1;
	printk("  %p = %u %u %u\n", ptr1, ptr1[0], ptr1[1], ptr1[2]);

	unmap_page(MAP_VADDR_LOW_0);
	unmap_page(MAP_VADDR_LOW_1);
	map_page(MAP_VADDR_LOW_0, pa1);
	map_page(MAP_VADDR_LOW_1, pa0);

	printk("  %p = %u %u %u\n", ptr1, ptr1[0], ptr1[1], ptr1[2]);

	munmap(MAP_VADDR_LOW_0);
	munmap(MAP_VADDR_LOW_1);
}

static void do_lazy_alloc(void)
{
	uint8_t *ptr0 = (uint8_t *) MAP_VADDR_HIGH_0;
	uint8_t *ptr1 = (uint8_t *) MAP_VADDR_HIGH_1;

	printk("  %p = %u\n", ptr0, *ptr0);

	*ptr1 = 23;
	printk("  %p = %u\n", ptr1, *ptr1);

	munmap(MAP_VADDR_HIGH_1);
	printk("  %p = %u\n", ptr1, *ptr1);
}

static void do_stuff(void)
{
	printk("mmap munmap\n");
	do_mmap_munmap();

	printk("\ndouble map\n");
	do_double_map();

	printk("\ndo zero copy\n");
	do_zero_copy();

	printk("\ndo lazy alloc\n");
	do_lazy_alloc();
}

__attribute__((noreturn))
void main_multiboot2(void)
{
	clear();
	printk("Guest starting...\n\n");

	setup_interrupts();                           /* setup a 64-bits IDT */
	interrupt_vector[INT_PF] = pgfault;      /* setup page fault handler */

	disable_pic();                         /* disable anoying legacy PIC */
	sti();                                          /* enable interrupts */

	do_stuff();                        /* do useless but pedagogic stuff */

	printk("\nGoodbye!\n");                                 /* fairewell */
	die();                        /* the work is done, we can die now... */
}
