/**************************************************************
This code is a part of a course on cuda taught by the author: 
Lokman A. Abbas-Turki

Those who re-use this code should mention in their code 
the name of the author above.
***************************************************************/
#include <stdio.h>

// Function that catches the error 
void testCUDA(cudaError_t error, const char *file, int line)  {
	if (error != cudaSuccess) {
	   printf("There is an error in file %s at line %d\n", file, line);
       exit(EXIT_FAILURE);
	} 
}

// Has to be defined in the compilation in order to get the correct value 
// of the macros __FILE__ and __LINE__
#define testCUDA(error) (testCUDA(error, __FILE__ , __LINE__))

// add kernel
__global__ void add_k(int *A, int *B, int N){

	//for (int i = 0; i < 50; i++) { // or 1000
		int idx = threadIdx.x + blockIdx.x * blockDim.x;
		if (idx < N) {
			B[idx] += A[idx];
		}
	//}
}

// subtract kernel
__global__ void sub_k(int *A, int *B, int N){

	int idx = threadIdx.x + blockIdx.x * blockDim.x;
	if(idx<N){
		B[idx] -= A[idx];
	}
}


/////////////////////////////////////////////////////////////////////////
// To show the difference between transferring 
// everything once vs. using smaller frames 
/////////////////////////////////////////////////////////////////////////
void withoutStream (int *aCPU, int *bCPU, int size, int NBS){

	int Qsize = 128;
	int F = size/Qsize;
	size_t L = F*sizeof(int);
	int *a, *b, *cCPU; 
	float TimeVar;
	cudaEvent_t start, stop;

	testCUDA(cudaEventCreate(&start));
	testCUDA(cudaEventCreate(&stop));
	cudaHostAlloc(&cCPU, size*sizeof(int), cudaHostAllocDefault);
	testCUDA(cudaMalloc(&a,size*sizeof(int)));
	testCUDA(cudaMalloc(&b,size*sizeof(int)));
	testCUDA(cudaEventRecord(start,0));

	if(NBS<0){
	  testCUDA(cudaMemcpy(a,aCPU,size*sizeof(int),cudaMemcpyHostToDevice));
	  testCUDA(cudaMemcpy(b,bCPU,size*sizeof(int),cudaMemcpyHostToDevice));
	  add_k<<<(size+1023)/1024,1024>>>(a,b,size);
	  testCUDA(cudaMemcpy(cCPU,b,size*sizeof(int),cudaMemcpyDeviceToHost));
	}else{
  	  for (int i=0; i<size; i+= NBS*F){
		for (int j=0; j <NBS; j++){
		  testCUDA(cudaMemcpy(a+j*F+i,aCPU+j*F+i,L,cudaMemcpyHostToDevice));
		  testCUDA(cudaMemcpy(b+j*F+i,bCPU+j*F+i,L,cudaMemcpyHostToDevice));
		}
		for (int j=0; j <NBS; j++){
		  add_k<<<(F+1023)/1024,1024>>>(a+j*F+i,b+j*F+i,F);
		}
		for (int j=0; j <NBS; j++){
		  testCUDA(cudaMemcpy(cCPU+j*F+i,b+j*F+i,L,cudaMemcpyDeviceToHost));
		}
	  }
	}

	testCUDA(cudaEventRecord(stop,0));
	testCUDA(cudaEventSynchronize(stop));
	testCUDA(cudaEventElapsedTime(&TimeVar, start, stop));
	for (int i=size-F-3; i<size-F; i++){
		printf("a[i]+b[i] = %i,  %i\n",aCPU[i]+bCPU[i],cCPU[i]);
	}
	if(NBS<0){
		printf("Processing time for doing everything once: %f ms\n", TimeVar);
	}else{
		printf("Processing time when using frames without streams: %f ms\n", TimeVar);
	}
	testCUDA(cudaEventDestroy(start));
	testCUDA(cudaEventDestroy(stop));
	testCUDA(cudaFree(a));
	testCUDA(cudaFree(b));
	cudaFreeHost(cCPU);
}


int main (void){

	//Compare using different sizes
	// Small size
	int size = 32*512*512;
	// Average size
	//int size = 128*512*512;
	// Big size
	//int size = 512 * 512 * 512;


	// Number of streams 
	int NBS = 16;

	int *a, *b;

	cudaHostAlloc(&a, size*sizeof(int), cudaHostAllocDefault);
	cudaHostAlloc(&b, size*sizeof(int), cudaHostAllocDefault);

	for (int i=0; i<size; i++){
		a[i] = i;
		b[i] = 2*i;
	}

	// Warming up the GPU
	withoutStream (a, b, size, -1);

	// Only one transfer
	withoutStream (a, b, size, -1);
	// Transfer frames without streams: Processing them NBS by NBS frames
	withoutStream (a, b, size, NBS);

	// Transfer frames using NBS streams: Processing them NBS by NBS frames
	//withStream (a, b, size, NBS);

	cudaFreeHost(a);
	cudaFreeHost(b);
}