#include <iostream>
#include <signal.h>
#include <unistd.h>
#include <sys/wait.h>

pid_t fini_pid = -1;
pid_t pid;

void filsFini(int signo) {
    int status;
    fini_pid = wait(&status);
}

void alarm0(int signo) {
    std::cout << "delai" << std::endl;
    kill(pid,SIGTERM);
    exit(0);
}

int wait_till_pid(pid_t pid) {
    int status;
    while ((fini_pid = wait(&status)) != -1) {  //=-1 -> pas de fils a attendre
        if (fini_pid == pid) {
            return pid;
        }
    }
    return -1;
}

int wait_till_pid(pid_t pid, int sec) {
    struct sigaction sa;

    sa.sa_handler = filsFini;
    sigaction(SIGCHLD, &sa, NULL);//SIGCHLD -> filsFini

    sa.sa_handler = alarm0;
    sigaction(SIGALRM, &sa, NULL);//SIGALRM -> alarm0

    alarm(sec);

    while (true) {
        pause();  // attendre jusque'a signal         signal -> traiter -> continuer
        if (fini_pid == pid) {
            alarm(0);  // annuler alarm0
            return pid;
        }
        if (fini_pid != -1) {
            return -1;
        }
    }
}

int main() {
    pid = fork();


    if (pid == 0) { //fils

        sleep(3);
        std::cout << "processus fils fini~" << std::endl;
        exit(0);

    } else {   //parent

        std::cout << "Attente de la fin de processus fils."<<std::endl;

        if (wait_till_pid(pid,4) == pid) {

            std::cout << "tous sont terminé."<<std::endl;
        }
    }

    return 0;
}

