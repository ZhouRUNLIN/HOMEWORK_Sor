from Turing import *


print("\n\n----------------------------------------------\n\n")
print("Test 1 : Machine M_ex2")
print("---------------------------")
(b1,i1,L1)=exec_MT_1(M_ex2, ["A","a","b","a","A","b"],0)
assert b1==False, "Erreur dans l'etat final de la machine"

print("\n\n----------------------------------------------\n\n")
print("Test 2 : Machine M_ex2")
print("---------------------------")
(b1,i1,L1)=exec_MT_1(M_ex2, ["a","a","b","a","a","b"],0)
assert b1==False, "Erreur dans l'etat final de la machine"

print("\n\n----------------------------------------------\n\n")
print("Test 3 : Machine M_ex2")
print("---------------------------")
(b1,i1,L1) = exec_MT_1(M_ex2,["Z"],0)
assert b1 == True, "la machine devrait accepter le mot vide"

print("\n\n----------------------------------------------\n\n")
print("Test 4 : Machine M_ex2")
print("---------------------------")
(b1,i1,L1) = exec_MT_1(M_ex2,["a","b","a","b","a","a","Z"],0)
assert b1 == False, "Erreur dans l'etat final de la machine"


print("\n\n----------------------------------------------\n\n")
print("Test 5 : Machine M_ex2")
print("---------------------------")
(b1,i1,L1) = exec_MT_1(M_ex2,["a","b","b","a","b","a","Z"],0)
assert b1 == True, "Erreur dans l'etat final de la machine"


print("\n\n----------------------------------------------\n\n")
print("Test 6 : Machine M_ex2")
print("---------------------------")
(b1,i1,L1) = exec_MT_1(M_ex2,["b","b","a","b","a","a","Z"],0)
assert b1 == True, "Erreur dans l'etat final de la machine"


print("\n\n----------------------------------------------\n\n")
print("Test 7 : Machine M_ex2")
print("---------------------------")
(b1,i1,L1) = exec_MT_1(M_ex2,["b","b","a","Z"],0)
assert b1 == False, "Erreur dans l'etat final de la machine"