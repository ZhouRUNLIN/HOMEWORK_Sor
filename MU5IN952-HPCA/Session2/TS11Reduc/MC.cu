#include <stdio.h>


// Question 1
// a)
__global__ void biggest_k1a(int *In, int *Out, int N) {
  int i;
  *Out = In[0];

  for (i = 0; i < N; i++) {
    *Out = max(*Out, In[i]);
  }
}

// b)
__global__ void biggest_k1b(int *In, int *Out, int N) {
  int i;
  __shared__ int M[2];
  M[threadIdx.x] = In[threadIdx.x];

  for (i = threadIdx.x + 2; i < N; i += 2) {
    M[threadIdx.x] = max(M[threadIdx.x], In[i]);
  }
  
  if (threadIdx.x == 0) {
    *Out = max(M[0], M[1]);
  }
}

// c)
__global__ void biggest_k1c(int *In, int *Out, int N) {
  int i;
  __shared__ int M[4];
  M[threadIdx.x] = In[threadIdx.x];

  for (i = threadIdx.x + 4; i < N; i += 4) {
    M[threadIdx.x] = max(M[threadIdx.x], In[i]);
  }
  
  if (threadIdx.x < 2) {
    M[threadIdx.x] = max(M[threadIdx.x], M[threadIdx.x + 2]);
  }
  if (threadIdx.x == 0) {
    *Out = max(M[0], M[1]);
  }
}

// d)
__global__ void biggest_k1d(int *In, int *Out, int N) {
  int i;
  __shared__ int M[64];
  M[threadIdx.x] = max(In[threadIdx.x], In[threadIdx.x + 64]);
  __synrthreads();

  i = 32;
  while (i > 1) {
    if (threadIdx.x < i) {
      M[threadIdx.x] = max(M[threadIdx.x], M[threadIdx.x + i])
    }
    i /= 2;
  }
  for (i = threadIdx.x + 4; i < N; i += 4) {
    M[threadIdx.x] = max(M[threadIdx.x], In[i]);
  }

  if (threadIdx.x == 0) {
    *Out = max(M[0], M[1]);
  }
}

// Question 3
__global__ void biggest_k3(int *In, int *Out, int N) {
  int i;
  __shared__ int M[64];
  M[threadIdx.x] = In[threadIdx.x];

  for (i = threadIdx.x + 64; i < N; i += 64) {
    M[threadIdx.x] = max(M[threadIdx.x], M[threadIdx.x + 64])
  }
  __synrthreads();

  i = 32;
  while (i > 0) {
    if (threadIdx.x < i) {
      M[threadIdx.x] = max(M[threadIdx.x], M[threadIdx.x + i])
    }
    i /= 2;
  }
  for (i = threadIdx.x + 4; i < N; i += 4) {
    M[threadIdx.x] = max(M[threadIdx.x], In[i]);
  }

  if (threadIdx.x == 0) {
    *Out = max(M[0], M[1]);
  }
}

// Question 4
// a)
__global__ void biggest_k4a(int *In, int *Out, int N) {
  int i;
  int idx = threadIdx.x + blockIdx.x*blockDim.x
  __shared__ int M[64];
  M[threadIdx.x] = max(In[idx], In[idx + 128]);
  __synrthreads();

  i = 32;
  while (i > 0) { // i > 1
    if (threadIdx.x < i) {
      M[threadIdx.x] = max(M[threadIdx.x], M[threadIdx.x + i])
    }
    i /= 2;
  }

  if (threadIdx.x == 0) {
    atomicMax(Out, M[0]); // max(M[0], M[1])
  }
}

// b)
__global__ void biggest_k4b(int *In, int *Out, int N) {
  int i;
  int idx = threadIdx.x + blockIdx.x*blockDim.x
  __shared__ int M[64];

  if (idx < N) {
    M[threadIdx.x] = In[idx];
  } else {
    M[threadIdx.x] = In[idx + 128];
  }
  __synrthreads();

  i = blockDim.x / 2;
  while (i > 0) {
    if (threadIdx.x < i) {
      M[threadIdx.x] = max(M[threadIdx.x], M[threadIdx.x + i])
    }
    i /= 2;
    __synrthreads();
  }

  if (threadIdx.x == 0) {
    atomicMax(Out, M[0]);
  }
}