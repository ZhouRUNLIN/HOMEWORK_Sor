#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <string.h>


int main(int argc, char const *argv[]){
    // la taille maximum de commande
    int tailleMAX = 255;
    char b[tailleMAX];
    char *ls[tailleMAX];

    // les variables du processus
    int pid, pos;
    int bool_WAIT = 1;

    while(strcmp(fgets(b, tailleMAX, stdin), "quit\n")!= 0){

        b[strlen(b) - 1] = '\0';  		
        if(b[strlen(b) - 1] == '&'){		// pour scaner la dernier mot de la commande
            bool_WAIT = 0;
            b[strlen(b) - 1] = '\0';
        }
        if((pid = fork()) == 0){			// fils
            char *ptr = strtok(b, " ");
            int pos = 0;
            while (ptr != NULL){
                ls[pos++] = ptr;
                ptr = strtok(NULL, " ");
            }
            ls[pos] = NULL;
            execvp(ls[0], ls);
            exit(EXIT_SUCCESS);
        }
		else{ 								// père
            if(bool_WAIT == 1){
                struct rusage waitForFils;
                wait3(NULL, 0, &waitForFils);
                struct timeval UserTime = waitForFils.ru_utime, SysTime = waitForFils.ru_stime;
                printf("User Time: %ld.%lds\n Sys Time: %ld.%lds\n", UserTime.tv_sec + UserTime.tv_usec/1000000, UserTime.tv_usec%1000000, SysTime.tv_sec + SysTime.tv_usec/1000000, SysTime.tv_usec%1000000);
            }
        }
    }

    return 0;
}






