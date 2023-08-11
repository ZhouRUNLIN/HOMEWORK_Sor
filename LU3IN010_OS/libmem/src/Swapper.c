#include "Swapper.h"
#include <stdio.h>
#include <stdlib.h>

void printFrames(Swapper*swap){
	int i;
	for ( i=0 ; i<swap->frame_nb ; i++ )
		if( swap->frame[i] == -1 )
			fprintf(stderr,"(frame %i: _) ",i);
		else
			fprintf(stderr,"(frame %i: %i) ",i,swap->frame[i]);
}

int isInMemory(Swapper*swap,Page page){
	int i;
	for ( i=0 ; i<swap->frame_nb ; i++ )
		if( swap->frame[i] == page )
			return i;
	return -1;
}

void initFrames(Swapper*swap){
	int i;
	for( i=0 ; i<swap->frame_nb ; i++)
		swap->frame[i] = -1;
}

void replaceFrame(Swapper*swap,unsigned int frame,Page page){
	swap->frame[frame] = page;
}

int reference(Swapper*swap,Page page){
	int ret = 0;
	unsigned int frame = isInMemory(swap,page);
	DEBUG(fprintf(stderr,"Page %i referenced\n",page));
	if(frame==-1){
		frame = swap->choose(swap);
		DEBUG(
			fprintf(stderr,"/!\\ PAGE FAULT !!! /!\\\n");
			fprintf(stderr,"Frame %i has been choosen\n",frame);

		);
		replaceFrame(swap,frame,page);
		ret = 1;
	}
	swap->reference(swap,frame);
	DEBUG(
		printFrames(swap);
		fprintf(stderr,"\n\n");
	);
	return ret;
}

int dummy_init(Swapper*swap){
	return 0;
}

void dummy_reference(Swapper*swap,unsigned int frame){
	return;
}

unsigned int dummy_choose(Swapper*swap){
	return 0;
}

void dummy_finalize(Swapper*swap){
	return;
}


int initSwapper(
	Swapper*swap,
	unsigned int frames,				//number of physical frames
	int (*init)(Swapper*),				//initialize private data (structs needed etc)
	void (*reference)(Swapper*,unsigned int),	//called on each reference to keep stats
	unsigned int (*choose)(Swapper*),		//return the number of the frame that should be dropped
	void (*finalize)(Swapper*)			//should free what was allocated in init
){
	if( ! (swap->frame = (Page*)calloc(frames,sizeof(Page))) )
		return -1;
	swap->frame_nb = frames;
	swap->init_private =	(init!=NULL) ?		init 		: dummy_init;
	swap->reference = 	(reference!=NULL) ?	reference 	: dummy_reference;
	swap->choose = 		(choose!=NULL) ?	choose 		: dummy_choose;
	swap->finalize = 	(finalize!=NULL) ?	finalize 	: dummy_finalize;
	if(swap->init_private(swap)){
		free(swap->frame);
		return -1;
	}
	initFrames(swap);
	return 0;
}
