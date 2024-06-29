#pragma once

#include <mutex>
#include <condition_variable>

namespace pr {

    class Barrier {
        mutable std::mutex m;
        std::condition_variable cond;
        int cpt = 0;
        int goal;

    public:
        Barrier(int n) : goal(n) {}

        void done() {
            std::unique_lock<std::mutex> lg(m);
            cpt++;
            if (cpt == goal) {
                cond.notify_all();
            }
        }

        void waitFor() {
            std::unique_lock<std::mutex> lg(m);
            cond.wait(lg, [this] { return cpt == goal; });
        }
    };

}
