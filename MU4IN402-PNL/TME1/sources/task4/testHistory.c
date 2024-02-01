#include <stdlib.h>
#include <stdio.h>

#include "history.h"

int main(int argc, char const *argv[])
{
	struct history *history = new_history("Circle of Life");
	struct commit *tmp, *victim, *last;

	display_commit(last_commit(history));
	printf("\n");

	display_history(history);

	tmp = add_minor_commit(last_commit(history), "Work 1");
	tmp = add_minor_commit(tmp, "Work 2");
	victim = add_minor_commit(tmp, "Work 3");
	last = add_minor_commit(victim, "Work 4");
	display_history(history);

	tmp = add_major_commit(last, "Release 1");
	tmp = add_minor_commit(tmp, "Work 1");
	tmp = add_minor_commit(tmp, "Work 2");
	tmp = add_major_commit(tmp, "Release 2");
	tmp = add_minor_commit(tmp, "Work 1");
	display_history(history);

	del_commit(victim);
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

	return 0;
}
