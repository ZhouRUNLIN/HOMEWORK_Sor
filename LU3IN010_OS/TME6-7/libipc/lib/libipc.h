/* fichier LIBIPC.H 
 *
 * Liste des fonctions de la libipc 
 * Pierre.Sens@lip6.fr
 */
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>


/* Fonctions de manipulation des semaphores */
int creer_sem(int);

int utiliser_sem(int);

int init_un_sem(int, unsigned short); 

int init_sem(unsigned short *);

int det_sem(void);

void P(int);

void V(int);

/* Fonctions de manipulation de la memoire partagée */

char *init_shm(int);

int det_shm(char *);

