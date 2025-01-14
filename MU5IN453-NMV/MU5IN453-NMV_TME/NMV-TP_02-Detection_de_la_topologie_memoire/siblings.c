#include "hwdetect.h"

#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/mman.h>
#include <math.h>

static inline char *alloc(size_t n)
{
	size_t i;
	char *ret = mmap(NULL, n, PROT_READ | PROT_WRITE,
			 MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);

	if (ret == MAP_FAILED)
		abort();

	for (i = 0; i < n; i += PAGE_SIZE)
		ret[i] = 0;

	return ret;
}
void *stress_alu(void *arg)
{
	int i,p;
	uint64_t start,end;
	setcore(2);

	start = now;
	for(p = 0; p < 100; ++p){
		for(i = 0; i< 10000000; ++i){
			sqrt(i);
		}
	}
	end = now();
	printf("%ld \t", (end - start)/100);

	return NULL;
}

int main(void)
{
	int i,p;
	uint64_t start, end;
	pthread_t thread1;
	pthread_t thread2;
	pthread_create(&thread1,NULL,stress_alu,NULL);
	pthread_create(&thread2,NULL,stress_alu,NULL);

	start = now();
	pthread_join(&thread1);
	pthread_join(&thread2);//TODO
	end = now();
		
	return end-start;
}
