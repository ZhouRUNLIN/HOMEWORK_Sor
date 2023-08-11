#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "tab.h"
#include <sys/time.h>
#include <sys/resource.h>

void PrintMem(){
    int ret;
    struct rusage usage;
    ret = getrusage(RUSAGE_SELF, &usage);
	if (0 != ret) {
		printf("getrusage failed\n");
	}
    else{
        printf("%s: %.3fM\n", "ru_maxrss", (usage.ru_maxrss / 1024.0));
    }
}

int main(){
    //test pour 2.1-2.3
    int tab1[NMAX];
    PrintMem();
    initTab(tab1, NMAX);
    PrintMem();
    printTab(tab1, 5);

    int* tab2 = (int*)malloc(sizeof(int)*NMAX);
    PrintMem();
    initTab(tab2, NMAX);
    PrintMem();
    printTab(tab2, 5);

    //test pour 2.5-2.6
    printf("somme de tab1 est : %d\n", sumTab(tab1, 5));
    printf("somme de tab2 est : %d\n", sumTab(tab2, 5));

    int min;
    minSumTab(&min, tab1, 5);
    printf("smallest number in the tab1 : %d\n", min);

    minSumTab(&min, tab2, 5);
    printf("smallest number in the tab1 : %d\n", min);

    free(tab2);
}

/*
    reponse pour Question 2.9:
    La mémoire n'est pas occupée lorsque le tableau est déclaré, mais après l'exécution de la fonction initTab().
    je pense que la raison est quand on déclaré les tableaux, les données sont vides, donc l'état de la mémoire 
        est qu'elle est simplement allouée, mais pas occupée.
        donc après on remplir les tableaux par la focntiomn initTab(), les espaces mémoires sont bien occupées
*/