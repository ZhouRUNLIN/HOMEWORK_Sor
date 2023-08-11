from ensembles import *
from automates_finis import *

ex_B = ([0,1,2,3],\
        [(0,"a",1),(1,None,2),(1,"b",2),(2,None,3),(3,"a",3), \
         (3,None ,1)],\
        [0],[2], eq_atom)

(exB_S,exB_T,exB_I,exB_F, exB_eq) = ex_B



a1 = ([0,1,2,3], [(0,"a",1),(0,"b",1),(1,None,2), (3,"a",2)], [0],\
               [2], eq_atom)
a2 = ([0,1,2], [(0, "a", 0), (0, "b", 1)], [0], [0], eq_atom)

(ex2_S, ex2_T, ex2_I, ex2_F, ex2_eq) = a2

#etat initial isole
a3=([0,1,2], [(1, "a", 1), (1, None, 0)], [2], [1], eq_atom)

print("\n\n----------------------------------------------\n\n")
print("Test 1 : Fonction reach_A")
print("---------------------")

assert eq_set(eq_atom, reach_A(ex_B), [0,1,2, 3]), "Erreur accessibilite"


print("\n\n----------------------------------------------\n\n")
print("Test 2 : Fonction reach_A")
print("---------------------")


assert eq_set(eq_atom, reach_A(a1), [0,1,2]), "Erreur accessibilite"


print("\n\n----------------------------------------------\n\n")
print("Test 3 : Fonction reach_A")
print("---------------------")


assert eq_set(eq_atom, reach_A(a2), [0,1]), "Erreur accessibilite"


print("\n\n----------------------------------------------\n\n")
print("Test 4 : Fonction reach_A")
print("---------------------")

assert eq_set(eq_atom, reach_A(a3), [2]), "Erreur accessibilite"


print("---------------------")
print("Test fonction reach_A : OK")
print("\n\n----------------------------------------------\n\n")

