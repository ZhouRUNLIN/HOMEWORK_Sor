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
 * Memory model for the guest
 *
 * +----------------------+ 0xfffffffffffffff
 * | Forbidden            |
 * +----------------------+ 0x700000000000
 * | Guest memory         |
 * | Automatic mapping    |
 * +----------------------+ 0x100000000
 * | Forbidden            |
 * +----------------------+ 0x80000000
 * | Guest memory         |
 * | Manual mapping       |
 * +----------------------+ 0x200000
 * | Guest memory         |
 * +----------------------+ 0x100000
 * | Device mapped memory |
 * +----------------------+ 0x0
 */


typedef uint64_t  pte_t;

#define PAGE_SHIFT   12
#define PAGE_SIZE    (1ul << PAGE_SHIFT)
#define PAGE_MASK    (~(PAGE_SIZE - 1))
#define PTE_P        (1ul << 0)
#define PTE_W        (1ul << 1)
#define PTE_U        (1ul << 2)
#define PTE_ADDR     0xffffffffffff000
#define PGD_SHIFT    9
#define PGD_SIZE     (1ul << PGD_SHIFT)
#define PGD_MASK     (PGD_SIZE - 1)


void map_page(vaddr_t vaddr, paddr_t paddr)
{
	paddr_t pgt = store_cr3() & PTE_ADDR, new;
	pte_t *pgd = (pte_t *) pgt;
	uint8_t lvl = 3;
	vaddr_t idx;

	while (lvl > 0) {
		idx = (vaddr >> (PAGE_SHIFT + PGD_SHIFT * lvl)) & PGD_MASK;

		if (!(pgd[idx] & PTE_P)) {
			new = alloc_page();
			pgd[idx] = (new & PTE_ADDR)| PTE_U | PTE_W | PTE_P;
			pgt = new;
		} else {
			pgt = pgd[idx] & PTE_ADDR;
		}

		pgd = (pte_t *) pgt;
		lvl--;
	}

	idx = (vaddr >> PAGE_SHIFT) & PGD_MASK;
	pgd[idx] = (paddr & PTE_ADDR) | PTE_U | PTE_W | PTE_P;
}

paddr_t unmap_page(vaddr_t vaddr)
{
	paddr_t pgt = store_cr3() & PTE_ADDR;
	pte_t *pgd = (pte_t *) pgt;
	uint8_t lvl = 3;
	vaddr_t idx;

	while (lvl > 0) {
		idx = (vaddr >> (PAGE_SHIFT + PGD_SHIFT * lvl)) & PGD_MASK;

		if (!(pgd[idx] & PTE_P)) {
			return 0;
		} else {
			pgt = pgd[idx] & PTE_ADDR;
		}

		pgd = (pte_t *) pgt;
		lvl--;
	}

	idx = (vaddr >> PAGE_SHIFT) & PGD_MASK;
	if (!(pgd[idx] & PTE_P))
		return 0;

	pgt = pgd[idx] & PTE_ADDR;

	pgd[idx] = 0;
	invlpg(vaddr);

	return pgt;
}

void mmap(vaddr_t vaddr)
{
	paddr_t new = alloc_page();

	memset((void *) new, 0, PAGE_SIZE);
	map_page(vaddr, new);
}

void munmap(vaddr_t vaddr)
{
	paddr_t pte = unmap_page(vaddr);

	if (pte == 0)
		return;

	free_page(pte);
}

void pgfault(struct interrupt_context *ctx __attribute__ ((unused)))
{
	vaddr_t fault = store_cr2();

	if (fault < 0x100000000 || fault >= 0x700000000000) {
		printk("Segfault %p\n", fault);
		die();
	}

	fault = fault & PAGE_MASK;
	mmap(fault);
}


static paddr_t duplicate_level(struct task *ctx, paddr_t paddr, uint8_t lvl)
{
	paddr_t sub, new;
	pte_t *pgd;
	size_t idx;

	if (paddr >= ctx->load_paddr && paddr < ctx->load_end_paddr)
		return paddr;

	new = alloc_page();
	pgd = (pte_t *) new;

	memcpy((void *) new, (void *) paddr, PAGE_SIZE);
	if (lvl == 0)
		return new;

	for (idx = 0; idx < PGD_SIZE; idx++) {
		if (!(pgd[idx] & PTE_P))
			continue;
		if ((lvl == 3) && idx == 0)
			continue;

		sub = duplicate_level(ctx, pgd[idx] & PTE_ADDR, lvl - 1);
		pgd[idx] = (pgd[idx] & ~PTE_ADDR) | (sub & PTE_ADDR);
	}

	return new;
}

void duplicate_task(struct task *ctx)
{
	paddr_t old_pml4 = ctx->pgt;
	paddr_t new_pml4 = duplicate_level(ctx, old_pml4, 4);

	ctx->pgt = new_pml4;
}
