from Turing import *
from ensembles import *



# exemple
# Mots sur {0,1} terminant par 1 (position de la tete de lecture sur le
# premier symbole a l'issue du calcul


d_fb = [((0,"a"),(0,"a","R")),((0,"b"),(0,"b","R")),((0,"S"),(0,"S","R")),((0,"D"),(0,"D","R")),((0,"Z"),(1,"Z","L"))]
M_fb = (d_fb, 0, 1, 2)

d_bd = [((0,"a"),(0,"a","L")),((0,"b"),(0,"b","L")),((0,"S"),(0,"S","L")),((0,"D"),(1,"D","L"))]
		#((1,"a"),(2,"a","L")),((1,"b"),(2,"b","L")),((1,"S"),(2,"S","L")),((2,"a"),(3,"a","L")),((2,"b"),(3,"b","L")),((2,"S"),(3,"S","L"))]
M_bd = (d_bd, 0, 1, 2)


d_e = [((0,"a"),(1,"a","L")),((0,"b"),(2,"b","L")),((0,"S"),(3,"S","L")), ((0,"D"),(4,"D","L")),\
		((1,"a"),(5,"a","R")),((1,"b"),(6,"a","R")),((1,"D"),(8,"a","R")),((1,"S"),(7,"a","R")),\
		((2,"a"),(5,"b","R")),((2,"b"),(6,"b","R")), ((2,"D"),(8,"b","R")),((2,"S"),(7,"b","R")),\
		((3,"a"),(5,"S","R")),((3,"b"),(6,"S","R")),((3,"D"),(8,"S","R")),((3,"S"),(7,"S","R")),\
		((4,"a"),(5,"D","R")),((4,"b"),(6,"D","R")),((4,"D"),(8,"D","R")),((4,"S"),(7,"D","R")),\
		((5,"a"),(9,"a","R")),((5,"b"),(9,"a","R")),((5,"S"),(9,"a","R")),((5,"D"),(9,"a","R")),\
		((6,"a"),(9,"b","R")),((6,"b"),(9,"b","R")),((6,"S"),(9,"b","R")),((6,"D"),(9,"b","R")),\
		((7,"a"),(9,"S","R")),((7,"b"),(9,"S","R")),((7,"S"),(9,"S","R")),((7,"D"),(9,"S","R")),\
		((8,"a"),(9,"D","R")),((8,"b"),(9,"D","R")),((8,"S"),(9,"D","R")),((8,"D"),(9,"S","R")),\
		((9,"a"),(10,"a","L")),((9,"b"),(10,"b","L")),((9,"S"),(10,"S","R")),((9,"D"),(10,"D","R")),((9,"Z"),(10,"Z","L"))]

M_e = (d_e, 0, 10, 11)



# arithmetique unaire

# successeur + repositionnement de la tete de lecture sur le premier caractere

d_succ_un = [((0,"I"),(0,"I","R")), ((0,"Z"),(1,"I","L")), \
             ((1,"I"),(1,"I","L")), ((1,"Z"),(2,"Z","R"))]
M_succ_un = (d_succ_un,0,2,3)


# soustraction unaire

d_sub_un = [((0,"D"),(4,"Z","R")), ((0,"I"),(1,"Z","R")), \
            ((1,"I"),(1,"I","R")), ((1,"D"),(1,"D","R")), ((1,"Z"),(2,"Z","L")), \
            ((2,"I"),(3,"Z","L")), \
            ((3,"I"),(3,"I","L")), ((3,"D"),(3,"D","L")), ((3,"Z"),(0,"Z","R"))]
M_sub_un = (d_sub_un,0,4,5)


# arithmetique binaire

d_add_un = [((0,"D"),(3,"Z","R")), ((0,"I"),(1,"Z","R")), \
            ((1,"I"),(1,"I","R")), ((1,"D"),(1,"D","R")), ((1,"Z"),(2,"I","L")), \
            ((2,"I"),(2,"I","L")), ((2,"D"),(2,"D","L")), ((2,"Z"),(0,"Z","R"))]
M_add_un = (d_add_un,0,3,4)




# (propagation du bit de signe) : duplication du dernier bit d'un mot binaire


d_prop1 = [((0,"0"),(1,"0","R")), ((0,"1"),(2,"1","R")), \
           ((1,"0"),(1,"0","R")), ((1,"1"),(2,"1","R")), ((1,"Z"),(3,"0","L")),\
           ((2,"0"),(1,"0","R")), ((2,"1"),(2,"1","R")), ((2,"Z"),(3,"1","L")),\
           ((3,"0"),(3,"0","L")), ((3,"1"),(3,"1","L")), ((3,"Z"),(4,"Z","R"))]


M_prop1 =(d_prop1,0,4,5)

d_id = []
M_id = (d_id, 0, 0, 1)

def test_noms_etats(d,d1,d2):
	expected = []
	for t in d1:
		((s,a),(s1,b,M))=t
		expected = ajout(eq_atom, (1,s), expected)
		expected = ajout(eq_atom, (1,s1), expected)
		
	for t in d2:
		((s,a),(s1,b,M))=t
		expected = ajout(eq_atom, (2,s), expected)
		expected = ajout(eq_atom, (2,s1), expected)
		
	noms_etats = []
	for t in d:
		((s,a),(s1,b,M))=t
		noms_etats = ajout(eq_atom, s, noms_etats)
		noms_etats = ajout(eq_atom, s1, noms_etats)
		
	return is_subset(eq_atom, noms_etats, expected)

def eq_trans(t1,t2):
	((s1,a1),(sp1, b1, M1)) = t1
	((s2,a2),(sp2,b2, M2))=  t2
	return eq_atom(s1,s2) and eq_atom(a1,a2) and eq_atom(sp1,sp2) and eq_atom(b1,b2) and eq_atom(M1,M2)
	
	
eq_set_trans = make_eq_set(eq_trans)


d1=[(((1, 0), '0'), ((1, 0), '1', 'R')), (((1, 0), '1'), ((1, 0), '0', 'R')), (((1, 0), 'Z'), ((1, 1), 'Z', 'L')), (((1, 1), '0'), ((1, 1), '0', 'L')), (((1, 1), '1'), ((1, 1), '1', 'L')), (((1, 1), 'Z'), ((2, 0), 'Z', 'R')), (((2, 0), '0'), ((2, 1), '1', 'L')), (((2, 0), '1'), ((2, 0), '0', 'R')), (((2, 0), 'Z'), ((2, 2), '1', 'R')), (((2, 1), '0'), ((2, 1), '0', 'L')), (((2, 1), '1'), ((2, 1), '1', 'L')), (((2, 1), 'Z'), ((2, 3), 'Z', 'R')), (((2, 2), 'Z'), ((2, 1), 'Z', 'L'))]
d2=[(((1, 0), 'a'), ((1, 0), 'a', 'R')), (((1, 0), 'b'), ((1, 0), 'b', 'R')), (((1, 0), 'S'), ((1, 0), 'S', 'R')), (((1, 0), 'D'), ((1, 0), 'D', 'R')), (((1, 0), 'Z'), ((2, 0), 'Z', 'L')), (((2, 0), 'a'), ((2, 0), 'a', 'L')), (((2, 0), 'b'), ((2, 0), 'b', 'L')), (((2, 0), 'S'), ((2, 0), 'S', 'L')), (((2, 0), 'D'), ((2, 1), 'D', 'L'))]

d3=[(((1, (1, 0)), 'a'), ((1, (1, 0)), 'a', 'R')), (((1, (1, 0)), 'b'), ((1, (1, 0)), 'b', 'R')), (((1, (1, 0)), 'S'), ((1, (1, 0)), 'S', 'R')), (((1, (1, 0)), 'D'), ((1, (1, 0)), 'D', 'R')), (((1, (1, 0)), 'Z'), ((1, (2, 0)), 'Z', 'L')), (((1, (2, 0)), 'a'), ((1, (2, 0)), 'a', 'L')), (((1, (2, 0)), 'b'), ((1, (2, 0)), 'b', 'L')), (((1, (2, 0)), 'S'), ((1, (2, 0)), 'S', 'L')), (((1, (2, 0)), 'D'), ((2, 0), 'D', 'L')), (((2, 0), 'a'), ((2, 1), 'a', 'L')), (((2, 0), 'b'), ((2, 2), 'b', 'L')), (((2, 0), 'S'), ((2, 3), 'S', 'L')), (((2, 0), 'D'), ((2, 4), 'D', 'L')), (((2, 1), 'a'), ((2, 5), 'a', 'R')), (((2, 1), 'b'), ((2, 6), 'a', 'R')), (((2, 1), 'D'), ((2, 8), 'a', 'R')), (((2, 1), 'S'), ((2, 7), 'a', 'R')), (((2, 2), 'a'), ((2, 5), 'b', 'R')), (((2, 2), 'b'), ((2, 6), 'b', 'R')), (((2, 2), 'D'), ((2, 8), 'b', 'R')), (((2, 2), 'S'), ((2, 7), 'b', 'R')), (((2, 3), 'a'), ((2, 5), 'S', 'R')), (((2, 3), 'b'), ((2, 6), 'S', 'R')), (((2, 3), 'D'), ((2, 8), 'S', 'R')), (((2, 3), 'S'), ((2, 7), 'S', 'R')), (((2, 4), 'a'), ((2, 5), 'D', 'R')), (((2, 4), 'b'), ((2, 6), 'D', 'R')), (((2, 4), 'D'), ((2, 8), 'D', 'R')), (((2, 4), 'S'), ((2, 7), 'D', 'R')), (((2, 5), 'a'), ((2, 9), 'a', 'R')), (((2, 5), 'b'), ((2, 9), 'a', 'R')), (((2, 5), 'S'), ((2, 9), 'a', 'R')), (((2, 5), 'D'), ((2, 9), 'a', 'R')), (((2, 6), 'a'), ((2, 9), 'b', 'R')), (((2, 6), 'b'), ((2, 9), 'b', 'R')), (((2, 6), 'S'), ((2, 9), 'b', 'R')), (((2, 6), 'D'), ((2, 9), 'b', 'R')), (((2, 7), 'a'), ((2, 9), 'S', 'R')), (((2, 7), 'b'), ((2, 9), 'S', 'R')), (((2, 7), 'S'), ((2, 9), 'S', 'R')), (((2, 7), 'D'), ((2, 9), 'S', 'R')), (((2, 8), 'a'), ((2, 9), 'D', 'R')), (((2, 8), 'b'), ((2, 9), 'D', 'R')), (((2, 8), 'S'), ((2, 9), 'D', 'R')), (((2, 8), 'D'), ((2, 9), 'S', 'R')), (((2, 9), 'a'), ((2, 10), 'a', 'L')), (((2, 9), 'b'), ((2, 10), 'b', 'L')), (((2, 9), 'S'), ((2, 10), 'S', 'R')), (((2, 9), 'D'), ((2, 10), 'D', 'R')), (((2, 9), 'Z'), ((2, 10), 'Z', 'L'))]


print("\n\n----------------------------------------------\n\n")
print("Test 1 : make_seq_MT")
print("---------------------------")
(d,q0,qok,qko)= make_seq_MT(M_compl_bin,M_succ_bin)
assert test_noms_etats(d,d_compl_bin,d_succ_bin), "Erreur dans les noms d'etats de la machine sequence"
assert eq_set_trans(d, d1), "Erreur dans les transitions de make_seq_MT"
assert q0==(1,0), "mauvais etat initial"
assert qok ==(2,3), "mauvais etat acceptant"
assert qko == (2,4), "mauvais etat rejetant"
(b1,i1,L1)=exec_MT_1((d,q0,qok,qko),["Z"],0)
assert b1==True, "erreur dans le langage accepte par la machine"
assert L1 == ["Z","1","Z"], "erreur dans la bande a la fin du calcul"
(b1,i1,L1)=exec_MT_1((d,q0,qok,qko),["0"],0)
assert b1 == True, "erreur dans le langage accepte par la machine"
assert L1 == ["Z","0","1","Z"], "erreur dans la bande a la fin du calcul"

print("\n\n----------------------------------------------\n\n")
print("Test 2 : make_seq_MT")
print("---------------------------")
M1 = make_seq_MT(M_fb,M_bd)
(d,q0,qok,qko) = M1
print(d)
assert test_noms_etats(d,d_fb,d_bd), "Erreur dans les noms d'etats de la machine sequence"
assert eq_set_trans(d, d2), "Erreur dans les transitions de make_seq_MT"
assert q0==(1,0), "mauvais etat initial"
assert qok ==(2,1), "mauvais etat acceptant"
assert qko == (2,2), "mauvais etat rejetant"


print("\n\n----------------------------------------------\n\n")
print("Test 3 : make_seq_MT")
print("---------------------------")
(d_comp,q0_comp,qok_comp,qko_comp)= make_seq_MT(M1,M_e)
assert test_noms_etats(d_comp,d,d_e), "Erreur dans les noms d'etats de la machine sequence"
assert eq_set_trans(d_comp, d3), "Erreur dans les transitions de make_seq_MT"
assert q0_comp==(1,(1,0)), "mauvais etat initial"
assert qok_comp ==(2,10), "mauvais etat acceptant"
assert qko_comp == (2,11), "mauvais etat rejetant"
(b3,i3,L3) = exec_MT_1((d_comp,q0_comp,qok_comp,qko_comp), ["a","b","D","a","b"],0)
assert b3==True, "Erreur dans le langage accepte"
assert L3==["b","a","D","a","b","Z"], "Erreur dans la bande a la fin du calcul"
(b3,i3,L3) = exec_MT_1((d_comp,q0_comp,qok_comp,qko_comp), ["a","b","a","b"],0)
assert b3 == False, "Erreur dans le langage accepte"

print("\n\n----------------------------------------------\n\n")
print("Test 4 : make_seq_MT")
print("---------------------------")
(d_comp,q0_comp,qok_comp,qko_comp)= make_seq_MT(M_ex1,M_id)
#assert test_noms_etats(d_comp,d,d_id), "Erreur dans les noms d'etats de la machine sequence"
#assert eq_set_trans(d_comp, d3), "Erreur dans les transitions de make_seq_MT"
#assert q0_comp==(1,(1,0)), "mauvais etat initial"
#assert qok_comp ==(2,10), "mauvais etat acceptant"
#assert qko_comp == (2,11), "mauvais etat rejetant"
(b3,i3,L3) = exec_MT_1((d_comp,q0_comp,qok_comp,qko_comp), ["A","B","A","B","B"],0)
assert b3==True, "Erreur dans le langage accepte, composition avec machine vide"
assert L3==["A","B","A","B","B"], "Erreur dans la bande a la fin du calcul, composition avec machine vide"
(b3,i3,L3) = exec_MT_1((d_comp,q0_comp,qok_comp,qko_comp), ["Z"],0)
assert b3 == False, "Erreur dans le langage accepte, composition avec machine vide"
assert L3 == ["Z"], "Erreur dans la bande a la fin du calcul, composition avec machine vide"


print("\n\n----------------------------------------------\n\n")
print("Test 5 : make_seq_MT")
print("---------------------------")
(d_comp,q0_comp,qok_comp,qko_comp)= make_seq_MT(M_succ_un,M_id)
#assert test_noms_etats(d_comp,d,d_id), "Erreur dans les noms d'etats de la machine sequence"
#assert eq_set_trans(d_comp, d3), "Erreur dans les transitions de make_seq_MT"
#assert q0_comp==(1,(1,0)), "mauvais etat initial"
#assert qok_comp ==(2,10), "mauvais etat acceptant"
#assert qko_comp == (2,11), "mauvais etat rejetant"
(b3,i3,L3) = exec_MT_1((d_comp,q0_comp,qok_comp,qko_comp), ["I","I","I","I","Z"],0)
assert b3==True, "Erreur dans le langage accepte, composition avec machine vide"
assert i3 == 1, "Erreur dans la position de la tete de lecture a la fin du calcul, composition avec machine vide"
assert L3==["Z","I","I","I","I","I"], "Erreur dans la bande a la fin du calcul, composition avec machine vide"
(b3,i3,L3) = exec_MT_1((d_comp,q0_comp,qok_comp,qko_comp), ["Z"],0)
assert b3 == True, "Erreur dans le langage accepte, composition avec machine vide"
assert L3 == ["Z","I"], "Erreur dans la bande a la fin du calcul, composition avec machine vide"

print("\n\n----------------------------------------------\n\n")
print("Test 6 : make_seq_MT")
print("---------------------------")
(d_comp,q0_comp,qok_comp,qko_comp)= make_seq_MT(M_id,M_succ_un)
#assert test_noms_etats(d_comp,d,d_id), "Erreur dans les noms d'etats de la machine sequence"
#assert eq_set_trans(d_comp, d3), "Erreur dans les transitions de make_seq_MT"
#assert q0_comp==(1,(1,0)), "mauvais etat initial"
#assert qok_comp ==(2,10), "mauvais etat acceptant"
#assert qko_comp == (2,11), "mauvais etat rejetant"
(b3,i3,L3) = exec_MT_1((d_comp,q0_comp,qok_comp,qko_comp), ["I","I","I","I","Z"],0)
assert b3==True, "Erreur dans le langage accepte, composition avec machine vide"
assert i3 == 1, "Erreur dans la position de la tete de lecture a la fin du calcul, composition avec machine vide"
assert L3==["Z","I","I","I","I","I"], "Erreur dans la bande a la fin du calcul, composition avec machine vide"
(b3,i3,L3) = exec_MT_1((d_comp,q0_comp,qok_comp,qko_comp), ["Z"],0)
assert b3 == True, "Erreur dans le langage accepte, composition avec machine vide"
assert L3 == ["Z","I"], "Erreur dans la bande a la fin du calcul, composition avec machine vide"
