/*
    Auteur :
        ZHOU    runlin      28717281
        ZHANG   zhile       21201131
        XU      mengqian    21306077
*/

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "src/utils.h"
#include "src/multPoly.h"
#include "src/fastFourierTrans.h"

// Function to measure the time taken by a function
double measureTime(struct polynomial (*function)(struct polynomial, struct polynomial), struct polynomial polyA, struct polynomial polyB) {
    clock_t start, end;

    start = clock();
    struct polynomial poly = function(polyA, polyB);
    end = clock();
    delPoly(poly);
    return ((double) (end - start)) / CLOCKS_PER_SEC;
}

// Enregistre le temps d'exécution de NaiveMultPoly et fftMultPoly 
// à partir de n=1 respectivement et stocke-les dans des fichiers
void runBenchmarks(int n) {
    // Le temps consomme pour effectuer la procédure de test
    // des polynômes de degré croissant par 100
    int taille = (int)(n/100);

    FILE* file = fopen("benchmark_results.csv", "w");
    if (file == NULL) {
        perror("Error opening file");
        exit(EXIT_FAILURE);
    }
    // en-tête du ficher
    fprintf(file, "Size,NaiveTime,FFTTime\n");

    // remplir le tableau par le temps qu'il consomme
    for (int i = 1; i <= taille; i++) {
        printf("start to calculate poly in degree : %d \n", i*100);
        struct polynomial polyA = createRandomPoly(i*100);
        struct polynomial polyB = createRandomPoly(i*100);
        // enregister les donnees
        fprintf(file, "%d,%f,%f\n", i*100, measureTime(NaiveMultPoly, polyA, polyB), measureTime(fftMultPoly, polyA, polyB));
    }
    fclose(file);
}

int main(int argc, char *argv[]) {
    // Check if there are command line arguments
    if (argc < 2) {
        printf("Usage: %s <argument>\n", argv[0]);
        return 1;  // Exit with an error code
    }
    runBenchmarks(atof(argv[1]));

    return 0; 
}

