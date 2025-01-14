#include <memory.h>
#include <printk.h>
#include <string.h>
#include <x86.h>


#define PHYSICAL_POOL_PAGES  64
#define PHYSICAL_POOL_BYTES  (PHYSICAL_POOL_PAGES << 12)
#define BITSET_SIZE          (PHYSICAL_POOL_PAGES >> 6)


extern __attribute__((noreturn)) void die(void);

static uint64_t bitset[BITSET_SIZE];

static uint8_t pool[PHYSICAL_POOL_BYTES] __attribute__((aligned(0x1000)));


paddr_t alloc_page(void)
{
	size_t i, j;
	uint64_t v;

	for (i = 0; i < BITSET_SIZE; i++) {
		if (bitset[i] == 0xffffffffffffffff)
			continue;

		for (j = 0; j < 64; j++) {
			v = 1ul << j;
			if (bitset[i] & v)
				continue;

			bitset[i] |= v;
			return (((64 * i) + j) << 12) + ((paddr_t) &pool);
		}
	}

	printk("[error] Not enough identity free page\n");
	return 0;
}

void free_page(paddr_t addr)
{
	paddr_t tmp = addr;
	size_t i, j;
	uint64_t v;

	tmp = tmp - ((paddr_t) &pool);
	tmp = tmp >> 12;

	i = tmp / 64;
	j = tmp % 64;
	v = 1ul << j;

	if ((bitset[i] & v) == 0) {
		printk("[error] Invalid page free %p\n", addr);
		die();
	}

	bitset[i] &= ~v;
}


/*
 * Memory model for Rackdoll OS
 *
 * +----------------------+ 0xffffffffffffffff
 * | Higher half          |
 * | (unused)             |
 * +----------------------+ 0xffff800000000000
 * | (impossible address) |
 * +----------------------+ 0x00007fffffffffff
 * | User                 |
 * | (text + data + heap) | My payload
 * +----------------------+ 0x2000000000
 * | User                 |
 * | (stack)              |
 * +----------------------+ 0x40000000
 * | Kernel               |
 * | (valloc)             |
 * +----------------------+ 0x201000
 * | Kernel               |
 * | (APIC)               |
 * +----------------------+ 0x200000
 * | Kernel               |
 * | (text + data)        |
 * +----------------------+ 0x100000
 * | Kernel               |
 * | (BIOS + VGA)         |
 * +----------------------+ 0x0
 *
 * This is the memory model for Rackdoll OS: the kernel is located in low
 * addresses. The first 2 MiB are identity mapped and not cached.
 * Between 2 MiB and 1 GiB, there are kernel addresses which are not mapped
 * with an identity table.
 * Between 1 GiB and 128 GiB is the stack addresses for user processes growing
 * down from 128 GiB.
 * The user processes expect these addresses are always available and that
 * there is no need to map them explicitely.
 * Between 128 GiB and 128 TiB is the heap addresses for user processes.
 * The user processes have to explicitely map them in order to use them.
 */

//Exo2
#define ADRESSE_MASK 0xFFFFFFFFFFFF000
#define HUGE_PAGE_MASK 0x080
#define GET_ADRESSE(p) (ADRESSE_MASK & p)
#define PAGE_PRESENT 0x01
#define IS_HUGE_PAGE(p) (p & HUGE_PAGE_MASK)

//TODO EXO2
#define PML4_MASK 0xFF8000000000
#define PML3_MASK 0x7FC0000000
#define PML2_MASK 0x3FE00000
#define PML1_MASK 0x1FF000
#define GET_INDEX_PML4(p)((p & PML4_MASK)>>39)
#define GET_INDEX_PML3(p)((p & PML3_MASK)>>30)
#define GET_INDEX_PML2(p)((p & PML2_MASK)>>21)
#define GET_INDEX_PML1(p)((p & PML1_MASK)>>12)
#define test 0b1000000001000000000000
#define IS_VALIDE(p)(p & 0x1)

void map_page(struct task *ctx, vaddr_t vaddr, paddr_t paddr)
{
    uint64_t *p;
    int index;

    p = (uint64_t*)ctx->pgt;
    printk("p4: %p\n",p);
    //detection dans PML4
    index = GET_INDEX_PML4(vaddr);
    if(!IS_VALIDE(p[index])){
        printk("PML4 non valide\n");
        paddr_t PML3 = alloc_page();
        memset((void*)PML3,0,4096);
        p[index] |= (PML3| 0x7);
    }

    //detection dans PML3
    p = (uint64_t *)GET_ADRESSE(p[index]);
    index = GET_INDEX_PML3(vaddr);

    printk("p3: %p\n",p);
    if(!IS_VALIDE(p[index])){
        printk("PML3 non valide\n");
        paddr_t PML2 = alloc_page();
        memset((void*)PML2,0,4096);
        p[index] |= (PML2|0x7);
    }
    //detection dans PML2
    p = (uint64_t *)GET_ADRESSE(p[index]);
    index = GET_INDEX_PML2(vaddr);
    printk("p2: %p\n",p);
    if(!IS_VALIDE(p[index])){
        printk("PML2 non valide\n");
        paddr_t PML1 =alloc_page();
        memset((void*)PML1,0,4096);
        p[index]|= (PML1|0x7);
    }
    //detection dans PML1
    p = (uint64_t *)GET_ADRESSE(p[index]);
    index = GET_INDEX_PML1(vaddr);
    printk("p1: %p\n",p);
    if(!IS_VALIDE(p[index])){
        printk("PML1 non valide\n");
        memset((void *)paddr, 0, 4096);
        p[index]|=paddr|0x7;
    }
}

void load_task(struct task *ctx)
{
    uint64_t *p;
    paddr_t paddr;
    vaddr_t vaddr;

    //alloc PML4
    paddr = alloc_page();
    memset((void *)paddr, 0, 4096);
    ctx->pgt = paddr;

    //alloc PML3
    p = (uint64_t *)ctx->pgt;
    paddr = alloc_page();
    memset((void *)paddr, 0, 4096);
    p[0] |= (paddr | 0x7);

    //mapper payload
    for(paddr = ctx->load_paddr; paddr < ctx->load_end_paddr; paddr += (4096)){
        vaddr = ctx->load_vaddr + paddr - ctx->load_paddr;

        map_page(ctx, vaddr, paddr);
    }
    //mapper bss
    for(vaddr = ctx->load_vaddr + ctx->load_end_paddr - ctx->load_paddr;
        vaddr < ctx->bss_end_vaddr; vaddr += (4096)){
        paddr = alloc_page();
        memset((void *)paddr, 0, 4096);
        map_page(ctx, vaddr, paddr);
    }
}

void set_task(struct task *ctx)
{
    load_cr3(ctx->pgt);
}

void mmap(struct task *ctx, vaddr_t vaddr)
{
    paddr_t tmp = alloc_page();
    memset((void *)tmp, 0, 4096);
    map_page(ctx, vaddr, tmp);
}

void munmap(struct task *ctx, vaddr_t vaddr)
{

}

void pgfault(struct interrupt_context *ctx)
{
	printk("Page fault at %p\n", ctx->rip);
	printk("  cr2 = %p\n", store_cr2());
	asm volatile ("hlt");
}

void duplicate_task(struct task *ctx)
{
}
