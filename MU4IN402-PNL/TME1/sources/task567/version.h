#ifndef VERSION_H
#define VERSION_H

struct version {
	char flags;
	unsigned short major;
	unsigned long minor;
};

int is_unstable(struct version *v);

void display_version(struct version *v);

int cmp_version(struct version *v, unsigned short major, unsigned long minor);


#endif
