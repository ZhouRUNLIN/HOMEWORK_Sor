#include "Semaphore.h"
#include <iostream>

void pinger(int n) {
	for (int i=0; i < n ; i++) {
		std::cout << "ping ";
	}
}

void ponger(int n) {
	for (int i=0; i < n ; i++) {
		std::cout << "pong ";
	}
}

int main () {
	// a faire varier si on le souhaite
	const int NBITER = 20;

	// TODO : instancier un thread pinger et un thread ponger avec n=NBITER

	// TODO : terminaison et sortie propre

	return 0;
}

