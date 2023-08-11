from Turing import *
from ensembles import * 
from machines3b import *


print("\n\n----------------------------------------------\n\n")
print("Test 1 : exec_MT_k")
print("--------------------------------------------------")
(b,T,L) = exec_MT_k(M2_palin_bin,2,[["Z"],["Z"]],[0,0])
assert b== True, "Erreur dans le langage accepte"
assert T == [0,0], "Erreur dans les positions des testes de lectures sur les bandes"
assert L == [["Z","Z"],["Z","Z"]], "Erreur dans le contenu des bandes"

print("\n\n----------------------------------------------\n\n")
print("Test 2 : exec_MT_k")
print("--------------------------------------------------")
(b,T,L) = exec_MT_k(M2_palin_bin,2,[["0","Z"],["Z"]],[0,0])
assert b == True, "Erreur dans le langage accepte"
assert T == [2,0], "Erreur dans les positions des tetes de lecture sur les bandes"
assert L == [["Z","X","Z"], ["Z","X","Z"]], "Erreur dans le contenu des bandes"

print("\n\n----------------------------------------------\n\n")
print("Test 3 : exec_MT_k")
print("--------------------------------------------------")
(b,T,L) = exec_MT_k(M2_palin_bin,2,[["1","1","1","0","1","Z"],["Z"]],[0,0])
assert b == False, "Erreur dans le langage accepte"
assert T == [2,3], "Erreur dans les positions des tetes de lecture sur les bandes"
assert L == [["Z","X","1","1","0","1","Z"],["1","1","1","0","X","Z"]], "Erreur dans le contenu des bandes"


print("\n\n----------------------------------------------\n\n")
print("Test 4 : exec_MT_k")
print("--------------------------------------------------")
(b,T,L) = exec_MT_k(M2_palin_bin,2,[["1","0","1","0","1","Z"],["Z"]],[0,0])
assert b == True, "Erreur dans le langage accepte"
assert T == [6,0], "Erreur dans les positions des tetes de lecture sur les bandes"
assert L == [["Z","X","X","X","X","X","Z"],["Z","X","X","X","X","X","Z"]], "Erreur dans le contenu des bandes"
