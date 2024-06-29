#include "ServerSocket.h"
#include <iostream>
#include <unistd.h>
#include <string>


int main00() {
	pr::Socket sock;
	sock.connect("localhost", 1664);
	int N=42;
	write(sock.getFD(),&N,sizeof(int));
	read(sock.getFD(),&N,sizeof(int));
	std::cout << N << std::endl;
	return 0;
}


// avec controle
int main0() {

	pr::Socket sock;

	sock.connect("localhost", 1664);

	if (sock.isOpen()) {
		int fd = sock.getFD();
		int i = 10;
		ssize_t msz = sizeof(int);
		if (write(fd, &i, msz) < msz) {
			perror("write");
		}
		std::cout << "envoyé =" << i << std::endl;
		int lu;
		auto nblu = read(fd, &lu, msz);
		if (nblu == 0) {
			std::cout << "Fin connexion par serveur" << std::endl;
		} else if (nblu < msz) {
			perror("read");
		}
		std::cout << "lu =" << lu << std::endl;
	}

	return 0;
}


// avec une boucle, on attend un 0
int main() {

	pr::Socket sock;

	sock.connect("localhost", 1664);

	if (sock.isOpen()) {
		int fd = sock.getFD();

		ssize_t msz = sizeof(int);
		for (int i = 10; i >= 0; i--) {
			if (write(fd, &i, msz) < msz) {
				perror("write");
				break;
			}
			std::cout << "envoyé =" << i << std::endl;

			int lu;
			auto nblu = read(fd, &lu, msz);
			if (nblu == 0) {
				std::cout << "Fin connexion par serveur" << std::endl;
				break;
			} else if (nblu < msz) {
				perror("read");
				break;
			}
			std::cout << "lu =" << lu << std::endl;
		}
	}

	return 0;
}

