from Turing import *
from ensembles import *

def eq_trans(t1,t2):
	((s1,a1),(sp1, b1, M1)) = t1
	((s2,a2),(sp2,b2, M2))=  t2
	return eq_atom(s1,s2) and eq_atom(a1,a2) and eq_atom(sp1,sp2) and eq_atom(b1,b2) and eq_atom(M1,M2)
	
	
eq_set_trans = make_eq_set(eq_trans)


# test symbole different de Z
M_neq_Z = make_test_neq("Z",["0","1","Z"])
# effacement du caractere lu
d_writeZR = [((0,"0"),(1,"Z","R")), ((0,"1"),(1,"Z","R"))]
M_writeZR = [d_writeZR,0,1,2]
# effacement du mot
M_eff = make_loop_MT(M_neq_Z, M_writeZR)
d_exp = [(((0, 0), '0'), ((0, 2), '0', 'R')), (((0, 1), '0'), ((0, 3), '0', 'L')), (((0, 2), '0'), ((1, 0), '0', 'L')), (((0, 0), '1'), ((0, 2), '1', 'R')), (((0, 1), '1'), ((0, 3), '1', 'L')), (((0, 2), '1'), ((1, 0), '1', 'L')), (((0, 0), 'Z'), ((0, 1), 'Z', 'R')), (((0, 1), 'Z'), ((0, 3), 'Z', 'L')), (((0, 2), 'Z'), ((1, 0), 'Z', 'L')), (((1, 0), '0'), ((0, 0), 'Z', 'R')), (((1, 0), '1'), ((0, 0), 'Z', 'R'))]

# positionnement de la tete de lecture sur le premier 1 d'un mot
# binaire s'il existe et a la fin du mot sinon

d_foo1 = [(((0, 0), '0'), ((0, 1), '0', 'R')), (((0, 1), '0'), ((1, 0), '0', 'L')), (((0, 2), '0'), ((0, 4), '0', 'L')), (((0, 0), '1'), ((0, 2), '1', 'R')), (((0, 1), '1'), ((1, 0), '1', 'L')), (((0, 2), '1'), ((0, 4), '1', 'L')), (((0, 0), 'Z'), ((0, 2), 'Z', 'R')), (((0, 1), 'Z'), ((1, 0), 'Z', 'L')), (((0, 2), 'Z'), ((0, 4), 'Z', 'L')), (((1, 0), '0'), ((0, 0), '0', 'R')), (((1, 0), '1'), ((0, 0), '1', 'R')), (((1, 0), 'Z'), ((0, 0), 'Z', 'R'))]

print("\n\n----------------------------------------------\n\n")
print("Test 1 : make_loop_MT")
print("---------------------------")
(d,q0,qok,qko) = M_eff
assert eq_set_trans(d,d_exp), "Erreur dans la fonctiond de transition"
assert q0==(0,0), "Erreur dans l'etat initial"
assert qok==(0,3), "Erreur dans l'etat acceptant"
assert qko==(1,2), "Erreur dans l'etat rejetant"
(b1,i1,L1)=exec_MT_1(M_eff, ["0","1","1","Z"],0)
assert b1 == True, "Erreur dans le langage accepte par la machine"
assert L1 == ["Z","Z","Z","Z","Z"], "Erreur dans le mot ecrit sur la bande"

print("\n\n----------------------------------------------\n\n")
print("Test 2 : M_foo1")
print("---------------------------")
(d,q0,qok,qko) = M_foo1
print(d)
assert eq_set_trans(d,d_foo1), "Erreur dans la fonctiond de transition"
assert q0==(0,0), "Erreur dans l'etat initial"
assert qok==(0,4), "Erreur dans l'etat acceptant"
assert qko==(1,2), "Erreur dans l'etat rejetant"
(b1,i1,L1)=exec_MT_1(M_foo1, ["0","1","0","1","0"],0)
assert b1 == True, "Erreur dans le langage accepte par la machine"
assert i1 == 1, "Erreur dans la position de la tete de lecture"
assert L1 == ["0","1","0","1","0"], "Erreur dans le mot ecrit sur la bande"
(b2,i2,L2) = exec_MT_1(M_foo1,["0","0","0"],0)
assert b2 == True, "Erreur dans le langage accepte par la machine"
assert i2 == 3, "Erreur dans la position de la tete de lecture"
assert L2 == ["0","0","0","Z","Z"], "Erreur dans le mot ecrit sur la bande"


