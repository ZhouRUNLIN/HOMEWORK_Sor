#include "HashMap.h"
#include "MultiHashMap.h"
#include <iostream>

using namespace pr;


#define N 3

int main () {
	HashMap<std::string, int> map(1024);

	map.put("toto",3);

	std::cout << *map.get("toto") << std::endl;

	MHashMap<std::string, int> map2(1024);

	map2.put("toto",3);

	std::cout << *map2.get("toto") << std::endl;

	return 0;
}


