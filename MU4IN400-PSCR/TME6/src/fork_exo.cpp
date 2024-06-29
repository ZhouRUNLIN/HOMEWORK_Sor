#include <iostream>
#include <unistd.h>
#include <sys/wait.h>

int main () {
    const int N = 3;
    std::cout << "main pid=" << getpid() << std::endl;

    for (int i=1, j=N; i<=N && j==N && fork()==0 ; i++ ) {
        std::cout << " i:j " << i << ":" << j << std::endl;
        std::cout << "pid: "<<getpid() << std::endl;
        for (int k=1; k<=i && j==N ; k++) {
            if ( fork() == 0) {
                j=0;
                std::cout << " k:j " << k << ":" << j << std::endl;
                std::cout << "pid: "<< getpid() << std::endl;
            }
        }
    }

    for(int i = 0; i < N; i++) {
        wait(NULL);
    }


    return 0;
}