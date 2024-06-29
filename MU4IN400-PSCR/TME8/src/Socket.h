#ifndef SRC_SOCKET_H_
#define SRC_SOCKET_H_

#include <netinet/ip.h>
#include <string>
#include <iosfwd>

namespace pr {

class Socket {
	int fd;

public :
	Socket():fd(-1){}
	Socket(int fd):fd(fd){}

	// tente de se connecter à l'hôte fourni
	void connect(const std::string & host, int port);
	void connect(in_addr addr, int port);

	bool isOpen() const {return fd != -1;}
	int getFD() { return fd ;}

	void close();
};

std::ostream & operator<< (std::ostream & os, struct sockaddr_in * addr);

}

#endif /* SRC_SOCKET_H_ */
