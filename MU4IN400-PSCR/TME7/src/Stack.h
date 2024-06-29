#pragma once
#include <fcntl.h>
#include <cstring> // size_t,memset
#include <semaphore.h>
#include <iostream>

namespace pr {

#define STACKSIZE 2

    template<typename T>
    class Stack {
        T tab [STACKSIZE];
        size_t sz;
        sem_t * prod;
        sem_t * cons;
        sem_t * mt_sz;
        sem_t * mt_tab;
    public :
        Stack () : sz(0) {
            memset(tab,0,sizeof tab);
            prod = sem_open("/prod",O_CREAT|O_EXCL|O_RDWR, 0666, STACKSIZE);
            cons = sem_open("/cons",O_CREAT|O_EXCL|O_RDWR, 0666, 0);
            mt_sz = sem_open("/mt_sz", O_CREAT | O_EXCL | O_RDWR, 0666, 1);
            mt_tab = sem_open("/mt_tab", O_CREAT | O_EXCL | O_RDWR, 0666, 1);
        }

        ~Stack(){
            sem_close(prod);
            sem_close(cons);
            sem_close(mt_sz);
            sem_close(mt_tab);
            sem_unlink("/prod");
            sem_unlink("/cons");
            sem_unlink("/mt_sz");
            sem_unlink("/mt_tab");
        }

        T pop () {
            // bloquer si vide
            sem_wait(cons);     //-1
            sem_wait(mt_sz);
            sem_wait(mt_tab);
            std::cout << "popping " << std::endl;
            T toret = tab[--sz];
            sem_post(mt_tab);    //+1
            sem_post(mt_sz);
            sem_post(prod);
            return toret;
        }

        void push(T elt) {
            //bloquer si plein
            sem_wait(prod);     //-1
            sem_wait(mt_sz);
            sem_wait(mt_tab);
            std::cout << "pushing " << std::endl;
            tab[sz++] = elt;
            sem_post(mt_tab);    //+1
            sem_post(mt_sz);
            sem_post(cons);
        }
    };

}