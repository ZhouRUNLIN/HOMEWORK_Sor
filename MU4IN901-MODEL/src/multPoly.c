/*
    Auteur :
        ZHOU    runlin      28717281
        ZHANG   zhile       21201131
        XU      mengqian    21306077
*/

#include "multPoly.h"

struct polynomial NaiveMultPoly(struct polynomial polyA, struct polynomial polyB){
    int size = polyA.cpt + polyB.cpt - 1;
    struct polynomial res = createPoly(size);

    for (int i = 0; i < polyA.cpt; i++){
        for (int j = 0; j < polyB.cpt; j++){
            res.data[i+j] = addComplexNumber(res.data[i+j], multComplexNumber(polyA.data[i], polyB.data[j]));
        }
    }
    res.cpt = size;
    return res;
}

struct polynomial fftMultPoly(struct polynomial polyA, struct polynomial polyB){
    int maxDegree = polyA.size+polyB.size-1;
    // Compute the FFTs of P and Q with omega a primitive nth root of unity,
    int k = getNearestK(maxDegree);
    struct polynomial resP = fft(polyA, k);
    struct polynomial resQ = fft(polyB, k);
    struct polynomial stocker = createPoly(pow(2, k));

    // Multiply coefficient by coefficient these FFTs to get the FFT of R, where R = PQ
    for (int i = 0; i < stocker.size; i++){
        addElement( multComplexNumber(resP.data[i], resQ.data[i]), &stocker);
    }

    // Compute the inverse FFT of the FFT of R to retrieve R
    stocker = fftInverse(stocker, k);
    for (int i = pow(2, k); i > maxDegree; i--){
        subElement(i, &stocker);
    }

    delPoly(resP);
    delPoly(resQ);

    return stocker;
}