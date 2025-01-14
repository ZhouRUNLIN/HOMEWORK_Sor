#include <idt.h>
#include <types.h>
#include <printk.h>
#include <x86.h>


#define IO_WAIT_PORT               0x80

#define PIC_MASTER_COMMAND_PORT    0x20
#define PIC_MASTER_DATA_PORT       0x21
#define PIC_SLAVE_COMMAND_PORT     0xa0
#define PIC_SLAVE_DATA_PORT        0xa1
#define PIC_COMMAND_INIT           0x11
#define PIC_DATA_8086              0x01
#define PIC_DATA_DISABLE           0xff
#define PIC_MASTER_REMAP_IRQ       0x20
#define PIC_MASTER_IDENTITY        0x04
#define PIC_SLAVE_REMAP_IRQ        0x28
#define PIC_SLAVE_IDENTITY         0x02

#define LAPIC_BASE_MSR             0x1b
#define LAPIC_BASE_ENABLE          (1ul << 11)
#define LAPIC_VADDR                0x200000      /* see memory.c and entry.S */
#define LAPIC_TIMER_PERIODIC       (1u << 17)
#define LAPIC_DISABLE              (1u << 16)
#define LAPIC_SPURIOUS_ASE         (1u << 8)

#define INTERRUPT_GATE_TYPE        0xee00
#define TRAP_GATE_TYPE             0xef00


extern vaddr_t trap_vector[];

static struct {
	uint16_t off0;
	uint16_t sel;
	uint16_t flags;
	uint16_t off1;
	uint32_t off2;
	uint32_t _ign0;
} __attribute__((packed)) idt64[INTERRUPT_VECTOR_SIZE];

struct {
	uint16_t limit;
	uint64_t base;
} __attribute__((packed)) idtr64;


interrupt_handler_t  interrupt_vector[INTERRUPT_VECTOR_SIZE];

static void default_interrupt(struct interrupt_context *ctx)
{
	uint64_t cr2;

	printk("Interrupt %lu (err = %#lx)\n", ctx->itnum, ctx->errcode);

	if (ctx->itnum == INT_PF) {
		cr2 = store_cr2();
		printk("  CR2 = %p\n", cr2);
	}

	asm volatile ("hlt");
}

void trap(struct interrupt_context *ctx)
{
	interrupt_handler_t handler = interrupt_vector[ctx->itnum];

	if (handler == NULL)
		default_interrupt(ctx);
	else
		handler(ctx);
}


static void io_wait(void)
{
       out8(IO_WAIT_PORT, 0);
}

void remap_pic(void)
{
       uint8_t master_mask, slave_mask;

       master_mask = in8(PIC_MASTER_DATA_PORT);
       slave_mask = in8(PIC_SLAVE_DATA_PORT);

       out8(PIC_MASTER_COMMAND_PORT, PIC_COMMAND_INIT);
       io_wait();
       out8(PIC_SLAVE_COMMAND_PORT, PIC_COMMAND_INIT);
       io_wait();

       out8(PIC_MASTER_DATA_PORT, PIC_MASTER_REMAP_IRQ);
       io_wait();
       out8(PIC_SLAVE_DATA_PORT, PIC_SLAVE_REMAP_IRQ);
       io_wait();

       out8(PIC_MASTER_DATA_PORT, PIC_MASTER_IDENTITY);
       io_wait();
       out8(PIC_SLAVE_DATA_PORT, PIC_SLAVE_IDENTITY);
       io_wait();

       out8(PIC_MASTER_DATA_PORT, PIC_DATA_8086);
       io_wait();
       out8(PIC_SLAVE_DATA_PORT, PIC_DATA_8086);
       io_wait();

       out8(PIC_MASTER_DATA_PORT, master_mask);
       out8(PIC_SLAVE_DATA_PORT, slave_mask);
}

void disable_pic(void)
{
	out8(PIC_MASTER_DATA_PORT, PIC_DATA_DISABLE);
	out8(PIC_SLAVE_DATA_PORT, PIC_DATA_DISABLE);
}

void setup_interrupts(void)
{
	size_t i;

	idtr64.limit = sizeof (idt64) - 1;
	idtr64.base = (uint64_t) &idt64;

	for (i = 0; i < INTERRUPT_VECTOR_SIZE; i++) {
		idt64[i].off0 = ((trap_vector[i] >> 00) &     0xffff);
		idt64[i].off1 = ((trap_vector[i] >> 16) &     0xffff);
		idt64[i].off2 = ((trap_vector[i] >> 32) & 0xffffffff);
		idt64[i].sel = KERNEL_CODE_SELECTOR;

		if (i == INT_USER_SYSCALL) {
			idt64[i].flags = TRAP_GATE_TYPE;
		} else {
			idt64[i].flags = INTERRUPT_GATE_TYPE;
		}
	}

	asm volatile ("lidt idtr64");
}


struct lapic_register
{
	volatile uint32_t  reg;
	uint32_t           _pad[3];
} __attribute__ ((packed));

struct lapic
{
	struct lapic_register  _pad0[2];
	struct lapic_register  id;
	struct lapic_register  version;
	struct lapic_register  _pad1[4];
	struct lapic_register  tpr;
	struct lapic_register  apr;
	struct lapic_register  ppr;
	struct lapic_register  eoi;
	struct lapic_register  rrr;
	struct lapic_register  ldr;
	struct lapic_register  dfr;
	struct lapic_register  svr;
	struct lapic_register  isr[8];
	struct lapic_register  tmr[8];
	struct lapic_register  irr[8];
	struct lapic_register  esr;
	struct lapic_register  _pad2[7];
	struct lapic_register  icr_low;
	struct lapic_register  icr_high;
	struct lapic_register  timer_entry;
	struct lapic_register  thermal_entry;
	struct lapic_register  performance_entry;
	struct lapic_register  local0_entry;
	struct lapic_register  local1_entry;
	struct lapic_register  error_entry;
	struct lapic_register  timer_initial;
	struct lapic_register  timer_current;
	struct lapic_register  _pad3[4];
	struct lapic_register  timer_divide;
	struct lapic_register  _pad4;
	struct lapic_register  extended_feature;
	struct lapic_register  extended_control;
	struct lapic_register  seoi;
	struct lapic_register  _pad5[5];
	struct lapic_register  ier[8];
	struct lapic_register  extended_vector[3];
} __attribute__ ((packed));

struct lapic *lapic = (struct lapic *) LAPIC_VADDR;


static void timer_interrupt(struct interrupt_context *ctx
			    __attribute__ ((unused)))
{
	lapic->eoi.reg = 0;
}

void setup_apic(void)
{
	uint64_t val = rdmsr(LAPIC_BASE_MSR);

	interrupt_vector[INT_USER_TIMER] = timer_interrupt;

	wrmsr(LAPIC_BASE_MSR, val & ~LAPIC_BASE_ENABLE);

	lapic->tpr.reg = 0;

	lapic->timer_entry.reg = LAPIC_DISABLE;
	lapic->thermal_entry.reg = LAPIC_DISABLE;
	lapic->performance_entry.reg = LAPIC_DISABLE;
	lapic->local0_entry.reg = LAPIC_DISABLE;
	lapic->local1_entry.reg = LAPIC_DISABLE;
	lapic->error_entry.reg = LAPIC_DISABLE;

	lapic->timer_entry.reg = INT_USER_TIMER | LAPIC_TIMER_PERIODIC;
	lapic->timer_divide.reg = 3;                         /* divide by 16 */
	lapic->timer_initial.reg = 0x1000;   /* by the power of black magic! */

	wrmsr(LAPIC_BASE_MSR, val | LAPIC_BASE_ENABLE);

	lapic->svr.reg |= (INT_USER_SPURIOUS | LAPIC_SPURIOUS_ASE);
}
