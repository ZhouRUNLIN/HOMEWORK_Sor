#ifndef _INCLUDE_HWDETECT_H_
#define _INCLUDE_HWDETECT_H_


#define _GNU_SOURCE

#include <sched.h>
#include <stdint.h>
#include <time.h>


#define PAGE_SHIFT   12
#define PAGE_SIZE    (1ul << PAGE_SHIFT)


static inline uint64_t now(void)
{
	struct timespec ts;

	clock_gettime(CLOCK_REALTIME, &ts);
	return ((ts.tv_sec * 1000000000ul) + ts.tv_nsec);
}

static inline void writemem(void *addr)
{
	asm volatile ("movb $0x1, (%0)" : : "r" (addr) : "memory");
}

static inline void setcore(uint8_t coreid)
{
	cpu_set_t set;

	CPU_ZERO(&set);
	CPU_SET(coreid, &set);

	sched_setaffinity(0, sizeof (set), &set);
}


#endif
