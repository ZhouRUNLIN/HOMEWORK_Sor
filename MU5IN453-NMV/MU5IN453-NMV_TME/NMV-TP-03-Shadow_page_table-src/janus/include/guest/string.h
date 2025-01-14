#ifndef _INCLUDE_STRING_H_
#define _INCLUDE_STRING_H_


#include <types.h>


static inline size_t strlen(const char *str)
{
	const char *ptr = str;

	while (*ptr != '\0')
		ptr++;

	return (ptr - str);
}

static inline void memset(void *addr, uint8_t c, size_t len)
{
	size_t i;

	for (i = 0; i < len; i++)
		((char *) addr)[i] = c;
}

static inline void memcpy(void *dest, const void *src, size_t len)
{
	size_t i;

	for (i = 0; i < len; i++)
		((uint8_t *) dest)[i] = ((uint8_t *) src)[i];
}


#endif
