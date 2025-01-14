#include "intercept.h"

#include <xed-interface.h>
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <string.h>
#include <sys/mman.h>
#include <ucontext.h>

#include "memory.h"
#include "shadow.h"
#include "state.h"


#define HANDLER_STACK_SIZE   0x4000

#define REG_R8                0
#define REG_R9                1
#define REG_R10               2
#define REG_R11               3
#define REG_R12               4
#define REG_R13               5
#define REG_R14               6
#define REG_R15               7
#define REG_RDI               8
#define REG_RSI               9
#define REG_RBP              10
#define REG_RBX              11
#define REG_RDX              12
#define REG_RAX              13
#define REG_RCX              14
#define REG_RSP              15
#define REG_RIP              16
#define REG_EFL              17

#define MAX_INSTR_LEN        16


__attribute__ ((aligned(0x1000)))
static char guest_exit_stack[HANDLER_STACK_SIZE];


static void emulation_failure(ucontext_t *uc)
{
	printf("emulation failure %llx\n", uc->uc_mcontext.gregs[REG_RIP]);
	exit(EXIT_FAILURE);
}


static uint64_t read_register(xed_reg_enum_t reg, ucontext_t *uc)
{
	uint64_t mask, len = xed_get_register_width_bits(reg);
	xed_reg_enum_t enclosing = xed_get_largest_enclosing_register(reg);
	uint64_t val;

	if (len < 64)
		mask = (1ul << len);
	else
		mask = 0;
	mask -= 1;

	switch (enclosing) {
	case XED_REG_RAX: val = uc->uc_mcontext.gregs[REG_RAX]; break;
	case XED_REG_RBX: val = uc->uc_mcontext.gregs[REG_RBX]; break;
	case XED_REG_RCX: val = uc->uc_mcontext.gregs[REG_RCX]; break;
	case XED_REG_RDX: val = uc->uc_mcontext.gregs[REG_RDX]; break;
	case XED_REG_RDI: val = uc->uc_mcontext.gregs[REG_RDI]; break;
	case XED_REG_RSI: val = uc->uc_mcontext.gregs[REG_RSI]; break;
	case XED_REG_RBP: val = uc->uc_mcontext.gregs[REG_RBP]; break;
	case XED_REG_RSP: val = uc->uc_mcontext.gregs[REG_RSP]; break;
	case XED_REG_R8:  val = uc->uc_mcontext.gregs[REG_R8] ; break;
	case XED_REG_R9:  val = uc->uc_mcontext.gregs[REG_R9] ; break;
	case XED_REG_R10: val = uc->uc_mcontext.gregs[REG_R10]; break;
	case XED_REG_R11: val = uc->uc_mcontext.gregs[REG_R11]; break;
	case XED_REG_R12: val = uc->uc_mcontext.gregs[REG_R12]; break;
	case XED_REG_R13: val = uc->uc_mcontext.gregs[REG_R13]; break;
	case XED_REG_R14: val = uc->uc_mcontext.gregs[REG_R14]; break;
	case XED_REG_R15: val = uc->uc_mcontext.gregs[REG_R15]; break;
	default:
		emulation_failure(uc);
	}

	return val & mask;
}

static void write_register(xed_reg_enum_t reg, ucontext_t *uc, uint64_t val)
{
	uint64_t mask, len = xed_get_register_width_bits(reg);
	xed_reg_enum_t enclosing = xed_get_largest_enclosing_register(reg);
	uint64_t *ptr;
	void *tmp;

	if (len < 64)
		mask = (1ul << len);
	else
		mask = 0;
	mask -= 1;

	switch (enclosing) {
	case XED_REG_RAX: tmp = &uc->uc_mcontext.gregs[REG_RAX]; break;
	case XED_REG_RBX: tmp = &uc->uc_mcontext.gregs[REG_RBX]; break;
	case XED_REG_RCX: tmp = &uc->uc_mcontext.gregs[REG_RCX]; break;
	case XED_REG_RDX: tmp = &uc->uc_mcontext.gregs[REG_RDX]; break;
	case XED_REG_RDI: tmp = &uc->uc_mcontext.gregs[REG_RDI]; break;
	case XED_REG_RSI: tmp = &uc->uc_mcontext.gregs[REG_RSI]; break;
	case XED_REG_RBP: tmp = &uc->uc_mcontext.gregs[REG_RBP]; break;
	case XED_REG_RSP: tmp = &uc->uc_mcontext.gregs[REG_RSP]; break;
	case XED_REG_R8:  tmp = &uc->uc_mcontext.gregs[REG_R8] ; break;
	case XED_REG_R9:  tmp = &uc->uc_mcontext.gregs[REG_R9] ; break;
	case XED_REG_R10: tmp = &uc->uc_mcontext.gregs[REG_R10]; break;
	case XED_REG_R11: tmp = &uc->uc_mcontext.gregs[REG_R11]; break;
	case XED_REG_R12: tmp = &uc->uc_mcontext.gregs[REG_R12]; break;
	case XED_REG_R13: tmp = &uc->uc_mcontext.gregs[REG_R13]; break;
	case XED_REG_R14: tmp = &uc->uc_mcontext.gregs[REG_R14]; break;
	case XED_REG_R15: tmp = &uc->uc_mcontext.gregs[REG_R15]; break;
	default:
		emulation_failure(uc);
	}

	ptr = (uint64_t *) tmp;
	*ptr &= ~mask;
	*ptr |= val & mask;
}


static void emulate_lgdt(xed_decoded_inst_t *inst, ucontext_t *uc)
{
	uint64_t disp = xed_decoded_inst_get_memory_displacement(inst, 0);

	lgdt(disp);
	uc->uc_mcontext.gregs[REG_RIP] += xed_decoded_inst_get_length(inst);
}

static void emulate_lidt(xed_decoded_inst_t *inst, ucontext_t *uc)
{
	uint64_t disp = xed_decoded_inst_get_memory_displacement(inst, 0);

	lidt(disp);
	uc->uc_mcontext.gregs[REG_RIP] += xed_decoded_inst_get_length(inst);
}

static void emulate_rdmsr(xed_decoded_inst_t *inst, ucontext_t *uc)
{
	uint32_t addr = uc->uc_mcontext.gregs[REG_RCX];
	uint64_t val = rdmsr(addr);

	uc->uc_mcontext.gregs[REG_RAX] &= ~0xffffffff;
	uc->uc_mcontext.gregs[REG_RAX] |= (val & 0xffffffff);
	
	uc->uc_mcontext.gregs[REG_RDX] &= ~0xffffffff;
	uc->uc_mcontext.gregs[REG_RDX] |= ((val >> 32) & 0xffffffff);

	uc->uc_mcontext.gregs[REG_RIP] += xed_decoded_inst_get_length(inst);
}

static void emulate_wrmsr(xed_decoded_inst_t *inst, ucontext_t *uc)
{
	uint32_t addr = uc->uc_mcontext.gregs[REG_RCX];
	uint64_t val = 0;

	val |= uc->uc_mcontext.gregs[REG_RAX] & 0xffffffff;
	val |= (uc->uc_mcontext.gregs[REG_RDX] & 0xffffffff) << 32;

	wrmsr(addr, val);

	uc->uc_mcontext.gregs[REG_RIP] += xed_decoded_inst_get_length(inst);
}

static void emulate_out(xed_decoded_inst_t *inst, ucontext_t *uc)
{
	const xed_inst_t *xi = xed_decoded_inst_inst(inst);
	xed_operand_enum_t opn0 = xed_operand_name(xed_inst_operand(xi, 0));
	xed_operand_enum_t opn1 = xed_operand_name(xed_inst_operand(xi, 1));
	xed_reg_enum_t reg;
	uint16_t port;
	uint64_t val;
	size_t len;

	switch (opn0) {
	case XED_OPERAND_REG0:
		reg = xed_decoded_inst_get_reg(inst, opn0);
		port = (uint16_t) read_register(reg, uc);
		break;
	case XED_OPERAND_IMM0:
		port = xed_decoded_inst_get_unsigned_immediate(inst);
		break;
	default:
		emulation_failure(uc);
	}

	reg = xed_decoded_inst_get_reg(inst, opn1);
	len = xed_get_register_width_bits(reg);
	val = read_register(reg, uc);

	switch (len) {
	case 8:  out8(port,  (uint8_t)  val); break;
	case 16: out16(port, (uint16_t) val); break;
	case 32: out32(port, (uint32_t) val); break;
	default: emulation_failure(uc);
	}

	uc->uc_mcontext.gregs[REG_RIP] += xed_decoded_inst_get_length(inst);
}

static void emulate_sti(xed_decoded_inst_t *inst, ucontext_t *uc)
{
	sti();
	uc->uc_mcontext.gregs[REG_RIP] += xed_decoded_inst_get_length(inst);
}

static void emulate_invlpg(xed_decoded_inst_t *inst, ucontext_t *uc)
{
	uc->uc_mcontext.gregs[REG_RIP] += xed_decoded_inst_get_length(inst);
}

static void emulate_ljmp(xed_decoded_inst_t *inst, ucontext_t *uc)
{
	uint64_t disp = xed_decoded_inst_get_branch_displacement(inst);
	uint16_t cs = xed_decoded_inst_get_unsigned_immediate(inst);

	mov_to_selector(cs, SELECTOR_CS);
	uc->uc_mcontext.gregs[REG_RIP] = disp;
}

static void emulate_mov_to_sel(xed_reg_enum_t dest, xed_reg_enum_t src,
			       ucontext_t *uc)
{
	uint64_t val = read_register(src, uc);
	uint8_t sel = 0;

	switch (dest) {
	case XED_REG_DS: sel = SELECTOR_DS; break;
	case XED_REG_ES: sel = SELECTOR_ES; break;
	case XED_REG_FS: sel = SELECTOR_FS; break;
	case XED_REG_GS: sel = SELECTOR_GS; break;
	case XED_REG_SS: sel = SELECTOR_SS; break;
	default: emulation_failure(uc);
	}

	mov_to_selector(val, sel);
}

static void emulate_mov_to_cr(xed_reg_enum_t dest, xed_reg_enum_t src,
			      ucontext_t *uc)
{
	uint64_t val = read_register(src, uc);
	uint8_t crn = 0;

	switch (dest) {
	case XED_REG_CR0: crn = 0; break;
	case XED_REG_CR2: crn = 2; break;
	case XED_REG_CR3: crn = 3; break;
	case XED_REG_CR4: crn = 4; break;
	case XED_REG_CR8: crn = 8; break;
	default: emulation_failure(uc);
	}

	mov_to_control(val, crn);
}

static void emulate_mov_from_cr(xed_reg_enum_t dest, xed_reg_enum_t src,
				ucontext_t *uc)
{
	uint8_t crn = 0;
	uint64_t val;

	switch (src) {
	case XED_REG_CR0: crn = 0; break;
	case XED_REG_CR2: crn = 2; break;
	case XED_REG_CR3: crn = 3; break;
	case XED_REG_CR4: crn = 4; break;
	case XED_REG_CR8: crn = 8; break;
	default: emulation_failure(uc);
	}

	val = mov_from_control(crn);
	write_register(dest, uc, val);
}

static void emulate_mov_cr(xed_decoded_inst_t *inst, ucontext_t *uc)
{
	const xed_inst_t *xi = xed_decoded_inst_inst(inst);
	xed_operand_enum_t opn0 = xed_operand_name(xed_inst_operand(xi, 0));
	xed_operand_enum_t opn1 = xed_operand_name(xed_inst_operand(xi, 1));
	xed_reg_enum_t reg0 = xed_decoded_inst_get_reg(inst, opn0);
	xed_reg_enum_t reg1 = xed_decoded_inst_get_reg(inst, opn1);

	if (xed_reg_class(reg0) == XED_REG_CLASS_CR) {
		emulate_mov_to_cr(reg0, reg1, uc);
		goto out;
	}

	if (xed_reg_class(reg1) == XED_REG_CLASS_CR) {
		emulate_mov_from_cr(reg0, reg1, uc);
		goto out;
	}

	emulation_failure(uc);

 out:
	uc->uc_mcontext.gregs[REG_RIP] += xed_decoded_inst_get_length(inst);
}

static void emulate_mov_to_mem(xed_decoded_inst_t *inst, uint64_t val,
			       size_t len, siginfo_t *si, ucontext_t *uc)
{
	vaddr_t addr = (vaddr_t) si->si_addr;
	int done = trap_write(addr, len, val);

	if (!done)
		return;
	uc->uc_mcontext.gregs[REG_RIP] += xed_decoded_inst_get_length(inst);
}

static void emulate_mov_from_mem(xed_decoded_inst_t *inst, xed_reg_enum_t dest,
				 siginfo_t *si, ucontext_t *uc)
{
	vaddr_t addr = (vaddr_t) si->si_addr;
	size_t len = xed_get_register_width_bits(dest) / 8;
	uint64_t val;
	int done;

	done = trap_read(addr, len, &val);
	if (!done)
		return;

	write_register(dest, uc, val);
	uc->uc_mcontext.gregs[REG_RIP] += xed_decoded_inst_get_length(inst);
}

static void emulate_mov(xed_decoded_inst_t *inst, siginfo_t *si,ucontext_t *uc)
{
	const xed_inst_t *xi = xed_decoded_inst_inst(inst);
	xed_operand_enum_t opn0 = xed_operand_name(xed_inst_operand(xi, 0));
	xed_operand_enum_t opn1 = xed_operand_name(xed_inst_operand(xi, 1));
	xed_reg_enum_t reg0, reg1;
	uint64_t val = 0;
	size_t len = 0;

	if (opn0 == XED_OPERAND_REG0 && opn1 == XED_OPERAND_REG1) {
		reg0 = xed_decoded_inst_get_reg(inst, opn0);
		reg1 = xed_decoded_inst_get_reg(inst, opn1);

		if (xed_reg_class(reg0) == XED_REG_CLASS_SR) {
			emulate_mov_to_sel(reg0, reg1, uc);
			goto out;
		}

		emulation_failure(uc);
	}

	if (opn0 == XED_OPERAND_MEM0) {
		if (opn1 == XED_OPERAND_REG0) {
			reg1 = xed_decoded_inst_get_reg(inst, opn1);
			len = xed_get_register_width_bits(reg1) / 8;
			val = read_register(reg1, uc);
		} else if (opn1 == XED_OPERAND_IMM0) {
			val = xed_decoded_inst_get_unsigned_immediate(inst);
			len = xed_decoded_inst_get_immediate_width(inst);
		}
		emulate_mov_to_mem(inst, val, len, si, uc);
		return;
	}

	if (opn1 == XED_OPERAND_MEM0) {
		reg0 = xed_decoded_inst_get_reg(inst, opn0);
		emulate_mov_from_mem(inst, reg0, si, uc);
		return;
	}

	emulation_failure(uc);
 out:
	uc->uc_mcontext.gregs[REG_RIP] += xed_decoded_inst_get_length(inst);
}

static void emulate_iretq(ucontext_t *uc)
{
	uint64_t rsp = uc->uc_mcontext.gregs[REG_RSP];
	uint64_t *stack = (uint64_t *) rsp;

	uc->uc_mcontext.gregs[REG_RIP] = stack[0];
	uc->uc_mcontext.gregs[REG_RSP] = rsp + 5 * sizeof (uint64_t);
}

static void branch_interrupt(uint8_t vec, uint64_t code, ucontext_t *uc)
{
	uint64_t rsp = uc->uc_mcontext.gregs[REG_RSP];
	uint64_t *stack = (uint64_t *) rsp;

	stack[-1] = get_selector(SELECTOR_SS);
	stack[-2] = rsp;
	stack[-3] = 0;                           /* rflags - not implemented */
	stack[-4] = get_selector(SELECTOR_CS);
	stack[-5] = uc->uc_mcontext.gregs[REG_RIP];

	switch (vec) {
	case INTERRUPT_DF:
	case INTERRUPT_CS:
	case INTERRUPT_TS:
	case INTERRUPT_NP:
	case INTERRUPT_SS:
	case INTERRUPT_GP:
	case INTERRUPT_PF:
	case INTERRUPT_MF:
	case INTERRUPT_AC:
		stack[-6] = code;
		uc->uc_mcontext.gregs[REG_RSP] = rsp - 6 * sizeof (uint64_t);
		break;
	default:
		uc->uc_mcontext.gregs[REG_RSP] = rsp - 5 * sizeof (uint64_t);
		break;
	}

	uc->uc_mcontext.gregs[REG_RIP] = interrupt_entry(vec);
}

static void emulate(siginfo_t *si, ucontext_t *uc)
{
	uint64_t rip = uc->uc_mcontext.gregs[REG_RIP];
	xed_decoded_inst_t inst;
	uint64_t mode = 0, awidth = 0;

	switch (guest_state.mode) {
	case MODE_32_BITS:
		mode = XED_MACHINE_MODE_LEGACY_32;
		awidth = XED_ADDRESS_WIDTH_32b;
		break;
	case MODE_64_BITS:
		mode = XED_MACHINE_MODE_LONG_64;
		awidth = XED_ADDRESS_WIDTH_64b;
		break;
	}

	xed_decoded_inst_zero(&inst);
	xed_decoded_inst_set_mode(&inst, mode, awidth);
	xed_decode(&inst, (uint8_t *) rip, 15);

	switch (xed_decoded_inst_get_iclass(&inst)) {
	case XED_ICLASS_LGDT:
		emulate_lgdt(&inst, uc);
		break;
	case XED_ICLASS_LIDT:
		emulate_lidt(&inst, uc);
		break;
	case XED_ICLASS_MOVZX:
	case XED_ICLASS_MOV:
		emulate_mov(&inst, si, uc);
		break;
	case XED_ICLASS_MOV_CR:
		emulate_mov_cr(&inst, uc);
		break;
	case XED_ICLASS_JMP_FAR:
		emulate_ljmp(&inst, uc);
		break;
	case XED_ICLASS_RDMSR:
		emulate_rdmsr(&inst, uc);
		break;
	case XED_ICLASS_WRMSR:
		emulate_wrmsr(&inst, uc);
		break;
	case XED_ICLASS_OUT:
		emulate_out(&inst, uc);
		break;
	case XED_ICLASS_STI:
		emulate_sti(&inst, uc);
		break;
	case XED_ICLASS_INVLPG:
		emulate_invlpg(&inst, uc);
		break;
	case XED_ICLASS_IRETQ:
		emulate_iretq(uc);
		break;
	case XED_ICLASS_HLT:
		display_vga();
		exit(EXIT_SUCCESS);
		break;
	default:
		printf("unhandled instruction at %lx: %s\n", rip,
		       xed_iclass_enum_t2str
		       (xed_decoded_inst_get_iclass(&inst)));
		display_vga();
		exit(EXIT_FAILURE);
		break;
	}
}

static void handler(int signum __attribute__ ((unused)),
		    siginfo_t *si, ucontext_t *uc)
{
	uint8_t vec;
	uint64_t code;
	
	unprotect_low_memory();

	emulate(si, uc);

	if (pending_interrupt(1, &vec, &code))
		branch_interrupt(vec, code, uc);

	protect_low_memory();
}

void setup_interception(void)
{
	struct sigaction sa;
	stack_t ss;

	sigemptyset(&sa.sa_mask);
	sa.sa_sigaction = (void (*)(int, siginfo_t *, void *)) handler;
	sa.sa_flags = SA_SIGINFO | SA_ONSTACK;

	ss.ss_sp = guest_exit_stack;
	ss.ss_size = sizeof (guest_exit_stack);
	ss.ss_flags = 0;
	sigaltstack(&ss, NULL);

	sigaction(SIGSEGV, &sa, NULL);
	sigaction(SIGILL, &sa, NULL);

	xed_tables_init();
}
