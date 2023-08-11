#include <stdio.h>
#include <string.h>
#include "lanceCommande.h"

int main(int argc, char *argv[]){
    for (int i = 1; i < argc; i++){
        lanceCommandeNew(argv[i]);
    }
    
    return 0;
}