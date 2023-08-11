#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include <libipc.h>
/* Definition des semaphores */
 
#define SEM1    0
#define SEM2    1
 
/* Definition du format du segment de memoire partagee */
typedef struct {
	int a;
} t_segpart;

t_segpart *sp; 


/* fonction appele lors la fin d'un fils */
/*void child()
{
	while(wait3((int *)NULL,WNOHANG,(struct rusage *)NULL) > 0)
	{*/
	/* Un nouveau fils vient de se termine */
/*
	printf("*****\n");
	}
}
*/

/* fonction du premier processus fils */
void fils1(void)
{
        sleep(10);
	sp->a++;
        V(SEM1);
        printf("fin1\n");
        exit(0);
}
 
 
/* fonction du premier processus fils */
void fils2(void)
{
        sleep(10);
	sp->a+=2;
        V(SEM2);
        printf("fin2\n");
        exit(0);
}

 
int main(int argc, char *argv[])
{
        int semid, pid;

	/* Associer SIGCHLD a la fonction child */
	/*signal (SIGCHLD,child); */
	/* Creer le segment de memoire partager */
	if ( (sp = (t_segpart *)init_shm(sizeof(t_segpart) ))== NULL) {
		perror("init_shm");
		exit(1);
	}

        /* Creer les semaphores */
        if ((semid = creer_sem(2)) == -1) {
                perror("creer_sem");
                exit(1);
        }
 
        /* Initialiser les valeurs */
 
        init_un_sem(SEM1,0);
        init_un_sem(SEM2,0);

	sp->a = 0;

        /* creer le premier processus fils */
        if ((pid = fork()) == -1) {
                perror("fork");
                exit(2);
        }
        if (pid == 0) {
                /* Premier processus fils */
                fils1();
        }
 
        /* creer le second processus fils */
        if ((pid = fork()) == -1) {
                perror("fork");
                exit(2);
        }
        if (pid == 0) {
                /* Second processus fils */
                fils2();
        }
 
        /* Processus Pere */
        printf("le pere attend...\n");
        P(SEM1);
        printf("fin du fils 1\n");
        P(SEM2);
        printf("fin du fils 2\n");
	printf("valeur du compteur = %d\n", sp->a);

        /* Destruction des semaphores et du segment de memoire */
        det_sem();
	det_shm((char *) sp);
	return EXIT_SUCCESS; 
}
 
