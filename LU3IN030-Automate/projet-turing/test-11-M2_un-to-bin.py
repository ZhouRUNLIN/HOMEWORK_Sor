from Turing import *
from ensembles import * 
#from machines3b import *



print("\n\n----------------------------------------------\n\n")
print("Test 1 : exec_MT_k")
print("--------------------------------------------------")
(b,T,L) = exec_MT_k(M2_un_to_bin,2,[["$"],["Z"]],[0,0])
assert b == True, "Erreur dans le langage accepte"
assert T == [1,1], "Erreur dans les positions des tetes de lecture sur les bandes"
assert L == [["$", "Z"],["$","0"]], "Erreur dans le contenu des bandes"

print("\n\n----------------------------------------------\n\n")
print("Test 2 : exec_MT_k")
print("--------------------------------------------------")
(b,T,L)= exec_MT_k(M2_un_to_bin,2,[["$","I","I","I","I","I","I"],["Z"]],[0,0])
assert b == True, "Erreur dans le langage accepte"
assert T == [7,1], "Erreur dans les positions des tetes de lecture sur les bandes"
assert L == [["$","I","I","I","I","I","I","Z"],["$","1","1","0","Z"]], "Erreur dans le contenu des bandes"
