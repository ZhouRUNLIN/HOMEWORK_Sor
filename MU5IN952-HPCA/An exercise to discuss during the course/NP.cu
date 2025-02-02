/**************************************************************
This code is a part of a course on cuda taught by the author: 
Lokman A. Abbas-Turki

Those who re-use this code should mention in their code 
the name of the author above.
***************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define NB 2048
#define NTPB 1024

__device__ float Glob[7*NB*NTPB];	// Global variable solution
__constant__ float Cst[7];		// Constant variable solution

// Function that catches the error 
void testCUDA(cudaError_t error, const char *file, int line)  {

	if (error != cudaSuccess) {
	   printf("There is an error in file %s at line %d\n", file, line);
       exit(EXIT_FAILURE);
	} 
}


// Has to be defined in the compilation in order to get the correct value of the macros
// __FILE__ and __LINE__
#define testCUDA(error) (testCUDA(error, __FILE__ , __LINE__))


/*One-Dimensional Normal Law. Cumulative distribution function. */
float NP(float x)
{
  float p = 0.2316419f;
  float b1= 0.3193815f;
  float b2= -0.3565638f;
  float b3= 1.781478f;
  float b4= -1.821256f;
  float b5= 1.330274f;
  float one_over_twopi= 0.3989423f;  
  float t;

  if(x >= 0.0f){
      t = 1.0f / ( 1.0f + p * x );
      return (1.0f - one_over_twopi * expf( -x * x / 2.0f ) * t * 
			 ( t *( t * ( t * ( t * b5 + b4 ) + b3 ) + b2 ) + b1 ));
  }else{/* x < 0 */
      t = 1.0f / ( 1.0f - p * x );
      return ( one_over_twopi * expf( -x * x / 2.0f ) * t * 
			 ( t *( t * ( t * ( t * b5 + b4 ) + b3 ) + b2 ) + b1 ));
  }
}


// Shared based solution
__global__ void  NP_SHA_k(float* x)
{
	/*************************************************************

	Once requested, replace this comment by the appropriate code

	*************************************************************/
}


// Constant memory based solution
__global__ void  NP_CST_k(float* x)
{
	/*************************************************************

	Once requested, replace this comment by the appropriate code

	*************************************************************/
}


// Global memory based solution with read only caching
__global__ void  NP_GLOreadOnly_k(float* x)
{
	/*************************************************************

	Once requested, replace this comment by the appropriate code

	*************************************************************/
}

// Global memory based solution
__global__ void  NP_GLO_k(float *x)
{
  int idx = threadIdx.x + blockIdx.x*blockDim.x;
  Glob[idx] = 0.2316419f;
  Glob[idx + NB * NTPB] = 0.3193815f;
  Glob[idx + NB * NTPB * 2] = -0.3565638f;
  Glob[idx + NB * NTPB * 3] = 1.781478f;
  Glob[idx + NB * NTPB * 4] = -1.821256f;
  Glob[idx + NB * NTPB * 5] = 1.330274f;
  Glob[idx + NB * NTPB * 6] = 0.3989423f;  
  float t, X;

  X = x[idx];

  if(X >= 0.0f){
      t = 1.0f / ( 1.0f + Glob[idx] * X );
      x[idx] = (1.0f - Glob[idx+6* NB * NTPB] * expf( -X * X / 2.0f ) * t *
			   ( t *( t * ( t * ( t * Glob[idx+5* NB * NTPB] + Glob[idx+4* NB * NTPB] ) +
			   Glob[idx+3* NB * NTPB] ) + Glob[idx+2* NB * NTPB] ) + Glob[idx+ NB * NTPB] ));
  }else{/* X < 0 */
      t = 1.0f / ( 1.0f - Glob[idx] * X );
      x[idx] = ( Glob[idx+6* NB * NTPB] * expf( -X * X / 2.0f ) * t *
			   ( t *( t * ( t * ( t * Glob[idx+5* NB * NTPB] + Glob[idx+4* NB * NTPB] ) +
			   Glob[idx+3* NB * NTPB] ) + Glob[idx+2* NB * NTPB] ) + Glob[idx+ NB * NTPB] ));

  }
}

// The wrapper
void NP_GPU(float *a, float *b, int flag, float *TimerAdd){

	float *aGPU;

	cudaEvent_t start, stop;				// GPU timer instructions
	testCUDA(cudaEventCreate(&start));		// GPU timer instructions
	testCUDA(cudaEventCreate(&stop));		// GPU timer instructions

	testCUDA(cudaMalloc(&aGPU, NB*NTPB*sizeof(float)));
	testCUDA(cudaMemcpy(aGPU, a, NB*NTPB*sizeof(float), cudaMemcpyHostToDevice));

	testCUDA(cudaEventRecord(start,0));		// GPU timer instructions
	// Launching the operation on the GPU
	if (flag == 0)	NP_GLO_k<<<NB, NTPB>>>(aGPU);
	if (flag == 1)	NP_GLOreadOnly_k<<<NB, NTPB>>>(aGPU);
	if (flag == 2)	NP_CST_k<<<NB, NTPB>>>(aGPU);
	if (flag == 3)	NP_SHA_k<<<NB, NTPB>>>(aGPU);

	testCUDA(cudaEventRecord(stop,0));		// GPU timer instructions
	testCUDA(cudaEventSynchronize(stop));	// GPU timer instructions
	testCUDA(cudaEventElapsedTime(TimerAdd,	// GPU timer instructions
			 start, stop));					// GPU timer instructions

	// Copying the value from one ProcUnit to the other ProcUnit
	testCUDA(cudaMemcpy(b, aGPU, NB*NTPB*sizeof(float), cudaMemcpyDeviceToHost));

	// Freeing the GPU memory
	testCUDA(cudaFree(aGPU));
	testCUDA(cudaEventDestroy(start));		// GPU timer instructions
	testCUDA(cudaEventDestroy(stop));		// GPU timer instructions
}


int main (void){

	// Variables definition
	float *a, *b;
	float TimerAdd;
	int i;
	
	// Length for the size of arrays
	int length = NB*NTPB;

	// Memory allocation of arrays 
	a = (float*)malloc(length*sizeof(float));
	b = (float*)malloc(length*sizeof(float));

	// Setting values
	for(i=0; i<length; i++){
		a[i] = (float)(i-length/2.0f)/length;
	}


	// Warming the GPU
	NP_GPU(a, b, 0, &TimerAdd);

	// Executing the different options 
	NP_GPU(a, b, 0, &TimerAdd);
	printf("Execution time using large global: %f ms\n", TimerAdd);
	printf(" %f is equal to %f \n", b[5], NP(a[5])); // Check the result 
	NP_GPU(a, b, 1, &TimerAdd);
	printf("Execution time using large read-only global: %f ms\n", TimerAdd);
	printf(" %f is equal to %f \n", b[25], NP(a[25])); // Check the result
	NP_GPU(a, b, 2, &TimerAdd);
	printf("Execution time using constant memory: %f ms\n", TimerAdd);
	printf(" %f is equal to %f \n", b[250], NP(a[250])); // Check the result 
	NP_GPU(a, b, 3, &TimerAdd);
	printf("Execution time using shared memory: %f ms\n", TimerAdd);
	printf(" %f is equal to %f \n", b[577], NP(a[577])); // Check the result 

	// Freeing the memory
	free(a);
	free(b);

	return 0;
}