//HUA Zhichun 28713132
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<time.h>

//Question 2.1
/* je choisis la version "alloue_tableau(int n)"
	raison: la seconde version a besoin d'allouer un Tableau a l'avance. Mais ce qui a la risque de fuite memoire 
*/
int* alloue_tableau(int n){
    return (int *)malloc(n*sizeof(int));
}

void desalloue_tableau(int *T){
    free(T);
}

void remplir_tableau(int *T, int V, int n){
    //srand(time(NULL));
    for(int i = 0; i < n; i ++){
        T[i] = rand()%V;
    }
}

void afficher_tableau(int*T, int n){
    printf("[ ");
    for(int i = 0; i < n; i ++){
        printf("%d ", T[i]);
    }
    printf("]\n");
}

//exo2.2
/*
	Question 2.2.1:
		on utilise duex boucles: 
			un est t(i), et une autre est t(j) 
		calculer les carres des differences entre les elements du tableau directment pour n*n fois
	
	Question 2.2.2:
		on peut changer (x - y)^2 a (x^2 + y^2 - 2*x*y)
		somme n,i=1 (somme n,j=1 (xi - xj)^2 ) = somme n,i=1 (somme n,j=1 (xi^2 + xj^2 - 2*xi*xj)) 
			= somme n,i=1 (somme n,j=1 (xi^2 + xj^2)) - 2*(somme n,i=1(somme n,j=1 (2*xi*xj))) 
			= somme n,i=1 (somme n,j=1 (xi^2)) + somme n,i=1 (somme n,j=1 (xj^2)) - 2*((somme n,i=1 (xi))^2)
			= 2*( n* somme n,i=1 (xi^2) - (somme n,i=1 (xi))^2)
*/
int algo1(int *T,int n){
    int r = 0;
    for(int i = 0; i < n; i++){
        for(int j = 0; j < n; j++){
            r += (T[i] - T[j])*(T[i] - T[j]);
        }
    }
    return r;
}

int algo2(int *T, int n){
    int diff = 0;
    int carre = 0;
    for(int i = 0; i < n; i++){
        diff += T[i];
        carre += T[i] * T[i]*n;
    }
    diff = diff*diff;

    return 2*(carre-diff);
}

//exo2.4
int** alloue_matrice(int n){
    int **mat = (int **)malloc(sizeof(int*)*n);
    if(mat == NULL){
        printf("Erreur de l'allocation");
        return NULL;
    }
    for(int i =0; i<n; i++){
        mat[i] = (int*)malloc(sizeof(int*)*n);
        if(mat[i]==NULL){
            printf("Erreur de l'allocation");
            for(int j = 0; j<i;j++){
                free(mat[j]);
            }
            free(mat);
            return NULL;
        }
    }
    return mat;
}

void desalloue_matrice(int **mat, int n){
    for (int i=0; i<n;i++){
        desalloue_tableau(mat[i]);
    }
    free(mat);
}

void remplir_matrice(int **mat, int V, int n){
    for(int i =0; i<n; i++){
        remplir_tableau(mat[i], V, n);
    }
}

void afficher_matrice(int **mat, int n){
    for(int i = 0; i<0; i++){
        afficher_tableau(mat[i],n);
    }
}

//exo2.5
//les deux fonctions de la Question 2.5 
//retourne 1 si tous les elements dans la matrice est different
//er retourne 0 sinon
int mat_algo1(int **mat, int n){
    for(int i =0; i< n; i++){
        for(int j = 0; j<n; j++){
            for (int k =0; k<n; k++){
                for (int m=0; m<n; m++){
                    if ((mat[i][j] == mat[k][m])&&((i!=k)||(j!= m))) {
                        return 0;
                    }
                }
            }
        }
    }
    return 1;
}

int mat_algo2(int **mat, int n, int V){
    int *tab = (int*)malloc(sizeof(int)*V);
    for(int i = 0; i<V; i++){
        tab[i] = 0;
    }
    for(int j = 0; j<n; j++){
        for(int k = 0; k<n; k++){
            int e = mat[j][k];
            if(tab[e] != 0){
                return 0;
            }else{
                tab[e] = e;
            }
        }
    }
    return 1;
}

//Question 2.6.1
int** calculer_matrice_algo1(int **mat1, int **mat2, int n){
	int **mat = alloue_matrice(n);

	for(int i = 0; i < n; i++){
		for(int j = 0; j < n; j++){
            mat[i][j] = 0;
			for(int k = 0; k < n; k++){
				mat[i][j] += mat1[i][k] * mat2[k][j];
			}
		}
	}
	return mat;
}

//Question 2.6.2
int** calculer_matrice_algo2(int **mat1, int **mat2, int n){
	int ** res = alloue_matrice(n);
  	if (!res) return NULL;
  
  	int i, j, k;
	int index;
	
	/* On parcourt la matrice resultat */
	for (i = 0 ; i < n ; i++) {
		for (j = 0 ; j < n ; j++) {
		res[i][j] = 0;
		/* La variable k sert a initialiser la variable de la boucle for en-dessous.
			Plus d'explications dans le rapport du projet */
		k = (i > j) ? i : j; // k = max(i, j)
		/* On calcule la valeur de chaque case en ignorant les cases contenant des 0 */
		for (index = k ; index < n ; index++) {
			res[i][j] += mat1[i][index] * mat2[index][j];
		}
		}
	}
	return res;
	}
	

