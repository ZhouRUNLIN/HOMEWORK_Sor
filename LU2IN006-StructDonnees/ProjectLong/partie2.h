//
// Created by runlin ZHOU 28717281
//

#ifndef PARTIE2
#define PARTIE2

//Question 3.1
typedef struct key{
    long val;
    long n;
}Key;

//Question 3.2
void init_key(Key* key, long val, long n);

//Question 3.3
void init_pair_keys(Key* pKey, Key* sKey, long low_size, long up_size);

//Question 3.4
char* key_to_str(Key* key);
Key* str_to_key(char* str);

//Question 3.5
typedef struct signature{
    int maxTaille;
    long* tab;
}Signature;

//Question 3.6
Signature* init_signature(long* content, int size);
void free_signature(Signature* sgn);

//Question 3.7
Signature* sign(char* mess, Key* sKey);

//Question 3.8
char* signature_to_str(Signature* sgn);
Signature* str_to_signature(char* str);

//Question 3.9
typedef struct protected{
    Key* pKey; 
    char* mess;
    Signature* s;
}Protected;

//Question 3.10
Protected* init_protected(Key* pKey, char* mess, Signature* sgn);
void free_protected(Protected* p);

//Question 3.11
int verify(Protected* pr);

//Question 3.12
char* protected_to_str(Protected* p);
Protected* str_to_protected(char* mess);

//Question 4.1
void generate_random_data(int nv, int nc);

#endif