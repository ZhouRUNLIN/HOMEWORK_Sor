/*
Un processus zombie signifie que lorsque le processus enfant se termine, 
le processus parent ne gère pas correctement le signal SIGCHLD qu'il envoie, 
ce qui fait que le processus enfant reste dans l'état zombie et attend que son processus parent récupère le cadavre pour lui. 
processus dans cet état est un processus zombie.
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int main(){
	int p = 0;
	if((p=fork())==0){
		printf("Child1 proc\n");
		exit(1);
	}
	else{
		if((p=fork())==0){
			printf("Child2 proc\n");
			exit(1);
		}
		else sleep(10);
	}
	return 0;
}