/*
    Auteur :
        ZHOU    runlin      28717281
        ZHANG   zhile       21201131
        XU      mengqian    21306077
*/

#include <time.h>
#include "src/utils.h"
#include "src/fastFourierTrans.h"
#include "src/multPoly.h"

int main(){
    // tests for random data
    printf("---------------------------------------------------------------\n");
    printf("test for func : randomConsVecNum\n");
    int size = rand()%10;
    struct polynomial polyTestRandom = createRandomPoly(size);
    printPoly(polyTestRandom);
    delPoly(polyTestRandom);

    //tests for primitiveRoot
    printf("---------------------------------------------------------------\n");
    printf("test for func : primitiveRoot\n");
    int pri_size = 4;
    struct polynomial primitiveList = primitiveRoot(pri_size);
    printPoly(primitiveList);
    delPoly(primitiveList);

    printf("---------------------------------------------------------------\n");
    printf("test for func : fft and fftInverse\n");
    // init data for tests
    struct polynomial poly = createPoly(4);
    struct numComplex el1 = {1, 0};
    struct numComplex el2 = {2, 0};
    struct numComplex el3 = {3, 0};
    struct numComplex el4 = {4, 0};
    // struct numComplex el5 = {16, 0};
    // struct numComplex el6 = {32, 0};
    addElement(el1, &poly);
    addElement(el2, &poly);
    addElement(el3, &poly);
    addElement(el4, &poly);
    // addElement(el5, &poly);
    // addElement(el6, &poly);
    // end of init
    // poly = fft(poly, getNearestK(poly.cpt));
    struct polynomial res_fft = fft(poly, getNearestK(poly.size));
    printPoly(res_fft);
    delPoly(res_fft);

    // les tests pour multPoly

    // creer deux polynomial random
    printf("---------------------------------------------------------------\n");
    printf("test for func : mult of poly\n");
    int sizeA = rand()%10;
    struct polynomial polyA = createRandomPoly(sizeA);
    printf("polyA :\n");
    printPoly(polyA);
    
    printf("---------------------------------------------------------------\n");
    int sizeB = rand()%10;
    struct polynomial polyB = createRandomPoly(sizeB);
    printf("polyB :\n");
    printPoly(polyB);

    printf("---------------------------------------------------------------\n");
    struct polynomial multPoly = NaiveMultPoly(polyA, polyB);
    printf("poly by naive methode\n");
    printPoly(multPoly);

    printf("---------------------------------------------------------------\n");
    struct polynomial multFFTpoly = fftMultPoly(polyA, polyB);
    printPoly(multFFTpoly);

    isEqPoly(multFFTpoly, multPoly);
    delPoly(multPoly);
    delPoly(multFFTpoly);

    return 0;
}