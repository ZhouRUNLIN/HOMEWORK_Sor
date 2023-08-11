//HUA Zhichun 28713132
#include<stdio.h>
#include<stdlib.h>

const static int len = 10;

int main(void) {
    int *tab;
    int i;

    tab = (int*)malloc(len*sizeof(int));

    for (i=len-1; i>=0; i--) {
        tab[i] = i;
    }

    free(tab);
    return 0;
}
/*Q1.1*/
/*Le programme alloue le memoire d'un tableau d'entier de taille 10. Et il stock son index 0~9 dans le tableau. 
    Après avoir lancé le programme, le terminal affiche Erreur de segmentation.*/
/*Q1.2*/
/*Après 0, i vaut 4294967295. i doit valoir -1 pour sortir la boucle.*/

/*Q1.3*/
/*on peut enlever unsigned pour résoudre ce probleme.*/

