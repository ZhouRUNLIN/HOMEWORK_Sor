#include <stdlib.h>
#include <stdio.h>

#include "history.h"

int main(int argc, char const *argv[])
{
	struct history *history = new_history("Circle of Life");
	struct commit *tmp, *victim, *last;
	struct commit *victim_major;

	display_commit(last_commit(history));
	printf("\n");

	display_history(history);

	tmp = add_minor_commit(last_commit(history), "Work 1");
	tmp = add_minor_commit(tmp, "Work 2");
	victim = add_minor_commit(tmp, "Work 3");
	last = add_minor_commit(victim, "Work 4");
	display_history(history);

	victim_major = add_major_commit(last, "Release 1");
	tmp = add_minor_commit(victim_major, "Work 1");
	tmp = add_minor_commit(tmp, "Work 2");
	tmp = add_major_commit(tmp, "Release 2");
	tmp = add_minor_commit(tmp, "Work 1");
	display_history(history);

	del_commit(victim);
	printf("After deleting commit 0.3\n");
	display_history(history);

	del_commit(victim_major);
	printf("After deleting commit 1.0\n");
	display_history(history);

	add_minor_commit(last, "Security backport!!!");
	display_history(history);

	printf("Searching for commit 1.2 :   ");
	infos(history, 1, 2);
	printf("\n");

	printf("Searching for commit 1.7 :   ");
	infos(history, 1, 7);
	printf("\n");

	printf("Searching for commit 4.2 :   ");
	infos(history, 4, 2);
	printf("\n");

	free_history(history);

	return 0;
}
