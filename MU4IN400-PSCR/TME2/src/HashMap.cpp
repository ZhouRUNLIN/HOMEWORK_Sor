#include <iostream>
#include "HashMap.h"

int main() {

    HashTable<std::string,int> table(10);


    table.put("xxx", 1);


    if (table.get("xxx")) {
        std::cout << "xxx: " << *table.get("xxx") << std::endl;
    } else {
        std::cout << "ne peut pas trouver" << std::endl;
    }

    if (table.get("yyy")) {
        std::cout << "yyy: " << *table.get("yyy") << std::endl;
    } else {
        std::cout << "ne peut pas trouver" << std::endl;
    }

    table.put("zzz",3);

    if (table.get("zzz")) {
        std::cout << "zzz: " << *table.get("zzz") << std::endl;
    } else {
        std::cout << "ne peut pas trouver" << std::endl;
    }


    std::cout << "Taille de table: " << table.size() << std::endl;

    return 0;
}
