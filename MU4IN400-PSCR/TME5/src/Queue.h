#ifndef SRC_QUEUE_H_
#define SRC_QUEUE_H_

#include <cstdlib>
#include <mutex>
#include <condition_variable>

namespace pr {

    template <typename T>
    class Queue {
        T ** tab;
        const size_t allocsize;
        size_t begin;
        size_t sz;
        mutable std::mutex m;
        std::condition_variable cond_vide;
        std::condition_variable cond_plein;
        bool isBlocking;
        bool empty() const {
            return sz == 0;
        }
        bool full() const {
            return sz == allocsize;
        }

    public:

        Queue(size_t size) :allocsize(size), begin(0), sz(0),isBlocking(true) {
            tab = new T*[size];
            for(size_t i = 0; i < size; ++i) {
                tab[i] = nullptr;
            }
        }

        size_t size() const {
            std::unique_lock<std::mutex> lg(m);
            return sz;
        }

        T* pop() {
            std::unique_lock<std::mutex> lg(m);
            while (isBlocking && empty()) {
                cond_vide.wait(lg);
            }
            
            if (empty()) {
                return nullptr;
            }
            auto ret = tab[begin];
            tab[begin] = nullptr;
            sz--;
            begin = (begin + 1) % allocsize;
            cond_plein.notify_all();
            return ret;
        }

        bool push(T* elt) {
            std::unique_lock<std::mutex> lg(m);
            while (isBlocking && full()) {
                cond_plein.wait(lg);
            }
            if (full()) {
                return false;
            }
            tab[(begin + sz) % allocsize] = elt;
            sz++;
            cond_vide.notify_all();
            return true;
        }

        void setBlocking(bool block) {
            std::unique_lock<std::mutex> lg(m);
            isBlocking = block;
            if (!isBlocking) {
                cond_vide.notify_all();
                cond_plein.notify_all();
            }
        }

        ~Queue() {
            setBlocking(false);
            for (size_t i = 0; i < sz; i++) {
                auto ind = (begin + i) % allocsize;
                delete tab[ind];
            }
            delete[] tab;
        }
    };

}

#endif /* SRC_QUEUE_H_ */
