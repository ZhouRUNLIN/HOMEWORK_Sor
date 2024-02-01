#include <stdlib.h>
#include <stdio.h>

#include "version.h"

int main(int argc, char const *argv[])
{
	struct version v = {.major = 3,
			    .minor = 5,
			    .flags = 0};

	display_version(&v);
	printf("\n");

	v.minor++;
	display_version(&v);
	printf("\n");

	v.major++;
	v.minor = 0;
	display_version(&v);
	printf("\n");

	v.minor++;
	display_version(&v);
	printf("\n");

	return 0;
}
