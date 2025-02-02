/**************************************************************
This code is a part of a course on cuda taught by the author: 
Lokman A. Abbas-Turki

Those who re-use this code should mention in their code 
the name of the author above.
***************************************************************/

#ifndef SEEK_SET
#define SEEK_SET 0
#endif
#ifndef CLOCKS_PER_SEC
#include <unistd.h>
#define CLOCKS_PER_SEC _SC_CLK_TCK
#endif

class Timer { 

	private:
	double _start;
    double sum;

	public:
    Timer(void) : _start(0.0), sum(0.0) {
	}


	public:
	inline void start(void) {
		_start = clock();
	}

	inline void add(void) {
		sum += (clock() - _start)/(double)CLOCKS_PER_SEC;
    }

	inline double getstart(void) const {
		return _start;
	}

    inline double getsum(void) const {
		return sum;
	}

    inline void reset(void) {
		sum = 0.0;
	}

};