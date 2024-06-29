#include <iostream>
#include <fstream>
#include <regex>
#include <chrono>
#include <unordered_map>
#include <vector>
#include <algorithm>

int main() {
    using namespace std;
    using namespace std::chrono;

    ifstream input("/home/weida/CLionProjects/TME3/WarAndPeace.txt");

    auto start = steady_clock::now();
    cout << "Parsing War and Peace" << endl;

    unordered_map<string, int> wordCounts;

    regex re(R"([^a-zA-Z])");
    string word;
    while (input >> word) {
        word = regex_replace(word, re, "");
        transform(word.begin(), word.end(), word.begin(), ::tolower);

        wordCounts[word]++;
    }
    input.close();

    vector<pair<string, int>> vec(wordCounts.begin(), wordCounts.end());


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
