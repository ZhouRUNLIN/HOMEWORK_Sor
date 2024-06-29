#include <iostream>
#include <unistd.h>
#include <signal.h>
#include <cstdlib>
#include <ctime>
#include <sys/wait.h>
#include "rsleep.h"

int vies_fils = 3;
int vies_parent = 3;
pid_t parent_pid, child_pid;
bool isfils = true;
bool running = true;

void traiter_signal(int sig) {
    if (isfils) {
        vies_parent--;
        std::cout << "fils coup paré" << std::endl;
        std::cout << "nb. de vies de parent: " << vies_parent << std::endl;
        if (vies_parent == 0) {
            kill(parent_pid, SIGTERM);
            std::cout << "~~~pere decede~~~" << std::endl;
            std::cout << "~~~fils gagne~~~" << std::endl;
            running = false;
        }
    } else {
        vies_fils--;
        std::cout << "parent coup paré" << std::endl;
        std::cout << "nb. de vies de fils: " << vies_fils << std::endl;
        if (vies_fils == 0) {
            kill(child_pid, SIGTERM);
            std::cout << "~~~fils decede~~~" << std::endl;
            std::cout << "~~~pere gagne~~~" << std::endl;
            running = false;
        }
    }
}

void attaque(pid_t ennemis) {
    if (kill(ennemis, SIGINT) == -1) {   // -1 defaut d'envoi, ennemi mort
        exit(0);
    }
    randsleep();
}

void defense() {

    sigset_t set;
    sigemptyset(&set);  //assuer set est vide
    sigaddset(&set, SIGINT);

    sigprocmask(SIG_BLOCK, &set, NULL);  //signal -> pendingb

    randsleep();

    sigpending(&set); //pending -> set

    if (sigismember(&set, SIGINT)) {  // i y a pending SIGINT dans set -> defendu

        int sig;
        sigwait(&set, &sig);

        if (isfils){

        std::cout << "fils s'est defendu" << std::endl;

        } else{

        std::cout << "parent s'est defendu" << std::endl;
        }
    }

    sigprocmask(SIG_UNBLOCK, &set, NULL);
}

void combat(pid_t ennemis) {
    while (running) {
        defense();
        attaque(ennemis);
    }
}

int main() {
    srand(time(0));
    parent_pid = getpid();

    std::cout << "~~~~~~~~~~la bataille a commence~~~~~~~~~~" << std::endl;

    pid_t pid = fork();
    child_pid = pid;

    signal(SIGINT, traiter_signal);  //recu SIGINT -> taiter signal

    if (pid == 0) { // fils
        isfils = true;
        srand(time(0) + getpid());
        combat(getppid());  // pere -> ennemis
    } else { // parent
        isfils = false;
        srand(time(0) + getpid());
        combat(pid);  // fils -> ennemis
        wait(NULL);
    }

    return 0;
}

// Q3
//    if (pid == 0) {    // fils
//        isfils = true;
//        combat(getppid());
//    } else {              // parent
//        isfils = false;
//        combat(pid);
//        wait(NULL);
//    }
//
//    return 0;
//}

// Q6
// Les batailles sont équitables en raison de la présence de nombres aléatoires,
// et le comportement des deux parties est imprévisible,
// ce qui permet des batailles équitables.