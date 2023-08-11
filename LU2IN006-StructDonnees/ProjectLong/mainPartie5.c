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
#include <dirent.h>

int main(){
    DIR *rep = opendir("./ Blockchain/");
    if (rep != NULL){
        struct dirent* dir;
        while ((dir = readdir(rep))) {
            if (strcmp(dir -> d_name,".") != 0 && strcmp(dir -> d_name, "..") != 0){
                printf("Chemin du fichier : ./ Blockchain/%s \n",dir -> d_name);
            }
        closedir(rep);
        }
    }



    //Generation d’un probleme de vote avec 1000 citoyens et 5 candidats
    int nv = 32, nc = 3;
    generate_random_data(nv, nc);

    //Lecture des declarations de vote, des candidats et des citoyens
    CellKey *voteur = read_public_keys("keys.txt");
    CellKey *candidate = read_public_keys("candidates.txt");
    CellProtected *decl = read_protected("declarations.txt");

    //la creation de premier block
    
    //    on fait un hypothese que la premier block est publie par un promoteur

    Block* b = malloc(sizeof(Block));
    
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
    
    Key* blockKeyP = malloc(sizeof(Key));
    Key* blockKeyS = malloc(sizeof(Key));
    init_key(blockKeyP, s, n);
    init_key(blockKeyP, u, n);

    char* str = key_to_str(blockKeyP);
    
    b -> author = blockKeyP;
    b -> previous_hash = (unsigned char*)strdup("fisrt_block");
    b -> nonce = 0;
    b -> hash = (unsigned char*)block_to_str(b);
   
    CellTree* ct = create_node(b);

    //Soumission de tous les votes                      
    CellProtected* declarations = decl;
    CellKey* vote = voteur;
    int d = 10;

    while(declarations){
        //avec la creation d’un bloc valide tous les 10 votes soumis
        for(int i = 0; i < d; i++){
            if(declarations == NULL){
                continue;
            }
            submit_vote(declarations -> data);
            declarations = declarations -> next;
        }
       
        create_block(ct, vote -> data, d);
       
        char* name = key_to_str(vote -> data);
        vote = vote -> next;

        sprintf(name, "%s.txt", name);
        //suivi directement par l’ajout du bloc dans la blockchain
        add_block(d, name);


        free(name);
    }
    print_tree(ct);

    delete_node(ct);
    free(blockKeyS);
    free(str);
    delete_list_keys(voteur);
    delete_list_keys(candidate);
    delete_list_protected(decl);

    CellTree* treeFinal = read_tree();
    Key* winnerKey = compute_winner_bt(treeFinal, voteur, candidate, nc, nv);

    printf("the winner is : (%ld, %ld)\n", winnerKey -> val, winnerKey -> n);

    delete_Celltree(treeFinal);
    free(winnerKey);

    return 0;
}