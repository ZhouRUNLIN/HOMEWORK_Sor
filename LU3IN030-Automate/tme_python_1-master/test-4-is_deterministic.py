from ensembles import *
from automates_finis import *
from automates_finis_det import *
from test_fonctions import *

ex_B = ([0,1,2,3],\
        [(0,"a",1),(1,None,2),(1,"b",2),(2,None,3),(3,"a",3), \
         (3,None ,1)],\
        [0],[2], eq_atom)

(exB_S,exB_T,exB_I,exB_F, exB_eq) = ex_B

ex_C=([0,1,2,3,4],\
[(0,None,3), (0,"a",1), (1,"a",2),(1,"b",0),(2,"b",1),(3,"a",4),(4,None, 2),(4,"b",3)],\
[0],[1,2,3,4], eq_atom)

a0 = ([0,1,2,3,4], [(0,None,2),(0,None,3),(1,"b",1),(1,"a",2),(1,"b",3), \
 (1,"b",4),(3,"a",1),(3,"b",1),(3,None,2),(4,"a",0),(4,None,0)],\
    [0],\
    [2], eq_atom)

a1 = ([0,1], [(0,"a",1),(0,"a",0),(1,"a",0)], [0],\
               [1], eq_atom)
a2 = ([0,1], [(0, "a", 0), (0, "b", 1)], [0], [0], eq_atom)
a3=([0,1], [(1, "a", 1), (1, None, 0)], [0], [1], eq_atom)

a4=([0,1], [(0,"a",0), (1, "a", 1), (0, "b", 0)], [0], [1], eq_atom)

print("\n\n----------------------------------------------\n\n")
print("Test 1 : Fonction is_deterministic")
print("---------------------")
assert is_deterministic(ex_B) == False, "Erreur test determinisme"

print("\n\n----------------------------------------------\n\n")
print("Test 2 : Fonction is_deterministic")
print("---------------------")
assert is_deterministic(ex_C) == False, "Erreur test determinisme"

print("\n\n----------------------------------------------\n\n")
print("Test 3 : Fonction is_deterministic")
print("---------------------")
assert is_deterministic(a0) == False, "Erreur test determinisme"

print("\n\n----------------------------------------------\n\n")
print("Test 4 : Fonction is_deterministic")
print("---------------------")
assert is_deterministic(a1) == False, "Erreur test determinisme"

print("\n\n----------------------------------------------\n\n")
print("Test 5 : Fonction is_deterministic")
print("---------------------")
assert is_deterministic(a2) == True, "Erreur test determinisme"


print("\n\n----------------------------------------------\n\n")
print("Test 6 : Fonction is_deterministic")
print("---------------------")
assert is_deterministic(a3) == False, "Erreur test determinisme"

print("\n\n----------------------------------------------\n\n")
print("Test 7 : Fonction is_deterministic")
print("---------------------")
assert is_deterministic(a4) == True, "Erreur test determinisme"


print("\n\n----------------------------------------------\n\n")
print("Tests is_deterministic OK")
print("---------------------------------------------------")
