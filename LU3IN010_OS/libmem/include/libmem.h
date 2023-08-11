#ifndef _LIBMEM_H_
#define _LIBMEM_H_

#include "Swapper.h"
#include <stdio.h>

int	swapSimulation(Swapper*,FILE*);
/*
The given file should contain a list of page number.
Returns 0 on succes, -1 on failure.
*/

#endif
