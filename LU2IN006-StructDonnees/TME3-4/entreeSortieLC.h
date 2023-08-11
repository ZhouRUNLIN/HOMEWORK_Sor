#ifndef ENTREESORTIELC_H
#define ENTREESORTIELC_H
#include "biblioLC.h"
#include "biblioH.h"

//Question 1.3
Biblio* charger_n_entrees(char* nomfic, int n);
void enregistrer_biblio(Biblio *b, char* nomfic);

//Question 2.6
BiblioH* charger_n_entreesH(char* nomfic, int n);
void enregistrer_biblioH(BiblioH* b, char* nomfic);
#endif