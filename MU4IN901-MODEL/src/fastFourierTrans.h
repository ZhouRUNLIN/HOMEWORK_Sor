/*
    Auteur :
        ZHOU    runlin      28717281
        ZHANG   zhile       21201131
        XU      mengqian    21306077
*/

#ifndef FASTFOURIERTRANS
#define FASTFOURIERTRANS
#include "utils.h"

// étendre le tableau à 2^k, 
// faites une copie du tableau original et remplissez l'extension avec (0+0i).
struct polynomial extenstionVec(struct polynomial poly, int size); 

// Renvoie le tableau correspondant (n éléments ! !!! degré max = n-1)
// Nous allons étirer à l'intérieur de la fonction fft et ensuite supprimer l'étirement (0+0i)
struct polynomial fft(struct polynomial poly, int k);
struct polynomial fftInverse(struct polynomial poly, int k);

#endif