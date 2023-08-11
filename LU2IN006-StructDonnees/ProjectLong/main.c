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

void print_long_vector(long *result, int size){
    printf("Vector : [ ");
    for(int i = 0; i < size; i++){
        printf("%ld ", result[i]);
    }
    printf("] \n");
}

int main(){
    //Question 1.2
    //Pour mesurer le temps de calcul de la fonction, nous utilisons la fonction double difftime(time_t time2, time_t time1);
    long t = 3;
    //En utilisant des cycles continus pour que la valeur de t augmente progressivement
    //jusqu'à ce que la différence entre "debut" et "fin" soit supérieure ou égale à 2
    while(t){   
        time_t debut, fin;
        time(&debut);
        is_prime_naive(t); 
        t=2*t + 1;           //je fait t*=3 pour reduire le temps que j'attends
        time(&fin);
        if(fin - debut >= 2){
            printf("Le plus grand nombre premier que vous arrivez a tester en moins de 2s avec is_prime_naive est %ld \n", t);
            break;
        }
    }

    //Question 1.5
    //pour comparer les performances des deux methodes
    //on socker la vitesse des deux fonctions avec les meme parametres
    
    clock_t temps_init, temps_fin;
    double temps_cpu;
    FILE *fic = fopen("sortie_vitesse.txt", "w");  //stocker les donnees dans le ficher
    if(fic == NULL){
        printf("Erreur de fichier\n");
        return 0;
    }
    int i = 1;
    while(i < 1000){          //on tester 1000 fois
        temps_init = clock();
        modpow_naive(2, i, 2);
        temps_fin = clock();
        temps_cpu = ((double)(temps_fin - temps_init)/CLOCKS_PER_SEC);
        fprintf(fic, "%d %f ", i, temps_cpu);
        
        temps_init = clock();
        modpow(2, i, 2);
        temps_fin = clock();
        temps_cpu = ((double)(temps_fin - temps_init)/CLOCKS_PER_SEC);
        fprintf(fic, "%f \n", temps_cpu);

        i+=5;
    }
    fclose(fic);

    printf("\n");
    //Question 1.6
    //Pour calculer la probabilité d'erreur de Miller-Rabin
    //Nous avons vérifié les résultats par rapport à is_prime_naive 
    //et enregistré le nombre de fois où les résultats différaient
    int fois_err = 0, fois_total = 1000;
    for(int j = 1; j < 10000; j*=10){
        //Nous examinerons également la variation la probilité d'erreur à différents niveaux de précision.
        for(int i = 0; i < fois_total; i++){    //changer la valeur de fois_total pour preciser la probabilite
            if(is_prime_miller(i, j) != is_prime_naive(i))
                fois_err++;
        }
        printf("k = %d, probabilite d'error = %f \n", j, 1 - (double)fois_err / fois_total);
        fois_err = 0;
    }

    //Exercice 2
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
    
    //Question 4.1
    int nv = 32, nc = 3; 
    generate_random_data(nv, nc);

    //la creation des listes chainees
    CellKey *voteur = read_public_keys("keys.txt");
    CellKey *candidate = read_public_keys("candidates.txt");
    CellProtected *decl =read_protected("declarations.txt");

    print_list_keys(candidate);
    print_list_keys(voteur);
    print_list_protected(decl);
    
    //afficher le(s) winner(s)
    Key* winnerKey = compute_winner(decl, candidate, voteur, nc, nv);
    printf("the winner's key is (%ld, %ld)\n", winnerKey -> val, winnerKey -> n);

    //free(str);
    free(winnerKey);
    delete_list_keys(voteur);
    delete_list_keys(candidate);
    delete_list_protected(decl);
    
    return 0;  
}
