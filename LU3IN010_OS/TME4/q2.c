#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#define MAXFILS 2
void insert_term(int* l,int p){
	int i;
	for(i=0;*(l+i)!=-1;i++);
	*(l+i)=p;
}

void delete_term(int* l,int p){
	int i;
	for(i=0;*(l+i)!=p;i++);
	*(l+i)=-1;
}

int wait_fils(int* l){
	int i;
	while(1){
		for(i=0;i<MAXFILS;i++){
			if(waitpid(*(l+i),NULL,WNOHANG)!=0){
				delete_term(l,*(l+i));
				return *(l+i);
			}
		}
		sleep(1);
	}
}

int main(int argc, char *argv[]){
	char* command = argv[1];
	int i = 2; int p = 0;
	int cmd;
	int* list_fils = malloc(MAXFILS*sizeof(int));
	for(int i0=0;i0<MAXFILS;i0++) *(list_fils+i0)=-1;
	int n_fils = 0;
	while(i<argc){
		if(n_fils<MAXFILS){
			if((p=fork()) == 0){
				execl("./", "grep", command, argv[i]);
				printf("p:%d i:%d\n",p,i);
				exit(0);
			}
			else{
				i++;
				n_fils++;
				insert_term(list_fils,p);
				printf("nb_fils:%d\n",n_fils);
			}
		}
		else{
			wait_fils(list_fils);
			n_fils--;
			printf("nb_fils:%d\n",n_fils);
		}
	}
	free(list_fils);
	return 0;
}