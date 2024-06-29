#ifndef H_CHAT_COMMON
#define H_CHAT_COMMON

#define _XOPEN_SOURCE 700
#define _REENTRANT
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <ctype.h>

#include <sys/ipc.h>
#include <sys/mman.h>
#include <sys/stat.h>        /* Pour les constantes des modes */
#include <fcntl.h>           /* Pour les constantes O_* */
#include <semaphore.h>
#include <signal.h>
#include <sys/time.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <time.h>
#include <errno.h>

#include <string.h>

#define MAX_MESS 50
#define MAX_USERS 10
#define TAILLE_MESS 10

struct message {
  long type;
  char content[TAILLE_MESS];
};

struct myshm {
  int read; /* nombre de messages retransmis par le serveur */
  int write; /* nombre de messages non encore retransmis par le serveur */
  int nb; /* nombre total de messages emis */
  sem_t sem;
  struct message messages[MAX_MESS];
};

char *getName(char *name);

#endif
