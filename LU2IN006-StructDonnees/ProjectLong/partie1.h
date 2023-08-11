//
// Created by runlin ZHOU 28717281
//

#ifndef PARTIE1
#define PARTIE1

//Exercice 1
//Question 1.1
int is_prime_naive(long p);

//Question 1.3
long modpow_naive(long a, long m, long n);

//Question 1.4
int modpow(long a, long m, long n);

//Question 1.6-1.7
int witness(long a, long b, long d, long p);
long rand_long(long low, long up);
int is_prime_miller(long p, int k);

//Question 1.8
long random_prime_number(int low_size, int up_size, int k);

//Exercice 2
//Question 2.1
long methode_bezout(long a, long b, long* u);
long extended_gcd(long s, long t, long *u, long *v);
void generate_key_values(long p, long q, long* n, long *s, long *u);

//Question 2.2
long* encrypt(char* chaine, long s, long n);

//Question 2.3
char* decrypt(long* crypted,int size, long u, long n);

#endif