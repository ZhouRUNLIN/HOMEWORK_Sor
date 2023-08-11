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

//Question 3.2
void init_key(Key* key, long val, long n){
    key -> val = val;
    key -> n = n;
}

//Question 3.3
void init_pair_keys(Key* pKey, Key* sKey, long low_size, long up_size){
    //initialiser les variables 
    long s, u, n;
    long p, q;

    //la creation du deux prime random
    p = random_prime_number(low_size, up_size, 5000);
    do{
        q = random_prime_number(low_size, up_size, 5000);
    }while(p == q);     // pour assurer les deux entiers sont distincts

    generate_key_values(p, q, &n, &s, &u);
    init_key(sKey, u, n);
    init_key(pKey, s, n);
}

//Question 3.4
char* key_to_str(Key* key){
    char* s = malloc(256 * sizeof(char));
    sprintf(s, "(%ld,%ld)", key -> val, key -> n);

    return s;
}

Key* str_to_key(char* str){
    long val, n;
    sscanf(str, "(%ld,%ld)", &val, &n);
    
    Key* key = malloc(sizeof(Key));
    init_key(key, val, n);

    return key;
}

//Question 3.6
Signature* init_signature(long* content, int size){
    Signature* signature = malloc(sizeof(Signature));

    signature -> maxTaille = size;
    signature -> tab = malloc(size * sizeof(long));
    for(int i = 0; i < size; i++){
        signature -> tab[i] = content[i];
    }

    return signature;
}

void free_signature(Signature* sgn){
    free(sgn -> tab);
    free(sgn);
}

//Question 3.7
Signature* sign(char* mess, Key* sKey){
    long *ls = encrypt(mess, sKey -> val, sKey -> n);
    
    Signature *s = init_signature(ls, strlen(mess));
    
    free(ls);
    
    return s;
}

//Quesstion 3.8
char* signature_to_str(Signature* sgn){
    //init les varibales
    char* result = malloc(10 * sgn -> maxTaille * sizeof(char)); 
    result[0] = '#';
    int pos = 1;
    char buffer[156];

    //les boucles du crops
    for(int i = 0; i < sgn -> maxTaille; i++){ 
        sprintf(buffer, "%ld", sgn -> tab[i]); 
        for(int j = 0; j < strlen(buffer); j++){
            result[pos] = buffer[j];
            pos = pos + 1; 
        }
        result[pos] = '#';
        pos = pos + 1; 
    }

    //les chaines caracteres doivent se terminer avec '\0'
    result[pos] = '\0';
    result = realloc(result, (pos + 1) * sizeof(char)); 
    
    return result;
}

Signature* str_to_signature(char* str){
    int len = strlen(str);
    long* content = (long *)malloc(sizeof(long) * len); 
    int num = 0;
    char buffer[256];
    int pos = 0;
    
    for (int i = 0; i < len; i++){
        if (str[i] != '#'){ 
            buffer[pos] = str[i]; 
            pos = pos + 1;
        }
        else{
            if (pos != 0){
                buffer[pos] = '\0';
                sscanf(buffer, "%ld", &(content[num])); 
                num = num + 1;
                pos = 0;
            } 
        }
    } 

    content = realloc(content, num*sizeof(long)); 
    Signature* s = init_signature(content, num);
    
    free(content);

    return s;
}

//Question 3.10
Protected* init_protected(Key* pKey, char* mess, Signature* sgn){
    //alouer les espaces memoire pour les valables dans Protected
    Protected* protected = malloc(sizeof(Protected));
    Key* pK = malloc(sizeof(Key));
    init_key(pK, pKey -> val, pKey -> n);
    
    //affectation aux variables 
    Signature* s = init_signature(sgn -> tab, sgn -> maxTaille);
    protected -> pKey = pK;
    protected -> mess = strdup(mess);
    protected -> s = s;

    return protected;
}

void free_protected(Protected* p){
    free(p -> pKey);
    free(p -> mess);    //a cause de l'utilisation de la fonction strdup()
    free_signature(p -> s);
    free(p);
}

//Question 3.11
int verify(Protected* pr){
    //decrypter la decalration dans la signature associe
    char* decrpt_mess = decrypt(pr -> s -> tab, pr -> s -> maxTaille, pr -> pKey -> val, pr -> pKey -> n);
    
    int val = strcmp(pr -> mess, decrpt_mess);
    free(decrpt_mess);

    //Le vote est valide ssi 
    //le message après décrypter est strictement egale a mess 
    if(val == 0)      
        return 1;       
    else
        return 0;
}

//Question 3.12
char* protected_to_str(Protected* p){
    char *p_str = malloc(256 * sizeof(char));
    char *str_key = key_to_str(p -> pKey);
    char *str_sgn = signature_to_str(p -> s);

    sprintf(p_str, "%s, %s, %s\n", str_key, p -> mess, str_sgn);

    free(str_key);
    free(str_sgn);

    return p_str;
}

Protected* str_to_protected(char* mess){
    //declarer les variables
    char pKey[256], m[256], s[256];
    //lire les donnees sur le chaine de caractere
    sscanf(mess, "%s, %s, %s\n", pKey, m, s);

    Key *keyPublique = str_to_key(pKey);
    Signature *sgn = str_to_signature(s);
    Protected *p = init_protected(keyPublique, m, sgn);

    return p;
}

//Question 4.1
//creer une liste qui contient les candidates qui sont choisir aleatoirement
//num est le nombre de candidates : nc
//upsize est le numero maxiume : cad que le numbre de voter  (nv)
int *random_int_list(int num, int upsize){
    int *list = malloc(num * sizeof(int));
    srand((unsigned int)time(0));
    
    for(int i = 0; i < num; i++){
        list[i] = rand() % (upsize + 1);
        while(i > 0){
            int prev = list[i];
            //pour assurer chauque nombre dans la liste est diferrent
            if(prev != list[i - 1])     break;
            else        list[i] = rand() % (upsize + 1);
        }
    }

    return list;
}

//la fonction retourne 1 si le "num" est dans "ls", 0 sinon
int is_in_liste(int *ls, int num, int size){
    for(int i = 0; i < size; i++){
        if(ls[i] == num)        return 1;
    }
    return 0;
}

void generate_random_data(int nv, int nc){
    //on creer une listes de numero de candidates en utilisent la variable nc
    int *ls = random_int_list(nc, nv);

    //genere nv couples de cles (publique, secrete) differents representant les nv citoyens
    FILE *fkeys = fopen("keys.txt", "w");
    //cree un fichier candidates.txt contenant la cle publique de tous les candidats
    FILE *fc = fopen("candidates.txt", "w");
    
    if(fkeys == NULL || fc == NULL){
        printf("Erreur de fichier\n");
    }
    for(int i = 0; i < nv; i++){
        //la creation du paire de cle pour les electeurs
        Key* pKey = malloc(sizeof(Key));
        Key* sKey = malloc(sizeof(Key)); 
        init_pair_keys(pKey, sKey, 3, 7);

        //changer les keys a les chaines de caracteres pour stocker dans les fichers .txt
        char *str_pK = key_to_str(pKey);
        char *str_sK = key_to_str(sKey); 
        fprintf(fkeys, "%s %s\n", str_pK, str_sK);
        if(is_in_liste(ls, i, nc)){
            fprintf(fc, "(%ld,%ld)\n", pKey -> val, pKey -> n);
        }

        free(pKey);
        free(sKey);
        free(str_pK);
        free(str_sK);
    }
    
    free(ls);

    fclose(fkeys);
    fclose(fc);

    FILE *fdeclaration = fopen("declarations.txt", "w");
    FILE *data_key = fopen("keys.txt", "r");

    if(fdeclaration == NULL || data_key == NULL){
        printf("Erreur de fichier\n");
    }
    while(! feof(data_key)){
        //Generer un nombre aleatoire pour indiquer pour quel candidat voter,
        //avec une valeur egale au nombre de lignes du fichier
        int num_candidate = rand() % nc;
        char str_p[256];
        char str_s[256];
        
        fscanf(data_key, "%s %s\n", str_p, str_s);
        FILE *data_can = fopen("candidates.txt", "r"); 
        if(data_can == NULL){
            printf("Erreur de fichier\n");
        } 
        int current_line = 0;

        while(! feof(data_can)){
            char pKeyc_str[256];
            if(current_line == num_candidate){
                //lire les donnees sur les fichiers
                Key *s = str_to_key(str_s);
                Key *p = str_to_key(str_p);
                fscanf(data_can, "%s\n", pKeyc_str);
                Signature* sgn = sign(pKeyc_str, s);
                char* c = signature_to_str(sgn);
                Protected* pr = init_protected(p, pKeyc_str, sgn);

                //on verifier si les votes sont valide ou non ici
                if (verify(pr)){ 
                    fprintf(fdeclaration, "%s %s %s\n", str_p, pKeyc_str, c);
                }
                else{
                    printf("Signature non valide\n");
                }

                //free les pointeurs
                free(c);
                free(s);
                free(p);
                free_signature(sgn);
                free_protected(pr);
                current_line++;
            }
            current_line++;
            fgets(pKeyc_str, 256, data_can);
        }
        fclose(data_can);
    }

    fclose(fdeclaration);
    fclose(data_key);
}