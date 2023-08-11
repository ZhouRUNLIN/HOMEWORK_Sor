//
// Created by runlin ZHOU 28717281
//

#ifndef PARTIE5
#define PARTIE5

//Exercice 7
typedef struct block{
    Key* author;
    CellProtected* votes;
    unsigned char* hash;
    unsigned char* previous_hash;
    int nonce;
}Block;

typedef struct Block* ListeBlock;

//Question 7.1
void writeBlock(Block* b);

//Question 7.2
Block* readBlock(char *nom_fic);

//Question 7.3
char* block_to_str(Block* block);
Block* str_to_block(char* str);

//Question 7.5
char* encrypt_sha(char* s);

//Question 7.6
void compute_proof_of_work(Block* b, int d);

//Question 7.7
int verify_block(Block* b, int d);

//Question 7.9
void delete_block(Block* b);

//Exercice 8
typedef struct block_tree_cell{
    Block* block;
    struct block_tree_cell* father;
    struct block_tree_cell* firstChild;
    struct block_tree_cell* nextBro;
    int height;
}CellTree;

//Question 8.1
CellTree* create_node(Block* b);

//Question 8.2
int update_height(CellTree* father, CellTree* child);

//Question 8.3
void add_child(CellTree* father, CellTree* child);

//Question 8.4
void print_tree(CellTree* t);

//Question 8.5
void delete_node(CellTree* node);
void delete_CellTree(CellTree* t);

//Question 8.6
CellTree* highest_child(CellTree* cell);

//Question 8.7
CellTree* last_node(CellTree* t);

//Question 8.8
CellProtected* fusionner(CellProtected* cp1, CellProtected* cp2);
Key* compute_winner_BT(CellTree* tree, CellKey* candidates, CellKey* voters, int sizeC, int sizeV);
//Question 8.9
CellProtected* declarationLongest(CellTree* cellTree);

//Question 9.1
void submit_vote(Protected* p);

//Question 9.2
void create_block(CellTree* tree, Key* author, int d);
Key* compute_winner_bt(CellTree* treeFinal,CellKey* voteur,CellKey* candidate, int nc, int nv);

//Question 9.3
void add_block(int d, char* name);
void delete_Celltree(CellTree* t);

//Question 9.4
CellTree* read_tree();

//Question 9.5
Key* compute_winner_BT(CellTree* tree, CellKey* candidates, CellKey* voters, int sizeC, int sizeV);
#endif