#include "Semaphore.h"
#include <thread>
#include <vector>

// TODO : classe à modifier
class Data {
	std::vector<int> values;
public :
	int read() const {
		if (values.empty())
			return 0;
		else
			return values[rand()%values.size()];
	}
	void write() {
		values.push_back(rand());
	}
};

// Pas de modifications dans la suite.
void worker(Data & data) {
	for (int i=0; i < 20 ; i++) {
		auto r = ::rand() % 1000 ; // 0 to 1 sec
		std::this_thread::sleep_for (std::chrono::milliseconds(r));
		if (r % 2)
			auto lu = data.read();
		else
			data.write();
	}
}

int main2 () {
	// a faire varier
	const int NBTHREAD=10;

	// le data partagé
	Data d;

	std::vector<std::thread> threads;
	for (int i=0; i < NBTHREAD; i++)
		threads.emplace_back(worker,std::ref(d));

	for (auto & t: threads)
		t.join();
	return 0;
}

