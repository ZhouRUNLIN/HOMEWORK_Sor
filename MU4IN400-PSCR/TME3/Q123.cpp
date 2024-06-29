#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <string>
#include <regex>
#include <chrono>

template <typename Iterator>
size_t count (Iterator begin, Iterator end){
    size_t count = 0;
    while(begin != end){
        count++;
        begin++;
    }
    return count;
}

template <typename Iterator, typename T>
size_t count_if_equal (Iterator begin, Iterator end, const T & val){
    size_t count = 0;
    while(begin != end){
        if (*begin == val){
            count++;
        }
        begin++;
    }
    return count;
}


int main () {
    using namespace std;
    using namespace std::chrono;

    ifstream input = ifstream("/home/weida/CLionProjects/TME3/WarAndPeace.txt");

    auto start = steady_clock::now();
    cout << "Parsing War and Peace" << endl;

    vector<string> words;

    size_t nombre_lu = 0;
    // prochain mot lu
    string word;
    // une regex qui reconnait les caractères anormaux (négation des lettres)
    regex re( R"([^a-zA-Z])");
    while (input >> word) {
        // élimine la ponctuation et les caractères spéciaux
        word = regex_replace ( word, re, "");
        // passe en lowercase
        transform(word.begin(),word.end(),word.begin(),::tolower);

        words.push_back(word);
    }
    input.close();




    size_t wordCount = count(words.begin(),words.end());
    size_t wordCountIf = count_if_equal(words.begin(),words.end(),"war");

    cout << "Count:" << wordCount << endl;
    cout << "Size:" << words.size() << endl;

    if (wordCount == words.size()){
        cout << "Count = Size" << endl;
    } else{
        cout << "Count != Size" << endl;
    }

    cout << "nb de 'war' : " <<wordCountIf << endl;

    return 0;
}

