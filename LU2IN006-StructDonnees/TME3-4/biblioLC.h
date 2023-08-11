#ifndef BIBLIOLC
#define BIBLIOLC

//Exercice 1
//Question 1.1
typedef struct livre{ 
	int num;
	char* titre;
	char* auteur;
	struct livre* suiv;
} Livre;

typedef struct biblio{ /* Tete fictive */ 
	Livre* L; /* Premier element */
} Biblio;

//Question 1.2
Livre* creer_livre(int num, char* titre, char* auteur);
void liberer_livre(Livre* l);
Biblio* creer_biblio();
void liberer_biblio(Biblio* b);
void inserer_en_tete(Biblio* b, int num, char* titre, char* auteur);

//Question 1.6
void afficher_livre(Livre* l);
void afficher_biblio(Biblio* b);
Livre* chercher_num(Biblio* b, int num);
Livre* chercher_titre(Biblio* b, char* titre);
Biblio* chercher_auteur(Biblio* b, char* auteur);
void supprimer_livre(Biblio* b, int num, char* titre, char* auteur);
Biblio* trouver_exemplaire(Biblio* b);

//Question 1.8
void menu();

#endif