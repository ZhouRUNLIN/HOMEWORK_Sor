#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <malloc.h>
#include "sched.h"

void ProcLong(int *pid) {
  long i;

  for (i=0;i<4E8;i++)
    if (i%(long)4E7 == 0) 
      printf("Proc. Long%d - %ld\n",*pid, i);
  printf("############ FIN LONG %d\n\n", *pid );
}

void ProcCourt(int *pid) {
  long i;

  for (i=0;i<1E8;i++)
    if (i%(long)1E7 == 0) 
      printf("Proc. Court%d - %ld\n",*pid, i);
  printf("############ FIN COURT %d\n\n", *pid );
}



// Exemples de primitive d'election definie par l'utilisateur
// Remarques : les primitives d'election sont appelées directement
//             depuis la librairie. Elles ne sont appélée que si au
//             moins un processus est à l'etat pret (RUN)
//             Ces primitives manipulent la table globale des processus
//             définie dans sched.h


// Election aléatoire
int RandomElect(void) {
  int i;

  printf("RANDOM Election !\n");

  do {
    i = (int) ((float)MAXPROC*rand()/(RAND_MAX+1.0));
  } while (Tproc[i].flag != RUN);

  return i;
}



int main (int argc, char *argv[]) {
  int i;
  int *j;  

  // Créer les processus long
  for  (i = 0; i < 2; i++) {
    j = (int *) malloc(sizeof(int));
    *j= i;
    CreateProc((function_t)ProcLong,(void *)j, 80);
  }

  // Créer les processus court
  for  (i = 0; i < 2; i++) {
    j = (int *) malloc(sizeof(int));
    *j= i;
    CreateProc((function_t)ProcCourt,(void *)j, 10);
  }


  // Definir une nouvelle primitive d'election avec un quantum de 0.3 seconde
  SchedParam(NEW, 0.3, RandomElect);

  // Definir une nouvelle primitive d'election sans quantum (batch)
  //SchedParam(NEW, 0, SJFElect);

  // Redefinir le quantum par defaut 
  //SchedParam(PREMPT, 2, NULL);

  // Passer en mode batch
  //SchedParam(BATCH, 0, NULL);


  // Lancer l'ordonnanceur en mode non "verbeux"
  sched(0);	

  // Imprimer les statistiques
  PrintStat();

  return EXIT_SUCCESS;

}
