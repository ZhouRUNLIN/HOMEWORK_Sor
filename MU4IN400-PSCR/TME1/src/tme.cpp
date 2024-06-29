#include <iostream>
#include "String.h"
#include <iostream>

int main() {

    int tab[10] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    for (int i=9; i >= 0 ; i--) {
        if (tab[i] - tab[i-1] != 1) {
            std::cout << "probleme !" << std::endl;
        }
    }

        String s1("Hello");
        String s2("World");

        std::cout << "s1: " << s1 << "s2: " << s2 << std::endl;

        String s3 = s1;
        std::cout << "s3 (copié de s1): " << s3 << std::endl;

        s3 = s2;
        std::cout << "s3 (après l'affectation de s2): " << s3 << std::endl;

        if (s1 < s2) {
            std::cout << "s1 < s2" << std::endl;
        } else {
            std::cout << "s1 >= s2" << std::endl;
        }

        if (s1 == s2) {
            std::cout << "s1 = s2" << std::endl;
        } else {
            std::cout << "s1 != s2" << std::endl;
        }

        return 0;
}
