#include <iostream>
#include <vector>
#include <unordered_map>
#include <forward_list>

class Person {
public:
    std::string prenom;
    std::string nom;
    int age;

    Person(std::string prenom, std::string nom, int a)
            : prenom(prenom), nom(nom), age(a){}

};


void printPerson(const Person& person) {
    std::cout << person.prenom << " " << person.nom
              << ", Age: " << person.age << std::endl;
}

int main() {
    std::vector<Person> people = {
            Person("AAA", "aaa", 20),
            Person("BBB", "bbb", 25),
            Person("CCC", "ccc", 30),
            Person("DDD", "ddd", 35),

            Person("AAA", "TEST", 20),

    };

    // Regroupés par âge
    std::unordered_map<int, std::forward_list<Person>> ageGroups;

    for (const auto& person : people) {
        ageGroups[person.age].push_front(person);
    }

    std::cout << "----------------Regroupés par âge----------------" << std::endl;

    for (const auto& group : ageGroups) {
        std::cout << "Age: " << group.first << std::endl;
        for (const auto& person : group.second) {
            printPerson(person);
        }
        std::cout << "------" << std::endl;
    }

    // Regroupés par prénoms
    std::unordered_map<std::string, std::forward_list<Person>> nomGroups;

    for (const auto& person : people) {
        nomGroups[person.prenom].push_front(person);
    }

    std::cout << "----------------Regroupés par prénoms----------------" << std::endl;

    for (const auto& group : nomGroups) {
        std::cout << "Name: " << group.first << std::endl;
        for (const auto& person : group.second) {
            printPerson(person);
        }
        std::cout << "------" << std::endl;
    }

    return 0;
}
