#include "state.h"

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "memory.h"
#include "shadow.h"


struct state guest_state;


static void unsupported(const char *op)
{
	fprintf(stderr, "Error: unsupported opperation: %s\n", op);
	exit(EXIT_FAILURE);
}

static void trigger_gpfault(uint64_t code)
{
	trigger_interrupt(INTERRUPT_GP, code);
}

static void activate_longmode(void)
{
	guest_state.mode = MODE_64_BITS;
}


void setup_state(void)
{
	memset(&guest_state, 0, sizeof (guest_state));

	guest_state.mode = MODE_32_BITS;
	guest_state.controls[0] = CR0_PE | CR0_ET;
}


static void lgdt32(uint32_t val)
{
	struct gdtr32 *gdtr = (struct gdtr32 *) ((uint64_t) val);

	if (((gdtr->limit + 1) % 8) != 0)
		trigger_gpfault(0);

	guest_state.gdt_size = (gdtr->limit + 1) / 8;
	guest_state.gdt = (uint64_t *) ((uint64_t) gdtr->base);
}

static void lgdt64(uint64_t val)
{
	struct gdtr64 *gdtr = (struct gdtr64 *) val;

	if (((gdtr->limit + 1) % 8) != 0)
		trigger_gpfault(0);

	guest_state.gdt_size = (gdtr->limit + 1) / 8;
	guest_state.gdt = (uint64_t *) gdtr->base;
}

void lgdt(uint64_t val)
{
	if (guest_state.mode == MODE_32_BITS) {
		lgdt32((uint32_t) val);
	} else if (guest_state.mode == MODE_64_BITS) {
		lgdt64(val);
	} else {
		unsupported("lgdt 16 bits");
	}
}

static void lidt64(uint64_t val)
{
	struct idtr64 *idtr = (struct idtr64 *) val;

	if (((idtr->limit + 1) % 16) != 0)
		trigger_gpfault(0);

	guest_state.idt_size = (idtr->limit + 1) / 16;
	guest_state.idt = (uint64_t *) idtr->base;
}

void lidt(uint64_t val)
{
	if (guest_state.mode == MODE_64_BITS) {
		lidt64(val);
	} else {
		unsupported("lgdt 16/32 bits");
	}
}

void mov_to_selector(uint64_t val, uint8_t selector)
{
	size_t index = val / 8;

	if (index > guest_state.gdt_size)
		trigger_gpfault(0);
	if ((guest_state.gdt[index] & DESCRIPTOR_P) == 0)
		trigger_gpfault(0);

	if (selector == SELECTOR_CS &&
	    (guest_state.gdt[index] & DESCRIPTOR_L) == 0 &&
	    guest_state.mode == MODE_64_BITS)
		unsupported("32 bits compatibility mode");

	if (selector == SELECTOR_CS &&
	    (guest_state.controls[0] & CR0_PE) &&
	    (guest_state.controls[0] & CR0_PG) &&
	    (guest_state.efer & EFER_LMA)) {
		activate_longmode();
	}

	guest_state.selectors[selector] = val;
}

uint64_t get_selector(uint8_t selector)
{
	return guest_state.selectors[selector];
}

uint64_t mov_from_control(uint8_t control)
{
	return guest_state.controls[control];
}

static void mov_to_cr0(uint64_t val)
{
	uint64_t activate = val & ~guest_state.controls[0];
	uint64_t deactivate = guest_state.controls[0] & ~val;

	if (deactivate & CR0_PE) {
		fprintf(stderr, "Error: unsupported sequence CR0.PE = 0\n");
		exit(EXIT_FAILURE);
	}

	if ((activate & CR0_PG) && (guest_state.efer & EFER_LME) &&
	    !(guest_state.controls[4] & CR4_PAE))
		trigger_gpfault(0);

	if (activate & CR0_PG) {
		set_page_table();
		if (guest_state.efer & EFER_LME)
			guest_state.efer |= EFER_LMA;
	}

	guest_state.controls[0] = val | CR0_ET;
}

static void mov_to_cr4(uint64_t val)
{
	uint64_t deactivate = guest_state.controls[4] & ~val;

	if ((deactivate & CR4_PAE) && (guest_state.efer & EFER_LMA))
		trigger_gpfault(0);

	guest_state.controls[4] = val;
}

void mov_to_control(uint64_t val, uint8_t control)
{
	switch (control) {
	case 0: mov_to_cr0(val); break;
	case 4: mov_to_cr4(val); break;
	default:
		guest_state.controls[control] = val;
	}
}

void set_control(uint64_t val, uint8_t control)
{
	guest_state.controls[control] = val;
}


uint64_t rdmsr(uint32_t addr)
{
	switch (addr) {
	case MSR_EFER: return guest_state.efer;
	default:       return ~0ul;
	}
}

static void wrmsr_efer(uint64_t val)
{
	uint64_t activate = val & ~guest_state.efer;
	uint64_t deactivate = guest_state.efer & ~val;

	if (activate & EFER_LMA)
		trigger_gpfault(0);

	if ((activate & EFER_LME) && (guest_state.controls[0] & CR0_PG))
		trigger_gpfault(0);
	if ((deactivate & EFER_LME) && (guest_state.controls[0] & CR0_PG))
		trigger_gpfault(0);

	guest_state.efer = val;
}

void wrmsr(uint32_t addr, uint64_t val)
{
	switch (addr) {
	case MSR_EFER: wrmsr_efer(val); break;
	}
}


void cli(void)
{
	/* nothing to do */
}

void sti(void)
{
	/* nothing to do */
}

uint64_t interrupt_entry(uint8_t vec)
{
	uint64_t ret = 0;

	if (vec >= guest_state.idt_size)
		return 0;

	ret |= (guest_state.idt[vec * 2    ] >>  0) & 0x000000000000ffff;
	ret |= (guest_state.idt[vec * 2    ] >> 32) & 0x00000000ffff0000;
	ret |= (guest_state.idt[vec * 2 + 1] << 32) & 0xffffffff00000000;

	return ret;
}

void trigger_interrupt(uint8_t vec, uint64_t code)
{
	guest_state.itpending[vec] = 1;
	guest_state.itcode[vec] = code;
}

int pending_interrupt(int clear, uint8_t *vec, uint64_t *code)
{
	size_t i;

	for (i = 0; i < INTERRUPT_COUNT; i++) {
		if (guest_state.itpending[i] == 0)
			continue;

		*vec = (uint8_t) i;
		*code = guest_state.itcode[i];

		if (clear)
			guest_state.itpending[i] = 0;

		return 1;
	}

	return 0;
}


static int is_ignorable(uint16_t port)
{
	switch (port) {
	case PORT_VGA_ADDR:
	case PORT_VGA_DATA:
	case PORT_PIC_MASTER_COMMAND:
	case PORT_PIC_MASTER_DATA:
	case PORT_PIC_SLAVE_COMMAND:
	case PORT_PIC_SLAVE_DATA:
		return 1;
	default:
		return 0;
	}
}

void out8(uint16_t port, uint8_t val __attribute__ ((unused)))
{
	if (is_ignorable(port))
		return;
	unsupported("out");
}

void out16(uint16_t port, uint16_t val __attribute__ ((unused)))
{
	if (is_ignorable(port))
		return;
	unsupported("out");
}

void out32(uint16_t port, uint32_t val __attribute__ ((unused)))
{
	if (is_ignorable(port))
		return;
	unsupported("out");
}


void write_vga(uint16_t off, uint16_t val)
{
	if (off >= VGA_SIZE)
		abort();

	guest_state.vga[off] = val;
	display_vga(); 
}

uint16_t read_vga(uint16_t off)
{
	if (off >= VGA_SIZE)
		abort();

	return guest_state.vga[off];
}

void display_vga(void)
{
	size_t i, j, idx;

	for (i = 0; i < VGA_LINES; i++) {
		for (j = 0; j < VGA_COLUMNS; j++) {
			idx = i * VGA_COLUMNS + j;
			printf("%c", guest_state.vga[idx] & 0xff);
		}
		printf("\n");
	}
}
