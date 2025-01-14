#include "memory.h"

#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

#include "state.h"


#define MAPS_LINE_SIZE               1024
#define MAPS_MAX_SIZE                64
#define MAPS_LOW_LIMIT               0x100000
#define GUEST_MEMORY_BACKEND_PATH    "/tmp/janus-backend.mem"


struct maps
{
	vaddr_t   start;                               /* start of the range */
	vaddr_t   end;                                   /* end of the range */
	uint8_t   prot;                                   /* mmap protection */
} __attribute__ ((packed));

struct mapping
{
	struct maps   maps;                    /* memory mapping information */
	char          file[256];                /* possible backed file path */
};


static size_t low_memory_size;
static struct maps low_memory_maps[MAPS_MAX_SIZE];
static int guest_memory_backend;
static size_t guest_memory_size;
static uint64_t *guest_bitset;


static void parse_host_mapping(struct mapping *entry, char *buffer)
{
	char *st = buffer, *ptr = buffer;
	char *err;

	while (*ptr != '-')
		ptr++;

	entry->maps.start = strtol(st, &err, 16);
	if (*err != '-')
		abort();

	ptr += 1;
	st = ptr;

	while (*ptr != ' ')
		ptr++;

	entry->maps.end = strtol(st, &err, 16);
	if (*err != ' ')
		abort();

	ptr += 1;

	entry->maps.prot = 0;
	if (*ptr++ == 'r')
		entry->maps.prot |= PROT_READ;
	if (*ptr++ == 'w')
		entry->maps.prot |= PROT_WRITE;
	if (*ptr++ == 'x')
		entry->maps.prot |= PROT_EXEC;
	
	ptr += 2;

	/* offset */

	while (*ptr != ' ')
		ptr++;
	ptr++;

	/* device */

	while (*ptr != ' ')
		ptr++;
	ptr++;

	/* size */

	while (*ptr != ' ')
		ptr++;

	while (*ptr == ' ')
		ptr++;

	strncpy(entry->file, ptr, sizeof (entry->file));
	entry->file[sizeof (entry->file) - 1] = '\0';

	for (ptr = entry->file; *ptr != '\0'; ptr++)
		if (*ptr == '\n') {
			*ptr = '\0';
			break;
		}
}

static size_t read_host_mapping(struct mapping *dest, size_t len)
{
	char buffer[MAPS_LINE_SIZE];
	FILE *stream;
	size_t i;

	if ((stream = fopen("/proc/self/maps", "r")) == NULL)
		abort();

	i = 0;

	while (fgets(buffer, sizeof (buffer), stream) != NULL) {
		if (i < len)
			parse_host_mapping(dest + i, buffer);
		i++;
	}

	fclose(stream);

	return i;
}

void protect_low_memory(void)
{
	struct mapping *mapping;
	size_t i, len, size;

	mapping = malloc(sizeof (struct mapping) * MAPS_MAX_SIZE);
	if (mapping == NULL)
		abort();

	len = read_host_mapping(mapping, MAPS_MAX_SIZE);

	for (i = 0; i < len; i++) {
		if (mapping[i].maps.start >= MAPS_LOW_LIMIT)
			break;
		low_memory_maps[i] = mapping[i].maps;
	}

	free(mapping);

	if (i == MAPS_MAX_SIZE)
		goto err;
	low_memory_size = i;

	for (i = 0; i < low_memory_size; i++) {
		size = low_memory_maps[i].end - low_memory_maps[i].start;
		mprotect((void *) low_memory_maps[i].start, size,
			 low_memory_maps[i].prot & ~PROT_WRITE);
	}

	return;
 err:
	fprintf(stderr, "Error: unexpected low memory areas\n");
	abort();
}

void unprotect_low_memory(void)
{
	size_t i, size;
	
	for (i = 0; i < low_memory_size; i++) {
		size = low_memory_maps[i].end - low_memory_maps[i].start;
		mprotect((void *) low_memory_maps[i].start, size,
			 low_memory_maps[i].prot);
	}
}

void display_mapping(void)
{
	char buffer[128];

	snprintf(buffer, sizeof (buffer), "cat /proc/%d/maps", getpid());
	system(buffer);
}


void allocate_physical_memory(size_t ram)
{
	size_t bitset_size = ((ram / 64 + 1) * sizeof (*guest_bitset));
	int fd;

	guest_bitset = malloc(bitset_size);
	if (guest_bitset == NULL)
		abort();

	fd = open(GUEST_MEMORY_BACKEND_PATH, O_RDWR | O_CREAT | O_TRUNC, 0644);
	if (fd == -1) {
		perror("open backend");
		abort();
	}

	unlink(GUEST_MEMORY_BACKEND_PATH);

	if (ftruncate(fd, ram) == -1) {
		perror("allocate backend");
		abort();
	}
	
	guest_memory_backend = fd;
	guest_memory_size = ram;

	memset(guest_bitset, 0, bitset_size);
}

paddr_t alloc_page(void)
{
	size_t i, j, len = guest_memory_size / 64 + 1;

	for (i = 0; i < len; i++)
		if (guest_bitset[i] != ~0ul)
			break;

	if (i == len)
		return -1;

	for (j = 0; j < 64; j++) {
		if ((guest_bitset[i] & (1ul << j)) != 0)
			continue;
		guest_bitset[i] |= 1ul << j;
		break;
	}

	return ((64 * i) + j) << 12;
}

void free_page(paddr_t addr)
{
	addr = addr >> 12;
	guest_bitset[addr / 64] &= ~(1ul << (addr % 64));
}

void map_page(vaddr_t vaddr, paddr_t paddr, size_t len)
{
	void *ret = mmap((void *) vaddr, len,
			 PROT_READ | PROT_WRITE | PROT_EXEC,
			 MAP_SHARED | MAP_FIXED, guest_memory_backend, paddr);

	if (ret == MAP_FAILED)
		abort();
}

void unmap_page(vaddr_t vaddr, size_t len)
{
	munmap((void *) vaddr, len);
}

void read_physical(void *dest, size_t size, paddr_t paddr)
{
	size_t sz = size;
	void *tmp;

	if ((sz & 0xfff) != 0)
		sz = (sz + 0x1000) & ~0xfff;

	tmp = mmap(NULL, sz, PROT_READ | PROT_WRITE | PROT_EXEC,
		   MAP_SHARED, guest_memory_backend, paddr & ~0xfff);

	memcpy(dest, tmp + (paddr & 0xfff), size);
	munmap(tmp, sz);
}

void write_physical(const void *src, size_t size, paddr_t paddr)
{
	size_t sz = size;
	void *tmp;

	if ((sz & 0xfff) != 0)
		sz = (sz + 0x1000) & ~0xfff;

	tmp = mmap(NULL, sz, PROT_READ | PROT_WRITE | PROT_EXEC,
		   MAP_SHARED, guest_memory_backend, paddr & ~0xfff);

	memcpy(tmp + (paddr & 0xfff), src, size);
	munmap(tmp, sz);
}
