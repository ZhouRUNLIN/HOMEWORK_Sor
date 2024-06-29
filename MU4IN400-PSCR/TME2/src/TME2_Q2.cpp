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

    vector<string> uniqueWords;
    size_t nombre_lu = 0;

    // une regex qui reconnait les caractères anormaux (négation des lettres)
    regex re(R"([^a-zA-Z])");
    string word;
    while (input >> word) {
        // élimine la ponctuation et les caractères spéciaux
        word = regex_replace(word, re, "");
        // passe en lowercase
        transform(word.begin(), word.end(), word.begin(), ::tolower);

        // word est maintenant "tout propre"

        if (find(uniqueWords.begin(), uniqueWords.end(), word) == uniqueWords.end()) { // pas dans uniqueWords
            uniqueWords.push_back(word);
        }

        nombre_lu++;
    }
    input.close();

    cout << "Finished Parsing War and Peace" << endl;

    auto end = steady_clock::now();
    cout << "Parsing took "
         << duration_cast<milliseconds>(end - start).count()
         << "ms.\n";

    cout << "Found a total of " << nombre_lu << " words." << endl;
    cout << "Found a total of " << uniqueWords.size() << " unique words." << endl;

    return 0;
}
