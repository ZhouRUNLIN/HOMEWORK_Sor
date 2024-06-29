#include "Banque.h"
#include <chrono>
#include <random>
#include <cstdlib>
#include <iostream>

using namespace std;



void transaction(pr::Banque& banque)
{
    srand((unsigned)time(NULL));  //seed

    for (int i = 0; i < 1000; ++i)
    {
        int cred = 0;
        int deb = 0;
        int val = 0;
        do
        {
            cred = (rand() % (banque.size()-1));
            deb = (rand() % (banque.size()-1));
            val = (rand()%1000);
        } while (cred == deb);

        banque.transfert(deb, cred, val);

        std::cout << "Transfer from account " << deb << " to account " << cred << "   :  " << val << std::endl;

        int sleepTime = (rand()%20);
        std::this_thread::sleep_for(std::chrono::milliseconds(sleepTime));
    }
}



int main () {
    vector<thread> threads;

    pr::Banque banque(10, 1000);

    const int NB_THREAD = 10;


    for (int i = 0; i < NB_THREAD; ++i)
    {
        threads.emplace_back(transaction, std::ref(banque));
    }

    banque.effectuerBilan();

    for (auto & thread : threads)
    {
        thread.join();

    }

    if (banque.comptabiliser(10000))
    {
        std::cout << "--------------Account Status--------------" <<std::endl;

        for (int i = 0; i < banque.size(); i++)
        {
            cout << "Compte " << i << "  Solde : " << banque.getCompte(i).getSolde() << endl;
        }
    }

    return 0;
}

