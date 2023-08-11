/* Definition du tas */

#define TAILTAS 127	/* l'indice max et la taille de la plus grande donnee possible */
#define TAILMIN 2	/* le plus petit bloc utilisable */

char tas[TAILTAS+1];
int libre;		/* indice du premier bloc libre dans le tas */

void afficher_tas(void);
    /* affiche le contenu du tas */
    
void tas_init(void);
    /* initialise le contenu du tas quand celui-ci ne contient pas de donnees */
    
