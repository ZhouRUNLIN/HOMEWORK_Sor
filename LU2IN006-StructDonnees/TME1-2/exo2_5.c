//HUA Zhichun 28713132
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<time.h>
#include "exo2.h"

int main(){
    // le test d'exercice 2.5
    srand(time(NULL));
    clock_t temps_initial;
    clock_t temps_final;

    double temps_cpu;

    int n;
    FILE *fic = fopen("sortie_vitesse_2_5.txt", "w");
    if(fic == NULL){
        printf("Erreur de fichier\n");
        return 0;
    }
    int i,j,x;
    for (n = 10; n<100; n++){
        x=0;
        int **mat = alloue_matrice(n);
        remplir_matrice(mat,n*n,n);

        temps_initial = clock();
        mat_algo1(mat, n);
        temps_final = clock();
        temps_cpu = ((double)(temps_final - temps_initial)) / CLOCKS_PER_SEC;
        fprintf(fic, "%d %f ", n, temps_cpu);
                
        temps_initial = clock();
        mat_algo2(mat, n, n*n);
        temps_final = clock();
        temps_cpu = ((double)(temps_final - temps_initial)) / CLOCKS_PER_SEC;
        fprintf(fic, "%f \n", temps_cpu);
        desalloue_matrice(mat,n);
    }
    fclose(fic);

    return 0;
    

}

/*2.5.3*/
/*Le pire cas dans les deux algorithmes est que la deuxième occurrence d’une variable soit 
située à la fin de la boucle parcourant la matrice, c’est-à-dire qu’on a parcourue toute la 
matrice pour savoir qu’il y a une valeur répétée. Cela explique les pics de ces deux courbes, plus le pic est haut, 
plus on découvre tard qu’il y a une valeur répétée.
Cependant, on constate facilement qu’en moyenne le deuxième algorithme a une meilleure 
complexité car il est de complexité en O(𝑛# + 𝑉) comparée à la complexité 𝑂(𝑛') du premier algorithme.*/