from Turing import *
from ensembles import *

def eq_trans(t1,t2):
	((s1,a1),(sp1, b1, M1)) = t1
	((s2,a2),(sp2,b2, M2))=  t2
	return eq_atom(s1,s2) and eq_atom(a1,a2) and eq_atom(sp1,sp2) and eq_atom(b1,b2) and eq_atom(M1,M2)
	
	
eq_set_trans = make_eq_set(eq_trans)

d_exp = [(((0, 0), '0'), ((0, 1), '0', 'R')), (((0, 0), '1'), ((0, 2), '1', 'R')), (((0, 1), '0'), ((0, 1), '0', 'R')), (((0, 1), '1'), ((0, 2), '1', 'R')), (((0, 1), 'Z'), ((0, 3), 'Z', 'L')), (((0, 2), '0'), ((0, 1), '0', 'R')), (((0, 2), '1'), ((0, 2), '1', 'R')), (((0, 2), 'Z'), ((0, 4), 'Z', 'L')), (((0, 3), '0'), ((0, 3), '0', 'L')), (((0, 3), '1'), ((0, 3), '1', 'L')), (((0, 3), 'Z'), ((2, 0), 'Z', 'R')), (((0, 4), '0'), ((0, 4), '0', 'L')), (((0, 4), '1'), ((0, 4), '1', 'L')), (((0, 4), 'Z'), ((1, (1, 0)), 'Z', 'R')), (((1, (1, 0)), '0'), ((1, (1, 0)), '1', 'R')), (((1, (1, 0)), '1'), ((1, (1, 0)), '0', 'R')), (((1, (1, 0)), 'Z'), ((1, (1, 1)), 'Z', 'L')), (((1, (1, 1)), '0'), ((1, (1, 1)), '0', 'L')), (((1, (1, 1)), '1'), ((1, (1, 1)), '1', 'L')), (((1, (1, 1)), 'Z'), ((1, (2, 0)), 'Z', 'R')), (((1, (2, 0)), '0'), ((1, (2, 1)), '1', 'L')), (((1, (2, 0)), '1'), ((1, (2, 0)), '0', 'R')), (((1, (2, 0)), 'Z'), ((1, (2, 2)), '1', 'R')), (((1, (2, 1)), '0'), ((1, (2, 1)), '0', 'L')), (((1, (2, 1)), '1'), ((1, (2, 1)), '1', 'L')), (((1, (2, 1)), 'Z'), ((2, 0), 'Z', 'R')), (((1, (2, 2)), 'Z'), ((1, (2, 1)), 'Z', 'L'))]

print("\n\n----------------------------------------------\n\n")
print("Test 1 : make_cond_MT")
print("---------------------------")
(d,q0,qok,qko) = make_cond_MT(M_isneg,M_opp_int_bin,M_id)
print(M_isneg)
print(M_opp_int_bin)
print(M_id)
print((d,q0,qok,qko))
assert eq_set_trans(d,d_exp), "Erreur dans la fonctiond de transition"
assert q0==(0,0), "Erreur dans l'etat initial"
assert qok==(2,0), "Erreur dans l'etat acceptant"
assert qko==(2,1), "Erreur dans l'etat rejetant"


print("\n\n----------------------------------------------\n\n")
print("Test 2 : M_abs")
print("---------------------------")
(d_a,q0_a,qok_a,qko_a) = M_abs
(b,i,L)=exec_MT_1(M_abs,["1","0","1","0"],0)
assert b==True, "Erreur dans le langage accepte"
assert L==["Z","1","0","1","0","Z"], "Erreur dans le contenu de la bande"

print("\n\n----------------------------------------------\n\n")
print("Test 3 : M_abs")
print("---------------------------")
(d_a,q0_a,qok_a,qko_a) = M_abs
(b,i,L)=exec_MT_1(M_abs,["1","1","0","1"],0)
assert b==True, "Erreur dans le langage accepte"
assert L==["Z","1","0","1","0","Z"], "Erreur dans le contenu de la bande"

print("\n\n----------------------------------------------\n\n")
print("Test 4 : M_abs")
print("---------------------------")
(d_a,q0_a,qok_a,qko_a) = M_abs
(b,i,L)=exec_MT_1(M_abs,["1","1","0","1","1"],0)
assert b==True, "Erreur dans le langage accepte"
assert L==["Z","1","0","1","0","0","Z"], "Erreur dans le contenu de la bande"


print("\n\n----------------------------------------------\n\n")
print("Test 5 : M_abs")
print("---------------------------")
(d_a,q0_a,qok_a,qko_a) = M_abs
(b,i,L)=exec_MT_1(M_abs,["0","1","0","1","1"],0)
assert b==True, "Erreur dans le langage accepte"
assert L==["Z","0","1","1","0","0","Z"], "Erreur dans le contenu de la bande"




