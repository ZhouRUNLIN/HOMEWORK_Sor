#ifndef _INCLUDE_STDARG_H_
#define _INCLUDE_STDARG_H_


typedef __builtin_va_list          va_list;


#define va_start(value, list)      __builtin_va_start(value, list)
#define va_arg(list, type)         __builtin_va_arg(list, type)
#define va_end(list)               __builtin_va_end(list)
#define va_copy(dest, src)         __builtin_va_copy(dest, src)


#endif
