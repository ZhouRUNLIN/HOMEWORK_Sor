//
// Created by runlin ZHOU 28717281
//

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "partie1.h"

//Question 1.1
int is_prime_naive(long p){
    if(p % 2 == 0){       //pour assurer p est impair
        return 1;
    }
    
    int i = 3;
    while(i < p - 1){       //enumerer tous les entiers entre 3 et p − 1
        if(p % i == 0)      return 1;
        i++;
    }
    return 0;
}

//Question 1.3
long modpow_naive(long a, long m, long n){
    if(m == 0)      return 1;
    return (modpow_naive(a, m - 1, n)*a)%n;
}

//Question 1.4 
int modpow(long a, long m, long n){
    if(m == 0)      return 1;
    if(m % 2 == 0)
        return (modpow(a, m/2, n) * modpow(a, m/2, n)) % n;
    else
        return (modpow(a, (m-1)/2, n) * modpow(a, (m-1)/2, n) * a) % n;    
}

//Question 1.6 - 1.7
//methode Miller-Rabin
int witness(long a, long b, long d, long p) { 
    long x = modpow(a, d, p);
    if(x == 1){
        return 0;
    }

    for(long i = 0; i < b; i++){ 
        if(x == p - 1){
            return 0; 
        }
    x = modpow(x, 2, p); }
    return 1; 
}

long rand_long(long low, long up){ 
    return rand() % (up - low + 1) + low;
}

int is_prime_miller(long p, int k){ 
    if (p == 2) {
        return 1; 
    }

    if (!(p & 1) || p <= 1) { //on verifie que p est impair et different de 1 
        return 0;
    }

    //on determine b et d :
    long b = 0;
    long d = p - 1;
    while (!(d & 1)){ //tant que d n’est pas impair
        d = d / 2;
        b = b + 1;
    }
    // On genere k valeurs pour a, et on teste si c’est un temoin :
    long a;
    int i;
    for(i = 0; i < k; i++){
        a = rand_long(2, p - 1);
        if(witness(a, b, d, p)){ 
            return 0;
        } 
    }
    return 1; 
}

//Question 1.8
long random_prime_number(int low_size, int up_size, int k){
    while(1){
        long num = rand_long(pow(2, low_size), pow(2, up_size));
        if(is_prime_miller(num ,k))
            return num;
    }
}

//Exercice 2
//Question 2.1
//l’algorithme d’Euclide
long methode_bezout(long a, long b, long *m){
    long u[3] = {1, 0, a};
    long v[3] = {0, 1, b};
    long t[3];
    while(v[2] != 0){
        int q = u[2] / v[2];
        for(int i = 0; i < 3; i++){
            t[i] = u[i] - q * v[i];
            u[i] = v[i];
            v[i] = t[i];
        }
    }

    *m = u[0];
    return u[2];
}

long extended_gcd(long s, long t, long* u, long* v) {
    if (t == 0) {
        *u = 1;
        *v = 0;
        return s;
    }

    long uPrim, vPrim;
    long gcd = extended_gcd(t, s % t, &uPrim, &vPrim);
    *u = uPrim;

    if (v) {
        *v = uPrim - (s / t) * vPrim;
    }

    return gcd;
}

void generate_key_values(long p, long q, long* n, long* s, long* u) {
    *n = p * q;
    long t = (p - 1) * (q - 1);
    long num;
    do {
        num = rand_long(0, t);
        *s = num;
        // todo vérifier pour _ dans l'appel à extended_gcd
    } while (methode_bezout(num, t, u) != 1 || *u <= 0);
}

//Question 2.2
long* encrypt(char* chaine, long s, long n) {
    int len = strlen(chaine);

    long* res = malloc(len * sizeof(long));
    if (!res) {
        return NULL;
    }

    long c;
    for(int i = 0; i < len; i++){
        long c = chaine[i];
        res[i] = modpow(c, s, n);
    }
    return res;
}

//Question 2.3
char* decrypt(long* chaine, int size, long u, long n) {
    char* res = malloc((size + 1) * sizeof(char));

    if (!res)   return NULL;

    for (int i = 0; i < size; i++) {
        res[i] = (char) modpow(chaine[i], u, n);
    }

    res[size] = '\0';
    return res;
}
