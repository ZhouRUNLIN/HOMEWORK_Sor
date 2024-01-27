/*
    Auteur :
        ZHOU    runlin      28717281
        ZHANG   zhile       21201131
        XU      mengqian    21306077
*/

#include "fastFourierTrans.h"

int isValideK(int cpt, int k){
    if (pow(2, k) >= cpt){
        return 1;
    }
    printf("Err : invalide k for exetension \n");
    return 0;
}

struct polynomial extenstionVec(struct polynomial poly, int k) {
    struct polynomial res = createPoly(pow(2, k));
    // extension du tableau
    if(isValideK(poly.cpt, k)){
        // create new obj
        int i;
        // copier the original data
        for (i = 0; i < poly.cpt; i++){
            addElement(poly.data[i], &res);
        }
        for (; i < pow(2, k); i++){
            addElement(zeroComplexNum(), &res);
        }
    }
    delPoly(poly);
    return res;
}

struct polynomial coreFFT(struct polynomial poly){
    struct polynomial res = createPoly(poly.size);
    if (poly.cpt == 1){
        addElement(poly.data[0], &res);
    }
    else{
        // Split p into p_odd and p_even
        struct polynomial p_odd = createPoly(poly.size/2);
        struct polynomial p_even = createPoly(poly.size/2);
        for (int i = 0; i < poly.size/2; i++){
            addElement(poly.data[2*i+1], &p_odd);
            addElement(poly.data[2*i], &p_even);
        }
        // partie recursive
        p_odd = coreFFT(p_odd);
        p_even = coreFFT(p_even);
        
        // calculation du res
        struct polynomial primitiveList = primitiveRoot(poly.size);
        for (int i = 0; i < poly.size/2; i++){      
            res.data[i] = addComplexNumber(p_even.data[i], 
                                            multComplexNumber((primitiveList.data[i]), p_odd.data[i])
                                        );
            res.data[poly.size/2+i] = addComplexNumber(p_even.data[i], 
                                            multComplexNumber(primitiveList.data[poly.size/2+i], p_odd.data[i])
                                        );
            res.cpt += 2;
        }

        // free les memoires
        delPoly(p_odd);
        delPoly(p_even);
        delPoly(primitiveList);
    }
    delPoly(poly);
    return res;
}

struct polynomial fft(struct polynomial poly, int k){
    // verifier si on a besoin de fait op extension
    if (isValideK(poly.cpt, k)){
        poly = extenstionVec(poly, k);
        poly= coreFFT(poly);
    }
    return poly;
}   

struct polynomial fftInverse(struct polynomial poly, int k){
    poly = fft(poly, k);

    struct polynomial res = createPoly(poly.size);
    addElement(divComplexNum(poly.size, poly.data[0]), &res); 
    
    // calcule 1/n du fft en direction opposée
    for (int i = 1; i < poly.size; i++){
        addElement(divComplexNum(poly.size, poly.data[poly.size-i]), &res);  
    }
    delPoly(poly);
    return res;
}
