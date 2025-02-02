/**************************************************************
Lokman A. Abbas-Turki code

Those who re-use this code should mention in their code
the name of the author above.
***************************************************************/

#include <stdio.h>
#include <cuda_runtime.h>


// Function that catches the error 
void testCUDA(cudaError_t error, const char *file, int line)  {

	if (error != cudaSuccess) {
	   printf("There is an error in file %s at line %d\n", file, line);
       exit(EXIT_FAILURE);
	} 
}

// Has to be defined in the compilation in order to get the correct value of the 
// macros __FILE__ and __LINE__
#define testCUDA(error) (testCUDA(error, __FILE__ , __LINE__))

int main() {
    int device = 0;
	int deviceCount = 0;
    // Question 1.2.1
    cudaError_t error_id = cudaGetDeviceCount(&deviceCount);
    if (error_id != cudaSuccess) {
        printf("cudaGetDeviceCount returned %d -> %s\n", (int)error_id, cudaGetErrorString(error_id));
        printf("No CUDA devices found!\n");
        return 1;
    }
    printf("Detected %d CUDA capable device(s).\n", deviceCount);

    // 1.2.4
    struct cudaDeviceProp deviceProp;
    // error_id = cudaGetDeviceProperties(&deviceProp, device);

    // b)
    // error_id = cudaGetDeviceProperties(&deviceProp, 20);
    // RETURN :: cudaGetDeviceProperties returned 101 -> invalid device ordinal
    testCUDA(cudaGetDeviceProperties(&deviceProp, 20));    // c)
    // There is an error in file DevQuery.cu at line 40
    
    // if (error_id != cudaSuccess) {
    //     printf("cudaGetDeviceProperties returned %d -> %s\n", (int)error_id, cudaGetErrorString(error_id));
    //     return 1;
    // }

    // a) Global memory size
    printf("Global memory size: %zu bytes\n", deviceProp.totalGlobalMem);
    printf("Largest single-precision floating-point array: %zu elements\n", deviceProp.totalGlobalMem / sizeof(float));

    // b) Maximum grid size (maxGridSize)
    // maxGridSize表示在特定设备上，每个网格维度Grid Dimension的最大尺寸。
    // CUDA网格是由块组成的三维结构，因此 maxGridSize 是一个包含三个元素的数组，分别表示在 X、Y 和 Z 方向上每个维度可以有多少个块。
    // maxGridSize[0], [1], [2]分别表示x, y, z上的三个方向上的最大块数
    printf("Max grid size: (%d, %d, %d)\n", deviceProp.maxGridSize[0], deviceProp.maxGridSize[1], deviceProp.maxGridSize[2]);

    // c) Maximum number of threads per block (maxThreadsDim and maxThreadsPerBlock)
    // maxThreadsDim表示每个线程块在特定设备上的每个维度的最大线程数。类似于 maxGridSize，maxThreadsDim 是一个包含三个元素的数组，分别对应于线程块的 X、Y 和 Z 维度的最大大小
    printf("Max threads per block: %d\n", deviceProp.maxThreadsPerBlock);
    printf("Max thread dimensions: (%d, %d, %d)\n", deviceProp.maxThreadsDim[0], deviceProp.maxThreadsDim[1], deviceProp.maxThreadsDim[2]);

    // d) Warp size
    // warp是GPU调度线程的最小单位。
    // CUDA 线程是分组执行的，每个warp中的线程同时执行同一条指令，只是每个线程处理的数据可能不同
    printf("Warp size: %d threads\n", deviceProp.warpSize);

    // e) Shared memory size per block
    printf("Shared memory per block: %zu bytes\n", deviceProp.sharedMemPerBlock);

    // f) Number of registers per block
    printf("Registers per block: %d\n", deviceProp.regsPerBlock);

    // g) 2D texture size
    // 由于纹理缓存的大小非常有限，不可能将整个大尺寸的纹理放入缓存中。
    // 但是，CUDA 会利用纹理缓存来缓存局部访问的数据，从而提高性能。
    printf("Max 2D texture size: %d x %d\n", deviceProp.maxTexture2D[0], deviceProp.maxTexture2D[1]);

    // h) Number of CUDA cores
    int cudaCores = 0;
    int multiprocessorCount = deviceProp.multiProcessorCount;
    int cudaCoresPerSM = 128;  // 通常每个SM有128个CUDA核心
    cudaCores = multiprocessorCount * cudaCoresPerSM;
    printf("CUDA cores: %d\n", cudaCores);

    return 0;
}
