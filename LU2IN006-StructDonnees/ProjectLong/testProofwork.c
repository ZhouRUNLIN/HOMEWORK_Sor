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

int main(){
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
    
    Block* b = malloc(sizeof(Block));
    b -> author = blockKeyP;
    b -> previous_hash = (unsigned char*)strdup("fisrt_block");
    b -> nonce = 0;
    b -> hash = (unsigned char*)block_to_str(b);

        //Question 1.5
    //pour comparer les performances des deux methodes
    //on socker la vitesse des deux fonctions avec les meme parametres
    
    clock_t temps_init, temps_fin;
    double temps_cpu;
    FILE *fic = fopen("sortieTime.txt", "w");  //stocker les donnees dans le ficher
    if(fic == NULL){
        printf("Erreur de fichier\n");
        return 0;
    }
    int i = 0;
    while(i < 4){        
        temps_init = clock();
        compute_proof_of_work(b, i);
        temps_fin = clock();
        temps_cpu = ((double)(temps_fin - temps_init)/CLOCKS_PER_SEC);
        fprintf(fic, "%d %f\n", i, temps_cpu);
        if(temps_cpu >= 1)
            printf("%d\n", i);
        i++;
    }
    fclose(fic);

    delete_block(b);
    free(blockKeyS);
}