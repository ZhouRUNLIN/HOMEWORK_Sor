#include <iostream>
#include <fstream>
#include <regex>
#include <chrono>
#include <vector>
#include <algorithm>

int main() {
    using namespace std;
    using namespace std::chrono;

    ifstream input("/home/weida/CLionProjects/TME2/WarAndPeace.txt");

    auto start = steady_clock::now();
    cout << "Parsing War and Peace" << endl;

    vector<pair<string,int>> wordCounts;

    // une regex qui reconnait les caractères anormaux (négation des lettres)
    regex re(R"([^a-zA-Z])");
    string word;
    while (input >> word) {
        // élimine la ponctuation et les caractères spéciaux
        word = regex_replace(word, re, "");
        // passe en lowercase
        transform(word.begin(), word.end(), word.begin(), ::tolower);



        auto it = find_if(wordCounts.begin(), wordCounts.end(),
                          [&word](const pair<string, int>& element)
             { return element.first == word; });

        if (it == wordCounts.end()) {
            wordCounts.push_back({word, 1});
        } else {
            it->second++;
        }


   }
    input.close();

    cout << "Finished Parsing War and Peace" << endl;

    for (const auto& searchWord : {"war", "peace", "toto"}) {
        auto it = find_if(wordCounts.begin(), wordCounts.end(),
                          [&searchWord](const pair<string, int>& element)
                          { return element.first == searchWord; });


            cout << "The word \"" << searchWord << "\" appeared " << it->second << " times." << endl;

    }

    return 0;
}
