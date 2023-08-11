
#=============================================================#
# UE Calculabilite L3                                         #
# TME Grammaires Hors Contexte : langage engendre par une GHC #
# Mathieu.Jaume@lip6.fr                                       #
#=============================================================#

from ensembles import *

# Grammaires hors-contexte
#-------------------------

# G = (nt,t,r,si,eqnt)
# nt : symboles non terminaux 
# t  : symboles terminaux
# r  : liste de productions 
#      production (nt,lnt) ou lnt est une liste non vide de listes de symboles 
#      ex : S --> aS | S2S2 | epsilon est represente par 
#           (S,[[a,S],[S2,S2],[]])
# si : symbole de nt initial
# eqnt : egalite sur les non terminaux
# Hypothese : eq_atom permet de tester l'egalite sur les terminaux
# Hypothese : r contient au plus une paire dont la premiere composante est un
#             non terminal donne

# Exemple : GHC G1
# ----------------

g1_nt = ["S","A1","A2","A3","A4","A5","A6","A7","A8","A9"]
g1_t = ["a","b","c"]
g1_r = [("S",[["A1"],["A2"],["A3"]]),\
        ("A1",[["a","A1","A1"],["a","A2","A4"],["a","A3","A7"],["A4"]]),\
        ("A2",[["a","A1","A2"],["a","A2","A5"],["a","A3","A8"],["A5"]]),\
        ("A3",[["a","A1","A3"],["a","A2","A6"],["a","A3","A9"],["A6"]]),\
        ("A4",[["b","A4"]]),\
        ("A5",[["b","A5"]]),\
        ("A6",[["b","A6"],[]]),\
        ("A9",[["c"]])]
g1_s = "S"
g1_g = (g1_nt,g1_t,g1_r,g1_s,eq_atom)


# Egalite sur les parties droites de productions

def make_eq_prod(nt,eqnt):
    # nt : liste de symboles non terminaux
    # eqnt : egalite sur les non terminaux
    def _eq_prod(p1,p2):
        if p1==[] or p2==[]:
            return p1==[] and p2==[]
        else:
            b1 = is_in(eqnt,p1[0],nt)
            b2 = is_in(eqnt,p2[0],nt)
            if b1 and b2:
                return eqnt(p1[0],p2[0]) and _eq_prod(p1[1:],p2[1:])
            else:
                if ((not b1) and b2) or (b1 and (not b2)):
                    return False
                else:
                    return eq_atom(p1[0],p2[0]) and _eq_prod(p1[1:],p2[1:])
    return _eq_prod


# Ajout de la production ns --> nl a l'ensemble r

def add_prod(ns,nl,nt,r,eqnt):
    # ns : symbole non terminal
    # nl : partie droite de production
    # nt : symboles non terminaux
    # r : liste de productions
    # eqnt : egalite sur les non terminaux
    new_nr = []
    r_aux = r
    while not(r_aux == []):
        s,ls = r_aux[0]
        if eqnt(s,ns):
            ls = ajout(make_eq_prod(nt,eqnt),nl,ls)
            new_nr = new_nr + [(s,ls)] + r_aux[1:]
            return new_nr
        else:
            new_nr = new_nr + [(s,ls)]
            r_aux = r_aux[1:]
    new_nr = new_nr + [(ns,[nl])]
    return new_nr



# Liste des parties droites des productions d'un symbole non-terminal

def prods_s(r,eqnt,x):
    # r : ensemble de production
    # eqnt : egalite sur les non terminaux
    # x : symbole non terminal
    for s,ls in r:
        if eqnt(x,s):
            return ls
    return []
