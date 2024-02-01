#include <stdlib.h>
#include <stdio.h>

#include "comment.h"

int main(int argc, char const *argv[])
{
	struct comment *c;

	if ((c = new_comment(11, "first beta", 13, "B. Kernighan",
			     10, "C is born"))) {
		display_comment(c);
		free_comment(c);
	}
	
	if ((c = new_comment(6, "fixes", -1, "D. Ritchie", 11, "better now"))) {
		display_comment(c);
		free_comment(c);
	}

	if ((c = new_comment(14, "first release", 13, "B. Kernighan", -1,
			     "let's conquer the world with this language"))) {
		display_comment(c);
		free_comment(c);
	}

	return 0;
}
