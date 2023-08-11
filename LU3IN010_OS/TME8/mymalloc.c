#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "affiche_tas.h"


int first_fit(int taille){
	int i = 0;
	int j;
	while(i < TAILTAS){
		char size = tas[i];
		

		if(tas[i+1] == -1){
			if(TAILTAS - i > taille)
				return i;
			else
				return -1;
		}
		
		else if(size == 0){
			j = 0;		
			while(tas[i+j] == 0){
				j++;
				if (j+i > TAILTAS)
					return -1;
			}
			if (j > taille)
				return i;
			else
				i = i+j;
		} 	
		else{
			i = i + size + 1;
		}
	}
	return -1;
}


char *tas_malloc(unsigned int taille){
	int debut_zone = first_fit(taille);
	if(debut_zone == -1){
		return NULL;
	}
	
	if(tas[debut_zone+1] == -1){
		tas[debut_zone] = taille;
		tas[debut_zone+1] = 0;
		tas[debut_zone+taille+1] = TAILTAS-(debut_zone+taille+1);
		tas[debut_zone+taille+2] = -1;
		return tas+debut_zone+1;
	}
	else{
		tas[debut_zone] = taille;
		return tas+debut_zone+1;
	}
	
	
}


int tas_free(char *ptr){
	int size = ptr[-1];
	if(size == 0){
		return -1;
	}
	ptr[-1] = 0;
	for(int i = 0; i< size; i++){
		ptr[i] = 0;
	}
	if(ptr[size+1] == -1){
		
		ptr[-1] = ptr[size] + size + 1;
		ptr[0] = -1;
		ptr[size] = 0;
		ptr[size+1] = 0;
	}
	
	return size;
	
}



