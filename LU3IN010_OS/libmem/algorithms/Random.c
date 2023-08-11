#include "Random.h"

#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>

int initRandom(Swapper*);
unsigned int randomChoose(Swapper*);
void randomReference(Swapper*,unsigned int frame);

int initRandomSwapper(Swapper*swap,unsigned int frames){
	initSwapper(swap,frames,initRandom,NULL,randomChoose,NULL);
}

int initRandom(Swapper*swap){
	srand(getpid());
	return 0;
}

unsigned int randomChoose(Swapper*swap){
	int i;
	for ( i=0 ; i<swap->frame_nb ; i++ )
		if ( swap->frame[i] == -1 )
			return i;
	return random() % swap->frame_nb;
}
