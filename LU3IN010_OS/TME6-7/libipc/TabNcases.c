/* Diffusion tampon N case */

  #include <stdio.h> 
  #include <unistd.h> 
  #include <stdlib.h> 
  #include <signal.h> 
  #include <libipc.h>
  #include <string.h>

/************************************************************/

/* definition des parametres */ 

  #define NE          2     /*  Nombre d'emetteurs         */ 
  #define NR          5     /*  Nombre de recepteurs       */ 
  #define NMAX        3     /*  Taille du tampon           */ 

/************************************************************/

/* definition des semaphores */ 

	#define EMET 0
	int tabRecep[NR] = {1,2,3,4,5};
	int MutexL[NMAX]={6,7,8};
	int SC[NMAX]={9,10,11};
	#define MutexE 12
/************************************************************/

/* definition de la memoire partagee */ 
	typedef struct{
		char* messages[NMAX];
		int nb_recepteurs[NMAX];
		int id;
	}t_segpart;
	t_segpart *sp;

/************************************************************/

/* variables globales */ 
    int emet_pid[NE], recep_pid[NR]; 

/************************************************************/

/* traitement de Ctrl-C */ 

  void handle_sigint(int sig) 
  { int i;
  	for (i = 0; i < NE; i++) kill(emet_pid[i], SIGKILL); 
	for (i = 0; i < NR; i++) kill(recep_pid[i], SIGKILL); 
	det_sem(); 
	det_shm((char *)sp); 
 
  } 

/************************************************************/

/* fonction EMETTEUR */ 
	void EMETTEUR(char* msg){
		int I;
		while(1){
			P(EMET);
			
			P(MutexE);
			I = sp->id;
			sp->id = ((sp->id)+1)%NMAX;
			P(SC[I]);
			V(MutexE);
			sp->messages[I] = msg;
			V(SC[I]);
			
			int i;
			for(i =0;i<NR;i++ ){
				V(tabRecep[i]);
			}
		}
	}
/************************************************************/

/* fonction RECEPTEUR */ 
	void RECEPTEUR (int id){
		int i = 0;
		while(1){
			P(tabRecep[id]);
			P(MutexL[i]);
			P(SC[i]);
			sp->nb_recepteurs[i]++;
			printf("%s\n",sp->messages[i]);
			printf("%d\n",i);
			
			V(SC[i]);
			
			printf("cpt = %d\n", sp->nb_recepteurs[i]);
			if(sp->nb_recepteurs[i] == NR){
				V(EMET);
				sp->nb_recepteurs[i]=0;
				printf("ici\n");
			}
			
			V(MutexL[i]);
			i= (i+1)%NMAX;
			
		}

	}
/************************************************************/

int main() { 
    struct sigaction action;
    /* autres variables (a completer) */
    
    setbuf(stdout, NULL);

/* Creation du segment de memoire partagee */
	int i ;
	if( (sp = (t_segpart *) init_shm(sizeof(t_segpart))) == NULL ){
		perror("init_shm");
		exit(1);
	}
	for (i= 0; i<NMAX;i++){
		sp->nb_recepteurs[i] =0;
	}
	sp->id = 0;
/* creation des semaphores */ 

	if(creer_sem(13) == -1){
		perror("creer_sem");
		exit(1);
	}


/* initialisation des semaphores */ 

	init_un_sem(EMET,NMAX);
	for(i=0;i<NR;i++){
		init_un_sem(tabRecep[i],0);	
	}
	for(i=0;i<NMAX;i++){
		init_un_sem(MutexL[i],1);	
		init_un_sem(SC[i],1);
	}
	init_un_sem(MutexE,1);
/* creation des processus emetteurs */ 
	
	i=0;
	int p;
	while(i<NE ){

		if((p=fork())==0){
			EMETTEUR("Bonjour.\n");
			
		}else{
			i++;
			emet_pid[i]=p;
		}
	}
/* creation des processus recepteurs */ 
	if(p!=0){
		i=0;
		while(i<NR){
			if((p=fork())==0){
				RECEPTEUR(i);
			}
			else{
				i++;
				recep_pid[i]=p;
			}
		}
	}




/* redefinition du traitement de Ctrl-C pour arreter le programme */ 

    sigemptyset(&action.sa_mask);
    action.sa_flags = 0;
    action.sa_handler = handle_sigint;
    sigaction(SIGINT, &action, 0); 

    pause();                     /* attente du Ctrl-C */
    return EXIT_SUCCESS;
} 
