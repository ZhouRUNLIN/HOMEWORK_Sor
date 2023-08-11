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

a1 = ([0,1], [(0,"a",1),(0,"b",1),(1,None,0)], [0],\
               [1], eq_atom)
a2 = ([0,1], [(0, "a", 0), (0, "b", 1)], [0], [0], eq_atom)
a3=([0,1], [(1, "a", 1), (1, None, 0)], [0], [1], eq_atom)

a4=([0,1], [(0,"a",0), (0, "a", 1), (0, "b", 0)], [0], [1], eq_atom)

print("\n\n----------------------------------------------\n\n")
print("Test 1 : Fonction make_det")
print("---------------------")

assert is_deterministic (make_det(ex_A)), "Erreur automate non deterministe"
assert test_equal_auto(ex_A, make_det(ex_A)), "Erreur automate non equivalent"

print("\n\n----------------------------------------------\n\n")
print("Test 2 : Fonction make_det")
print("---------------------")

assert is_deterministic (make_det(ex_B)), "Erreur automate non deterministe"
assert test_equal_auto(ex_B, make_det(ex_B)), "Erreur automate non equivalent"

print("\n\n----------------------------------------------\n\n")
print("Test 3 : Fonction make_det")
print("---------------------")

assert is_deterministic (make_det(ex_C)), "Erreur automate non deterministe"
assert test_equal_auto(ex_C, make_det(ex_C)), "Erreur automate non equivalent"

print("\n\n----------------------------------------------\n\n")
print("Test 4 : Fonction make_det")
print("---------------------")

assert is_deterministic (make_det(a0)), "Erreur automate non deterministe"
assert test_equal_auto(a0, make_det(a0)), "Erreur automate non equivalent"

print("\n\n----------------------------------------------\n\n")
print("Test 5 : Fonction make_det")
print("---------------------")

assert is_deterministic (make_det(a1)), "Erreur automate non deterministe"
assert test_equal_auto(a1, make_det(a1)), "Erreur automate non equivalent"

print("\n\n----------------------------------------------\n\n")
print("Test 6 : Fonction make_det")
print("---------------------")

#test sur automate deja deterministe
assert is_deterministic (make_det(a2)), "Erreur automate non deterministe"
assert test_equal_auto(a2, make_det(a2)), "Erreur automate non equivalent"

print("\n\n----------------------------------------------\n\n")
print("Test 7 : Fonction make_det")
print("---------------------")

assert is_deterministic (make_det(a3)), "Erreur automate non deterministe"
assert test_equal_auto(a3, make_det(a3)), "Erreur automate non equivalent"

print("\n\n----------------------------------------------\n\n")
print("Test 8 : Fonction make_det")
print("---------------------")

#test non deterministe sans eps
assert is_deterministic (make_det(a4)), "Erreur automate non deterministe"
assert test_equal_auto(a4, make_det(a4)), "Erreur automate non equivalent"

print("---------------------")
print("Test fonction make_det : OK")
print("\n\n----------------------------------------------\n\n")
