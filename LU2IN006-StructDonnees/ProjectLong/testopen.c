#include <stdio.h>
#include <string.h>
#include <openssl/sha.h>
int main(){
    const char *s = "Rosetta code";
    unsigned char *d = (unsigned char *)SHA256((const unsigned char *)s, strlen(s), 0);
    int i;
    for (i = 0; i < SHA256_DIGEST_LENGTH; i++)
        printf("%02x", d[i]);
    putchar('\n');
}