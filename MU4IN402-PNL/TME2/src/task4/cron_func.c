#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <signal.h>

#include "func.h"

void handle_sigalrm(int signal)
{
	if (signal == SIGALRM) {
		func();
		alarm(3);
	}
}

int main(int argc, char const *argv[])
{
	struct sigaction sa;
	char c = 0;

	sa.sa_handler = &handle_sigalrm;
	sa.sa_flags = SA_RESTART;
	sigfillset(&sa.sa_mask);
	if (sigaction(SIGALRM, &sa, NULL) == -1)
		perror("Error: cannot handle SIGALRM");

	alarm(3);

	do {
		if (c == 0)
			printf("Enter : (i) insert (r) remove (e) end\n");

		if (read(STDIN_FILENO, &c, sizeof(char)) != sizeof(char))
			return -1;

		switch (c) {

		case 'i':
			printf("Inserting a new function\n");
			c = 0;
			break;
		case 'r':
			printf("Restoring the default function\n");
			c = 0;
			break;
		default:
			break;

		}
	} while (c != 'e');

	return 0;
}
