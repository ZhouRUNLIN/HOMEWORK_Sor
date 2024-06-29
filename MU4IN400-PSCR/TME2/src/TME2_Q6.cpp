#include <iostream>
#include <fstream>
#include <regex>
#include <chrono>
#include <algorithm>
#include "HashMap.h"

int main() {
    using namespace std;
    using namespace std::chrono;

    ifstream input("/home/weida/CLionProjects/TME2/WarAndPeace.txt");

    auto start = steady_clock::now();
    cout << "Parsing War and Peace" << endl;

    HashTable<string, int> wordCounts(10000);  // Assuming the initial size for your HashTable is 10000

    // A regex that identifies non-normal characters (i.e., non-letters)
    regex re(R"([^a-zA-Z])");
    string word;
    while (input >> word) {
        // Remove punctuation and special characters
        word = regex_replace(word, re, "");
        // Convert to lowercase
        transform(word.begin(), word.end(), word.begin(), ::tolower);

        const int* count = wordCounts.get(word);
        if (count) {
            wordCounts.put(word, *count + 1);
        } else {
            wordCounts.put(word, 1);
        }
    }
    input.close();

    cout << "Finished Parsing War and Peace" << endl;

    auto end = steady_clock::now();
    cout << "Parsing took "
         << duration_cast<milliseconds>(end - start).count()
         << "ms.\n";

    // Display the occurrences for "war", "peace", and "toto"
    for (const auto& searchWord : {"war", "peace", "toto"}) {
        const int* count = wordCounts.get(searchWord);
        if (count) {
            cout << "The word \"" << searchWord << "\" appeared " << *count << " times." << endl;
        } else {
            cout << "The word \"" << searchWord << "\" did not appear in the text." << endl;
        }
    }

    return 0;
}
