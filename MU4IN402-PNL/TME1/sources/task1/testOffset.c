#include <stdlib.h>
#include <stdio.h>

#include "version.h"

int main(){
    

    struct commit commit = {
        .id = 0,
        .version = {.flags = 0,
            .major = 3,
            .minor = 5, 
        },
        .comment = NULL,
        .next = NULL,
        .prev = NULL
    };



    printf("l'adresse id = %p\n", &(commit.id));
    printf("l'offset id et commit  = %lu\n", (char *)(&commit.id) - (char *)(&commit));
    printf("l'offset %lu\n", (unsigned long)(&((struct commit *)0)->version));
    printf("%p\n",commit_of(&commit.version));
    printf("l'adresse version = %p\n", &(commit.version));
    printf("l'adresse comment = %p\n", &(commit.comment));
    printf("l'adresse next = %p\n", &(commit.next));
    printf("l'adresse prev = %p\n", &(commit.prev));

    return 0;
}