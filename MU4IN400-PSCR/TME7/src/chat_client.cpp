#include "chat_common.h"
#include <iostream>
#include <string>
#include <sys/mman.h>
#include <fcntl.h>
#include <unistd.h>
#include <signal.h>

using namespace std;

myshm* shm_send;
myshm* shm_receive;

char buf_send[10];
char buf_receive[10];

void end(int sig) {
    munmap(shm_send, sizeof(myshm));
    shm_unlink(buf_send);

    munmap(shm_receive, sizeof(myshm));
    shm_unlink(buf_receive);

    exit(0);
}

void send_message(myshm* shm) {
    message mes;
    mes.type = 2;
    string input;

    while (getline(cin, input)) {
        strncpy(mes.content, input.c_str(), TAILLE_MESS - 1);
        mes.content[TAILLE_MESS - 1] = '\0';

        sem_wait(&(shm->sem));
        shm->messages[shm->write % MAX_MESS] = mes;
        shm->write++;
        sem_post(&(shm->sem));
    }
}


void receive_message(myshm* shm) {
    signal(SIGINT, end);
    message mes;
    while (true) {
        if (shm->read != shm->write) {
            sem_wait(&(shm->sem));
            mes = shm->messages[shm->read % MAX_MESS];
            shm->read++;
            sem_post(&(shm->sem));

            if (mes.type == 2) {
                cout <<"server: "<<mes.content << endl;
            }
        }
    }
}

int main(int argc, char* argv[]) {
    if (argc <= 2) {
        cerr << "argc <= 2" << endl;
        return 1;
    }

    strcpy(buf_send, "/");
    strcat(buf_send, argv[1]);

    strcpy(buf_receive, "/");
    strcat(buf_receive, argv[2]);

    int fd_send, fd_receive;


    if ((fd_send = shm_open(buf_send, O_RDWR | O_CREAT, 0600)) == -1) {
        perror("shm_open for send");
        exit(1);
    }

    if ((fd_receive = shm_open(buf_receive, O_RDWR | O_CREAT, 0600)) == -1) {
        perror("shm_open for receive");
        exit(1);
    }


    if (ftruncate(fd_send, sizeof(myshm)) == -1) {
        perror("ftruncate for send");
        exit(2);
    }

    if (ftruncate(fd_receive, sizeof(myshm)) == -1) {
        perror("ftruncate for receive");
        exit(2);
    }


    shm_send = (myshm*) mmap(NULL, sizeof(myshm), PROT_READ | PROT_WRITE, MAP_SHARED, fd_send, 0);
    if (shm_send == MAP_FAILED) {
        perror("mmap for send");
        exit(3);
    }

    shm_receive = (myshm*) mmap(NULL, sizeof(myshm), PROT_READ | PROT_WRITE, MAP_SHARED, fd_receive, 0);
    if (shm_receive == MAP_FAILED) {
        perror("mmap for receive");
        exit(3);
    }


    sem_init(&(shm_send->sem), 1, 1);
    sem_init(&(shm_receive->sem), 1, 1);

    pid_t pid = fork();
    if (pid == 0) {
        send_message(shm_send);
        return 0;
    } else if (pid > 0) {
        receive_message(shm_receive);
    } else {
        perror("fork");
        exit(4);
    }

    wait(NULL);
    return 0;
}
