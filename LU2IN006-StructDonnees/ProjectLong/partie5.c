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
#include "partie5.h"
#include <openssl/sha.h>

//Question 7.1
void writeBlock(Block* b){
    //Ouvrir le fichier en tant qu'annexe, ou le créer s'il n'existe pas.
    FILE* fic = fopen("Pending_block.txt", "a+");
    
    if(fic == NULL){
        printf("Erreur de fichier\n");
    }
    char* key = key_to_str(b -> author);

    fprintf(fic, "%s\n%s\n%s\n%d ", key, b -> hash, b -> previous_hash, b -> nonce);

    CellProtected* current = b -> votes;

    while(current){
        char* keyP = key_to_str(current -> data -> pKey);
        char* sgn = signature_to_str(current -> data -> s);

        fprintf(fic, "\n%s %s %s", keyP, current -> data -> mess, sgn);
        current = current->next;
        
        free(keyP);
        free(sgn);
    }
    fprintf(fic, "\n");
    free(key);

    fclose(fic); 
}

//Question 7.2
Block* readBlock(char* nomFic){
    FILE* fic = fopen(nomFic, "r");
    
    if(fic == NULL){
        printf("Erreur de fichier\n");
    }

    char* strkey = malloc(sizeof(char) * 256);
    char* strhash = malloc(sizeof(char) * 256);
    char* strpreivoushash = malloc(sizeof(char) * 256);
    int nonce;
    
    fscanf(fic, "%s\n%s\n%s\n%d", strkey, strhash, strpreivoushash, &nonce);
    
    CellProtected* list_cell;
    while(! feof(fic)){
        char str_pKey[256], mess[256], str_sgn[256];
            
        fscanf(fic, "%s %s %s\n", str_pKey, mess, str_sgn);
        Key* pKey = str_to_key(str_pKey);
        Signature* sgn = str_to_signature(str_sgn);
        Protected* pr = init_protected(pKey, mess, sgn);
        ajouter_en_tete_cellProtected(&list_cell, pr);

        free(pKey);
        free_signature(sgn);
        free_protected(pr);
    }

    Key* key = str_to_key(strkey);
    Block *b = malloc(sizeof(Block));
    b -> author = key;
    b -> votes = list_cell;
    b -> hash = (unsigned char *)strdup(strhash);
    b -> previous_hash = (unsigned char *)strdup(strpreivoushash);
    b -> nonce = nonce;

    free(strkey);
    free(strhash);
    free(strpreivoushash);

    fclose(fic); 

    return b;
}

//Question 7.3
char* block_to_str(Block* block){
    char* repr = malloc(16384 * sizeof(char));

    char* authorKeyRepr = key_to_str(block -> author);
    sprintf(repr, "%s\n%s\n%d", authorKeyRepr, block->previous_hash, block -> nonce);
    free(authorKeyRepr);

    CellProtected* current = block->votes;

    while(current){
        char* key = key_to_str(current -> data -> pKey);
        char* sgn = signature_to_str(current -> data -> s);

        sprintf(repr, "%s\n%s %s %s", repr, key, current -> data -> mess, sgn);
        current = current->next;
        
        free(key);
        free(sgn);
    }


    repr = realloc(repr, (strlen(repr) + 1) * sizeof(char));

    return repr;
}

Block* str_to_block(char* strBlock){
    Key* authorKey;
    unsigned char* previousHash;
    int nonce;
    CellProtected* votes;

    char buffer[256];
    size_t len = strlen(strBlock);
    int bufferIdx = 0;
    int bufferNo = 0;
    for (size_t i = 0; i < len; ++i) {
        if (strBlock[i] == '/') {
            buffer[bufferIdx] = '\0';
            bufferIdx = 0;
            ++bufferNo;

            if (bufferNo == 1) {
                authorKey = str_to_key(buffer);
            }
            else if (bufferNo == 2) {
                previousHash = (unsigned char*) strdup(buffer);
            }
            if (bufferNo == 3) {
                nonce = atoi(buffer);
            }

        } else {
            buffer[bufferIdx++] = strBlock[i];
        }
    }

    Block* block = malloc(sizeof(Block));
    if (!block) {
        fprintf(stderr, "[strToBlock] Erreur lors de l'allocation :(");
        free(authorKey);
        free(previousHash);

        return NULL;
    }

    block -> author = authorKey;
    block -> votes = votes;
    block -> nonce = nonce;
    block -> previous_hash = previousHash;

    free(authorKey);
    free(previousHash);

    return block;
}

//Question 7.5
char* encrypt_sha(char* s){
    unsigned char* s1 = (unsigned char*)SHA256((const unsigned char*)s, strlen(s), 0);
    char* s0 = malloc(100000 * sizeof(char));
    
    for (int i = 0; i < SHA256_DIGEST_LENGTH; i++)
        sprintf(s0,"%s%02x", s0,s1[i]);

    return s0;
}

//Question 7.6
void compute_proof_of_work(Block* B, int d){
    int valide = 0;

    while(valide == 0){
        valide = 1;
        char* res = block_to_str(B);
        unsigned char* hash = SHA256((const unsigned char*)res, strlen(res), 0);

        for(int i = 0; i < d; i++){
            if(hash[i] != 0){
                valide = 0;
                continue;
            }
        }
        B -> nonce = B -> nonce + 1;
        //printf("%d\n", B -> nonce);
        free(res);
    }

    B -> nonce = B -> nonce - 1;
}

//Question 7.7
int verify_block(Block* B, int d){
/*
    for(int i = 0; i < 4*d; i++){
        if(hash[i] != 0){
            return 0;
        }
    }

    free(res);
*/

    return 1;
}


//Question 7.9
void delete_block(Block* b){
    free(b -> author);
    free(b -> hash);
    free(b -> previous_hash);
    free(b);
}

//Exercice 8
//Question 8.1
CellTree* create_node(Block* b){
    CellTree* cellTree = malloc(sizeof(CellTree));

    cellTree -> block = b;
    cellTree -> father = NULL;
    cellTree -> firstChild = NULL;
    cellTree -> nextBro = NULL;
    cellTree -> height = 0;
    
    return cellTree;
}

//Question 8.2
int update_height(CellTree* father, CellTree* child){
    int height = (child -> height) + 1;
    if(father -> height < height){
        father -> height = height;
        return 1;
    }
    else    
        return 0;
}

//Question 8.3
void add_child(CellTree* father, CellTree* child){
    child -> father = father;
    child -> nextBro = father -> firstChild;
    father -> firstChild = child;
    update_height(father, child);
}

//Question 8.4
void print_tree(CellTree* t){
    printf("hauteur : %d, valeur hachee : %s\n", t -> height, t -> block -> hash);

    if(t -> firstChild){
        print_tree(t -> firstChild);
    }
    if(t -> nextBro){
        print_tree(t -> nextBro);
    }
}

//Question 8.5
void delete_node(CellTree* node){
    delete_block(node -> block);
    free(node);
}

void delete_CellTree(CellTree* t){
    if(t -> nextBro){
        delete_CellTree(t -> nextBro);
    }
    if(t -> firstChild){
        delete_CellTree(t -> firstChild);
    }
    delete_node(t);
}

//Question 8.6
CellTree* highest_child(CellTree* cell){
    int max = -1;
    CellTree* high;

    if(cell -> firstChild){
        CellTree* t = cell -> firstChild;
        CellTree* bro = t;
        while(bro -> nextBro){
            if(bro -> height > max){
                max = bro -> height;
                high = bro;
            }
            bro = bro -> nextBro;
        }
    }
    if(max == 0)
        return cell -> firstChild;
    else
        return high;
}

//Question 8.7
CellTree* lastNode(CellTree* cellTree){
    CellTree* res = cellTree;
    while(res->firstChild){
        res = highest_child(res);
    }
    return res;
}

//Question 8.8
CellProtected* fusionner(CellProtected* cp1, CellProtected* cp2){
    CellProtected *res = create_cell_protected(cp1->data);
    cp1 = cp1->next;

    while(cp1){
        ajouter_en_tete_cellProtected(&res, cp1->data);
        cp1 = cp1->next;
    }

    while (cp2) {
        ajouter_en_tete_cellProtected(&res, cp2 -> data);
        cp2 = cp2->next;
    }
    return cp1;
}

//Question 8.9
CellProtected* declarationLongest(CellTree* cellTree){
    CellProtected *res = cellTree->block->votes;
    cellTree = highest_child(cellTree);
    while (cellTree){
        res = fusionner(res, cellTree->block->votes);
        cellTree = highest_child(cellTree);
    }
    return res;
}

void delete_Celltree(CellTree* t){
    
}

//Question 9.1
void submit_vote(Protected* p){
    FILE* fic = fopen("Pending_votes.txt", "a+");

    if(fic == NULL){
        printf("Erreur de fichier\n");
    }
    char* key = key_to_str(p -> pKey);
    char* sgn = signature_to_str(p -> s);
    fprintf(fic, "%s %s %s\n", key, p -> mess, sgn);
    
    free(key);
    free(sgn);

    fclose(fic); 
}

CellTree* read_tree(){
    return NULL;
}
Key* compute_winner_bt(CellTree* treeFinal,CellKey* voteur,CellKey* candidate, int nc, int nv){
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
     
    Key* key = malloc(sizeof(Key));
    init_key(key, s, n);

    return key;
}

//Question 9.2
void create_block(CellTree* tree, Key* author, int d){
    Block* b = malloc(sizeof(Block));
    //remplir le author
    Key* a = malloc(sizeof(Key));
    init_key(a, author -> val, author -> n);   
    b -> author = a;

    //remplir le cellProtetced
    CellProtected* cellProtected = read_protected("Pending_votes.txt");
    b -> votes = cellProtected;

    //delete le fic
    remove("Pending_votes.txt");

    //remplir previous hash
    b -> previous_hash = (unsigned char *)strdup((const char *)tree -> block -> hash);

    //remplir le nonce
    b -> nonce = 0;
    //compute_proof_of_work(b, d);

    //remplir hash
    char* str = block_to_str(b);
    char* hash = encrypt_sha(str);
    b -> hash = (unsigned char *)strdup(hash);

    free(str);
    free(hash);

    //write fich.
    writeBlock(b);

    delete_list_protected(cellProtected);
    delete_block(b);
}

//Question 9.3
void add_block(int d, char* name){

    //verify
    Block* b = readBlock("Pending_block.txt");

    if(verify_block(b, d)){
        FILE* f = fopen(name, "w");

        char* key = key_to_str(b -> author);

        fprintf(f, "%s\n", key);
        fprintf(f, "%s\n", b -> hash);
        fprintf(f, "%s\n", b -> previous_hash);
        fprintf(f, "%d\n", b -> nonce);
        
        CellProtected* current = b -> votes;

        while(current != NULL){
            char* pr = protected_to_str(current -> data);

            fprintf(f, "%s", pr);
            current = current -> next;
            
            free(pr);
        }

        free(key);
        fclose(f);
    }


    delete_list_protected(b -> votes);
    delete_block(b);

    remove("Pending_block.txt");
}

//Question 9.5
Key* compute_winner_BT(CellTree* tree, CellKey* candidates, CellKey* voters, int sizeC, int sizeV){
    CellProtected *decl;
    CellTree* t;
    while(t != NULL){
        CellProtected* current = tree -> block -> votes;
        while(current){
            ajouter_en_tete_cellProtected(&decl, current -> data);
            current = current -> next;
        }
        t = t -> firstChild; 
    }
    return compute_winner(decl, candidates, voters, sizeC, sizeV);
}