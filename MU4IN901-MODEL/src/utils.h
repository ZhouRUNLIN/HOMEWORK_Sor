/*
    Auteur :
        ZHOU    runlin      28717281
        ZHANG   zhile       21201131
        XU      mengqian    21306077
*/

#ifndef UTILS
#define UTILS
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

// complex = real + imaginary * i
struct numComplex {
    double real;        
    double imaginary;  
};

struct polynomial{
    struct numComplex* data; // chaque element de tableau corresponde un degree de poly
    int cpt;    // l'index de queue du tableau, init à 0
    int size;  
};


// affichage
void printPoly(struct polynomial poly);

// gestion des structures

// gestion pour numComplex
struct numComplex creatComplexNum(double real, double imaginary);
struct numComplex zeroComplexNum();     // return 0+0i
struct numComplex randomConsNumber();   // return a complex number at random
int isEqNum(struct numComplex numA, struct numComplex numB);

// gestion pour polynomial
struct polynomial createPoly(int size);         // creation du poly, sans elements
struct polynomial createRandomPoly(int size);   // remplie les element au random
void delPoly(struct polynomial poly);           // free des memoires
// quelque operation vers l'objet
int addElement(struct numComplex el, struct polynomial* poly); 
int subElement(int index, struct polynomial* poly); 
int isEqPoly(struct polynomial polyA, struct polynomial polyB);

// arithmétique élémentaire for complex number
struct numComplex addComplexNumber(struct numComplex numA, struct numComplex numB);      
struct numComplex multComplexNumber(struct numComplex numA, struct numComplex numB);  
struct numComplex divComplexNum(int n, struct numComplex num);

//retourne tous les nth roots de unity
struct polynomial primitiveRoot(int n);
int getNearestK(int size);
#endif