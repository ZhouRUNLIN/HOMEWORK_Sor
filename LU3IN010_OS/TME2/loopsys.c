#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <unistd.h>

int main(){
    for (unsigned long i = 0; i < 50000000; i++)        getpid();
    
    return 0;
}