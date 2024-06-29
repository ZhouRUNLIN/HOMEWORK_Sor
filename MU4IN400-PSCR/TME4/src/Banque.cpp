#include "Banque.h"
#include <iostream>

using namespace std;

namespace pr {

    void Banque::transfert(size_t deb, size_t cred, unsigned int val) {
        Compte &debiteur = comptes[deb];
        Compte &crediteur = comptes[cred];

        {
        unique_lock<mutex> verifieGuard(verifieMutex);
        if (comptesVerifies.find(&debiteur) == comptesVerifies.end() ||
            comptesVerifies.find(&crediteur) == comptesVerifies.end()) {
            verifieGuard.unlock();
            this_thread::sleep_for(chrono::milliseconds(10));
            return transfert(deb, cred, val);
            }
        }


        Compte & lower_acc = comptes[min(deb, cred)];
        Compte & higher_acc = comptes[max(deb, cred)];

        unique_lock<recursive_mutex> lock_lower(lower_acc.getMutex());
        unique_lock<recursive_mutex> lock_higher(higher_acc.getMutex());

        if (debiteur.debiter(val))
        {
            crediteur.crediter(val);
        }
    }


    size_t Banque::size() const {
        return comptes.size();
    }

    bool Banque::comptabiliser(int attendu) const {
        int bilan = 0;
        int id = 0;
        for (const auto &compte: comptes) {
            if (compte.getSolde() < 0) {
                cout << "Compte " << id << " en négatif : " << compte.getSolde() << endl;
            }
            bilan += compte.getSolde();
            id++;
        }
        if (bilan != attendu) {
            cout << "Bilan comptable faux : attendu " << attendu << " obtenu : " << bilan << endl;
        }
        return bilan == attendu;
    }

    Compte Banque::getCompte(size_t index) {
        return this->comptes[index];
    }

    void Banque::effectuerBilan() {
        std::unique_lock<std::mutex> verrou(verifieMutex);
        for (auto &compte: comptes) {
            comptesVerifies.insert(&compte);
        }

    }
}