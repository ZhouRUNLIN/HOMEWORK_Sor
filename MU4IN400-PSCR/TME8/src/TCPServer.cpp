#include "TCPServer.h"
#include <iostream>
#include <unistd.h>
#include <algorithm>

namespace pr {


    void TCPServer::handleClient(Socket sc) {
        int i;
        read(sc.getFD(), &i, sizeof(i));
        i *= 2;
        write(sc.getFD(), &i, sizeof(i));
        sc.close();
    }



    bool TCPServer::startServer(int port) {
        ss = new ServerSocket(port);

        if (!ss->isOpen()) {
            exit(1);
        }
            while (true) {
                std::cout << "En attente sur accept" << std::endl;
                Socket sc = ss->accept();
                handleClient(sc);
                    threads.emplace_back(&TCPServer::handleClient, this, sc);

            }
            return true;
        }



    TCPServer::~TCPServer() {
        for (auto &t : threads) {
            t.join();
        }
    }

    void pr::TCPServer::stopServer () {
        if (ss != nullptr) {
            ss->close();
            delete ss;
            ss = nullptr;
        }

        for (auto &t: threads) {
            if (t.joinable()) {
                t.join();
            }
        }
    }

} // namespace pr
