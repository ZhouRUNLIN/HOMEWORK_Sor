#ifndef VERSION_H
#define VERSION_H

struct version {
	char flags;
	unsigned short major;
	unsigned long minor;
};

struct commit {
	unsigned long id;
	struct version version;
	char *comment;
	struct commit *next;
	struct commit *prev;
};

int is_unstable(struct version *v);

void display_version(struct version *v);

int cmp_version(struct version *v, unsigned short major, unsigned long minor);

struct commit * commit_of(struct version * version);

#endif
