//
// Created by runlin ZHOU 28717281
//

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>
#include <assert.h>
#include "partie1.h"
#include "partie2.h"
#include "partie3.h"
#include "partie4.h"
#include "partie5.h"
#include <openssl/sha.h>

void print_long_vector(long *result, int size){
    printf("Vector : [ ");
    for(int i = 0; i < size; i++){
        printf("%ld ", result[i]);
    }
    printf("] \n");
}

int main(){
    printf("tests dans Partie 1 \n");
    //tests pour les fonctions dans Question 1.1
    assert(is_prime_naive(7) == 0);
    assert(is_prime_naive(20) == 1);
    assert(is_prime_naive(199) == 0);

    //tests pour les fonctions dans Question 1.3, 1.4
    assert(modpow_naive(2, 4, 5) == 1);
    assert(modpow(2, 4, 5) == 1);
    printf("------------------------------------------------\n");

    //tests pour Question 1.6
    for(int i = 3; i < 100; i++){
        int a = is_prime_miller(i, 10000); 
        if(a == 1){
            printf("%d : %d \n", i, a); 
        }
    }
    printf("------------------------------------------------\n");

    //tests pour Question 1.8
    long num = random_prime_number(3, 12, 1000);
    assert(num > pow(2, 3) && num < pow(2, 12));
    printf("la valeur de numbre random : %ld \n", num);
    printf("------------------------------------------------\n");

    //tests pour Question 2.1
    //creer deux primes aleatoires
    long p = random_prime_number(7, 8, 5000);
    long q;
    do{
        q = random_prime_number(7, 8, 5000);
    }while(p == q);     //pour assurer p et q distincts
    
    //la creation des cles
    long n, s, u;
    generate_key_values(p, q, &n, &s, &u);

    printf("cle publique = (%ld, %ld) \n", s, n);
    printf("cle privee = (%ld, %ld) \n", u, n);

    //initilisation 
    char message[1000] = "hello";
    int len = strlen(message);

    //chiffrement
    long* crypted = encrypt(message, s, n);
    printf("Message original : %s\n", message);
    printf("Encoded representation : \n");
    print_long_vector(crypted, len);

    //dechiffrement
    char* decoded = decrypt(crypted, len, u, n);
    printf("Decoded : %s \n", decoded);

    //free les tablaux
    free(crypted);
    free(decoded);

    //pour Partie 2
    printf("\n");
    printf("tests dans Partie 2 \n");
    printf("---------------------------------------------------------------------------------\n");
    
    //Testing Init Keys
    Key* pKey = malloc(sizeof(Key));
    Key* sKey = malloc(sizeof(Key)); 
    init_pair_keys(pKey, sKey,3,7);
    printf("pKey : %ld, %ld \n", pKey -> val, pKey -> n); 
    printf("sKey : %ld, %ld \n", sKey -> val, sKey -> n);
    
    //Testing Key Serialization
    char* chaine = key_to_str(pKey);
    printf("key to str : %s \n", chaine);
    Key* k = str_to_key(chaine);
    printf("str to key : (%ld, %ld) \n", k -> val, k -> n);
    
    //Testing signature
    //Candidate keys:
    Key* pKeyC = malloc(sizeof(Key));
    Key* sKeyC = malloc(sizeof(Key)); 
    init_pair_keys(pKeyC, sKeyC, 3, 7);

    //Declaration:
    char* messC = key_to_str(pKeyC);
    char* mess = key_to_str(pKey);
    printf("%s vote pour %s\n", mess, messC); 
   
    Signature* sgn = sign(messC, sKey);
    printf("signature : \n");
    print_long_vector(sgn -> tab, sgn -> maxTaille);
    char *c = signature_to_str(sgn);
    printf("signature to str : %s \n", chaine);
    Signature* strtos = str_to_signature(c);
    printf("str to signature : \n"); 
    print_long_vector(strtos -> tab, strtos -> maxTaille);

    //Testing protected:
    Protected* pr = init_protected(pKey, messC, sgn);
    
    //Verification:
    if (verify(pr)){ 
        printf("Signature valide\n");
    }
    else{
        printf("Signature non valide\n");
    }
   
    char *str_p = protected_to_str(pr);
    printf("protected to str :\n %s\n", str_p);

    //free les pointeurs
    free(pKey); 
    free(sKey); 
    free(chaine);
    free(k);
    free(pKeyC); 
    free(sKeyC);
    free(messC);
    free(mess);
    free_signature(sgn);
    free(c);
    free_signature(strtos);
    free_protected(pr);
    free(str_p);

    //Partie 3
    printf("tests dans partie 3\n");
    printf("---------------------------------------------------------------------------------\n");

    //tests pour Question 5.1 et Question 5.2
    Key* test = malloc(sizeof(Key));
    init_key(test, 5, 7);
    
    CellKey* list_cell = NULL;
    ajouter_en_tete_Cellkey(&list_cell, test);
    ajouter_en_tete_Cellkey(&list_cell, test);
    ajouter_en_tete_Cellkey(&list_cell, test);
    
    //tests pour Question 5.3
    print_list_keys(list_cell);

    //tests pour Question 5.4
    CellKey* ls_key = read_public_keys("keys.txt");
    CellKey* ls_candidate = read_public_keys("candidates.txt");
    //afficher les resultats
    print_list_keys(ls_key);
    print_list_keys(ls_candidate);

    //tests pour Question 5.5
    Key* test2 = malloc(sizeof(Key));
    init_key(test2, 6, 9);
    ajouter_en_tete_Cellkey(&list_cell, test2);
    ajouter_en_tete_Cellkey(&list_cell, test);  
    printf("avant supprimer : \n");
    print_list_keys(list_cell);
    printf("apres supprimer : \n");
    delete_cell_key(&list_cell, test2);
    print_list_keys(list_cell);
    
    //tests pour Question 5.6 et Question 5.7
    Signature* sgn1 = init_signature(NULL, 0);
    Protected* pr1 = init_protected(test, "test", sgn1);
    Protected* pr2 = init_protected(test2, "test2", sgn1);
    CellProtected* list_pro = NULL;

    ajouter_en_tete_cellProtected(&list_pro, pr1);
    ajouter_en_tete_cellProtected(&list_pro, pr1);
    ajouter_en_tete_cellProtected(&list_pro, pr1);
    
    //tests pour Question 5.9
    print_list_protected(list_pro);
    
    //tests pour Question 5.8
    CellProtected* ls_pro = read_protected("declarations.txt");
    //afficher les resultats
    print_list_protected(ls_pro);

    //tests pour Question 5.5
    ajouter_en_tete_cellProtected(&list_pro, pr2);
    ajouter_en_tete_cellProtected(&list_pro, pr1);  
    printf("avant supprimer : \n");
    print_list_protected(list_pro);
    printf("apres supprimer : \n");
    print_list_protected(list_pro);

    //free les pointeurs
    free(test);
    free(test2);
    delete_list_keys(list_cell);
    delete_list_keys(ls_key);
    delete_list_keys(ls_candidate);
    free_signature(sgn1);
    free_protected(pr1);
    free_protected(pr2);
    delete_list_protected(list_pro);
    delete_list_protected(ls_pro);

    //tests pour partie 4
    printf("---------------------------------------------------------------------------------\n");
    //les tests pour create_hashcell()
    Key* test_key = malloc(sizeof(Key));
    init_key(test_key, 1, 2);
    HashCell* hc = create_hashcell(test_key);
    printf("hc | val : %d, pkey : (%ld, %ld)\n", hc -> val, hc -> key -> val, hc ->key -> n);

    free(test_key);
    delete_hashcell(hc);

    //les tests pour create_hashtable()
    CellKey *voteur = read_public_keys("keys.txt");
    CellKey *candidate = read_public_keys("candidates.txt");
    CellProtected *decl = read_protected("declarations.txt");

    print_list_keys(candidate);

    HashTable* candidatesHashTable = create_hashtable(candidate, 3);
    HashTable* votersHashTable = create_hashtable(voteur, 32);
    aff_hashtable(candidatesHashTable);
    aff_hashtable(votersHashTable);

    delete_list_keys(voteur);
    delete_list_keys(candidate);
    delete_list_protected(decl);

    delete_hashtable(candidatesHashTable);
    delete_hashtable(votersHashTable);

    //les tests pour Partie 5
    printf("---------------------------------------------------------------------------------\n");
   
    //la creation de block pour test
    Block* testBlock = malloc(sizeof(Block));
    Key* blockKey = malloc(sizeof(Key));
    init_key(blockKey, 10, 9);
    CellProtected* blockVote = read_protected("declarations.txt");
    char* hash = "123456";
    char* previoushash = "1234";
    
    testBlock -> author = blockKey;
    testBlock -> votes = blockVote;
    testBlock -> hash = (unsigned char *)strdup(hash);
    testBlock -> previous_hash = (unsigned char *)strdup(previoushash);
    testBlock -> nonce = 0;
    
    //les tests pour writeBlock
    remove("Pending_block.txt");
    writeBlock(testBlock);
    //les tests pour block_to_str
    char* block_str = block_to_str(testBlock);
    printf("%s\n", block_str);

    //les tests pour creat_node
    CellTree* ct = create_node(testBlock);

    //les tests pour print_tree et add_child
    print_tree(ct);
    add_child(ct, create_node(testBlock));
    add_child(ct, create_node(testBlock));
    add_child(ct, create_node(testBlock));
    add_child(ct, create_node(testBlock));
    CellTree *ct_high = highest_child(ct);
    
    print_tree(ct);
    print_tree(ct_high);


    //tests pour encrypt_sha()
    char* s1 = "Rosetta code";
    
    char* s0 = encrypt_sha(s1);
    printf("%s\n", s0);

    //tests pour creation_block
    //CellTree* last = last_node(ct);
    //print_tree(last);

    //delete_CellTree(ct);
    delete_block(testBlock);

    free(block_str);

    Block *readB = readBlock("Pending_block.txt");
    //printf("%ld,%ld %s %s %d\n", readB -> author -> val, readB -> author -> n, readB -> hash, readB -> previous_hash, readB -> nonce);
    //print_list_protected(readB -> votes);
    char* strblock = block_to_str(readB);
    printf("%s\n", strblock);
    
    return 0;
}