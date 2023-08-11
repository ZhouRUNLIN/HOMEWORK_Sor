#include <stdio.h>

double fac(double n){
    if(n == 0){
        return 1;
    }

    double sum = 1;
    for(int i = 1; i <= n; i++){
        sum*=i;
    }
    return sum;
}

double com(double n, double k){
    return fac(n)/(fac(k)*fac(n-k));
}

int main(){
    double n = 100;
    double esp = 0;

    FILE* fic;
    fic = fopen("data.txt", "w+");

    for(int k = 17; k <= n; k++){
        fprintf(fic, "%d %f\n", k, com(n - 17, k -17) / com(n, k));
        esp = k*com(n - 17, k -17) / com(n, k);
    }
    printf("E(n) = %f\n", esp);

    fclose(fic);
    return 0;
}