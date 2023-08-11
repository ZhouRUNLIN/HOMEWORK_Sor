#include "LRU.h"

#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>

int initLRU(Swapper *);
void referenceLRU(Swapper *, unsigned int frame);
unsigned int chooseLRU(Swapper *);
void finalizeLRU(Swapper *);

typedef struct
{
	unsigned int clock;
	unsigned int *age;
} InfoLRU;

int initLRUSwapper(Swapper *swap, unsigned int frames)
{
	return initSwapper(swap, frames, initLRU, referenceLRU, chooseLRU, finalizeLRU);
}

int initLRU(Swapper *swap)
{
	/* Allouer un tableau avec un compteur d'utilisation pour chaque case */
	swap->private_data = calloc(swap->frame_nb, sizeof(int));
	
	int *load_order = (int *)swap->private_data;
	
	int i;
	/* Initialisation du tableau */
	for (i = 0; i < swap->frame_nb; i++)
		load_order[i] = swap->frame_nb;
	
	return 0;
}

void referenceLRU(Swapper *swap, unsigned int frame)
{
	/* A ecrire en TME */
	int *distance = (int *)swap->private_data;
	/* A chaque acces à la case frame, la mettre dans la fin de queue de victime */
	distance[frame] = swap->frame_nb;

	/* décrémenter les autres */
	for (int i = 0; i < swap->frame_nb; i++){
		distance[i]-=1;
	}
}

unsigned int chooseLRU(Swapper *swap)
{
	unsigned int frame = 0;
	int *distance = swap->private_data;
	/* Choisir la case ayant le compteur avec le plus petit distance */

	for (unsigned int i = 0; i < swap->frame_nb; i++)
	{
		if (swap->frame[i] == -1){
			frame = i;
			break;
		}
		if (distance[i] < distance[frame])		frame = i;
	}

	distance[frame] = swap->frame_nb;

	return frame;
}

void finalizeLRU(Swapper *swap){
	free(swap->private_data);
}