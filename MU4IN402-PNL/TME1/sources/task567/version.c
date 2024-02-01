#include <stdlib.h>
#include <stdio.h>

#include "version.h"

int is_unstable(struct version *v)
{
	return 1 & ((char *)v)[sizeof(unsigned long)];
}

void display_version(struct version *v)
{
	printf("%2u.%lu %s", v->major, v->minor,
			     is_unstable(v) ? "(unstable)" : "(stable)  ");
}

int cmp_version(struct version *v, unsigned short major, unsigned long minor)
{
	return v->major == major && v->minor == minor;
}


