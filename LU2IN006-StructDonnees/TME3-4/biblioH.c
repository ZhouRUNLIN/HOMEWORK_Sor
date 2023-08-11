#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
#include "biblioH.h"

//Question 2.2
int fonctionClef(char* auteur) {
    int res = 0;
    
	char* c = auteur;
    while (*c) {
        res += *c;
        ++c;
    }

    return res;
}

//Question 2.3
LivreH* creer_livreH(int num, char* titre, char* auteur) {
    LivreH* livre = malloc(sizeof *livre);

    if (! livre)        return NULL;

    livre -> num = num;
    livre -> auteur = strdup(auteur);
    livre -> titre = strdup(titre);
    livre -> suiv = NULL;

    return livre;
}

void liberer_livreH(LivreH* l) {
    if (! l) return;
	
    LivreH* lv;
    while(l){
		free(l -> titre);
		free(l -> auteur);
		lv = l -> suiv;
		free(l);
		l = lv;
	}
}

void liberer_liste_livreH(LivreH* livres) {
    LivreH* tmp;
    while (livres) {
        tmp = livres -> suiv;
        free(livres);
        livres = tmp;
    }
}

BiblioH* creer_biblioH(int m) {
    BiblioH* biblio = malloc(sizeof *biblio);
    if (! biblio) 		return NULL;

    biblio -> m = m;
    biblio -> nE = 0;
    biblio -> T = malloc(m * sizeof(LivreH*));

    return biblio;
}

void liberer_biblioH(BiblioH* biblio) {
    if (!biblio) 		return;

    if (!biblio->T) 		free(biblio);

    for (int i = 0; i < biblio -> m; ++i) {
        liberer_livreH(biblio -> T[i]);
    }

    free(biblio->T);
    free(biblio);
}

//Question 2.4
int fonctionHachage(int clef, int m) {
    double A = (sqrt(5) - 1) / 2;
    return floor(m * (clef * A - floor(clef * A)));
}

//Question 2.5
void inserer_en_teteH(LivreH** liste, LivreH* nouveau) {
    nouveau -> suiv = *liste;
    *liste = nouveau;
}

void inserer(BiblioH* b, int num, char* titre, char* auteur){
    LivreH* livre = creer_livreH(num, titre, auteur);
    int index = fonctionHachage(fonctionClef(auteur), b->m);
    inserer_en_teteH(&(b->T[index]), livre);
}

// Question 2.6
LivreH* rechercher_numH(BiblioH* b, int num) {
    /*
     * Recherche d'un livre par son numéro,
     * en temps lineaire par rapport au nombre d'entrees stockees.
     */
    for (int i = 0; i < b->m; i++) {
        LivreH* curr = b->T[i];
        while (curr) {
            if (curr->num == num) {
                return curr;
            }
            curr = curr->suiv;
        }
    }

    return NULL;
}

LivreH* rechercher_titreH(BiblioH* b, char* titre) {
    /*
     * Recherche d'un livre par son titre,
     * en temps linéaire par rapport au nombre d'entrées stockées.
     */
    for (int i = 0; i < b->m; ++i) {
        LivreH* curr = b->T[i];
        while (curr) {
            if (strcmp(curr->titre, titre) == 0) {
                return curr;
            }
            curr = curr -> suiv;
        }
    }

    return NULL;
}

BiblioH* rechercher_auteurH(BiblioH* b, char* auteur) {
    // TODO vérifier
    BiblioH* res = creer_biblioH(1);
    int cle_auteur = fonctionClef(auteur);
    LivreH* candidats = b->T[fonctionHachage(cle_auteur, b->m)];
    while (candidats) {
        if (strcmp(candidats -> auteur, auteur) == 0) {
            inserer_en_teteH(res->T, creer_livreH(candidats -> num, candidats -> titre, candidats -> auteur));
        }
        candidats = candidats -> suiv;
    }

    return res;
}

void supprimer_livreH(BiblioH* b, int num, char* titre, char* auteur) {
    int index = fonctionHachage(fonctionClef(auteur), b->m);
    LivreH* prev = NULL;
    LivreH* curr = b -> T[index];
    while (curr) {
        if (curr -> num == num && strcmp(curr -> titre, titre) == 0 && strcmp(curr -> auteur, auteur) == 0) {
            // !prev signifie que curr est le premier élément de la liste.
            // Pour le supprimer, on a donc juste à mettre son successeur comme tête de la liste,
            // et à libérer la mémoire associée à curr.
            if (!prev) {
                b -> T[index] = curr -> suiv;
                liberer_livreH(curr);
            } 
			else {
                prev -> suiv = curr -> suiv;
                liberer_livreH(curr);
            }
        }
        prev = curr;
        curr = curr->suiv;
    }
}

void fusionnerH(BiblioH* gauche, BiblioH* droite) {
    // On ne prête pas attention aux doublons.
    // Todo erreur : ajoute des entrées étranges...
    for (int i = 0; i < droite -> m; ++i) {
        LivreH* curr = droite -> T[i];
        while (curr) {
            printf("Insertion de (%d %s %s).\n", curr -> num, curr -> titre, curr -> auteur);
            inserer(gauche, curr -> num, curr -> titre, curr -> auteur);
            LivreH* aSupprimer = curr;
            curr = curr -> suiv;
            liberer_livreH(aSupprimer);
        }
    }

    liberer_biblioH(droite);
}

int identiqueH(LivreH* gauche, LivreH* droite) {
    return strcmp(gauche -> titre, droite -> titre) == 0 && strcmp(gauche -> auteur, droite -> auteur) == 0;
}

BiblioH* recherche_exemplaireH(BiblioH* b) {
    BiblioH* res = creer_biblioH(b -> m);

    /*
     * En supposant que la taille des listes chaînées est bornée par un certain A, cette fonction s'exécute en
     * approximativement m*A*A instructions, soit environ Theta(nombre de livres) sous des hypothèses raisonnables...
     */
    LivreH* curr;
    LivreH* prev;
    for (int i = 0; i < b -> m; i++) {
        curr = b -> T[i];
        while (curr) {
            /*
             * On inspecte les éléments précédents de la liste pour voir si l'élément courant admet un doublon.
             */
            prev = b -> T[i];
            while (prev) {
                if (identiqueH(prev, curr) && prev -> num != curr -> num) {
                    inserer(res, curr -> num, curr -> titre, curr -> auteur);
                    break;
                }
                prev = prev -> suiv;
            }
            curr = curr -> suiv;
        }
    }

    return res;
}