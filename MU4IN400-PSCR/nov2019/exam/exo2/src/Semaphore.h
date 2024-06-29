#pragma once

namespace pr {

class Semaphore {
	int compteur;
public :
	Semaphore(int initial);
	void acquire(int qte);
	void release(int qte);
};

}
