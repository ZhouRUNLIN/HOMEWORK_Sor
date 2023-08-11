//HUA Zhichun 28713132
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<time.h>
#include "exo2.h"

int main(){
	//le test pour exercice 2.6
	clock_t temps_initial;
	clock_t temps_final;
	double temps_cpu;

    FILE *fic = fopen("sortie_vitesse_2_6.txt", "w");
    if(fic == NULL){
        printf("Erreur de fichier\n");
        return 0;
    }
    int i, j;

	for(int n = 1; n < 100; n++){
		int **mat_sup = alloue_matrice(n);
        if (mat_sup == NULL) return 1;
        int **mat_inf = alloue_matrice(n);
        if (mat_inf == NULL) return 1;
        
        remplir_matrice(mat_sup, 10, n);
        remplir_matrice(mat_inf, 10, n);
        for (i = 0 ; i < n ; i++) {
        for (j = 0 ; j < n ; j++) {
            if (i > j) mat_sup[i][j] = 0;
            if (j > i) mat_inf[i][j] = 0;
        }
        }
        
        temps_initial = clock();
        int **mat = calculer_matrice_algo1(mat_sup, mat_inf, n);
        temps_final = clock();
        temps_cpu = ((double)(temps_final - temps_initial)) / CLOCKS_PER_SEC;
        fprintf(fic, "%d %f ", n, temps_cpu);
        

        desalloue_matrice(mat, n);
        
        temps_initial = clock();
        int **mat2 = calculer_matrice_algo2(mat_sup, mat_inf, n);
        temps_final = clock();
        temps_cpu = ((double)(temps_final - temps_initial)) / CLOCKS_PER_SEC;
        fprintf(fic, "%f \n", temps_cpu);
        
        desalloue_matrice(mat2, n);
        
        desalloue_matrice(mat_sup, n);
        desalloue_matrice(mat_inf, n);
	}
    fclose(fic);

	
	return 0;
}

/*exo2.6.4*/
/*En effet, comme dit dans la question 3, on voit que les deux algorithmes 
prennent plus de temps quand n est grand et que le deuxième est plus rapide que 
le premier, on peut même dire que plus le n est grand, plus la difference de temps est grande.*/
