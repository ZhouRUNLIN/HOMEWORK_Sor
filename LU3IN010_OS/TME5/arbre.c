#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>

int main(int n, char *c[]) {

	//verification des arguments
	if (n == 1) {
		fprintf(stdout, "Pas assez d'argument\n");
		return 1;
	}
	
	if (n >= 3) {
		fprintf(stdout, "Trop d'argument\n");
		return 1;
	}
	
	//debut du programme
	int i;
	int j;
	int cpt = 0;
	
	for (i = 0; i < 2; i++) {
		for (j = 0; j < atoi(c[1]); j++) {
			fork();
			sleep(30);
			cpt++;
			if (cpt == 2) {
				cpt = 0;
				break;
			}
		}
	}
	return 0;
}
