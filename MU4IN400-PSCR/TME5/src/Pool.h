#pragma once

#include "Queue.h"
#include "Job.h"
#include <vector>
#include <thread>
#include <cstring>

namespace pr {

    class Pool {
    private:
        Queue<Job> queue;
        std::vector<std::thread> threads;


        static void poolWorker(Queue<Job>& queue) {
            while (true) {
                Job* j = queue.pop();
                if (!j) {
                    break;
                }
                j->run();
                delete j;
            }
        }

    public:
        Pool(int qsize) : queue(qsize) {}

        void start(int nbthread) {
            for (int i = 0; i < nbthread; ++i) {
                threads.emplace_back(poolWorker, std::ref(queue));
            }
        }

        void submit(Job* job) {
            while (!queue.push(job)) {
                std::this_thread::yield();
            }
        }

        void stop() {
            for (size_t i = 0; i < threads.size(); ++i) {
                queue.push(nullptr);
            }
            for (auto& th : threads) {
                if (th.joinable()) {
                    th.join();
                }
            }
        }

        ~Pool() {
            stop();
        }
    };

}
