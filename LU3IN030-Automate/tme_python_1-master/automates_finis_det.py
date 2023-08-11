#====================================#
# UE Calculabilite L3 / M1 SFTR      #
# TME Automates finis deterministes  #
# Mathieu.Jaume@lip6.fr              #
#====================================#

from automates_finis import *

# Liste des transitions de T dont l'origine est l'etat s
# ------------------------------------------------------

def lt_from_s(eqS,s,T):
    # eqS : fonction d'egalite sur les etats
    # s : etat
    # T : liste de transitions
    return [(s1,l,s2) for (s1,l,s2) in T if eqS(s1,s)]

# Liste des labels presents sur les transitions issues d'un etat s
# ----------------------------------------------------------------

def label_from(eqS,s,T):
    # eqS : fonction d'egalite sur les etats
    # s : etat
    # T : liste de transitions
    R = []
    for (si,l,sf) in T:
        if eqS(si,s) and l != None:
            R = ajout(eq_atom,l,R)
    return R

# Liste des labels presents sur les transitions issues d'un ensemble d'etats
# --------------------------------------------------------------------------

def label_from_set(eqS,S,T):
    # eqS : fonction d'egalite sur les etats
    # S : liste d'etats
    # T : liste de transitions
    R = []
    for s in S:
        R = union(eq_atom,label_from(eqS,s,T),R)
    return R

# Automates finis deterministes
#------------------------------

# determine si la relation de transition a partir d'un etat s est
# fonctionnelle et ne contient aucune epsilon-transition
# ---------------------------------------------------------------

def lt_from_s_deterministic(T):
    # T : liste de transitions
    def _aux(L):
        if len(L)==0:
            return True
        else:
            if L[0] == None or L[0] in L[1:]:
                return False
            else:
                return _aux(L[1:])
    return _aux([l for (_,l,_) in T])


# determine si un automate est deterministe
# -----------------------------------------

def is_deterministic(A):
    # A : automate fini
    (listeS, listeT, listeI, listeF, eqs) = A
    for sommet in listeS:
        if lt_from_s_deterministic(lt_from_s(eqs, sommet, listeT)) == False:
            return False
    return True

# Determinisation
#----------------

# Egalite entre transitions d'un automate
# ---------------------------------------

def eq_trans(eqS,t1,t2):
    (si1,l1,sf1) = t1
    (si2,l2,sf2) = t2
    return l1==l2 and eqS(si1,si2) and eqS(sf1,sf2)

def make_eq_trans(eqS):
    def _eq_trans(t1,t2):
        return eq_trans(eqS,t1,t2)
    return _eq_trans

# Determinisation d'un automate fini avec epsilon-transitions
# -----------------------------------------------------------

def make_det(A):
    # A : automate fini
    (listeS, listeT, listeI, listeF, eqs) = A
    fini=[]
    pasfini=[eps_cl_set(eqs, listeI, listeT)]
    T=[]

    while pasfini != []:
        ptr=pasfini[0]
        fini+=[ptr]
        pasfini=pasfini[1:]
        for l in label_from_set(eqs, ptr, listeT):
            next=[]
            for s in ptr:
                next=union(eqs, reach_from(eqs, s, l, listeT), next)
            if not is_in(eqs, next, fini):
                pasfini=ajout(eqs, next, pasfini)
                T=ajout(eqs, (ptr, l, next), T)
    
    F=[]
    for i in fini:
        for j in i:
            if is_in(eqs, j, listeF):
                F=ajout(eqs, i, F)
                continue
    return (fini, T, eps_cl_set(eqs, listeI, listeT), F, eqs)
 


