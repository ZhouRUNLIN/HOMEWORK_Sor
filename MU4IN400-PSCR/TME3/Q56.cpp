#include <iostream>
#include <fstream>
#include <regex>
#include <chrono>
#include <vector>
#include <algorithm>
#include "HashMap.h"

int main() {
    using namespace std;
    using namespace std::chrono;

    ifstream input("/home/weida/CLionProjects/TME3/WarAndPeace.txt");

    auto start = steady_clock::now();
    cout << "Parsing War and Peace" << endl;

    HashTable<string, int> wordCounts(100000);

    regex re(R"([^a-zA-Z])");
    string word;
    while (input >> word) {
        word = regex_replace(word, re, "");
        transform(word.begin(), word.end(), word.begin(), ::tolower);

        const int* count = wordCounts.get(word);
        if (count) {
            wordCounts.put(word, *count + 1);
        } else {
            wordCounts.put(word, 1);
        }
    }
    input.close();

    // Q5
    vector<pair<string, int>> vec;
    for (auto it = wordCounts.begin(); it != wordCounts.end(); ++it) {
        vec.push_back({it->key, it->value});
    }

    // Q6
    sort(vec.begin(), vec.end(), [](const pair<string, int>& a, const pair<string, int>& b) {
        return a.second > b.second;
    });

    cout << "Finished Parsing War and Peace" << endl;

    auto end = steady_clock::now();
    cout << "Parsing took "
         << duration_cast<milliseconds>(end - start).count()
         << "ms.\n";

    cout << "Top 10 most frequent words:\n";
    for (int i = 0; i < 10 && i < vec.size(); i++) {
        cout << vec[i].first << ": " << vec[i].second << " times\n";
    }

    return 0;
}
