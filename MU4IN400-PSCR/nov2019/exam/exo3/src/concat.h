#pragma once

#include <vector>
#include <string>

namespace pr {

class concat {
	// TODO : attributs stockant ref ou pointeurs vers les constituants v1,v2
public:
	concat(std::vector<std::string> & v1, std::vector<std::string> & v2);

	class iterator {
		// TODO : attributs
	public:
		// TODO : signature du constructeur
		iterator(/* A COMPLETER */);
		// TODO : contrat itérateur
		std::string & operator*();
		iterator & operator++();
		bool operator!=(const iterator & other) const;
	};

	iterator begin() ;
	iterator end() ;
};

}
