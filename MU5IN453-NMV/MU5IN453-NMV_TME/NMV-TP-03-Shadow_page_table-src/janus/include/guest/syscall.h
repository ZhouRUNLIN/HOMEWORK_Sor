#ifndef _INCLUDE_KERNEL_H_
#define _INCLUDE_KERNEL_H_


#include <stdarg.h>
#include <types.h>


#define TASK_HEADER_MAGIC  0xff10ADa64bC0DEff

#define SYSCALL_PRINT      (0ul)
#define SYSCALL_PRINTNUM   (1ul)
#define SYSCALL_MMAP       (2ul)
#define SYSCALL_MUNMAP     (3ul)
#define SYSCALL_YIELD      (4ul)
#define SYSCALL_EXIT       (5ul)
#define SYSCALL_FORK       (6ul)


struct task_header
{
	uint64_t  magic;
	vaddr_t   load_addr;
	vaddr_t   load_end_addr;
	vaddr_t   bss_end_addr;
	vaddr_t   header_addr;
	vaddr_t   entry_addr;
} __attribute__((packed));

void setup_syscalls(void);


struct syscall_context
{
	uint64_t  callnum;
	uint64_t  rflags;
	uint64_t  rip;
	uint64_t  rsp;
} __attribute__((packed));

static inline int syscall(size_t callnum, uint64_t arg0)
{
	int ret;

	asm volatile ("int $0x80\n"
		      : "=a" (ret)
		      : "D" (callnum), "S" (arg0));

	return ret;
}


static inline void syscall_print(const char *str)
{
	syscall(SYSCALL_PRINT, (uint64_t) str);
}

static inline void syscall_printnum(uint64_t num)
{
	syscall(SYSCALL_PRINTNUM, num);
}

static inline void syscall_mmap(vaddr_t addr)
{
	syscall(SYSCALL_MMAP, addr);
}

static inline void syscall_munmap(vaddr_t addr)
{
	syscall(SYSCALL_MUNMAP, addr);
}

static inline void syscall_yield(void)
{
	syscall(SYSCALL_YIELD, 0);
}

static inline void syscall_exit(void)
{
	syscall(SYSCALL_EXIT, 0);
}

static inline int syscall_fork(void)
{
	return syscall(SYSCALL_FORK, 0);
}


#endif
