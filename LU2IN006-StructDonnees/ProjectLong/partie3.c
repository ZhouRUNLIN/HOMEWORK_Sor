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

//Question 5.1
CellKey* create_cell_key(Key* key){
    CellKey* cellkey = malloc(sizeof(CellKey));
    
    Key* k = malloc(sizeof(Key));
    init_key(k, key -> val, key -> n);
    cellkey -> data = k;
    cellkey -> next = NULL;
    
    return cellkey;
}

//Question 5.2
void ajouter_en_tete_Cellkey(CellKey** cellkey, Key* key){
    CellKey* cell = create_cell_key(key);
	if(*cellkey == NULL){
        *cellkey = cell;
	}
	else{
		cell -> next = *cellkey;
		*cellkey = cell;
	}
}

//Question 5.3
CellKey* read_public_keys(char* nom_fic){
    CellKey* list_cell = NULL;
    if( ( strcmp(nom_fic, "keys.txt" ) != 0 ) && ( strcmp(nom_fic, "candidates.txt") != 0 ) ){
        printf("Err : wrong document name !");
    }
    else{
        if(strcmp(nom_fic, "keys.txt") == 0){
            FILE *fic = fopen(nom_fic, "r");
            if(fic == NULL){
                printf("Erreur de fichier\n");
            } 

            while(! feof(fic)){
                char keyp[256], keys[256];
                fscanf(fic, "%s %s\n", keyp, keys);
                Key* k = str_to_key(keyp);
                ajouter_en_tete_Cellkey(&list_cell, k);
                free(k);
            }
            fclose(fic);
        }

        if(strcmp(nom_fic, "candidates.txt") == 0){
            FILE *fic = fopen(nom_fic, "r");
            if(fic == NULL){
                printf("Erreur de fichier\n");
            } 

            while(! feof(fic)){
                char keyp[256];
                fscanf(fic, "%s\n", keyp);
                Key* k = str_to_key(keyp);
                ajouter_en_tete_Cellkey(&list_cell, k);
                free(k);
            }
            fclose(fic);    
        }
    }    
    return list_cell;
}

//Question 5.4
void print_list_keys(CellKey* liste_keys){
    int i = 0;
    while(liste_keys != NULL){
        i++;
        char *str = key_to_str(liste_keys -> data);
        printf("key %d : %s\n", i, str);
        free(str);
        liste_keys = liste_keys -> next;
    }
}

//Question 5.5
int is_equal_key(Key* key1, Key* key2){
    if(key1 -> val == key2 -> val && key1 -> n == key2 -> n){
        return 1;
    }
    else
        return 0;
}

void delete_cell_key(CellKey** c, Key* key){
    CellKey* ls = *c;
    CellKey* l;
    // si l'element qu'on espere rechercher est la premiere element dans la liste
    if(is_equal_key(ls -> data, key) == 1){
        *c = ls -> next;
        free(ls -> data);
        free(ls);
    }

    //sinon
    while(ls && is_equal_key(ls -> data, key) == 0){
        l = ls;
        ls = ls -> next;
    }
    if(is_equal_key(ls -> data, key) == 1){
        l -> next = ls -> next;
        free(ls -> data);
        free(ls);
    }
    else        printf("Attention, on ne trouve pas cet element !\n");
}

void delete_list_keys(CellKey* ls){
    CellKey *ptr;
    while(ls){
        free(ls -> data);
        ptr = ls -> next;
        free(ls);
        ls = ptr;
    }
}

//Question 5.6
CellProtected* create_cell_protected(Protected* pr){
    CellProtected* cellProtected = malloc(sizeof(CellProtected));
    
    Protected* p = init_protected(pr -> pKey, pr -> mess, pr -> s);
    cellProtected -> data = p;
    cellProtected -> next = NULL;
    
    return cellProtected;   
}

//Question 5.7
void ajouter_en_tete_cellProtected(CellProtected** cellProtected, Protected* pr){
    CellProtected* cell = create_cell_protected(pr);
	
    if(*cellProtected == NULL){
        *cellProtected = cell;
	}
	else{
		cell -> next = *cellProtected;
		*cellProtected = cell;
	}

}

//Question 5.8
CellProtected* read_protected(char* nom_fic){
    CellProtected* list_cell = NULL;

    FILE* fic = fopen(nom_fic, "r");
    if(fic == NULL){
        printf("Erreur de fichier\n");
    } 

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

    fclose(fic);

    return list_cell;
}

//Question 5.9
void print_list_protected(CellProtected* liste_protected){
    int i = 0;
    while(liste_protected != NULL){
        i++;
        char *str = protected_to_str(liste_protected -> data);
        printf("Protected %d : %s", i, str);      
        
        free(str);
        liste_protected = liste_protected -> next;
    }
}

//Question 5.10
int is_equal_signature(Signature* sgn1, Signature* sgn2){
    if(sgn1 -> maxTaille == sgn2 -> maxTaille){
        for(int i = 0; i < sgn1 -> maxTaille; i++){
            if(sgn1 -> tab[i] != sgn2 -> tab[i])
                return 0;
        }
        return 1;
    }
    else
        return 0;
}

int is_equal_protected(Protected* pr1, Protected* pr2){
    if(is_equal_key(pr1 -> pKey, pr2 -> pKey) 
        && strcmp(pr1 -> mess, pr2 -> mess) == 0 
        && is_equal_signature(pr1 ->s, pr2 -> s)){
        return 1;
    }
    else
        return 0;
}

void delete_cell_protected(CellProtected* c){
    free_protected(c -> data);
    free(c);
}

void delete_list_protected(CellProtected* ls){
    while(ls){
        CellProtected* next = ls -> next;
        delete_cell_protected(ls);
        ls = next;
    }
}