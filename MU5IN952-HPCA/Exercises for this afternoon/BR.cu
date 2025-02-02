#include <stdio.h>
#include <curand_kernel.h>

// Question 1:
//
// multiBatch_k1<<<2, 512, 512*d*d*sizeof(float)>>>(A, B, d);
// multiBatch_k1<<<16, 64, 64*d*d*sizeof(float)>>>(A, B, d);
__global__ void multiBatch_k(float *A, float *B, int d) {
    int idx = (blockIdx.x * blockDim.x + threadIdx.x)*d*d; 
    int tid = threadIdx.x*d*d;
    extern __shared__ float M[];
    int i, j, k;
    float sum;

    for (i = 0; i < d*d; i++) {
        M[tid + 1] = A[idx + i];
    }

    for (i = 0; i < d; i++) {
        for (j = 0; j < d; j++) {
            sum = 0.0f;
            for (k = 0; k < d; k++) {
                sum += M[tid + i*d + k]*B[idx + k*d + j];
            }
            A[idx + i*d + j] = sum;
        }
    }
}

// Question 2:
//
// multiBatch_k2<<<64, d*d*16, 16*d*d*sizeof(float)>>>(A, B, d);
// multiBatch_k2<<<1024, d*d, d*d*sizeof(float)>>>(A, B, d);
__global__ void multiBatch_k3(float *A, float *B, int d) {
    int midx = threadIdx.x/(d*d); 
    int tid = threadIdx.x - (midx*d*d);     // threadIdx.x%(d*d)

    int ldx = tidx/d;
    int cdx = tidx - (ldx*d);

    int gidx = midx + blockIdx.x*blockDim.x/(d*d);

    extern __shared__ float M[];
    int k;
    float sum = 0.0f;

    M[threadIdx.x] = A[gidx*d*d + tidx];
    __syncthreads();

    for (k = 0; k < d; k++) {
        sum += M[midx*d*d + ldx*d + k]*B[gidx*d*d + k*d + cdx];
    }
    A[gidx*d*d + tidx] = sum;
}

// Question 3:
//
// multiBatch_k3<<<64, d*d*16, 16*d*d*sizeof(float)>>>(A, B, d);
// multiBatch_k3<<<1024, d*d, d*d*sizeof(float)>>>(A, B, d);
__global__ void multiBatch_k3(float *A, float *B, int d) {
    int midx = threadIdx.x/d; 
    int tid = threadIdx.x - (midx*d);     // threadIdx.x%d

    int gidx = midx + blockIdx.x*blockDim.x/d;

    extern __shared__ float M[];
    int i, k;
    float sum;

    for (i = 0; i < d; i++ ) {
        M[threadIdx.x] = A[gidx*d*d + i*d + tidx];
        __syncthreads();
        sum = 0.0f;
        for (k = 0; k < d; k++) {
            sum += M[midx*d + i*d + k]*B[gidx*d*d + k*d + tidx];
        }
        A[gidx*d*d + i*d + tidx] = sum;
    }
}

int main(void) { 

    return 0;
}
