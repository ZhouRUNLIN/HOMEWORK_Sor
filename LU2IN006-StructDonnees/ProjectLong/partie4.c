//
// Created by runlin ZHOU 28717281
//

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>
#include "partie1.h"
#include "partie2.h"
#include "partie3.h"
#include "partie4.h"

//Question 6
//Question 6.1
void delete_liste_fraude(CellProtected* cellProtected){
    while(cellProtected){
        CellProtected* next = cellProtected -> next;
        delete_cell_protected(cellProtected);
        cellProtected = next;
    }
}

//Question 6.2
HashCell* create_hashcell(Key* key){
    HashCell* hashcell = malloc(sizeof(HashCell));
    Key* k = malloc(sizeof(Key));
    init_key(k, key -> val, key -> n);
    
    hashcell -> key = k;
    hashcell -> val = 0;
    
    return hashcell;
}

void delete_hashcell(HashCell* hashcell){
    free(hashcell -> key);
    free(hashcell);
}

//Question 6.3
int hash_function(Key* key, int size){
    return modpow(key -> val, key -> n, size);
}

//Question 6.4
int find_position(HashTable* t, Key* key){
    int idx = hash_function(key, t ->size);
    while(! is_equal_key(key, t -> tab[idx] -> key)){
        idx = (idx + 1) % t -> size;
    }

    return idx;
}

//Question 6.5
HashTable* create_hashtable(CellKey* keys, int size){
    HashTable* newtable = malloc(sizeof(HashTable));

    newtable -> size = size;
    HashCell** tab = malloc(size * sizeof(HashCell));

    //init les donnes et assurer tous les valeurs sont NULL
    for(int i = 0; i < size; i++){
        tab[i] = NULL;
    }
    
    while(keys != NULL){
        int idx = hash_function(keys -> data, size);

        //on va trouver une position qui ne stocke pas des donnees
        while(tab[idx] != NULL){
            idx = (idx + 1) % size;
        }

        tab[idx % size] = create_hashcell(keys -> data);
        keys = keys -> next;
    }
    newtable -> tab = tab;

    return newtable;
}

//Question 6.6
void delete_hashtable(HashTable* t){
    if(!t) return;

    for(int i = 0; i < t -> size; i++){
        delete_hashcell(t -> tab[i]);
    }
    free(t -> tab);
    free(t);
}

//pour verifier hashtables
void aff_hashtable(HashTable* t){
    printf("hashtable :\n");
    for(int i = 0; i < t -> size; i++){
        printf("Cell %d : (%ld, %ld) -- %d\n", i, t -> tab[i] -> key -> val, t -> tab[i] -> key -> n, t -> tab[i] -> val);
    }
}

//Question 6.7
Key* htKiArgmax(HashTable* hashTable) {
    Key* argmax = malloc(sizeof(Key));
    int valmax = -1;

    for (int i = 0; i < hashTable -> size; ++i) {
        if (valmax < hashTable -> tab[i] -> val) {
            valmax = hashTable -> tab[i] -> val;
            init_key(argmax, hashTable -> tab[i] -> key -> val, hashTable -> tab[i] -> key -> n);
        }
    }

    return argmax;
}

Key* compute_winner(CellProtected* decl, CellKey* candidates, CellKey* voters, int sizeC, int sizeV){
    /*
     * On fait l'hypothèse que les signatures déclarations sont toutes valides.
     */
    HashTable* candidatesHashTable = create_hashtable(candidates, sizeC);
    HashTable* votersHashTable = create_hashtable(voters, sizeV);

    /*
        on definie un protocole que 
        si un voteur vote pour un candidate, la valeur de cette cell dans le hash table doit changer a 1
        si un candidate a recu un vote, la valeur de leur cells doit se augementer a 1
    */

    while(decl){
        //Confirmer si cet electeur a vote
        Key* votersKey = decl -> data -> pKey;
        int idx_vote = find_position(votersHashTable, votersKey);
        if(votersHashTable -> tab[idx_vote] -> val == 1){
            printf("cet eleteur a deja vote !\n");
        }
        else{
            Key* candidatesKey = str_to_key(decl -> data -> mess);
            int idx_can = find_position(candidatesHashTable, candidatesKey);

            //On vérifie que le candidat soit bien enregistré en tant que candidat.
            //Si c'est le cas, on augmente son score de 1.
            votersHashTable -> tab[idx_vote] -> val = 1;
            candidatesHashTable -> tab[idx_can] -> val += 1;

            free(candidatesKey);        
        }

        decl = decl -> next;
    }
    Key* winnerKey = htKiArgmax(candidatesHashTable);

    delete_hashtable(candidatesHashTable);
    delete_hashtable(votersHashTable);

    return winnerKey;
}
