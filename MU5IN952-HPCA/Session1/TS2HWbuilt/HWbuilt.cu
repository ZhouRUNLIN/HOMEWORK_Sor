/**************************************************************
Lokman A. Abbas-Turki code

Those who re-use this code should mention in their code
the name of the author above.
***************************************************************/

#include <stdio.h>
#include <cuda_runtime.h>

// Function that catches the error 
void testCUDA(cudaError_t error, const char* file, int line) {

	if (error != cudaSuccess) {
		printf("There is an error in file %s at line %d\n", file, line);
		exit(EXIT_FAILURE);
	}
}

// Has to be defined in the compilation in order to get the correct value of the 
// macros __FILE__ and __LINE__
#define testCUDA(error) (testCUDA(error, __FILE__ , __LINE__))

__global__ void empty_k(void) {}

__global__ void print_k(void) {

	/*************************************************************

	Once requested, replace this comment by the appropriate code

	*************************************************************/

}

int main(void) {

	empty_k <<<1, 1>>> ();

    int device = 0;
    int major = 0, minor = 0;
    size_t printf_fifo_size = 0;
    cudaError_t error_id;

    // a) Use cudaDeviceGetAttribute to get the major and minor compute capability
    error_id = cudaDeviceGetAttribute(&major, cudaDevAttrComputeCapabilityMajor, device);
    if (error_id != cudaSuccess) {
        printf("cudaDeviceGetAttribute (major) returned %d -> %s\n", (int)error_id, cudaGetErrorString(error_id));
        return 1;
    }

    error_id = cudaDeviceGetAttribute(&minor, cudaDevAttrComputeCapabilityMinor, device);
    if (error_id != cudaSuccess) {
        printf("cudaDeviceGetAttribute (minor) returned %d -> %s\n", (int)error_id, cudaGetErrorString(error_id));
        return 1;
    }

    printf("Device compute capability: Major = %d, Minor = %d\n", major, minor);

    // Explain why cudaDeviceGetAttribute is faster than cudaGetDeviceProperties
    printf("cudaDeviceGetAttribute is faster because it directly queries specific attributes without loading all the properties of the device.\n");

    // b) Use cudaDeviceGetLimit to get the size of the FIFO buffer used for printf on the device
    error_id = cudaDeviceGetLimit(&printf_fifo_size, cudaLimitPrintfFifoSize);
    if (error_id != cudaSuccess) {
        printf("cudaDeviceGetLimit (printf FIFO size) returned %d -> %s\n", (int)error_id, cudaGetErrorString(error_id));
        return 1;
    }

    printf("Size of the printf FIFO buffer: %zu bytes\n", printf_fifo_size);

    // Explanation on why we need cudaDeviceSynchronize when using printf on the device
    printf("cudaDeviceSynchronize is needed after printf because device-side printf uses a FIFO buffer to transfer data to the host. cudaDeviceSynchronize ensures that the kernel completes execution and all the data in the printf buffer is transferred to the host before continuing execution on the host.\n");

    return 0;
}