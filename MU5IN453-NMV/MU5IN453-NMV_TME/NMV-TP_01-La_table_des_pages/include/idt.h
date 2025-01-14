#ifndef _INCLUDE_IDT_H_
#define _INCLUDE_IDT_H_


#include <types.h>


#define INTERRUPT_VECTOR_SIZE   256

#define INT_DE                  0
#define INT_DB                  1
#define INT_NMI                 2
#define INT_BP                  3
#define INT_OF                  4
#define INT_BR                  5
#define INT_UD                  6
#define INT_NM                  7
#define INT_DF                  8
#define INT_CO                  9
#define INT_TS                  10
#define INT_NP                  11
#define INT_SS                  12
#define INT_GP                  13
#define INT_PF                  14
#define INT_MF                  16
#define INT_AC                  17
#define INT_MC                  18
#define INT_XF                  19
#define INT_SX                  30
#define INT_USER_MIN            32
#define INT_USER_TIMER          32
#define INT_USER_SYSCALL        128
#define INT_USER_ENTER_TASKS    129
#define INT_USER_SPURIOUS       130
#define INT_USER_MAX            255


struct interrupt_context
{
	uint64_t  rbp;
	uint64_t  rbx;
	uint64_t  r15;
	uint64_t  r14;
	uint64_t  r13;
	uint64_t  r12;
	uint64_t  r11;
	uint64_t  r10;
	uint64_t  r9;
	uint64_t  r8;
	uint64_t  rcx;
	uint64_t  rdx;
	uint64_t  rsi;
	uint64_t  rdi;
	uint64_t  rax;
	uint64_t  itnum;
	uint64_t  errcode;
	uint64_t  rip;
	uint64_t  cs;
	uint64_t  rflags;
	uint64_t  rsp;
	uint64_t  ss;
} __attribute__((packed));

typedef void (*interrupt_handler_t)(struct interrupt_context *ctx);

extern interrupt_handler_t  interrupt_vector[INTERRUPT_VECTOR_SIZE];


void remap_pic(void);

void disable_pic(void);

void setup_apic(void);

void setup_interrupts(void);


static inline void cli(void)
{
	asm volatile ("cli");
}

static inline void sti(void)
{
	asm volatile ("sti");
}


#endif
