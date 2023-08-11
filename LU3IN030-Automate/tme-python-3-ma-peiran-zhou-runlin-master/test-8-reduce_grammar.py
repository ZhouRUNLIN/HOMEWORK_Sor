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
print("Test 1 : Fonction reduce_grammar")
print("---------------------------")
g3_nt,g3_t,g3_r,g3_si,g3_eqnt=reduce_grammar(g1_g)
g3_nt_sol = ["S","A3","A6","A9"]
g3_r_sol = [("S",[["A3"]]),\
        ("A3",[["a","A3","A9"],["A6"]]),\
        ("A6",[["b","A6"],[]]),\
        ("A9",[["c"]])]

assert eq_set(eq_atom, g3_nt, g3_nt_sol), "Erreur dans la fonction reduce_grammar : exemple 1 (variables)"
assert eq_set(eq_atom, g3_r, g3_r_sol), "Erreur dans la fonction reduce_grammar : exemple 1 (regles)"

print("Test 2 : Fonction reduce_grammar")
print("---------------------------")
exo11ared_nt,exo11ared_t,exo11ared_r,exo11ared_si,exo11ared_eqnt=reduce_grammar(exo11_a_g)
exo11ared_nt_sol = ["S","A","B"]
exo11ared_r_sol = exo11_a_r

assert eq_set(eq_atom, exo11ared_nt, exo11ared_nt_sol), "Erreur dans la fonction reduce_grammar : exercice 12a (variables)"
assert eq_set(eq_atom, exo11ared_r, exo11ared_r_sol), "Erreur dans la fonction reduce_grammar : exercice 12a (regles)"


print("Test 3 : Fonction reduce_grammar")
print("---------------------------")
exo13red_nt,exo13red_t,exo13red_r,exo13red_si,exo13red_eqnt=reduce_grammar(exo13_g)
exo13red_nt_sol = ["S0","S2","S3","S4","S5"]
exo13red_r_sol = [("S0",[["S3","S4"],["S5"]]),\
           ("S2",[["a","S2"],["S3"]]),\
           ("S3",[["S2"],["S4"],["S5","a","S3"]]),\
           ("S4",[["c","S4"],[]]),\
           ("S5",[["S4"],["b"]])]


assert eq_set(eq_atom, exo13red_nt, exo13red_nt_sol), "Erreur dans la fonction reduce_grammar : exercice 14 (variables)"
assert eq_set(eq_atom, exo13red_r, exo13red_r_sol), "Erreur dans la fonction reduce_grammar : exercice 14 (regles)"

print("Test 4 : Fonction reduce_grammar")
print("---------------------------")
g2red_nt,g2red_t,g2red_r,g2red_si,g2red_eqnt=reduce_grammar(g_red)
g2red_nt_sol = ["S","T"]
g2red_r_sol = [("S",[["a","T"],["a","b","T","S"]]),\
		("T",[["b","T"],["a"]])]


assert eq_set(eq_atom, g2red_nt, g2red_nt_sol), "Erreur dans la fonction reduce_grammar : exercice 14 (variables)"
assert eq_set(eq_atom, g2red_r,g2red_r_sol), "Erreur dans la fonction reduce_grammar : exercice 14 (regles)"
