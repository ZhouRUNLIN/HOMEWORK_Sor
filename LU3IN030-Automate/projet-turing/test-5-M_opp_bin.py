from Turing import *
from ensembles import *

def eq_trans(t1,t2):
	((s1,a1),(sp1, b1, M1)) = t1
	((s2,a2),(sp2,b2, M2))=  t2
	return eq_atom(s1,s2) and eq_atom(a1,a2) and eq_atom(sp1,sp2) and eq_atom(b1,b2) and eq_atom(M1,M2)
	
	
eq_set_trans = make_eq_set(eq_trans)
print("\n\n----------------------------------------------\n\n")
print("Test 1 : make_seq_MT")
print("---------------------------")
(d_o, q0_o, qok_o, qko_o)= M_opp_int_bin
(d,q0,qok,qko)= make_seq_MT(M_compl_bin,M_succ_bin)
assert eq_set_trans(d, d_o), "Erreur dans les transitions de M_opp_int_bin"
