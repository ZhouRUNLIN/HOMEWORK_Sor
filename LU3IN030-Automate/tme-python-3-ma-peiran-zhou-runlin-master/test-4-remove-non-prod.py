from reduced_ghc import *

# G12a
# -----

exo11_a_nt = ["S","A","B"]
exo11_a_t = ["a","b"]
exo11_a_r = [("S",[["A","S","B"],[]]),\
             ("A",[["a","A","S"],["a"]]),\
             ("B",[["S","b","S"],["A"],["b","b"]])]
exo11_a_s = "S"
exo11_a_g = (exo11_a_nt,exo11_a_t,exo11_a_r,exo11_a_s,eq_atom)


# G12b
#-----

exo11_b_nt = ["S","A","B"]
exo11_b_t = ["a","b"]
exo11_b_r = [("S",[["A","A","A"],["B"]]),\
             ("A",[["a","A"],["A","B"],[]]),\
             ("B",[["A"]])]
exo11_b_s = "S"
exo11_b_g = (exo11_b_nt,exo11_b_t,exo11_b_r,exo11_b_s,eq_atom)

# G13
#----


exo12_nt = ["S","A","B","C"]
exo12_t = ["a","b"]
exo12_r = [("S",[["A","B"],["B","C"]]),\
           ("A",[["B","A"],["a"]]),\
           ("B",[["C","C"],["b"]]),\
           ("C",[["A","B"],["a"]])]
exo12_s = "S"
exo12_g = (exo12_nt,exo12_t,exo12_r,exo12_s,eq_atom)



# G14
# ---

exo13_nt = ["S0","S1","S2","S3","S4","S5"]
exo13_t = ["a","b","c"]
exo13_r = [("S0",[["S1","S2"],["S3","S4"],["S5"]]),\
           ("S1",[["S1","S1"],["S1","S4"]]),\
           ("S2",[["a","S2"],["S3"]]),\
           ("S3",[["S2"],["S4"],["S5","a","S3"]]),\
           ("S4",[["c","S4"],[]]),\
           ("S5",[["S4"],["b"]])]
exo13_s = "S0"
exo13_g = (exo13_nt,exo13_t,exo13_r,exo13_s,eq_atom)

#ex
g_red_nt = ["S","T","U","V"]
g_red_t=["a","b"]
g_red_r=[("S",[["a","T"],["b","T","U"],["a","b","T","S"],["U","V"]]),\
		("T",[["a","U"],["b","T"],["a"]]),\
		("U",[["a","U"],["b","U"]]),\
		("V",[["a","T"],["b","S"],["a"]])]
g_red_s="S"
g_red=(g_red_nt, g_red_t, g_red_r, g_red_s, eq_atom)



print("\n\n----------------------------------------------\n\n")
print("Test 1 : Fonction remove_non_prod")
print("---------------------------")
g3_nt,g3_t,g3_r,g3_si,g3_eqnt=remove_non_prod(g1_g)
g3_r_sol = [("S",[["A3"]]),\
        ("A3",[["a","A3","A9"],["A6"]]),\
        ("A6",[["b","A6"],[]]),\
        ("A9",[["c"]])]
assert eq_set(eq_atom, g3_r, g3_r_sol), "Erreur dans la fonction remove_non_prod : exemple 1"



print("Test 2 : Fonction remove_non_prod")
print("---------------------------")

(exo11_a_nt_res,exo11_a_t_res,exo11_a_r_res,exo11_a_s_res,eq_atom)= remove_non_prod(exo11_a_g)
assert eq_set(eq_atom,exo11_a_nt_res, exo11_a_nt), "Erreur dans l'ensemble des variables"
assert eq_set(eq_atom,exo11_a_r_res, exo11_a_r), "Erreur dans l'ensemble des regles"

print("Test 3 : Fonction remove_non_prod")
print("---------------------------")

(exo11_b_nt_res,exo11_b_t_res,exo11_b_r_res,exo11_b_s_res,eq_atom)= remove_non_prod(exo11_b_g)
assert eq_set(eq_atom,exo11_b_nt_res, exo11_b_nt), "Erreur dans l'ensemble des variables"
assert eq_set(eq_atom,exo11_b_r_res, exo11_b_r), "Erreur dans l'ensemble des regles"

#assert eq_set(eq_atom, next_prod(exo11_b_t, exo11_b_r, eq_atom, next_prod(exo11_b_t, exo11_b_r, eq_atom, prod0(exo11_b_t, exo11_b_r))),['S', 'A', 'B']), "Erreur dans la fonction next_prod : exo11b - 2 etapes."


print("Test 4 : Fonction remove_non_prod")
print("-----------------------")

(exo12_nt_res,exo12_t_res,exo12_r_res,exo12_s_res,eq_atom)= remove_non_prod(exo12_g)
assert eq_set(eq_atom,exo12_nt_res, exo12_nt), "Erreur dans l'ensemble des variables"
assert eq_set(eq_atom,exo12_r_res, exo12_r), "Erreur dans l'ensemble des regles"

#assert eq_set(eq_atom, next_prod(exo12_t, exo12_r, eq_atom, next_prod(exo12_t, exo12_r, eq_atom, prod0(exo12_t, exo12_r))),['S', 'A', 'B', 'C']), "Erreur dans la fonction next_prod : exo12 - 2 etapes."



print("Test 5 : Fonction remove_non_prod")
print("-----------------------")


(exo13_nt_res,exo13_t_res,exo13_r_res,exo13_s_res,eq_atom)= remove_non_prod(exo13_g)
assert eq_set(eq_atom,exo13_nt_res, ['S0','S2','S3','S4','S5']), "Erreur dans l'ensemble des variables"
assert eq_set(eq_atom,exo13_r_res, [("S0",[["S3","S4"],["S5"]]),\
           ("S2",[["a","S2"],["S3"]]),\
           ("S3",[["S2"],["S4"],["S5","a","S3"]]),\
           ("S4",[["c","S4"],[]]),\
           ("S5",[["S4"],["b"]])]), "Erreur dans l'ensemble des regles"

print("Test 6 : Fonction remove_non_prod")
print("-----------------------")

(g_red_nt_res,g_red_t_res,g_red_r_res,g_red_s_res,eq_atom)= remove_non_prod(g_red)
assert eq_set(eq_atom,g_red_nt_res, ['S','T','V']), "Erreur dans l'ensemble des variables"
assert eq_set(eq_atom,g_red_r_res, [("S",[["a","T"],["a","b","T","S"]]),\
		("T",[["b","T"],["a"]]),\
		("V",[["a","T"],["b","S"],["a"]])]), "Erreur dans l'ensemble des regles"

