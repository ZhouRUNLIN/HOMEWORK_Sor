#include <unistd.h>
#include <vector>
#include <iostream>
#include <sys/wait.h>

int main(int argc, char* argv[]) {

    if (argc < 3) {
        std::cerr << "au moins deux commandes.\n";
        return 1;
    }

    int p = -1;    // trouver  '|'
    for (int i = 1; i < argc; i++) {
        if (std::string(argv[i]) == "|") {
            p = i;
            break;
        }
    }

    if (p == -1) {
        std::cerr << "pas de '|'.\n";
        return 1;
    }

    // separer les arguments en deux
    std::vector<const char*> commande1(argv + 1, argv + p);    // un -> 2 demi-.

    std::vector<const char*> commande2(argv + p + 1, argv + argc);
    commande1.push_back(nullptr);
    commande2.push_back(nullptr);

    // creer pipe
    int fd[2];
    if (pipe(fd) == -1) {
        perror("pipe");
        return 1;
    }

    pid_t pid = fork();
    if (pid == -1) {
        perror("fork");
        return 1;
    }

    if (pid == 0) {                 // fils
        close(fd[1]);  //write
        if (dup2(fd[0], STDIN_FILENO) == -1) {    //entree standard -> pipe
            perror("dup2");
            exit(1);
        }
        close(fd[0]);   //read
        execv(commande2[0], const_cast<char* const*>(commande2.data()));  //faire commande2 dans ce processus
        perror("execv commande2");
        exit(1);
    } else {                        // pere
        close(fd[0]);
        if (dup2(fd[1], STDOUT_FILENO) == -1) {   //sortie standard -> pipe
            perror("dup2");
            exit(1);
        }
        close(fd[1]);
        execv(commande1[0], const_cast<char* const*>(commande1.data())); //faire commande1 dans ce processus
        perror("execv commande1");
        exit(1);
    }

    waitpid(pid, NULL, 0);
    return 0;
}

//./pipe /bin/cat src/pipe.cpp \| /bin/wc -l

// /bin/cat src/pipe.cpp \| /bin/wc -l