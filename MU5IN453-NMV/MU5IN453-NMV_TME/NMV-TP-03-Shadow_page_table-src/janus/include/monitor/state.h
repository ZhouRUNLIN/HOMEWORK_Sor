#ifndef _INCLUDE_STATE_H_
#define _INCLUDE_STATE_H_


#include <stdint.h>
#include <stdlib.h>


#define MODE_16_BITS               0
#define MODE_32_BITS               1
#define MODE_64_BITS               2

#define VGA_LINES                  25
#define VGA_COLUMNS                80
#define VGA_SIZE                   (VGA_LINES * VGA_COLUMNS)

#define INTERRUPT_COUNT            256

#define SELECTOR_CS                0
#define SELECTOR_DS                1
#define SELECTOR_ES                2
#define SELECTOR_FS                3
#define SELECTOR_GS                4
#define SELECTOR_SS                5

#define DESCRIPTOR_P               (1ul << 47)
#define DESCRIPTOR_L               (1ul << 53)

#define CR0_PE                     (1ul <<  0)
#define CR0_MP                     (1ul <<  1)
#define CR0_EM                     (1ul <<  2)
#define CR0_TS                     (1ul <<  3)
#define CR0_ET                     (1ul <<  4)
#define CR0_NE                     (1ul <<  5)
#define CR0_WP                     (1ul << 16)
#define CR0_AM                     (1ul << 18)
#define CR0_NW                     (1ul << 29)
#define CR0_CD                     (1ul << 30)
#define CR0_PG                     (1ul << 31)

#define CR4_VME                    (1ul <<  0)
#define CR4_PVI                    (1ul <<  1)
#define CR4_TSD                    (1ul <<  2)
#define CR4_DE                     (1ul <<  3)
#define CR4_PSE                    (1ul <<  4)
#define CR4_PAE                    (1ul <<  5)
#define CR4_MCE                    (1ul <<  6)
#define CR4_PGE                    (1ul <<  7)
#define CR4_PCE                    (1ul <<  8)
#define CR4_OSFXSR                 (1ul <<  9)
#define CR4_OSXMMEXCPT             (1ul << 10)
#define CR4_FSGSBASE               (1ul << 16)
#define CR4_OSXSAVE                (1ul << 18)

#define MSR_EFER                   0xc0000080
#define EFER_SCE                   (1ul <<  0)
#define EFER_LME                   (1ul <<  8)
#define EFER_LMA                   (1ul << 10)
#define EFER_NXE                   (1ul << 11)
#define EFER_SVME                  (1ul << 12)
#define EFER_LMSLE                 (1ul << 13)
#define EFER_FFXSR                 (1ul << 14)
#define EFER_TCE                   (1ul << 15)

#define PORT_VGA_ADDR              0x3d4
#define PORT_VGA_DATA              0x3d5

#define PORT_PIC_MASTER_COMMAND    0x20
#define PORT_PIC_MASTER_DATA       0x21
#define PORT_PIC_SLAVE_COMMAND     0xa0
#define PORT_PIC_SLAVE_DATA        0xa1

#define INTERRUPT_DE                0
#define INTERRUPT_DB                1
#define INTERRUPT_NMI               2
#define INTERRUPT_BP                3
#define INTERRUPT_OF                4
#define INTERRUPT_BR                5
#define INTERRUPT_UD                6
#define INTERRUPT_NM                7
#define INTERRUPT_DF                8
#define INTERRUPT_CS                9
#define INTERRUPT_TS               10
#define INTERRUPT_NP               11
#define INTERRUPT_SS               12
#define INTERRUPT_GP               13
#define INTERRUPT_PF               14
#define INTERRUPT_MF               16
#define INTERRUPT_AC               17
#define INTERRUPT_MC               18
#define INTERRUPT_XF               19
#define INTERRUPT_SX               30


struct gdtr32
{
	uint16_t  limit;
	uint32_t  base;
} __attribute__ ((packed));

struct gdtr64
{
	uint16_t  limit;
	uint64_t  base;
} __attribute__ ((packed));

struct idtr64
{
	uint16_t  limit;
	uint64_t  base;
} __attribute__ ((packed));

struct state
{
	uint8_t    mode;
	uint64_t   selectors[6];
	uint64_t  *gdt;
	size_t     gdt_size;
	uint64_t  *idt;
	size_t     idt_size;
	uint64_t   controls[5];
	uint64_t   efer;
	uint16_t   vga[VGA_SIZE];
	uint8_t    itpending[INTERRUPT_COUNT];
	uint64_t   itcode[INTERRUPT_COUNT];
};

extern struct state guest_state;


void setup_state(void);

void lgdt(uint64_t val);
void lidt(uint64_t val);

void mov_to_selector(uint64_t val, uint8_t selector);
uint64_t get_selector(uint8_t selector);

uint64_t mov_from_control(uint8_t control);
void mov_to_control(uint64_t val, uint8_t control);
void set_control(uint64_t val, uint8_t control);

uint64_t rdmsr(uint32_t addr);
void wrmsr(uint32_t addr, uint64_t val);

void cti(void);
void sti(void);
uint64_t interrupt_entry(uint8_t vec);
void trigger_interrupt(uint8_t vec, uint64_t code);
int pending_interrupt(int clear, uint8_t *vec, uint64_t *code);

void out8(uint16_t port, uint8_t val);
void out16(uint16_t port, uint16_t val);
void out32(uint16_t port, uint32_t val);

void write_vga(uint16_t off, uint16_t val);
uint16_t read_vga(uint16_t off);
void display_vga(void);


#endif
