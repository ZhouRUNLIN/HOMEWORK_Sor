#pragma once

#include "Compte.h"
#include <vector>
#include <set>

namespace pr {

class Banque {
	typedef std::vector<Compte> comptes_t;
	comptes_t comptes;


    //std::mutex transfertMutex;
    std::recursive_mutex transfertMutex;

    std::set<Compte*> comptesVerifies;
    std::mutex verifieMutex;

public :
	Banque (size_t ncomptes, size_t solde) : comptes (ncomptes, Compte(solde)){
	}
	void transfert(size_t deb, size_t cred, unsigned int val) ;
	size_t size() const ;
	bool comptabiliser (int attendu) const ;
    Compte getCompte(size_t index);
    void effectuerBilan();
};

}
