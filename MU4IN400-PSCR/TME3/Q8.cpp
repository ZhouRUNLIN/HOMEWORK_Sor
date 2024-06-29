#include <iostream>
#include <fstream>
#include <regex>
#include <unordered_map>
#include <forward_list>

int main() {
    using namespace std;

    ifstream input("/home/weida/CLionProjects/TME3/WarAndPeace.txt");

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

    unordered_map<int, forward_list<string>> frequencyMap;

    for (const auto& kv : wordCounts) {
        frequencyMap[kv.second].push_front(kv.first);
    }


    int N = 7327;
    if (frequencyMap.find(N) != frequencyMap.end()) {
        cout << "Words that appear " << N << " times:\n";
        for (const auto& word : frequencyMap[N]) {
            cout << word << endl;
        }
    } else {
        cout << "No words appear " << N << " times.\n";
    }

    return 0;
}
