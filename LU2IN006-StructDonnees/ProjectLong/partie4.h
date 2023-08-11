//
// Created by runlin ZHOU 28717281
//

#ifndef PARTIE4
#define PARTIE4

//Partie 4
typedef struct hashcell{ 
    Key* key;
    int val; 
} HashCell;

typedef struct hashtable{ 
    HashCell** tab;
    int size; 
} HashTable;

//Exercice 6
//Question 6.1
void delete_liste_fraude(CellProtected* cellProtected);

//Question 6.2
HashCell* create_hashcell(Key* key);
void delete_hashcell(HashCell* hashcell);

//Question 6.3
int hash_function(Key* key, int size);

//Question 6.4
int find_position(HashTable* t, Key* key);

//Question 6.5
HashTable* create_hashtable(CellKey* keys, int size);

//Question 6.6
void delete_hashtable(HashTable* t);

void aff_hashtable(HashTable* t);

//Question 6.7
Key* compute_winner(CellProtected* decl, CellKey* candidates, CellKey* voter, int sizeV, int sizeC);

#endif