#ifndef HISTORY_H
#define HISTORY_H

#include "commit.h"

struct history {
	unsigned long commit_count;
	char *name;
	struct commit *commit_list;
};

struct history *new_history(char *name);

struct commit *last_commit(struct history *h);

void display_history(struct history *from);

void infos(struct history *h, int major, unsigned long minor);

void free_history(struct history *h);	

#endif
