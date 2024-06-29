#include <unistd.h>
#include <sys/socket.h>
#include <netdb.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <iostream>
#include "Socket.h"

namespace pr {


    std::ostream &operator<<(std::ostream &os, struct sockaddr_in *addr) {
        char hname[1024];
        if (getnameinfo((struct sockaddr *)addr, sizeof *addr, hname, 1024, nullptr, 0, 0) == 0) {
            os << '"' << hname << '"' << " ";
        }
        os << inet_ntoa(addr->sin_addr) << ":" << ntohs(addr->sin_port) << std::endl;
        return os;
    }

    void Socket::connect(in_addr addr, int port) {
        sockaddr_in dest;
        dest.sin_family = AF_INET;
        dest.sin_port = htons(port);
        dest.sin_addr = addr;



        fd = socket(AF_INET, SOCK_STREAM, 0);
        if (!fd) {
            perror("socket");
            return;
        }


        if (::connect(fd, (struct sockaddr *) &dest, sizeof dest)) {
            perror("connect");
            ::close(fd);
            fd = -1;
            return;
        }
    }

    void Socket::connect(const std::string &host, int port) {
        struct addrinfo *res;


        if (getaddrinfo(host.c_str(), nullptr, nullptr, &res) != 0) {
            perror("getaddrinfo");
            return;
        }

        in_addr ipv4 = ((struct sockaddr_in *) res->ai_addr)->sin_addr;
        freeaddrinfo(res);
        connect(ipv4, port);
    }


    void Socket::close() {
        if (fd != -1) {
            shutdown(fd, 1);
            ::close(fd);
        }
    }

} // namespace pr
