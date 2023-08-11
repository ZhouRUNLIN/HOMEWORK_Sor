#include "Fifo.h"

#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>

int initFifo(Swapper*);
unsigned int fifoChoose(Swapper*);
void fifoReference(Swapper*,unsigned int frame);
void finalizeFifo(Swapper*);

int initFifoSwapper(Swapper*swap,unsigned int frames){
	return	initSwapper(swap,frames,initFifo,NULL,fifoChoose,finalizeFifo);
}

int initFifo(Swapper* swap) {
    /* Allouer un tableau avec un compteur d'utilisation pour chaque case */
    swap->private_data = calloc(swap->frame_nb, sizeof(int));

    if (swap->private_data == NULL) {
        return 1;
    }

    int* load_order = (int*) swap->private_data;  //pointeur pour manipuler plus facilement
    
	for (unsigned int i = 0; i < swap->frame_nb; i++) {
        load_order[i] = 1 + i;
    }
    return 0;  // 0 si tout se passe bien
}

unsigned int fifoChoose(Swapper* swap){
	unsigned int frame = 0;
	int *load_order = swap->private_data;
	for(unsigned int i = 0; i < swap->frame_nb; i++){
		if(swap->frame[i] == -1){
			frame = i;
			break;
		}
		if(load_order[i] < load_order[frame])		frame = i;		
	}

	for(unsigned int i = 0; i < swap->frame_nb; i++){
		load_order[i]-=1;
	}
	
	return frame;
}

void finalizeFifo(Swapper*swap){
	/* A ecrire en TME */
	free(swap->private_data);
}
