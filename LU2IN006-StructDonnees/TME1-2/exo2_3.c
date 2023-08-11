//HUA Zhichun 28713132
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<time.h>
#include "exo2.h"

int main(){
    //le test d'exercice 2.3
    clock_t temps_initial1;
    clock_t temps_final1;

    clock_t temps_initial2;
    clock_t temps_final2;

    double temps_cpu1;
    double temps_cpu2;

    int n;
    FILE *fic = fopen("sortie_vitesse_2_3.txt", "w");
    if(fic == NULL){
        printf("Erreur de fichier\n");
        return 0;
    }
    for (n = 100; n<5000; n+=10){
        int *T = alloue_tableau(n);
        remplir_tableau(T,50,n);

        temps_initial1 = clock();
        algo1(T, n);
        temps_final1 = clock();
        temps_cpu1=((double)(temps_final1-temps_initial1))/CLOCKS_PER_SEC;

        temps_initial2 = clock();
        algo2(T, n);
        temps_final2 = clock();
        temps_cpu2=((double)(temps_final2-temps_initial2))/CLOCKS_PER_SEC;

        fprintf(fic,"%d %f %f\n", n, temps_cpu1, temps_cpu2);
        desalloue_tableau(T);
    }
    fclose(fic);

    return 0;
    

}