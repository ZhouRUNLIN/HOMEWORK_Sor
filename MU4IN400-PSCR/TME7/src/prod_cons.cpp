#include "Stack.h"
#include <iostream>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <signal.h>
#include <vector>

using namespace std;
using namespace pr;

bool interrupted = false;

void signalHandler(int signum) {
    interrupted = true;
}

void producteur(Stack<char>* stack, char ch) {
    close(STDIN_FILENO);
    if (!interrupted && ch != '\0') {
        stack->push(ch);
        cout << "Producteur push: " << ch << endl;
    }
}

void consommateur(Stack<char>* stack, bool active) {
    close(STDIN_FILENO);
    if (!interrupted && active) {
        char c = stack->pop();
        cout << "Consommateur pop: " << c << endl;
    }
    while (!interrupted && !active) {
        sleep(1);
    }
}

int main() {

    int n, m, x;
    cout << "nb de N: ";
    cin >> n;
    cout << "nb de M: ";
    cin >> m;
    cout << "nb d'entrer: ";
    cin >> x;

    vector<char> toPush(x, '\0');
    cout << "enter " << x << " characters: ";
    for (int i = 0; i < x; ++i) {
        cin >> toPush[i];
    }


    n = max(n, x);
    m = max(m, n);

    int fd;
    Stack<char>* sp;

    if ((fd = shm_open("/monshm", O_RDWR | O_CREAT, 0600)) == -1) {
        perror("shm_open");
        exit(1);
    }

    if (ftruncate(fd, sizeof(Stack<char>)) == -1) {
        perror("ftruncate");
        exit(1);
    }

    if ((sp = (Stack<char>*) mmap(NULL, sizeof(Stack<char>), PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0)) == MAP_FAILED) {
        perror("mmap");
        exit(1);
    }

    new (sp) Stack<char>();

    signal(SIGINT, signalHandler); // Ctrl-C

    vector<pid_t> children;


    for (int i = 0; i < n; ++i) {
        pid_t pid = fork();
        if (pid == 0) {
            sleep(i);
            producteur(sp, i < x ? toPush[i] : '\0');
            exit(0);
        }
        children.push_back(pid);
    }


    for (int i = 0; i < m; ++i) {
        pid_t pid = fork();
        if (pid == 0) {
            sleep(i);
            consommateur(sp, i < n);
            exit(0);
        }
        children.push_back(pid);
    }

    while (!interrupted) {
        sleep(1); //1s
    }

    for (pid_t pid : children) {
        kill(pid, SIGINT);
    }

    for (pid_t pid : children) {
        wait(0);
    }

    std::cout << std::endl;
    std::cout << "~~~~~fini~~~~~" << std::endl;
    sp->~Stack<char>();
    munmap(sp, sizeof(Stack<char>));
    shm_unlink("/monshm");

    return 0;
}


