#Fonctions utiles pour les tests

from ensembles import *
from automates_finis_det import *

class Special:
    pass



def make_eq_special(eq):
    def _eq_special(x,y):
        if isinstance(x,Special) and isinstance(y,Special):
            return x==y
        else:
            if (not isinstance(x,Special)) and (not isinstance(y,Special)):
                return eq(x,y)
            else:
                return False
    return _eq_special


def eq_pair(eq1, eq2, x1, x2):
    (s1, t1)=x1
    (s2, t2)=x2
    return eq1(s1, s2) and eq2(t1, t2)



def make_eq_pair(eq1, eq2):
    def _eq_pair(x1,x2):
        return eq_pair(eq1,eq2,x1,x2)
    return _eq_pair

def eq_trans(eqE, eqA, t1, t2):
    (x1, a1, z1)=t1
    (x2, a2, z2)=t2
    return eqE(x1, x2) and eqA(a1, a2) and eqE(z1, z2)

def make_eq_trans(eqE, eqA):
    """Construit la relation d'egalite sur les transitions a partir de la
    relation d'egalite entre etats eqE et de la relation d'egalite entre lettres
    eqA"""
    def _eq_trans(t1, t2):
        return eq_trans(eqE, eqA, t1, t2)
    return _eq_trans



def comp_auto(eqA, alph,auto):
    """
    Renvoie l'automate complete de auto par rapport a l'alphabet alpha
    """
    (S,T,I,F, eqE)=auto
    d = []
    st = Special()
    for s in S:
        """difference entre les lettres etiquetant les transitions sortantes de s
        et l'alphabet"""
        la = diff_set(eqA,alph,label_from(eqE,s,T))
        for x in la:
            d = [(s,x,st)] + d
    d = T + [(st,x,st) for x in alph] + d
    S_comp = S+ [st]
    T_comp = d
    I_comp = I
    F_comp = F
    auto_comp = (S_comp, T_comp, I_comp, F_comp, make_eq_special(eqE))
    return auto_comp

def is_complet (eqA, alph, auto):
    """ Renvoie True si l'automate auto est complet par rapport a l'alphabet
    alpha
    eqA egalite entre les lettres de l'alphabet
    """
    (S,T,I,F, eqE)=auto
    for s in S:
        """difference entre les lettres etiquetant les transitions sortantes de s
        et l'alphabet"""
        la = diff_set(eqA,alph,label_from(eqE,s,T))
        if not eq_set(eqA,la,[]):
            return False
    return True

def complementaire (eqA, alph, auto):
    """
    Renvoie l'automate complementaire de auto par rapport a l'alphabet alph
    """
    #si l'automate n'est pas deterministe, on le determinise
    dauto=auto
    if not is_deterministic(auto):
        dauto=make_det(auto)
        #eqE=make_eq_set(eqE)

    #si l'automate n'est pas complet, on le complete
    comp = dauto
    if not is_complet(eqA, alph, dauto):
        comp = comp_auto(eqA, alph, dauto)
        #eqE= make_eq_special(eqE)
    (S,T,I,F,eq)=comp
    S_compl = S
    T_compl = T
    I_compl = I
    F_compl = diff_set(eq, S, F)
    return (S_compl, T_compl, I_compl, F_compl,eq)



def dest_state(eqE, T, s, a):
    ls = [z for (x,y,z) in T if eqE(x,s) and a==y]
    return ls

def intersection_auto (eqA, auto1, auto2):
    (S1,T1,I1,F1, eqE1)=auto1
    (S2,T2,I2,F2, eqE2)=auto2
    alph = union(eqA,label_from_set(eqE1, S1, T1), label_from_set(eqE2, S2, T2))
    I = [(i1,i2) for i1 in I1 for i2 in I2]
    S = I
    T=[]
    eqE= make_eq_pair(eqE1, eqE2)
    for (s1,s2) in S:
        for a in alph:
            if a in label_from(eqE1, s1, T1) and a in label_from(eqE2, s2, T2):
                Z1=dest_state(eqE1, T1, s1, a)
                Z2=dest_state(eqE2, T2, s2, a)
                S=union(eqE, S,[(z1,z2) for z1 in Z1  for z2 in Z2])
                T=union(make_eq_trans(eqE, eqA), T,[((s1,s2),a,(z1,z2)) for z1 in Z1 for z2 in Z2])

    F=[(s1,s2) for (s1,s2) in S if is_in(eqE1,s1,F1) and is_in(eqE2,s2,F2)]
    return (S,T,I,F, eqE)


def test_vide(auto):
    (S,T,I,F,eqE)=auto
    alpha= label_from_set(eqE, S, T)
    working_list = I
    visited_states= working_list
    while working_list != []:
        list_succ=[]
        for s in working_list:
            for a in alpha:
                list_succ = union(eqE, list_succ,reach_from(eqE, s, a, T))
        working_list = diff_set(eqE,list_succ, visited_states)
        visited_states= union(eqE, visited_states, list_succ)
        list_succ=[]
    return eq_set(eqE,intersection(eqE, visited_states, F),[])






def test_equal_auto (auto1, auto2) :
    """
    Teste l'egalite de langages de deux automates par vide de la difference symmetrique
    """
    (S1,T1,I1,F1, eqE1)=auto1
    (S2,T2,I2,F2,eqE2)=auto2
    alphabet1 = label_from_set(eqE1, S1, T1)
    alphabet2 = label_from_set(eqE2, S2, T2)
    alphabet = union(eq_atom, alphabet1, alphabet2)
    auto1c = complementaire(eq_atom, alphabet, auto1)
    auto2c = complementaire(eq_atom, alphabet, auto2)
    auto1moins2 = intersection_auto(eq_atom, auto1,auto2c)
    auto2moins1 = intersection_auto(eq_atom, auto2,auto1c)
    return test_vide(auto1moins2) and \
    test_vide(auto2moins1)

# a0 = ([0,1,2,3,4], [(0,None,2),(0,None,3),(1,"b",1),(1,"a",2),(1,"b",3), \
#  (1,"b",4),(3,"a",1),(3,"b",1),(3,None,2),(4,"a",0),(4,None,0)],\
#     [0],\
#     [2])
#
# a1 = ([0,1], [(0,"a",1),(0,"b",1),(1,None,0)], [0],\
#                [1])
# a2 = ([0,1], [(0, "a", 0), (0, "b", 1)], [0], [0])
# a3=([0,1], [(1, "a", 1), (1, None, 0)], [0], [1])

#a= make_det(eq_atom, a0)
#print(a);
#eq_s = make_eq_set(eq_atom)
#print(eq_s([0,1], [1,0]))
#ac=complementaire(make_eq_set(eq_atom), eq_atom,["a","b"], a)
#print("complementaire")
#print(ac)
#print(is_complet(make_eq_set(eq_atom), eq_atom, ["a","b"], a))

#print(is_complet(eq_atom, eq_atom, ["a","b"],a0))
#a= comp_auto(eq_atom, eq_atom, ["a","b"], a0)
#print(a)
#print(is_complet(eq_atom, eq_atom,["a","b"], a))
#b = complementaire(eq_atom, eq_atom, ["a","b"], a0)
#print(b)
#print("\n")
#print("intersection\n")
#c=intersection_auto(eq_atom,eq_atom, eq_atom, a2,a1)
#print(c)

#print(test_equal_auto(eq_atom, eq_atom, a0, a1))
#print(test_equal_auto(eq_atom, eq_atom, a0, a0))
