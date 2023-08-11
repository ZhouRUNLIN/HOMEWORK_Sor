#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <malloc.h>
#include <sched.h>

#define LONGTIME 8E8
#define DATE_LIMITE 3600

void ProcLong(int *);
void ProcCourt(int *);

// Exemple de processus long (une simple bouble),
// Chaque processus long crée a son tour 4 processus courts
//
void ProcLong(int *pid) {
  long i;
  static int cpt = 0;

  for (i=0;i<LONGTIME;i++) {
    if (i%(long)(LONGTIME/4) == 0)  {
      int *tcpt = (int *) malloc(sizeof(int));
      *tcpt = cpt;
      CreateProc((function_t)ProcCourt,(void *)tcpt, 10);
      cpt++;
    }
    if (i%(long)(LONGTIME/100) == 0)
      printf("Proc. Long %d - %ld\n",*pid, i);
  }
  printf("############ FIN LONG %d\n\n", *pid );
}


// Processus court
void ProcCourt(int *pid) {
  long i;

  for (i=0;i<LONGTIME/10;i++)
    if (i%(long)(LONGTIME/100) == 0)
      printf("Proc. Court %d - %ld\n",*pid, i);
  printf("############ FIN COURT %d\n\n", *pid );
}




// Exemples de primitive d'election definie par l'utilisateur
// Remarques : les primitives d'election sont appelées directement
//             depuis la librairie. Elles ne sont appélées que si au
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


// Election de SJF "Shortest Job Fisrt"
int SJFElect(void) {

	// on ne connait pas le premier job parmi les processus qui a un flag RUN
	int p = -1;

	printf("Shortest Job First !\n");

	int i = 0;
 	while(i < MAXPROC){

		// on recupere l indice du premier processus qui a un flag RUN sans se soucier de sa duree
		if(Tproc[i].flag == RUN && p == -1){
			p = i;
		}
		// si on a un indice, on compare sa duree avec le processus suivant, on le remplace si ce premier
		// est plus long
		else if(Tproc[i].flag == RUN && Tproc[i].duration < Tproc[p].duration){
			p = i;
		}

		i++;
	}

	// on ne peut pas retourner un indice de processus qui n existe pas
	if(p == -1)
		return 0;

  	return p;	
}

// Approximation SJF
int ApproxSJF(void) {

	// on ne connait pas le premier job parmi les processus qui a un flag RUN
	int p = -1;

	// on recupere la date:
	struct timeval thisdate;
	gettimeofday(&thisdate, NULL);

	printf("Approx SJF!\n");

	int i = 0;
 	while(i < MAXPROC){
		if(Tproc[i].flag == RUN){
			// on recupere l indice du premier processus qui a un flag RUN sans se soucier de son ncpu
			if(p == -1){
				p = i;
			}
			// si le processus a ete cree au dela de la date limite (modifiable au debut du fichier)
			else if(thisdate.tv_sec - Tproc[i].start_time.tv_sec > DATE_LIMITE){
				return i;
			}
			// si on a un indice, on compare sa duree avec le processus suivant, on le remplace si ce premier
			// est plus long
			else if(Tproc[i].ncpu < Tproc[p].ncpu){
				p = i;
			}
		}
		i++;
	}

	// on ne peut pas retourner un indice de processus qui n existe pas
	if(p == -1)
		return 0;

  	return p;	
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



  // Definir une nouvelle primitive d'election avec un quantum de 1 seconde
  //SchedParam(NEW, 1, RandomElect);
  //SchedParam(NEW, 1, SJFElect);
  SchedParam(NEW, 1, ApproxSJF);

  // Lancer l'ordonnanceur en mode non "verbeux"
  sched(0);     

  // Imprimer les statistiques
  PrintStat();

  return EXIT_SUCCESS;

}
