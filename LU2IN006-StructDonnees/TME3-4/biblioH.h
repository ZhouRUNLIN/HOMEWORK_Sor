#ifndef BIBLIOH
#define BIBLIOH
//Exercice 2
//Question 2.1

#define const int DEFAULT_M = 10;

typedef struct livreh{
	int clef;
	int num;
	char* titre;
	char* auteur;
	struct livreh *suiv;
} LivreH;

typedef struct table{
	int nE; 		/*nombre d’elements contenus dans la table de hachage */
	int m; 			/*taille de la table de hachage */
	LivreH** T; 	/*table de hachage avec resolution des collisions par chainage */
} BiblioH;

//Question 2.2
int fonctionClef(char* auteur);

//Question 2.3
LivreH* creer_livreH(int num,char* titre,char* auteur);
void liberer_livreH(LivreH* l);
void liberer_liste_livreH(LivreH* l);
BiblioH* creer_biblioH(int m);
void liberer_biblioH(BiblioH* b);
int fonctionHachage(int cle, int m);

//Question 2.4
int fonctionHachage(int clef, int m);

//Question 2.5
void inserer_en_teteH(LivreH** liste, LivreH* nouveau);
void inserer(BiblioH* b, int num, char* titre, char* auteur);

//Question 2.6
LivreH* rechercher_numH(BiblioH* b, int num);
LivreH* rechercher_titreH(BiblioH* b, char* titre);
BiblioH* rechercher_auteurH(BiblioH* b, char* auteur);
void supprimer_livreH(BiblioH* b, int num, char* titre, char* auteur);
void fusionnerH(BiblioH* gauche, BiblioH* droite);
int identiqueH(LivreH* gauche, LivreH* droite);
BiblioH* recherche_exemplaireH(BiblioH* b);

#endif