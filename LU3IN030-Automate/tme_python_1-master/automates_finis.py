#============================================#
# UE Calculabilite L3 / M1 SFTR              #
# TME Automates finis : acceptation d'un mot #
# Mathieu.Jaume@lip6.fr                      #
#============================================#

from ensembles import *

# Automate represente par un tuple A = (S,T,I,F,eqS)
#---------------------------------------------------

# Exemple automate

ex_A = ([0,1,2,3,4],\
        [(0,None,2),(0,None,3),(1,"b",1),(1,"a",2),(1,"b",3), \
         (1,"b",4),(3,"a",1),(3,"b",1),(3,None,2),(4,"a",0),(4,None,0)],\
        [0],[2],eq_atom)

(ex_S,ex_T,ex_I,ex_F,ex_eqS) = ex_A

# Epsilon-fermeture d'un etat
#----------------------------

def eps_cl(eqS,s,T):
    # eqS : fonction d'egalite sur les etats
    # s : etat
    # T : liste de transitions
    done=[]
    toDo=[s]
    while toDo != []:
        ptr=toDo[0]
        done=[ptr]+done
        toDo=toDo[1:]
        for (sommet1, l, sommet2) in T:
            if eqS(sommet1, ptr) and l == None and not is_in(eqS, sommet2, done):
                toDo=ajout(eqS, sommet2, toDo)  
    return done


# Epsilon-fermeture d'un ensemble d'etats
#----------------------------------------

def eps_cl_set(eqS,S,T):
    # eqS : fonction d'egalite sur les etats
    # S : liste d'etats
    # T : liste de transitions
    listeTotal=[]
    for sommet in S:
        listFerme=eps_cl(eqS, sommet, T)
        listeTotal=union(eqS, listFerme, listeTotal)
    return listeTotal



# Liste des etats accessibles a partir d'un etat s et d'une lettre x
#-------------------------------------------------------------------

def reach_from(eqS,s,x,T):
    # eqS : fonction d'egalite sur les etats
    # s : etat
    # x : symbole de l'alphabet
    # T : liste de transitions
    s_sl = eps_cl(eqS,s,T)
    trans_x = []
    for (s1,l,s2) in T:
        if l==x and is_in(eqS,s1,s_sl):
            trans_x = ajout(eqS,s2,trans_x)
    return eps_cl_set(eqS,trans_x,T)


# Liste des successeurs d'un etat s
# ---------------------------------

def succ_s(eqS,s,T):
    # eqS : fonction d'egalite sur les etats
    # s : etat
    # T : liste de transitions
    r = []
    for (s1,_,s2) in T:
        if eqS(s,s1):
            r = ajout(eqS,s2,r)
    return r


# Liste des etats accessibles a partir d'un ensemble d'etats
# ----------------------------------------------------------

def reachable(eqS,Es,T):
    # eqS : fonction d'egalite sur les etats
    # Es : liste d'etats
    # T : liste de transitions
    _eqES = make_eq_set(eqS)
    def _add_reach(r):
        ar = []
        for qr in r:
            ar = union(eqS,ar,succ_s(eqS,qr,T))
        return union(eqS,r,ar)
    return fixpoint_from(_eqES,_add_reach,Es)


# Liste des etats accessibles a partir d'un etat initial
# ----------------------------------------------------------

def reach_A(A):
    # A : automate fini
    (listeS, listeT, listeI, listeF, eqs) = A
    return reachable(eqs, listeI, listeT)

# Liste des etats a partir desquels un etat acceptant est accessible
# ------------------------------------------------------------------

def co_reach_A(A):
    # A : automate fini
    (listeS, listeT, listeI, listeF, eqs) = A
    TA_s=[]
    for s in listeS:
        if intersection(eqs, reachable(eqs, [s], listeT), listeF) != []:
            TA_s+=[s]
    return TA_s

# Acceptation d'un mot
#---------------------

def accept_word_finite_aut(A,w):
    # A : automate fini
    # w : liste de symboles (mot)
    (S,T,I,F,eqS)=A
    def _aux(sa,wa):
        if wa==[]:
            return intersection(eqS,eps_cl(eqS,sa,T),F) !=[]
        else:
            trans = reach_from(eqS,sa,wa[0],T)
            for st in trans:
                if _aux(st,wa[1:]):
                    return True
            return False
    for si in I:
        if _aux(si,w):
            return True
    return False

