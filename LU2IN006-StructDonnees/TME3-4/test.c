#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "biblioLC.h"
#include "entreeSortieLC.h"
#include "biblioH.h"

int main(){
	//les tests pour Exercice 1
	//les tests pour les fonctions dans Question 1.3
	Biblio* bl = charger_n_entrees("GdeBiblio.txt", 20);
	
	//les tests pour les fonctions dans Question 1.2
	afficher_biblio(bl);	
	printf("-----------------------------------------------------------\n");

	//les tests pour les fonctions dans Question 1.6
	Livre* l1 = chercher_num(bl, 3);
	Livre* l3 = chercher_num(bl, 7);
	Livre* l2 = chercher_titre(bl, "BHHKICQC");
	Biblio* b = chercher_auteur(bl, "cnuvqhffbsaq");
	afficher_livre(l1);
	afficher_livre(l3);
	afficher_livre(l2);
	afficher_biblio(b);
	supprimer_livre(bl, 1, "SCDXRJ", "owfrx");
	afficher_biblio(bl);
	printf("-----------------------------------------------------------\n");

	Biblio* be = trouver_exemplaire(bl);
	afficher_biblio(be);
	
	//free des listes chainee
	liberer_biblio(be);
	liberer_biblio(b);
	liberer_biblio(bl);	
	
	printf("-----------------------------------------------------------\n");	
	//les tests pour les fonctions dans biblioH.c et biblioH.h
	//les tests poue QUestion 2.2
	char* name = strdup("SCDXRJ");
	int numT = fonctionClef(name);
	printf("le clé du cet auteur est : %d\n", numT);
	
	//free le pointeur name
	free(name);

	//les tests pour exercice 2

	BiblioH* bH = charger_n_entreesH("GdeBiblio.txt", 20);
	LivreH* lH1 = rechercher_numH(bH, 3);
	LivreH* lH3 = rechercher_numH(bH, 7);
	LivreH* lH2 = rechercher_titreH(bH, "BHHKICQC");

	BiblioH* blH = rechercher_auteurH(bH, "cnuvqhffbsaq");

	supprimer_livreH(blH, 1, "SCDXRJ", "owfrx");
	BiblioH* beH = recherche_exemplaireH(blH);

	// free des pointeurs
	liberer_biblioH(beH);
	liberer_biblioH(blH);	
	liberer_biblioH(bH);
	return 0;
}