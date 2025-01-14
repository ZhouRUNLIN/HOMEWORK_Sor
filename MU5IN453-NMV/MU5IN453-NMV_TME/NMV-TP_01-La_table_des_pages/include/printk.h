#ifndef _INCLUDE_PRINTK_H_
#define _INCLUDE_PRINTK_H_


#include <stdarg.h>
#include <types.h>


size_t printk(const char *format, ...);

size_t vprintk(const char *format, va_list ap);

size_t snprintk(char *buffer, size_t size, const char *format, ...);

size_t vsnprintk(char *buffer, size_t size, const char *format, va_list ap);


#endif
