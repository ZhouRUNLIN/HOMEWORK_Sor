from Turing import *
from ensembles import *

def eq_trans(t1,t2):
	((s1,a1),(sp1, b1, M1)) = t1
	((s2,a2),(sp2,b2, M2))=  t2
	return eq_atom(s1,s2) and eq_atom(a1,a2) and eq_atom(sp1,sp2) and eq_atom(b1,b2) and eq_atom(M1,M2)
	
	
eq_set_trans = make_eq_set(eq_trans)
d_foo0 = [(((0, 0), '0'), ((0, 1), '0', 'R')), (((0, 0), '1'), ((0, 2), '1', 'R')), (((0, 1), '0'), ((0, 1), '0', 'R')), (((0, 1), '1'), ((0, 2), '1', 'R')), (((0, 1), 'Z'), ((0, 3), 'Z', 'L')), (((0, 2), '0'), ((0, 1), '0', 'R')), (((0, 2), '1'), ((0, 2), '1', 'R')), (((0, 2), 'Z'), ((0, 4), 'Z', 'L')), (((0, 3), '0'), ((0, 3), '0', 'L')), (((0, 3), '1'), ((0, 3), '1', 'L')), (((0, 3), 'Z'), ((2, 0), 'Z', 'R')), (((0, 4), '0'), ((0, 4), '0', 'L')), (((0, 4), '1'), ((0, 4), '1', 'L')), (((0, 4), 'Z'), ((1, 0), 'Z', 'R')), (((1, 0), '0'), ((1, 1), '0', 'R')), (((1, 0), '1'), ((1, 2), '1', 'R')), (((1, 1), '0'), ((1, 1), '0', 'R')), (((1, 1), '1'), ((1, 2), '1', 'R')), (((1, 1), 'Z'), ((1, 3), '0', 'L')), (((1, 2), '0'), ((1, 1), '0', 'R')), (((1, 2), '1'), ((1, 2), '1', 'R')), (((1, 2), 'Z'), ((1, 3), '1', 'L')), (((1, 3), '0'), ((1, 3), '0', 'L')), (((1, 3), '1'), ((1, 3), '1', 'L')), (((1, 3), 'Z'), ((2, 0),'Z','R'))]
d_foo2 = [(((1, (0, 0)), '0'), ((1, (0, 1)), '0', 'R')), (((1, (0, 0)), '1'), ((1, (0, 2)), '1', 'R')), (((1, (0, 1)), '0'), ((1, (0, 1)), '0', 'R')), (((1, (0, 1)), '1'), ((1, (0, 2)), '1', 'R')), (((1, (0, 1)), 'Z'), ((1, (0, 3)), 'Z', 'L')), (((1, (0, 2)), '0'), ((1, (0, 1)), '0', 'R')), (((1, (0, 2)), '1'), ((1, (0, 2)), '1', 'R')), (((1, (0, 2)), 'Z'), ((1, (0, 4)), 'Z', 'L')), (((1, (0, 3)), '0'), ((1, (0, 3)), '0', 'L')), (((1, (0, 3)), '1'), ((1, (0, 3)), '1', 'L')), (((1, (0, 3)), 'Z'), ((2, (0, 0)), 'Z', 'R')), (((1, (0, 4)), '0'), ((1, (0, 4)), '0', 'L')), (((1, (0, 4)), '1'), ((1, (0, 4)), '1', 'L')), (((1, (0, 4)), 'Z'), ((1, (1, 0)), 'Z', 'R')), (((1, (1, 0)), '0'), ((1, (1, 1)), '0', 'R')), (((1, (1, 0)), '1'), ((1, (1, 2)), '1', 'R')), (((1, (1, 1)), '0'), ((1, (1, 1)), '0', 'R')), (((1, (1, 1)), '1'), ((1, (1, 2)), '1', 'R')), (((1, (1, 1)), 'Z'), ((1, (1, 3)), '0', 'L')), (((1, (1, 2)), '0'), ((1, (1, 1)), '0', 'R')), (((1, (1, 2)), '1'), ((1, (1, 2)), '1', 'R')), (((1, (1, 2)), 'Z'), ((1, (1, 3)), '1', 'L')), (((1, (1, 3)), '0'), ((1, (1, 3)), '0', 'L')), (((1, (1, 3)), '1'), ((1, (1, 3)), '1', 'L')), (((1, (1, 3)), 'Z'), ((2, (0, 0)), 'Z', 'R')), (((2, (0, 0)), '0'), ((2, (0, 1)), '0', 'R')), (((2, (0, 1)), '0'), ((2, (1, 0)), '0', 'L')), (((2, (0, 2)), '0'), ((2, (0, 4)), '0', 'L')), (((2, (0, 0)), '1'), ((2, (0, 2)), '1', 'R')), (((2, (0, 1)), '1'), ((2, (1, 0)), '1', 'L')), (((2, (0, 2)), '1'), ((2, (0, 4)), '1', 'L')), (((2, (0, 0)), 'Z'), ((2, (0, 2)), 'Z', 'R')), (((2, (0, 1)), 'Z'), ((2, (1, 0)), 'Z', 'L')), (((2, (0, 2)), 'Z'), ((2, (0, 4)), 'Z', 'L')), (((2, (1, 0)), '0'), ((2, (0, 0)), '0', 'R')), (((2, (1, 0)), '1'), ((2, (0, 0)), '1', 'R')), (((2, (1, 0)), 'Z'), ((2, (0, 0)), 'Z', 'R'))]
d_foo3 = [(((1, 0), '0'), ((2, 0), '0', 'R')), (((1, 0), '1'), ((2, 0), '1', 'R')), (((1, 0), 'Z'), ((2, 0), 'Z', 'R')), (((2, 0), '0'), ((2, 0), '1', 'R')), (((2, 0), '1'), ((2, 0), '0', 'R')), (((2, 0), 'Z'), ((2, 1), 'Z', 'L')), (((2, 1), '0'), ((2, 1), '0', 'L')), (((2, 1), '1'), ((2, 1), '1', 'L')), (((2, 1), 'Z'), ((2, 2), 'Z', 'R'))]
d_eq_Z=[((0, '0'), (2, '0', 'R')), ((1, '0'), (3, '0', 'L')), ((2, '0'), (4, '0', 'L')), ((0, '1'), (2, '1', 'R')), ((1, '1'), (3, '1', 'L')), ((2, '1'), (4, '1', 'L')), ((0, 'Z'), (1, 'Z', 'R')), ((1, 'Z'), (3, 'Z', 'L')), ((2, 'Z'), (4, 'Z', 'L'))]

print("\n\n----------------------------------------------\n\n")
print("Test 1 : M_foo0")
print("---------------------------")
(d,q0,qok,qko) = M_foo0
assert eq_set_trans(d,d_foo0), "Erreur dans la fonctiond de transition"
assert q0==(0,0), "Erreur dans l'etat initial"
assert qok==(2,0), "Erreur dans l'etat acceptant"
assert qko==(2,1), "Erreur dans l'etat rejetant"
(b1,i1,L1)=exec_MT_1(M_foo0, ["0","1","0","1","0"],0)
assert b1 == True, "Erreur dans le langage accepte par la machine"
assert i1 == 1, "Erreur dans la position de la tete de lecture"
assert L1 == ["Z","0","1","0","1","0","Z"], "Erreur dans le mot ecrit sur la bande"
(b2,i2,L2) = exec_MT_1(M_foo0,["0","0","0"],0)
assert b2 == True, "Erreur dans le langage accepte par la machine"
assert i2 == 1, "Erreur dans la position de la tete de lecture"
assert L2 == ["Z","0","0","0","Z"], "Erreur dans le mot ecrit sur la bande"


print("\n\n----------------------------------------------\n\n")
print("Test 2 : M_foo2")
print("---------------------------")
(d,q0,qok,qko) = M_foo2
assert eq_set_trans(d,d_foo2), "Erreur dans la fonctiond de transition"
assert q0==(1,(0,0)), "Erreur dans l'etat initial"
assert qok==(2, (0, 4)), "Erreur dans l'etat acceptant"
assert qko==(2, (1, 2)), "Erreur dans l'etat rejetant"
(b1,i1,L1)=exec_MT_1(M_foo2, ["0","1","0","1","0"],0)
assert b1 == True, "Erreur dans le langage accepte par la machine"
assert i1 == 2, "Erreur dans la position de la tete de lecture"
assert L1 == ["Z","0","1","0","1","0","Z"], "Erreur dans le mot ecrit sur la bande"
(b2,i2,L2) = exec_MT_1(M_foo2,["0","0","0"],0)
assert b2 == True, "Erreur dans le langage accepte par la machine"
assert i2 == 4, "Erreur dans la position de la tete de lecture"
assert L2 == ["Z","0","0","0","Z","Z"], "Erreur dans le mot ecrit sur la bande"

print("\n\n----------------------------------------------\n\n")
print("Test 2 : M_foo3")
print("---------------------------")
(d,q0,qok,qko) = M_foo3
assert eq_set_trans(d,d_foo3), "Erreur dans la fonctiond de transition"
assert q0==(1,0), "Erreur dans l'etat initial"
assert qok==(2,2), "Erreur dans l'etat acceptant"
assert qko==(2,3), "Erreur dans l'etat rejetant"
(b1,i1,L1)=exec_MT_1(M_foo3, ["0","1","0","1","0"],0)
assert b1 == True, "Erreur dans le langage accepte par la machine"
assert i1 == 1, "Erreur dans la position de la tete de lecture"
assert L1 == ["Z","0","0","1","0","1","Z"], "Erreur dans le mot ecrit sur la bande"
(b2,i2,L2) = exec_MT_1(M_foo3,["0","0","0"],0)
assert b2 == True, "Erreur dans le langage accepte par la machine"
assert i2 == 1, "Erreur dans la position de la tete de lecture"
assert L2 == ["Z","0","1","1","Z"], "Erreur dans le mot ecrit sur la bande"


print("\n\n----------------------------------------------\n\n")
print("Test 3 : M_eq_Z")
print("---------------------------")
(d,q0,qok,qko) = M_eq_Z

(b1,i1,L1)=exec_MT_1(M_eq_Z, ["0","1","0","1","0"],0)
assert b1 == False, "Erreur dans le langage accepte par la machine"
assert i1 == 0, "Erreur dans la position de la tete de lecture"
assert L1 == ["0","1","0","1","0"], "Erreur dans le mot ecrit sur la bande"
(b2,i2,L2) = exec_MT_1(M_eq_Z,["0","0","0"],0)
assert b2 == False, "Erreur dans le langage accepte par la machine"
assert i2 == 0, "Erreur dans la position de la tete de lecture"
assert L2 == ["0","0","0"], "Erreur dans le mot ecrit sur la bande"
(b1,i1,L1)=exec_MT_1(M_eq_Z, ["Z","1","0","1","0"],0)
assert b1 == True, "Erreur dans le langage accepte par la machine"
assert i1 == 0, "Erreur dans la position de la tete de lecture"
assert L1 == ["Z","1","0","1","0"], "Erreur dans le mot ecrit sur la bande"

print("\n\n----------------------------------------------\n\n")
print("Test 4 : M_foo4")
print("---------------------------")
(d,q0,qok,qko) = M_foo4
(b1,i1,L1)=exec_MT_1(M_foo4, ["0","1","0","1","0"],0)
assert b1 == True, "Erreur dans le langage accepte par la machine"
#assert i1 == 0, "Erreur dans la position de la tete de lecture"
assert L1 == ["Z","0","0","1","0","1","Z"], "Erreur dans le mot ecrit sur la bande"
(b2,i2,L2) = exec_MT_1(M_foo4,["0","0","0"],0)
assert b2 == True, "Erreur dans le langage accepte par la machine"
#assert i2 == 0, "Erreur dans la position de la tete de lecture"
assert L2 == ["Z","0","1","1","Z"], "Erreur dans le mot ecrit sur la bande"
(b1,i1,L1)=exec_MT_1(M_foo4, ["Z","1","0","1","0"],0)
assert b1 == True, "Erreur dans le langage accepte par la machine"
#assert i1 == 0, "Erreur dans la position de la tete de lecture"
assert L1 == ["Z","1","0","1","0"], "Erreur dans le mot ecrit sur la bande"

print("\n\n----------------------------------------------\n\n")
print("Test 5 : M_foo")
print("---------------------------")
(d,q0,qok,qko) = M_foo
(b1,i1,L1)=exec_MT_1(M_foo,["1","0","1","0"],0)
assert b1 == True, "Erreur dans le langage accepte par la machine"
#assert i1 == 0, "Erreur dans la position de la tete de lecture"
assert L1 == ["Z","1","1","0","1","Z"], "Erreur dans le mot ecrit sur la bande"
(b2,i2,L2) = exec_MT_1(M_foo,["0","0","0"],0)
assert b2 == True, "Erreur dans le langage accepte par la machine"
##assert i2 == 0, "Erreur dans la position de la tete de lecture"
assert L2 == ["Z","0","0","0","Z","Z"], "Erreur dans le mot ecrit sur la bande"
(b3,i3,L3) = exec_MT_1(M_foo,["1","1","0","1"],0)
assert b3 == True, "Erreur dans le langage accepte par la machine"
assert L3 == ["Z","1","0","1","0","0","Z"], "Erreur dans le mot ecrit sur la bande"
(b4,i4,L4)=exec_MT_1(M_foo,["0","0","1"],0)
assert b4 == True, "Erreur dans le langage accepte"
assert L4 == ["Z","0","0","1","0","Z"], "Erreur dans le mot ecrit sur la bande"
(b5,i5,L5)=exec_MT_1(M_foo,["1","1","1"],0)
assert b5 == True, "Erreur dans le langage accepte"
assert L5 == ["Z","1","0","0","0","Z"], "Erreur dans le mot ecrit sur la bande"


