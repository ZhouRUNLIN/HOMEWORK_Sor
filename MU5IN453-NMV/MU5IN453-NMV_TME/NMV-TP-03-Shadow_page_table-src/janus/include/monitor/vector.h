#ifndef _INCLUDE_VECTOR_H_
#define _INCLUDE_VECTOR_H_


#include <stdlib.h>
#include <string.h>


struct vector
{
	size_t   capacity;   /* max item count */
	size_t   size;       /* current item count */
	void    *buffer;
};


#define EMPTY_VECTOR  ((struct vector) { 0, 0, NULL })

static inline void vector_create(struct vector *self)
{
	self->capacity = 0;
	self->size = 0;
	self->buffer = NULL;
}

static inline void vector_destroy(struct vector *self)
{
	free(self->buffer);
}


static inline void vector_grow(struct vector *self, size_t cap, size_t len)
{
	void *nptr;

	if (cap <= self->capacity)
		return;

	nptr = realloc(self->buffer, cap * len);
	if (nptr == NULL)
		exit(EXIT_FAILURE);

	self->buffer = nptr;
}

static inline void *vector_push(struct vector *self, const void *e, size_t len)
{
	size_t nc, off;
	void *ptr;

	if (self->size == self->capacity) {
		if (self->capacity == 0)
			nc = 32;
		else
			nc = self->capacity * 2;
		vector_grow(self, nc, len);
	}

	off = self->size * len;
	ptr = ((char *) self->buffer) + off;
	memcpy(ptr, e, len);
	self->size++;
	return ptr;
}

static inline void vector_remove(struct vector *self, size_t index, size_t len)
{
	char *ptr;

	if (index >= self->size)
		return;

	if (index != self->size - 1) {
		ptr = (char *) self->buffer;
		memcpy(&ptr[index * len], &ptr[self->size * len], len);
	}

	self->size--;
}

static inline void vector_clear(struct vector *self)
{
	self->size = 0;
}

#define vector_buffer(self, type)     ((type *) (self)->buffer)


#endif
