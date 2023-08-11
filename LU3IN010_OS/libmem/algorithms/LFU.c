#include "LFU.h"

#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>

int	initLFU(Swapper*);
void	referenceLFU(Swapper*,unsigned int frame);
unsigned int chooseLFU(Swapper*);
void	finalizeLFU(Swapper*);

void printUse(Swapper*swap){
	int i;
	int * use = (int*)swap->private_data;
	for(i=0 ; i<swap->frame_nb ; i++)
		if( swap->frame[i]!=-1 )
			fprintf(stderr,"(page:%i time:%i) ",swap->frame[i],use[i]);
			
}

int initLFUSwapper(Swapper*swap,unsigned int frames){
	initSwapper(swap,frames,initLFU,referenceLFU,chooseLFU,finalizeLFU);
}

int	initLFU(Swapper*swap){
	/* Allouer un tableau avec un compteur d'utilisation pour chaque case */
	swap->private_data = calloc(swap->frame_nb,sizeof(int));
	int * use = (int*)swap->private_data;
	int i;
	/* Initialisation du tableau */
	for( i=0 ; i<swap->frame_nb ; i++ )
		use[i] = 0;
	return 0;
}

void	referenceLFU(Swapper*swap,unsigned int frame){
	int i;
	int * use = (int*)swap->private_data;
	/* A chaque acces à la case frame augmente son compteur d'utilisation */
	use[frame]++;
}

unsigned int chooseLFU(Swapper*swap){
	int i, frame = 0;
	int * use = swap->private_data;
	/* Choisir la case (contenant une page)  ayant le compteur le plus petit */
	
	for ( i=0 ; i<swap->frame_nb ; i++ ){
		if( swap->frame[i] == -1 ){
			frame = i;
			break;
		}
		if( use[i] < use[frame] )
			frame = i;
	}

	use[frame] = 0;

	DEBUG(
		fprintf(stderr,"LFU uses: ");
		printUse(swap);
		fprintf(stderr,"\n\n");
	);
	
	return frame;
}

void	finalizeLFU(Swapper*swap){
	free(swap->private_data);
}
