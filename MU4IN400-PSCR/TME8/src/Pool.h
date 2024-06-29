#ifndef SRC_POOL_H_
#define SRC_POOL_H_

#include "Queue.h"
#include "Job.h"
#include <vector>
#include <thread>

namespace pr {

	// fonction passee a ctor de  thread
	void poolWorker(Queue<Job> * queue) {
		while (true) {
			Job * j = queue->pop();

			// NB : ajout en fin de TD pour la terminaison propre
			if (j == nullptr) {
				// on est non bloquant = il faut sortir
				return;
			}

			j->run();
			delete j;
		}
	}

class Pool {

	Queue<Job> queue;
	std::vector<std::thread> threads;
public:
	Pool(int qsize) : queue(qsize) {}
	void start (int nbthread) {
		threads.reserve(nbthread);
		for (int i=0 ; i < nbthread ; i++) {
			threads.emplace_back(poolWorker, &queue);
		}
	}
	// Ajout a la fin du TD, pour la terminaison
	void stop() {
		queue.setBlocking(false);
		for (auto & t : threads) {
			t.join();
		}
		threads.clear();
	}
	~Pool() {
		stop();
	}
	void addJob (Job * job) {
		queue.push(job);
	}
};

} /* namespace pr */

#endif /* SRC_POOL_H_ */
