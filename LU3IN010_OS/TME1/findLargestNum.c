#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char *argv[]){
    if(argc == 1){
        printf("The argument supplied is vide !\n");
        return 1;
    }

    int max = atoi(argv[0]);
    for (int i = 0; i < argc; i++){
        int current=atoi(argv[i]);
        if (current > max)      max = current;
    }
    printf("the biggest number is %d\n", max);
    return 0;
}