#ifndef _SWAPPER_H_
#define _SWAPPER_H_

//#define _DEBUG_

#ifdef _DEBUG_
	#define DEBUG(msg)msg
#else
	#define DEBUG(msg)asm("")
#endif

typedef int Page;						//A valid page number is >= 0

typedef struct Swapper {
	int	(*init_private)(struct Swapper*);		//Initialize private data needed by the algorithm
	void	(*reference)(struct Swapper*,unsigned int);	//Called on each reference to a frame to keep stats etc...
	unsigned int (*choose)(struct Swapper*);		//Gives the number of the frame that should be swapped out
	void	(*finalize)(struct Swapper*);			//Should free all what as allocated by init
	unsigned int frame_nb;					//Number of frames
	Page *	frame;						//Table of frames (an empty one contains -1)
	void *	private_data;					//Private data to allow custom algorithm
} Swapper;

int initSwapper(
	Swapper*,
	unsigned int,						//Logical number of frames
	int (*)(Swapper*),					//Init for private data
	void (*)(Swapper*,unsigned int),			//Reference to keep stats
	unsigned int (*)(Swapper*),				//Choose function
	void (*)(Swapper*)					//Finalize function
);

int reference(Swapper*,Page);					// 1 => Page Fault else 0

#endif
