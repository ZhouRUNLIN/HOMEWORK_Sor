#include <iostream>
#include <fstream>
#include <regex>
#include <chrono>
#include <vector>
#include <algorithm>
#include "HashMap.h"  // Include your HashMap.h file

int main() {
    using namespace std;
    using namespace std::chrono;

    ifstream input("/home/weida/CLionProjects/TME2/WarAndPeace.txt");

    auto start = steady_clock::now();
    cout << "Parsing War and Peace" << endl;

    HashTable<string, int> wordCounts(10000);

    // A regex that identifies non-normal characters (i.e., non-letters)
    regex re(R"([^a-zA-Z])");
    string word;
    vector<string> distinctWords;  // Store distinct words for later reconstruction

    while (input >> word) {
        // Remove punctuation and special characters
        word = regex_replace(word, re, "");
        // Convert to lowercase
        transform(word.begin(), word.end(), word.begin(), ::tolower);

        const int* count = wordCounts.get(word);
        if (!count) {  // If word is not present in the hash table, add it to the distinctWords list
            distinctWords.push_back(word);
        }

        wordCounts.put(word, count ? (*count + 1) : 1);
    }
    input.close();

    // Initialize vector using the distinctWords list and the hash table
    vector<pair<string, int>> vec;
    for (const string& distinctWord : distinctWords) {
        const int* count = wordCounts.get(distinctWord);
        if (count) {
            vec.push_back({distinctWord, *count});
        }
    }

    // Sort the vector by word count in descending order
    sort(vec.begin(), vec.end(), [](const pair<string, int>& a, const pair<string, int>& b) {
        return a.second > b.second;
    });

    cout << "Finished Parsing War and Peace" << endl;

    auto end = steady_clock::now();
    cout << "Parsing took "
         << duration_cast<milliseconds>(end - start).count()
         << "ms.\n";

    // Display the top 10 most frequent words
    cout << "Top 10 most frequent words:\n";
    for (int i = 0; i < 10 && i < vec.size(); i++) {
        cout << vec[i].first << ": " << vec[i].second << " times\n";
    }

    return 0;
}
