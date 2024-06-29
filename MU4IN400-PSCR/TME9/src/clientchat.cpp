#include "ChatRoomProxy.h"
#include <iostream>
#include <unistd.h>
#include <string>


int main() {

	pr::ChatRoomProxy cr("localhost", 1664);
	std::cout << "Sujet =" << cr.getSubject();
	std::cout << "NbParticipants =" << cr.nbParticipants() << std::endl;
	std::cout << "History =" ;
	for (auto & h : cr.getHistory()) {
		std::cout << h.getAuthor() << "> " << h.getMessage() << std::endl;
	}

	return 0;
}

