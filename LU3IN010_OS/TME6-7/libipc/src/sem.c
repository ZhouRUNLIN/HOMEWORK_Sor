/*
 * sem.c
 *
 * 2001  Pierre Sens (Pierre.Sens@lip6.fr)
 *
 * Fonction de manipulation de semaphores
 *
 */



#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>
#include <signal.h>
#include <stdlib.h>
#include <stdio.h>

#if defined(__GNU_LIBRARY__) && !defined(_SEM_SEMUN_UNDEFINED)
      /* union semun is defined by including <sys/sem.h> */
#else
    /* according to X/OPEN we have to define it ourselves */
union semun {
   int val;                    /* value for SETVAL */
   struct semid_ds *buf;       /* buffer for IPC_STAT, IPC_SET */
   unsigned short int *array;  /* array for GETALL, SETALL */
   struct seminfo *__buf;      /* buffer for IPC_INFO */
};
#endif

int semid;
struct sembuf operation;
union semun arg;
 
int creer_sem(int nbsem)
{
  int flags = 0666 | IPC_CREAT;
  
  return (semid=semget(IPC_PRIVATE, nbsem, flags)); 
}
 
int init_sem(unsigned short *argvect)
{
  arg.array = argvect;
  return semctl(semid, 1, SETALL, arg);
}
 
int init_un_sem(int semnum, unsigned short argsem)
{
  arg.val = argsem;
  return semctl(semid, semnum, SETVAL, arg);
}
 
int det_sem()
{
  return semctl(semid, 1, IPC_RMID, arg);
}
 
void P(int sem)
{
  operation.sem_num=sem;
  operation.sem_op = -1;
  /* operation.sem_flg=SEM_UNDO;*/
  if (semop(semid,&operation,1)) {
    perror("Erreur dans P");
    exit(1);
  }
}
 
void V(int sem)
{
  operation.sem_num=sem;
  operation.sem_op = 1;
  /* operation.sem_flg=SEM_UNDO;*/
  if (semop(semid,&operation,1)) {
    perror("Erreur dans V");
    exit(1);
  }
}
 
 
