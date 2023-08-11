#include <stdio.h>
#include <stdlib.h>
#include "biblioLC.h"
#include "biblioH.h"
#include "entreeSortieLC.h"

//Question 1.3
Biblio *charger_n_entrees(char *nomfic, int n){
	//ouvrir le ficher
	FILE *file = fopen(nomfic, "r");  

	// creer une bibliotheque et entrisgter les donnees de n lignes du ficher
	Biblio *b = creer_biblio();
	for(int i = 0; i < n; i++){
		int num;
		char titre[256];
		char auteur[256];
		fscanf(file, "%d %s %s", &num, titre, auteur);
		//utiliser la fonction inserer_en_tete pour stocker 
		inserer_en_tete(b, num, titre, auteur); 		
	}
	fclose(file);

	return b;
}

void enregistrer_biblio(Biblio *b, char* nomfic){
		FILE *f = fopen(nomfic, "w");
		Livre* l = b -> L;
		while (l != NULL){
			char snum[20];
			sprintf(snum, "%d", l -> num);
			fputs(snum, f);
			fputs(l -> titre, f);
			fputs(l -> auteur, f);
			l = l -> suiv;
		}

		fclose(f);
}

//Question 2.6
BiblioH* charger_n_entreesH(char* nomfic, int n){
	//ouvrir le ficher
	FILE *file = fopen(nomfic, "r");  

	// creer une bibliotheque et entrisgter les donnees de n lignes du ficher
	BiblioH *b = creer_biblioH(n);

	for(int i = 0; i < n; i++){
		int num;
		char titre[256];
		char auteur[256];
		fscanf(file, "%d %s %s", &num, titre, auteur);
		
		//utiliser la fonction inserer pour stocker 
		inserer(b, num, titre, auteur); 		
	}
	fclose(file);

	return b;
}

void enregistrer_biblioH(BiblioH* b, char* nomfic){
		FILE *f = fopen(nomfic, "w");
		
		LivreH* l = *(b -> T);
		while (l != NULL){
			char snum[20] , sclef[20];
			sprintf(snum, "%d", l -> num);
			sprintf(sclef, "%d", l -> clef);
			fputs(sclef, f); 
			fputs(snum, f);
			fputs(l -> titre, f);
			fputs(l -> auteur, f);
			fputs("\n", f);
			l = l -> suiv;
		}

		fclose(f);
}