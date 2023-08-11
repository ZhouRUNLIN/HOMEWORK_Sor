//
// Created by runlin ZHOU 28717281
//

#ifndef PARTIE3
#define PARTIE3

//Exercice 5
typedef struct cellKey{
    Key* data;
    struct cellKey* next;
}CellKey;

//Question 5.1
CellKey* create_cell_key(Key* key);

//Question 5.2
void ajouter_en_tete_Cellkey(CellKey** cellkey, Key* key);

//Question 5.3
CellKey* read_public_keys(char* nom_fic);

//Question 5.4
void print_list_keys(CellKey* liste_keys);

//Question 5.5
void delete_cell_key(CellKey** c, Key* key);
void delete_list_keys(CellKey* ls);

typedef struct cellProtected{ 
    Protected* data;
    struct cellProtected* next;
} CellProtected;

//Question 5.6
CellProtected* create_cell_protected(Protected* pr);

//Question 5.7
void ajouter_en_tete_cellProtected(CellProtected** cellProtected, Protected* pr);

//Question 5.8
CellProtected* read_protected(char* nom_fic);

//Question 5.9
void print_list_protected(CellProtected* liste_protected);

//Question 5.10
void delete_cell_protected(CellProtected* c);
void delete_list_protected(CellProtected* ls);

//les operations avec is_equal()
//retourne 1 si les deux objects sont egales, 0 sinon
int is_equal_key(Key* key1, Key* key2);
int is_equal_signature(Signature* sgn1, Signature* sgn2);
int is_equal_protected(Protected* pr1, Protected* pr2);

#endif