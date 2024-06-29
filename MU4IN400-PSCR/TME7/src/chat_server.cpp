#include "chat_common.h"
#include <iostream>
#include <string>
#include <sys/mman.h>
#include <fcntl.h>
#include <unistd.h>
#include <signal.h>

using namespace std;

myshm* shm_receive;
myshm* shm_send;

char buf_receive[10];
char buf_send[10];

void end(int sig) {
    munmap(shm_receive, sizeof(myshm));
    shm_unlink(buf_receive);

    munmap(shm_send, sizeof(myshm));
    shm_unlink(buf_send);

    exit(0);
}

void server(myshm* shm_receive, myshm* shm_send) {
    signal(SIGINT, end);
    message mes;
    while (true) {


        if (shm_receive->read != shm_receive->write) {
            sem_wait(&(shm_receive->sem));
            mes = shm_receive->messages[shm_receive->read % MAX_MESS];
            shm_receive->read++;
            sem_post(&(shm_receive->sem));

            if (mes.type == 2) {
                cout << "client: " << mes.content << endl;


                message reply;
                reply.type = 2;
                strncpy(reply.content, "", TAILLE_MESS-1);
                strncat(reply.content, mes.content, TAILLE_MESS - strlen(reply.content)-1);

                sem_wait(&(shm_send->sem));
                shm_send->messages[shm_send->write % MAX_MESS] = reply;
                shm_send->write++;
                sem_post(&(shm_send->sem));
            }
        }
    }
}

int main(int argc, char* argv[]) {
    if (argc <= 2) {
        cerr << "argc <= 2" << endl;
        return 1;
    }

    strcpy(buf_receive, "/");
    strcat(buf_receive, argv[1]);

    strcpy(buf_send, "/");
    strcat(buf_send, argv[2]);

    int fd_receive, fd_send;


    if ((fd_receive = shm_open(buf_receive, O_RDWR | O_CREAT, 0600)) == -1) {
        perror("shm_open for receive");
        exit(1);
    }

    if ((fd_send = shm_open(buf_send, O_RDWR | O_CREAT, 0600)) == -1) {
        perror("shm_open for send");
        exit(1);
    }


    if (ftruncate(fd_receive, sizeof(myshm)) == -1) {
        perror("ftruncate for receive");
        exit(2);
    }

    if (ftruncate(fd_send, sizeof(myshm)) == -1) {
        perror("ftruncate for send");
        exit(2);
    }


    shm_receive = (myshm*) mmap(NULL, sizeof(myshm), PROT_READ | PROT_WRITE, MAP_SHARED, fd_receive, 0);
    if (shm_receive == MAP_FAILED) {
        perror("mmap for receive");
        exit(3);
    }

    shm_send = (myshm*) mmap(NULL, sizeof(myshm), PROT_READ | PROT_WRITE, MAP_SHARED, fd_send, 0);
    if (shm_send == MAP_FAILED) {
        perror("mmap for send");
        exit(3);
    }


    sem_init(&(shm_receive->sem), 1, 1);
    sem_init(&(shm_send->sem), 1, 1);

    server(shm_receive, shm_send);

    return 0;
}
