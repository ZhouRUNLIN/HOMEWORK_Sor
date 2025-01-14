#ifndef _INCLUDE_SHADOW_H_
#define _INCLUDE_SHADOW_H_


#include <stdint.h>

#include "memory.h"


/*
 * Memory access emulation and shadow paging.
 * Memory model for Janus VMM
 *
 * +----------------------+ 0xfffffffffffffff
 * | Monitor              |
 * | (forbidden accesses) |
 * +----------------------+ 0x700000000000
 * | Guest                |
 * +----------------------+ 0x100000000
 * | Monitor              |
 * | (forbidden accesses) |
 * +----------------------+ 0x80000000
 * | Guest                |
 * +----------------------+ 0x100000
 * | Monitor              |
 * | (trapped accesses)   |
 * +----------------------+ 0x0
 *
 * Trapped are safe to perform from the guest.
 * Forbidden accesses may succeed but would result in process corruption.
 *
 * Memory trap return code:
 *   0  - the guest should retry (typically, the memory has been mapped)
 *   !0 - the access has been emulated
 */


void set_flat_mapping(size_t ram);

void set_page_table(void);                  /* install new page table in CR3 */

int trap_read(vaddr_t addr, size_t size, uint64_t *val);      /* memory read */

int trap_write(vaddr_t addr, size_t size, uint64_t val);     /* memory write */



#endif
