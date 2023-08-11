# include <stdio.h>
# include <stdlib.h>
# include <sys/time.h>
# include <sys/times.h>
# include <time.h>
# include <unistd.h>
#include "lanceCommande.h"

void lanceCommande(char* commande){
    // les codes pour exercice 2 
    time_t start, end;
    time(&start);
    int status = system(commande);
    time(&end);
    
    if (-1 == status){       
        printf("system error!");
        return;
    }
    else{ 
        if (WIFEXITED(status)){
            if (0 != WEXITSTATUS(status)){
                printf("run shell script fail, script exit code: %d\n", WEXITSTATUS(status));
                return;
            }       
        }
    }
    // les codes precedent peuvent renvoyer un message d’erreur si la commande n’a pu être exécutée correctement.
    printf("Command : %s\nTime for execution : %ld\n", commande, end-start);
}

void lanceCommandeNew(char *commande){
    // les codes pour exercice 2 
	struct tms *startTms = malloc(sizeof(struct tms));
	struct tms *endTms = malloc(sizeof(struct tms));
	clock_t tStart,tEnd;	
	
	tStart = times(startTms);
    int status = system(commande);
    tEnd = times(endTms);
    
    if (-1 == status){       
        printf("system error!");
        return;
    }
    else{ 
        if (WIFEXITED(status)){
            if (0 != WEXITSTATUS(status)){
                printf("run shell script fail, script exit code: %d\n", WEXITSTATUS(status));
                return;
            }       
        }
    }
    // les codes precedent peuvent renvoyer un message d’erreur si la commande n’a pu être exécutée correctement.
    fprintf(stdout,"\nStatistiques de %s:\n", commande);
	fprintf(stdout,"Temps total : %6.6f\n",
		(double)(tEnd-tStart)/ CLOCKS_PER_SEC);
	fprintf(stdout,"Temps utilisateur : %6.6f\n", 
		(double)(endTms->tms_utime - startTms->tms_utime)/ CLOCKS_PER_SEC);
	fprintf(stdout,"Temps systeme : %6.6f\n",
		(double)(endTms->tms_stime - startTms->tms_stime)/ CLOCKS_PER_SEC);
	fprintf(stdout,"Temps utilisateur fils : %6.6f\n",
		(double)(endTms->tms_cutime - startTms->tms_cutime)/ CLOCKS_PER_SEC);
	fprintf(stdout,"Temps systeme fils : %6.6f\n",
		(double)(endTms->tms_cstime - startTms->tms_cstime)/ CLOCKS_PER_SEC);

    free(startTms);
    free(endTms);
}