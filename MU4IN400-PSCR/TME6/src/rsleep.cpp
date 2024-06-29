#include <ctime>
#include <cstdlib>
#include "rsleep.h"

void randsleep() {
  int r = rand();
  double ratio = (double)r / (double) RAND_MAX;
  struct timespec tosleep;
  tosleep.tv_sec =0;
  // 300 millions de ns = 0.3 secondes
  tosleep.tv_nsec = 300000000 + ratio*700000000;
  struct timespec remain;
  while ( nanosleep(&tosleep, &remain) != 0) {
    tosleep = remain;
  }
}


//Q4
//La boucle de la fonction randsleep garantit que le processus se met en pause pendant
// toute la durée spécifiée, même lorsqu'un signal est reçu et interrompu.
// Pour ce faire, elle vérifie la valeur de retour de nanosleep et d'errno,
// puis rappelle nanosleep pour le reste du temps de pause.
