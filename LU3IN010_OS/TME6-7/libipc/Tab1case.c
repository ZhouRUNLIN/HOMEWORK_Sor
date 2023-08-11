/* Diffusion tampon 1 case */

  #include <stdio.h> 
  #include <unistd.h> 
  #include <stdlib.h> 
  #include <signal.h> 
  #include <libipc.h>

/************************************************************/

/* definition des parametres */ 

  #define NE          2     /*  Nombre d'emetteurs         */ 
  #define NR          5     /*  Nombre de recepteurs       */ 

/************************************************************/

/* definition des semaphores */ 
	#define EMET 0
	int tab[5]={1,2,3,4,5};
        #define NB_RECEP 6
/************************************************************/

/* definition de la memoire partagee */ 
	typedef struct{
		char* message;
		int nb_recepteurs;
	}t_segpart;
	t_segpart *sp;
	

/************************************************************/

/* variables globales */ 
    int emet_pid[NE], recep_pid[NR]; 

/************************************************************/

/* traitement de Ctrl-C */ 

  void handle_sigint(int sig) { 
      int i;
      for (i = 0; i < NE; i++) kill(emet_pid[i], SIGKILL); 
      for (i = 0; i < NR; i++) kill(recep_pid[i], SIGKILL); 
      det_sem(); 
      det_shm((char *)sp); 
  } 

/************************************************************/

/* fonction EMETTEUR */ 
	void EMETTEUR(char* msg){
		while(1){
			P(EMET);
			sp->message = msg;
			int i;
			for(i =0;i<5;i++ ){
				V(tab[i]);
			}
		}
	}

/************************************************************/

/* fonction RECEPTEUR */ 

	void RECEPTEUR (int i){

		while(1){
			P(tab[i]);
			P(NB_RECEP);
			sp->nb_recepteurs++;
			printf("%s\n",sp->message);
			if(sp->nb_recepteurs == NR){
				V(EMET);
				sp->nb_recepteurs=0;
			}
			V(NB_RECEP);
		}

	}

/************************************************************/

int main() { 
    struct sigaction action;
    /* autres variables (a completer) */
    
    setbuf(stdout, NULL);

	/* Creation du segment de memoire partagee */
	
	if( (sp = (t_segpart *) init_shm(sizeof(t_segpart))) == NULL ){
		perror("init_shm");
		exit(1);
	}
	sp->nb_recepteurs =0;

	/* creation des semaphores */ 
	
	if(creer_sem(7) == -1){
		perror("creer_sem");
		exit(1);
	}


	/* initialisation des semaphores */ 
	init_un_sem(EMET,1);
	init_un_sem(NB_RECEP,1);
	int i;
	for(i=0;i<5;i++){
		init_un_sem(tab[i],0);
	}


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
    
    pause();                    /* attente du Ctrl-C  */
    return EXIT_SUCCESS;
} 
