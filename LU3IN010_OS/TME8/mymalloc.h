
/* réserve dans le tas une zone de taille octets. Cette fonction retourne l'adresse du début de la zone allouée, si impossible, retourne NULL */
char *tas_malloc(unsigned int taille);


/* libère la zone dont le début est désigné par ptr */
int tas_free(char *ptr);


/* recherche une zone libre selon la taille, retourne l'adresse la zone si possible sinon retourne -1 , 
pred est l'adresse dans le tas du début de la zone précédent la zone retournée */

int first_fit(int taille);
