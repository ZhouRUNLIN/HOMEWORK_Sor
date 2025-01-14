#ifndef _INCLUDE_X86_H_
#define _INCLUDE_X86_H_


/*
 * The zero, code and data selectors are set in the gdt64 declared in the
 * entry.S file.
 * Kernel code and data segment descriptors *must* be consecutive in the table
 * so we can use syscall and sysret instructions.
 * Kernel data and code segment descriptors *must* be consecutive in the table
 * so we can use syscall and sysret instructions (note that this time,  the
 * data segment must be placed before the code segment).
 */

#define ZERO_SELECTOR          0x00
#define KERNEL_CODE_SELECTOR   0x08
#define KERNEL_DATA_SELECTOR   0x10
#define USER_DATA_SELECTOR     0x18
#define USER_CODE_SELECTOR     0x20
#define TSS_SELECTOR           0x28


/*
 * Model Sepcific Registers defintions.
 * These ones are common to all families of modern Intel and AMD processors.
 */

#define MSR_EFER               0xc0000080
#define MSR_EFER_SCE           (1ul <<  0)
#define MSR_EFER_LME           (1ul <<  8)
#define MSR_EFER_LMA           (1ul << 10)
#define MSR_EFER_NXE           (1ul << 11)
#define MSR_EFER_SVME          (1ul << 12)
#define MSR_EFER_LMSLE         (1ul << 13)
#define MSR_EFER_FFXSR         (1ul << 14)
#define MSR_EFER_TCE           (1ul << 15)


/*
 * Rflags definitions.
 */

#define RFLAGS_CF              (1ul <<  0)
#define RFLAGS_PF              (1ul <<  2)
#define RFLAGS_AF              (1ul <<  4)
#define RFLAGS_ZF              (1ul <<  6)
#define RFLAGS_SF              (1ul <<  7)
#define RFLAGS_TF              (1ul <<  8)
#define RFLAGS_IF              (1ul <<  9)
#define RFLAGS_DF              (1ul << 10)
#define RFLAGS_OF              (1ul << 11)
#define RFLAGS_IOPL            (3ul << 12)
#define RFLAGS_NT              (1ul << 14)
#define RFLAGS_RF              (1ul << 16)
#define RFLAGS_VM              (1ul << 17)
#define RFLAGS_AC              (1ul << 18)
#define RFLAGS_VIF             (1ul << 19)
#define RFLAGS_VIP             (1ul << 20)
#define RFLAGS_ID              (1ul << 21)


static inline void load_rsp(uint64_t rsp)
{
	asm volatile ("movq %0, %%rsp" : : "r" (rsp));
}

static inline uint64_t store_rsp(void)
{
	uint64_t rsp;
	asm volatile ("movq %%rsp, %0" : "=r" (rsp));
	return rsp;
}


static inline void load_cr2(uint64_t cr2)
{
	asm volatile ("movq %0, %%cr2" : : "a" (cr2));
}

static inline uint64_t store_cr2(void)
{
	uint64_t cr2;
	asm volatile ("movq %%cr2, %0" : "=a" (cr2));
	return cr2;
}


static inline void load_cr3(uint64_t cr3)
{
	asm volatile ("movq %0, %%cr3" : : "a" (cr3));
}

static inline uint64_t store_cr3(void)
{
	uint64_t cr3;
	asm volatile ("movq %%cr3, %0" : "=a" (cr3));
	return cr3;
}


static inline void load_tr(uint16_t tr)
{
	asm volatile ("ltr %0" : : "r" (tr));
}

static inline uint16_t store_tr(void)
{
	uint16_t tr;
	asm volatile ("str %0" : "=r" (tr));
	return tr;
}


static inline void invlpg(vaddr_t vaddr)
{
	asm volatile ("invlpg (%0)" : : "r" (vaddr) : "memory");
}


typedef uint16_t  port_t;


static inline uint8_t in8(port_t port)
{
	uint8_t ret;
	asm volatile ("inb %1, %0" : "=a" (ret) : "dN" (port));
	return ret;
}

static inline uint16_t in16(port_t port)
{
	uint16_t ret;
	asm volatile ("inw %1, %0" : "=a" (ret) : "dN" (port));
	return ret;
}

static inline uint32_t in32(port_t port)
{
	uint32_t ret;
	asm volatile ("inl %1, %0" : "=a" (ret) : "dN" (port));
	return ret;
}


static inline void out8(port_t port, uint8_t val)
{
	asm volatile ("outb %0, %1" : : "a" (val), "dN" (port));
}

static inline void out16(port_t port, uint16_t val)
{
	asm volatile ("outw %0, %1" : : "a" (val), "dN" (port));
}

static inline void out32(port_t port, uint32_t val)
{
	asm volatile ("outl %0, %1" : : "a" (val), "dN" (port));
}


static inline void wrmsr(uint32_t msr, uint64_t val)
{
	uint32_t eax, edx;
	edx = (val >> 32) & 0xffffffff;
	eax = (val >>  0) & 0xffffffff;
	asm volatile ("wrmsr" : : "a" (eax), "c" (msr), "d" (edx));
}

static inline uint64_t rdmsr(uint32_t msr)
{
	uint32_t eax, edx;
	asm volatile ("rdmsr" : "=a" (eax), "=d" (edx) : "c" (msr));
	return (((uint64_t) edx) << 32) | eax;
}


#endif
