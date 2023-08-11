#include <stdio.h>
#include <stdlib.h>
#include "biblioLC.h"

//Question 1.5
int main(){
	//initialiser un biblio
	Biblio* bl = creer_biblio();

	//sotocker les premier 2000 livre sur "GdeBiblio.txt"
	int nb_livre = 2000; 		//changer le nombre de livre a ici
	Biblio* bl = charger_n_entrees("GdeBiblio.txt", nb_livre);
	
	//Question 1.8
	int rep;

	//affiche le menu a l'utilisateur
	do{
		menu();
		int num;
		char titre[256];
		char auteur[256];
		Biblio* b;
		Livre* l;
		scanf(" %d", &rep);
		switch(rep){ 
		case 1:
			printf("Affichage :\n"); 
			afficher_biblio(bl); 
			break;
		
		case 2:
			printf("Veuillez ecrire le numero, le titre et l’auteur de l’ouvrage.\n");
			scanf("%d %s %s", &num, titre, auteur);
			inserer_en_tete(bl, num, titre, auteur);
			break;

		case 3:
			printf("Veuillez ecrire le numero, le titre et l’auteur de l’ouvrage.\n");
			scanf("%d %s %s", &num, titre, auteur);
			supprimer_livre(bl, num, titre, auteur);
			break;

		case 4:
			printf("Veuillez ecrire le numero de l’ouvrage\n");
			scanf("%d", &num);
			l = chercher_num(bl, num);
			if(l != NULL){
				afficher_livre(l);
			}
			break;

		case 5:
			printf("Veuillez ecrire le numero de l’ouvrage\n");
			scanf("%s", titre);
			l = chercher_titre(bl, titre);
			if(l != NULL){
				afficher_livre(l);
			}
			break;

		case 6:
			printf("Veuillez ecrire l’auteur de l’ouvrage\n");
			scanf(" %s", auteur);
			b = chercher_auteur(bl, auteur);
			if(b -> L != NULL){
				afficher_biblio(b);
				liberer_biblio(b);
			}
			break;
		
		case 7:
			b = trouver_exemplaire(bl);
			afficher_biblio(b);
			if(b -> L != NULL){
				liberer_biblio(b);
			}
			break;
		}
	}while(rep != 0);

	printf("Merci, et au revoir!\n");
	liberer_biblio(bl);
	return 0;
}