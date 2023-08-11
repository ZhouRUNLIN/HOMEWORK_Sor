from Turing import *


print("\n\n----------------------------------------------\n\n")
print("Test 1 : Machine Misneg")
print("---------------------------")
(b1,i1,L1)=exec_MT_1(M_isneg,["0","0","1","0","Z"],0)
assert b1==False, "Erreur dans l'etat final de la machine"

print("\n\n----------------------------------------------\n\n")
print("Test 2 : Machine Misneg")
print("---------------------------")
(b1,i1,L1)=exec_MT_1(M_isneg,["1","0","1","0","1","Z"],0)
assert b1==True, "Erreur dans l'etat final de la machine"

print("\n\n----------------------------------------------\n\n")
print("Test 3 : Machine Misneg")
print("---------------------------")
(b1,i1,L1) = exec_MT_1(M_isneg,["1","0","1","0","0","Z"],0)
assert b1 == False, "Erreur dans l'etat final de la machine"

print("\n\n----------------------------------------------\n\n")
print("Test 4 : Machine Misneg")
print("---------------------------")
(b1,i1,L1) = exec_MT_1(M_isneg,["0","0","1","0","1","Z"],0)
assert b1 == True, "Erreur dans l'etat final de la machine"

