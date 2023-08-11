from ensembles import *
from automates_finis import *

ex_B = ([0,1,2,3],\
        [(0,"a",1),(1,None,2),(1,"b",2),(2,None,3),(3,"a",3), \
         (3,None ,1)],\
        [0],[2], eq_atom)

(exB_S,exB_T,exB_I,exB_F, exB_eq) = ex_B

ex_C=([0,1,2,3,4],\
[(0,None,3), (0,"a",1), (1,"a",2),(1,"b",0),(2,"b",1),(3,"a",4),(4,None, 2),(4,"b",3)],\
[0],[1,2,3,4], eq_atom)

(exC_S, exC_T, exC_I, exC_F, exC_eq) = ex_C

a0 = ([0,1,2,3,4], [(0,None,2),(0,None,3),(1,"b",1),(1,"a",2),(1,"b",3), \
 (1,"b",4),(3,"a",1),(3,"b",1),(3,None,2),(4,"a",0),(4,None,0)],\
    [0],\
    [2], eq_atom)

(ex0_S, ex0_T, ex0_I, ex0_F, ex0_eq) = a0

a1 = ([0,1], [(0,"a",1),(0,"b",1),(1,None,0)], [0],\
               [1], eq_atom)
a2 = ([0,1], [(0, "a", 0), (0, "b", 1)], [0], [0], eq_atom)

(ex2_S, ex2_T, ex2_I, ex2_F, ex2_eq) = a2

a3=([0,1], [(1, "a", 1), (1, None, 0)], [0], [1], eq_atom)

print("\n\n----------------------------------------------\n\n")
print("Test 1 : Fonction eps_cl")
print("---------------------")

assert eq_set(eq_atom, eps_cl(ex_eqS, 0, ex_T), [2, 3, 0]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl(ex_eqS, 1, ex_T), [1]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl(ex_eqS, 4, ex_T), [2, 3, 0, 4]), "Erreur mauvaise fermeture"

print("\n\n----------------------------------------------\n\n")
print("Test 2 : Fonction eps_cl")
print("---------------------")


assert eq_set(eq_atom, eps_cl(exB_eq, 0, exB_T), [0]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl(exB_eq, 1, exB_T), [1,2,3]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl(exB_eq, 2, exB_T), [1,2,3]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl(exB_eq, 3, exB_T), [1,2,3]), "Erreur mauvaise fermeture"

print("\n\n----------------------------------------------\n\n")
print("Test 3 : Fonction eps_cl")
print("---------------------")


assert eq_set(eq_atom, eps_cl(exC_eq, 0, exC_T), [0,3]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl(exC_eq, 1, exC_T), [1]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl(exC_eq, 2, exC_T), [2]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl(exC_eq, 3, exC_T), [3]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl(exC_eq, 4, exC_T), [2,4]), "Erreur mauvaise fermeture"

print("\n\n----------------------------------------------\n\n")
print("Test 4 : Fonction eps_cl")
print("---------------------")

assert eq_set(eq_atom, eps_cl(ex0_eq, 0, ex0_T), [0,2,3]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl(ex0_eq, 1, ex0_T), [1]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl(ex0_eq, 2, ex0_T), [2]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl(ex0_eq, 3, ex0_T), [3,2]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl(ex0_eq, 4, ex0_T), [0,2,3,4]), "Erreur mauvaise fermeture"

print("\n\n----------------------------------------------\n\n")
print("Test 5 : Fonction eps_cl")
print("---------------------")
#test sans eps
assert eq_set(eq_atom, eps_cl(ex2_eq, 0, ex2_T), [0]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl(ex2_eq, 1, ex2_T), [1]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl(ex2_eq, 2, ex2_T), [2]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl(ex2_eq, 3, ex2_T), [3]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl(ex2_eq, 4, ex2_T), [4]), "Erreur mauvaise fermeture"

print("---------------------")
print("Test fonction eps_cl : OK")
print("\n\n----------------------------------------------\n\n")

print("\n\n----------------------------------------------\n\n")
print("Test 6 : Fonction eps_cl_set")
print("---------------------")

assert eq_set(eq_atom, eps_cl_set(ex_eqS, [0,1,4], ex_T), [1, 2, 3, 0, 4]), "Erreur mauvaise fermeture d'ensemble"


print("\n\n----------------------------------------------\n\n")
print("Test 7 : Fonction eps_cl_set")
print("---------------------")


assert eq_set(eq_atom, eps_cl_set(exB_eq, [0,1,2,3], exB_T), [0,1,2,3]), "Erreur mauvaise fermeture d'ensemble"


print("\n\n----------------------------------------------\n\n")
print("Test 8 : Fonction eps_cl_set")
print("---------------------")


assert eq_set(eq_atom, eps_cl_set(exC_eq, [0,1,2,3,4], exC_T), [0,1,2,3,4]), "Erreur mauvaise fermeture d'ensemble"
assert eq_set(eq_atom, eps_cl_set(exC_eq, [1,2,4], exC_T), [1,2,4]), "Erreur mauvaise fermeture d'ensemble"
assert eq_set(eq_atom, eps_cl_set(exC_eq, [0,4], exC_T), [0,2,3,4]), "Erreur mauvaise fermeture d'ensemble"
assert eq_set(eq_atom, eps_cl_set(exC_eq, [3], exC_T), [3]), "Erreur mauvaise fermeture d'ensemble"
assert eq_set(eq_atom, eps_cl_set(exC_eq, [4], exC_T), [2,4]), "Erreur mauvaise fermeture d'ensemble"

print("\n\n----------------------------------------------\n\n")
print("Test 9 : Fonction eps_cl_set")
print("---------------------")

assert eq_set(eq_atom, eps_cl_set(ex0_eq, [0], ex0_T), [0,2,3]), "Erreur mauvaise fermeture d'ensemble"
assert eq_set(eq_atom, eps_cl_set(ex0_eq, [0,1,2], ex0_T), [0,1,2,3]), "Erreur mauvaise fermeture d'ensemble"
assert eq_set(eq_atom, eps_cl_set(ex0_eq, [2,4], ex0_T), [0,2,3,4]), "Erreur mauvaise fermeture d'ensemble"


print("\n\n----------------------------------------------\n\n")
print("Test 10 : Fonction eps_cl_set")
print("---------------------")
#test sans eps
assert eq_set(eq_atom, eps_cl_set(ex2_eq, [0], ex2_T), [0]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl_set(ex2_eq, [0,1], ex2_T), [0,1]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl_set(ex2_eq, [2,3], ex2_T), [2,3]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl_set(ex2_eq, [0,1,3], ex2_T), [0,1,3]), "Erreur mauvaise fermeture"
assert eq_set(eq_atom, eps_cl_set(ex2_eq, [1,4,2], ex2_T), [1,2,4]), "Erreur mauvaise fermeture"

print("---------------------")
print("Test fonction eps_cl_set : OK")
print("\n\n----------------------------------------------\n\n")
