#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "tab.h"

void initTab(int *tab, int size){
    if(size > NMAX){
        printf("Err: size out of range !");
    }
    else{
        for (int i = 0; i < size; i++){
            int a = rand() % 10; //une valeur aléatoire
            tab[i]= a;
        }
    }
}

void printTab(int *tab, int size){
    if(size > NMAX){
        printf("Err: size out of range !");
    }
    else{
        printf("[");
        for (int i = 0; i < size; i++)      printf("%d ", tab[i]);
        printf("]\n");
    }
}

int sumTab(int *tab, int size){
    int somme=0;
    for (int i = 0; i < size; i++)      somme+=tab[i];
    return somme;
}

void minSumTab(int *min, int *tab, int size){
    int ptr=0;
    for(int i=0; i<size; i++){
        if(tab[i]<ptr)      ptr=tab[i];
    }
    *min=ptr;
}