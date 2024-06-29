#include "ServerSocket.h"
#include <cstring>
#include <unistd.h>
#include <iostream>

namespace pr {

    ServerSocket::ServerSocket(int port) : socketfd(-1) {
        int fd = socket(AF_INET, SOCK_STREAM, 0);
        if (fd == -1) {
            perror("socket");
            return;
        }

        struct sockaddr_in sin;
        memset(&sin, 0, sizeof(sin));
        sin.sin_family = AF_INET;
        sin.sin_addr.s_addr = htonl(INADDR_ANY);
        sin.sin_port = htons(port);

        if (bind(fd, (struct sockaddr *) &sin, sizeof(sin)) < 0) {
            perror("bind");
            ::close(fd);
            return;
        }

        if (listen(fd, 50) < 0) {
            perror("listen");
            ::close(fd);
            return;
        }

        socketfd = fd;
    }

    Socket ServerSocket::accept() {
        struct sockaddr_in exp;
        socklen_t len = sizeof(exp);
        int scom = ::accept(socketfd, (struct sockaddr *) &exp, &len);
        if (scom < 0) {
            perror("accept");
        } else {
            std::cout << "Accepted connection from " << &exp << std::endl;
        }
        return scom;
    }

    void ServerSocket::close() {
        if (socketfd != -1) {
            ::close(socketfd);
        }
    }

} // namespace pr

