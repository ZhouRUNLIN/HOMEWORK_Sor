#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "biblioLC.h"

//Exercice 1
//Question 1.2
Livre* creer_livre(int num, char* titre, char* auteur){
	Livre* l = (Livre*)malloc(sizeof(Livre));
	l -> num = num;
	l -> titre = strdup(titre);
	l -> auteur = strdup(auteur);
	l -> suiv = NULL;
	return l;
}

void liberer_livre(Livre* l){
	Livre* lv;
	while(l){
		free(l -> titre);
		free(l -> auteur);
		lv = l -> suiv;
		free(l);
		l = lv;
	}
}

Biblio* creer_biblio(){
	Biblio* b = (Biblio*)malloc(sizeof(Biblio));
	b -> L = NULL;
	return b;
}

void liberer_biblio(Biblio* b){
	liberer_livre(b -> L);
	free(b);
}

int vide_biblio(Biblio* b){
	return (b -> L == NULL);
}

void inserer_en_tete(Biblio* b, int num, char* titre, char* auteur){
	Livre* l = creer_livre(num, titre, auteur);
	if(vide_biblio(b)){
		b -> L = l;
	}
	else{
		l -> suiv = b -> L;
		b -> L = l;
	}
	printf("Ajoute fait\n");
}

//Question 1.6
void afficher_livre(Livre* l){
	printf("l : num = %d, titre = %s,auteur = %s\n", l -> num, l -> titre, l -> auteur);
}

void afficher_biblio(Biblio* b){
	Livre* lv = b -> L;
	while(lv != NULL){
		afficher_livre(lv);
		lv = lv -> suiv;
	}
}

Livre* chercher_num(Biblio* b, int num){
	Livre* l = b -> L;
	while(l != NULL){
		if(l -> num == num){
			return l;
		}
		l = l -> suiv;
	}
	printf("On ne trouve pas l'ouvrage avec ce numéro.\n");
	return l;
}

Livre* chercher_titre(Biblio* b, char* titre){
	Livre* l = b -> L;
	while(l != NULL){
		if(! strcmp(l -> titre, titre)){
			return l;
		}
		l = l -> suiv;
	}
	printf("On ne trouve pas l'ouvrage avec ce titre.\n");
	return l;
}

Biblio* chercher_auteur(Biblio* b, char* auteur){
    /*Rechercher des livre d'un meme auteur*/
    Livre* l = b -> L;
    Biblio* res = creer_biblio(); //On allouer la mémoire pour sauvegarder des livres qu'on veut
    while(l){
        if(! strcmp(l -> auteur, auteur)){
            inserer_en_tete(res, l -> num, l -> titre, l -> auteur);
        }
        l = l -> suiv;
    }
    if( res -> L == NULL){
    	printf("On ne trouve pas l'ouvrage avec cet auteur.\n");
    }
    return res;
}

void supprimer_livre(Biblio* b, int num, char* titre, char* auteur){
	Livre* l = b -> L;
	Livre* lv;
	// si l'element qu'on espere rechercher est la premiere element dans la liste
	if(( l -> num == num && (! strcmp(l -> titre, titre)) && (! strcmp(l -> auteur, auteur)))){
		b -> L = l -> suiv;	
		free(l -> titre);
		free(l -> auteur);
		free(l);
	}
	//sinon
	while(l && (l -> num != num && strcmp(l -> titre, titre) && strcmp(l -> auteur, auteur))){
		lv = l;
		l = l -> suiv;
	}
	if(l -> num == num && (! strcmp(l -> titre, titre)) && (! strcmp(l -> auteur, auteur))){
		lv -> suiv = l -> suiv;
		free(l -> titre);
		free(l -> auteur);
		free(l);
	}
	else{
		printf("Attention, on ne trouve pas cet element !\n");
	}
}

Biblio* trouver_exemplaire(Biblio* b){
	Livre* l = b -> L;
	Biblio* res = creer_biblio();
	while(l){
		Livre* lv = l -> suiv;
		while(lv){
        	if(! strcmp(l -> titre, lv -> titre) && ! strcmp(l -> auteur, lv -> auteur)){
           		inserer_en_tete(res, l -> num, l -> titre, l -> auteur);
        	}
        	lv = lv -> suiv;
        }
        l = l -> suiv;
    }
    if( res -> L == NULL){
    	printf("On ne trouve pas l'ouvrage qui est exemplaire.\n");
    }
	return res;
}

//Question 1.8
void menu(){
	printf("Bonjour!\n");
	printf("-----------------------------------------------------------------------------------------------------------\n");
	printf("Les operations possible :\n");
	printf("0 : pour sortir\n");
	printf("1 : pour affciher tous les livre dans le bibliothèque \n");
	printf("2 : pour ajouter un livre au bibliothèque, avec le nom, le titre et l'auteur de l'ouvrage\n");
	printf("3 : pour supprimer un livre au bibliothèque, avec le nom, le titre et l'auteur de l'ouvrage\n");
	printf("4 : pour rechercher l'ouvrage qui a le même numéro\n");
	printf("5 : pour rechercher l'ouvrage qui a le même titre\n");
	printf("6 : pour rechercher les ouvrages qui ont le même auteur\n");
	printf("7 : pour rechercher les ouvrages qui ont le même titre et l'auteur, mais ont les numéros differents\n");
	printf("fin du operations disponibles\n");
	printf("-----------------------------------------------------------------------------------------------------------\n");
}