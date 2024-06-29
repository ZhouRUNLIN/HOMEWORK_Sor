#include <iostream>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <vector>
#include <signal.h>

#include "Stack.h"

using namespace std;
using namespace pr;

void producteur (Stack<char> *stack) {
    char c;
    while (cin.get(c)) {
        stack->push(c);
    }
}

void consomateur (Stack<char> *stack) {
    while (true) {
        char c = stack->pop();
        cout << static_cast<unsigned char>(c) << flush;
    }
}

std::vector<pid_t> tokill;

void killem(int) {
    for(auto p: tokill) {
        kill(p, SIGINT);
    }
}

int main () {
    size_t shmsize = sizeof(Stack<char>);
    std::cout << "Allocating segment size "<< shmsize << std::endl;

    int fd;
    void * addr;

    bool useAnonymous = true;
    if (useAnonymous) {
        addr = mmap(nullptr, shmsize, PROT_READ | PROT_WRITE, MAP_SHARED | MAP_ANONYMOUS, -1, 0);
        if (addr == MAP_FAILED) {
            perror("mmap anonymous");
            exit(1);
        }
    } else {
        // fd = shm_open("/myshm",O_CREAT|O_EXCL|O_RDWR,0666);
        fd = shm_open("/myshm",O_CREAT|O_RDWR,0666);
        if(fd < 0) {
            perror("shm_open");
            return 1;
        }
        if (ftruncate(fd,shmsize) == -1){
            perror("ftruncate");
            return 1;
        }
        addr = mmap(nullptr, shmsize, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
        if (addr == MAP_FAILED){
            perror("mmap anonymous");
            exit(1);
        }
    }

    Stack<char> * s = new (addr) Stack<char>();

    pid_t pp = fork();
    if (pp==0) {
        sleep(1);
        producteur(s);
        return 0;
    }

    pid_t pc = fork();
    if (pc==0) {
        sleep(1);
        consomateur(s);
        return 0;
    } else {
        tokill.push_back(pc);
    }

    signal(SIGINT, killem);
    wait(0);
    wait(0);
    s->~Stack();

    if (munmap(addr,shmsize) != 0) {
        perror("munmap");
        exit(1);
    }
    if (! useAnonymous){
        if(shm_unlink("/myshm") != 0){
            perror("sem unlink");
            exit(2);
        }
    }

    return 0;
}


