#include "multiboot2.h"

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>


#define SEARCH_ZONE_LEN               32768

#define MULTIBOOT_MAGIC               0xe85250d6

#define MULTIBOOT_TAG_TYPE_END        0
#define MULTIBOOT_TAG_TYPE_ADDRESS    2
#define MULTIBOOT_TAG_TYPE_ENTRY      3


struct multiboot2_header
{
	uint32_t  magic;
	uint32_t  architecture;
	uint32_t  header_length;
	uint32_t  checksum;
} __attribute__ ((packed));

struct multiboot2_tag
{
	uint16_t  type;
	uint16_t  flags;
	uint32_t  size;
} __attribute__ ((packed));

struct multiboot2_tag_address
{
	uint16_t  type;
	uint16_t  flags;
	uint32_t  size;
	uint32_t  header_addr;
	uint32_t  load_addr;
	uint32_t  load_end_addr;
	uint32_t  bss_end_addr;
} __attribute__ ((packed));

struct multiboot2_tag_entry
{
	uint16_t  type;
	uint16_t  flags;
	uint32_t  size;
	uint32_t  entry_addr;
} __attribute__ ((packed));


static int extract_multiboot2(struct multiboot2_load_info *info, size_t off,
			      const struct multiboot2_header *header)
{
	struct multiboot2_tag *tag;
	struct multiboot2_tag_address *addr;
	struct multiboot2_tag_entry *entry;
	const char *ptr = (const char *) header;
	size_t i = sizeof (*header);

	info->header_off = (uint32_t) off;

	while (i < header->header_length) {
		tag = (struct multiboot2_tag *) (ptr + i);

		if (tag->type == MULTIBOOT_TAG_TYPE_ADDRESS) {
			addr = (struct multiboot2_tag_address *) tag;
			info->header_addr = addr->header_addr;
			info->load_addr = addr->load_addr;
			info->load_end_addr = addr->load_end_addr;
			info->bss_end_addr = addr->bss_end_addr;
		}

		if (tag->type == MULTIBOOT_TAG_TYPE_ENTRY) {
			entry = (struct multiboot2_tag_entry *) tag;
			info->entry_addr = entry->entry_addr;
		}

		i += tag->size;
		if ((i % 8) != 0)
			i = (i & ~0x7) + 8;
	}

	return 0;
}

int parse_multiboot2(struct multiboot2_load_info *info, const char *path)
{
	struct multiboot2_header *ptr;
	char *buffer = malloc(SEARCH_ZONE_LEN);
	FILE *stream = fopen(path, "r");
	size_t size, i;
	uint32_t sum;
	int ret = -1;

	if (buffer == NULL)
		abort();
	if (stream == NULL)
		goto out;

	size = fread(buffer, 1, SEARCH_ZONE_LEN, stream);
	fclose(stream);

	for (i = 0; i < size; i += 4) {
		ptr = (struct multiboot2_header *) (buffer + i);

		if (ptr->magic != MULTIBOOT_MAGIC)
			continue;

		sum = ptr->magic
			+ ptr->architecture
			+ ptr->header_length
			+ ptr->checksum;

		if (sum != 0)
			continue;

		break;
	}

	if (i >= size)
		goto out;

	ret = extract_multiboot2(info, i, ptr);
 out:
	free(buffer);
	return ret;
}

int load_multiboot2(const struct multiboot2_load_info *info, const char *path)
{
	FILE *stream = fopen(path, "r");
	size_t len = info->load_end_addr - info->load_addr;
	size_t bss = info->bss_end_addr - info->load_end_addr;
	uint32_t start = info->header_off
		- (info->header_addr - info->load_addr);
	int ret = -1;

	if (stream == NULL)
		goto out;

	if (fseek(stream, start, SEEK_SET) != 0)
		goto err;

	if (fread((void *) ((uint64_t) info->load_addr), len, 1, stream) != 1)
		goto err;

	memset((void *) ((uint64_t) info->load_end_addr), 0, bss);

	ret = 0;
 err:
	fclose(stream);
 out:
	return ret;
}
