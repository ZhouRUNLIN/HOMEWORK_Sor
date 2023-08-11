//HUA Zhichun 28713132
#include<stdio.h>
#include<stdlib.h>
#include <string.h>

typedef struct adresse {
    int numero;
    char* rue;
    int code_postal;
} Adresse;

Adresse* creer_adresse(int n, char* r, int c) {
    Adresse* new = (Adresse*) malloc(sizeof(Adresse));

    new->numero = n;
    new->rue = strdup(r);
    new->code_postal = c;

    return new;
}

int main(void) {
    Adresse* maison = creer_adresse (12, "manoeuvre", 15670);
    
    printf("Adresse courante : %d rue %s %d France\n", maison->numero, maison->rue, maison->code_postal);
    
    return 0;
}

/*Q1.4*/
/*il définit une struct Adresse, qui a trois champ: numero(int), rue(char *), code_postal(int) 
fonction creer_addresse()
	qui cree une nouvelle variable nommée par "new", il faut trois arguments: n est le "numero", r est la "rue", c est la "code_postal"
et ces trois arguments vont enregistres dans "new", après cette fonction le retourne
main():
	cree une Addresse "maison" en utilisent de fonction creer_adresse(),et print les champ de "maison"
    Après avoir lancé, le terminal affiche Erreur de segmantation.*/

/*Q1.5*/
/*new->rue est 0x0*/
/*On n'a pas alloué le memoire pour le nom de la rue, donc la solution est que on utilise strdup au lieu de strcpy.*/
