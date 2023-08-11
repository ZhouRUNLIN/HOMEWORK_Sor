#include "libmem.h"

int swapSimulation(Swapper*swap,FILE*f){
	int	frame_nb;
	unsigned int ref;
	int	DP = 0;
	int	ref_nb = 0;
	
	while(!feof(f)){
		fscanf(f,"%i",&ref);
		if(ref<0){
			fprintf(stderr,"Error: reference %i to an invalid page number %i\n",ref_nb,ref);
			return -1;
		}
		ref_nb++;
		DP+=reference(swap,ref);
	}

	printf("%i/%i ~ %f%%\n",DP,ref_nb,(float)(100*DP)/(float)ref_nb);

	return 0;
}
