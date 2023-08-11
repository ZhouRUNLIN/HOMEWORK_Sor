from Turing import *

l_ex2 = [((0,"a"),(1,"X","R")), ((0,"b"),(2,"X","R")), \
         ((0,"Z"),(4,"Z","R")), ((1,"a"),(1,"a","R")), \
         ((1,"Y"),(1,"Y","R")), ((1,"b"),(3,"Y","L")), \
         ((2,"b"),(2,"b","R")), ((2,"Y"),(2,"Y","R")), \
         ((2,"a"),(3,"Y","L")), ((3,"a"),(3,"a","L")), \
         ((3,"b"),(3,"b","L")), ((3,"Y"),(3,"Y","L")), \
         ((3,"X"),(0,"X","R")), ((0,"Y"),(0,"Y","R"))]

M_ex2 =(l_ex2,0,4,5)

print("\n\n----------------------------------------------\n\n")
print("Test 1 : Fonction exec_MT_1")
print("---------------------------")
(b1,i1,L1)=exec_MT_1(M_ex1, ["A","a","b","a","a","B"],0)
assert b1==True, "Erreur dans l'etat final de la machine"
assert i1==6, "Erreur dans la position de la tete de lecture"
assert L1==["A","b","a","b","b","B","Z"], "Erreur dans la bande a la fin du calcul"

print("\n\n----------------------------------------------\n\n")
print("Test 2 : Fonction exec_MT_1")
print("---------------------------")
(b1,i1,L1)=exec_MT_1(M_ex2, ["a","a","b","a","a","b"],0)
assert b1==False, "Erreur dans l'etat final de la machine"
assert i1==6, "Erreur dans la position de la tete de lecture"
assert L1==["X","X","Y","X","a","Y","Z"], "Erreur dans la bande a la fin du calcul"

print("\n\n----------------------------------------------\n\n")
print("Test 3 : Fonction exec_MT_1")
print("---------------------------")

(b1,i1,L1) = exec_MT_1(M_ex2,["Z"],0)
assert b1 == True, "la machine devrait accepter le mot vide"
assert i1 == 1, "Erreur dans la position de la tete de lecture"
assert L1 == ["Z","Z"], "Erreur dans la bande a la fin du calcul"

print("\n\n----------------------------------------------\n\n")
print("Test 4 : Fonction exec_MT_1")
print("---------------------------")

(b1,i1,L1) = exec_MT_1(M_ex2,["a","b","a","b","a","a","Z"],0)
assert b1 == False, "Erreur dans l'etat final de la machine"
assert i1 == 6, "Erreur dans la position de la tete de lecture"
assert L1 == ["X","Y","X","Y","X","a","Z"]

print("\n\n----------------------------------------------\n\n")
print("Test 5 : Fonction exec_MT_1")
print("---------------------------")

(b1,i1,L1) = exec_MT_1(M_ex2,["a","b","b","a","b","a","Z"],0)
assert b1 == True, "Erreur dans l'etat final de la machine"
assert i1 == 7, "Erreur dans la position de la tete de lecture"
assert L1 == ["X","Y","X","Y","X","Y","Z","Z"]

print("\n\n----------------------------------------------\n\n")
print("Test 6 : Fonction exec_MT_1")
print("---------------------------")

(b1,i1,L1) = exec_MT_1(M_ex2,["b","b","a","b","a","a","Z"],0)
assert b1 == True, "Erreur dans l'etat final de la machine"
assert i1 == 7, "Erreur dans la position de la tete de lecture"
assert L1 == ["X","X","Y","X","Y","Y","Z","Z"]

print("\n\n----------------------------------------------\n\n")
print("Test 7 : Fonction exec_MT_1")
print("---------------------------")

(b1,i1,L1) = exec_MT_1(M_ex2,["b","b","a","Z"],0)
assert b1 == False, "Erreur dans l'etat final de la machine"
assert i1 == 3, "Erreur dans la position de la tete de lecture"
assert L1 == ["X","X","Y","Z"]

# 