#ifndef COMMIT_H
#define COMMIT_H

#include "version.h"
#include "list.h"

struct commit;

struct commit_ops {
	void (*display)(struct commit *);
	void (*extract)(struct commit *);
};

struct commit {
	unsigned long id;
	struct version version;
	char *comment;
	struct list_head minor_list;
	struct list_head major_list;
	struct commit *parent;
	struct commit_ops ops;
};


struct commit * commit_of(struct version * version);

struct commit *new_commit(unsigned short major, unsigned long minor,
			  char *comment);

struct commit *add_minor_commit(struct commit *from, char *comment);

struct commit *add_major_commit(struct commit *from, char *comment);

void display_commit(struct commit *from);

void display_major_commit(struct commit *from);

void display_minor_commit(struct commit *from);

void del_commit(struct commit *victim);

void extract_minor(struct commit *victim);

void extract_major(struct commit *victim);

#endif
