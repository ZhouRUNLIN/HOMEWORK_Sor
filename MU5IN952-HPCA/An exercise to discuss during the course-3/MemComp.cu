/**************************************************************
This code compares standard CPU allocation with the locked one.
It also checks the effeciency of mapping the CPU memory 

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

// This kernel is needed to compare the mapped memory to other memories
__global__ void test_kernel(int *Tab, int size, int i){

	int idx = threadIdx.x + blockIdx.x * blockDim.x;
	if(idx<size){
		Tab[idx] = i;
	}
}

float malloc_trans(int size, int NbT, bool flag) {

	int *a, *aGPU;
	float TimeVar = 0.0f;
	cudaEvent_t start, stop;
	testCUDA(cudaEventCreate(&start));
	testCUDA(cudaEventCreate(&stop));

	a = (int*)malloc(size*sizeof(int));
	testCUDA(cudaMalloc(&aGPU,size*sizeof(int)));

	testCUDA(cudaEventRecord(start,0));

	for (int i=0; i<NbT; i++) {
		if (flag){
			testCUDA(cudaMemcpy(aGPU, a, size*sizeof(int),	cudaMemcpyHostToDevice)); 
			// test_kernel<<<(size+127)/128,128>>>(aGPU,size,i);//Comparison with mapped
		}else{
			// test_kernel<<<(size+127)/128,128>>>(aGPU,size,i);//Comparison with mapped
			testCUDA(cudaMemcpy(a, aGPU, size*sizeof(int),	cudaMemcpyDeviceToHost));
		}
	}

	testCUDA(cudaEventRecord(stop,0));
	testCUDA(cudaEventSynchronize(stop));
	testCUDA(cudaEventElapsedTime(&TimeVar, start, stop));
	testCUDA(cudaEventDestroy(start));
	testCUDA(cudaEventDestroy(stop));
	testCUDA(cudaFree(aGPU));
	free(a);	
	return TimeVar;
}

float hostAlloc_trans(int size, int NbT, bool flag) {
	int *a, *aGPU;
	float TimeVar = 0.0f;
	cudaEvent_t start, stop;
	testCUDA(cudaEventCreate(&start));
	testCUDA(cudaEventCreate(&stop));

	a = (int*)malloc(size*sizeof(int));
	testCUDA(cudaMalloc(&aGPU,size*sizeof(int)));

	testCUDA(cudaEventRecord(start,0));

	for (int i=0; i<NbT; i++) {
		if (flag){
			testCUDA(cudaMemcpy(aGPU, a, size*sizeof(int),	cudaMemcpyHostToDevice)); 
			test_kernel<<<(size+127)/128,128>>>(aGPU,size,i);//Comparison with mapped
		}else{
			test_kernel<<<(size+127)/128,128>>>(aGPU,size,i);//Comparison with mapped
			testCUDA(cudaMemcpy(a, aGPU, size*sizeof(int),	cudaMemcpyDeviceToHost));
		}
	}

	testCUDA(cudaEventRecord(stop,0));
	testCUDA(cudaEventSynchronize(stop));
	testCUDA(cudaEventElapsedTime(&TimeVar, start, stop));
	testCUDA(cudaEventDestroy(start));
	testCUDA(cudaEventDestroy(stop));
	testCUDA(cudaFree(aGPU));
	free(a);	
	return TimeVar;
}

float mappedAlloc_trans(int size, int NbT) {

	float TimeVar = 0.0f;
	/*************************************************************

	Once requested, replace this comment by the appropriate code

	*************************************************************/
	return TimeVar;
}

int main (void){

	int size = 1024*1024;
	int NbT = 100;
	float TimeVar;

	
	TimeVar = malloc_trans(size, NbT, true);
	printf("Processing time when using malloc CPU2GPU: %f s\n", 
		   0.001f*TimeVar);
	TimeVar = malloc_trans(size, NbT, false);
	printf("Processing time when using malloc GPU2CPU: %f s\n", 
		   0.001f*TimeVar);

	TimeVar = hostAlloc_trans(size, NbT, true);
	printf("Processing time when using cudaHostAlloc CPU2GPU: %f s\n", 
		   0.001f*TimeVar);
	TimeVar = hostAlloc_trans(size, NbT, false);
	printf("Processing time when using cudaHostAlloc GPU2CPU: %f s\n", 
		   0.001f*TimeVar);

	TimeVar = mappedAlloc_trans(size, NbT);
	printf("Processing time for mapped memory: %f s\n", 
		   0.001f*TimeVar);
	return 0;
}

