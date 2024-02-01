#ifndef COMMIT_H
#define COMMIT_H

#include "version.h"

struct commit;

struct commit {
	unsigned long id;
	struct version version;
	char *comment;
	struct commit *next;
	struct commit *prev;
};


struct commit * commit_of(struct version * version);

struct commit *new_commit(unsigned short major, unsigned long minor,
			  char *comment);

struct commit *add_minor_commit(struct commit *from, char *comment);

struct commit *add_major_commit(struct commit *from, char *comment);

void display_commit(struct commit *from);

#endif
