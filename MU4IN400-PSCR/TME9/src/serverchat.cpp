#include "TextChatRoom.h"
#include "ChatServer.h"
#include "MTChatRoom.h"
#include <iostream>
#include <unistd.h>


int main() {

	pr::TextChatRoom tcr ("C++");
	pr::MTChatRoom mcr (&tcr);
	pr::TextChatter schat("Echo Server");
	mcr.posterMessage({"serveur","début session"});
	tcr.joinChatRoom(&schat);
	{
		pr::ChatServer server(&mcr,1664);

		// attend entree sur la console
		std::string s ;
		std::cin >> s ;

		std::cout << "Début fin du serveur." << std::endl ;

		// on quit = dtor du serveur
	}
	tcr.posterMessage({"serveur","fin session"});
	std::cout << "Ok fin du serveur." << std::endl;
	tcr.leaveChatRoom(&schat);

	return 0;
}

