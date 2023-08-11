/*
 * shmem.c
 *
 * 2001  Pierre Sens (Pierre.Sens@lip6.fr)
 *
 * Fonction de manipulation de la mémoire partagée
 *
 */

#include <stdio.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/shm.h>
 
#define MAXSEG 20

static struct {
	key_t cle;
	char *adr;
} TabSeg[MAXSEG];

 
char *init_shm(int taille)
{
  int i = 0;
  
  int flags = 0666 | IPC_CREAT | IPC_EXCL;


  /* Trouver un emplacement libre dans TabSeg */
  while ( i < MAXSEG && TabSeg[i].adr != NULL) 
    i++;
		
  if ( i == MAXSEG ) {
    fprintf(stderr,"Table des segments pleine\n");
    return NULL;
  }

 
  /* recuperation/creation du segment de memoire partagee */
  if ( (TabSeg[i].cle = shmget (IPC_PRIVATE, taille, flags)) == -1) {
    perror("Erreur à la creation du segment");
    return NULL;
  }
 
  /* attachement du segment */
  if ( (TabSeg[i].adr = (char *)shmat(TabSeg[i].cle, 0, 0666)) ==  (char *)-1) {
    perror("Erreur à l'attachement du segment");
    return NULL;
  }
  
  return TabSeg[i].adr;
}

 
int det_shm(char *adr)
{
  int i = 0;

  /* Rechercher les segments dans TabSeg */
  while ( i < MAXSEG && TabSeg[i].adr != adr) 
    i++;

  if ( i == MAXSEG) {
    fprintf(stderr,"Table des segments pleine\n");
    return -1;
  }  

  if (shmdt(adr) == -1) {
    perror("Erreur au detachement");
    return -1;
  }

  return shmctl(TabSeg[i].cle, IPC_RMID, NULL);
}
 
 
 
