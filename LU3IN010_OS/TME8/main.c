#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "affiche_tas.h"
#include "mymalloc.h"


int main(int argc, char** argv){

	tas_init();

	char *p1, *p2, *p3, *p4, *p5;

	printf("allocation des tp 1, 2 et 3 de taille 10, 9, 5:\n\n");
	p1 = (char *) tas_malloc(10);
	p2 = (char *) tas_malloc(9);
	p3 = (char *) tas_malloc(5);
	strcpy( p1, "tp 1" );
	strcpy( p2, "tp 2" );
	strcpy( p3, "tp 3" );
	afficher_tas();

	printf("liberation du tp 3:\n\n");
	tas_free(p3);
	afficher_tas();

	printf("allocation du tme 4 de taille 6:\n\n");
	p4 = (char *) tas_malloc(6);
	strcpy(p4, "tme 4" );
	afficher_tas();

	printf("liberation du tp 2:\n\n");
	tas_free(p2);
	afficher_tas();	

	printf("allocation du systeme de taille 8:\n\n");
	p5 = (char *) tas_malloc(8);
	strcpy(p5, "systeme" );
	afficher_tas();
	
}


