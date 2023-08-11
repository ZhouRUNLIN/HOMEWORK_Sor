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
g_red=(g_red_nt, g_red_t, g_red_r, g_red_s)

eq_set_atom = make_eq_set(eq_atom)

print("\n\n----------------------------------------------\n\n")
print("Test 1 : Fonction prod")
print("---------------------------")

assert eq_set(eq_atom, prod(g1_t,g1_r,eq_atom), ['S', 'A3', 'A6', 'A9']), "Erreur dans la fonction prod : exemple 1"

#assert eq_set(eq_atom, next_prod(g1_t, g1_r, eq_atom, next_prod(g1_t, g1_r, eq_atom, prod0(g1_t, g1_r))), ['S', 'A3', 'A6', 'A9']), "Erreur dans la fonction next_prod : exemple 1 - 2 etapes."


print("Test 2 : Fonction prod")
print("---------------------------")

assert eq_set(eq_atom, prod(exo11_a_t,exo11_a_r,eq_atom), ['S', 'A', 'B']), "Erreur dans la fonction prod : exo12a"

#assert eq_set(eq_atom, next_prod(exo11_a_t, exo11_a_r, eq_atom, next_prod(exo11_a_t, exo11_a_r, eq_atom, prod0(exo11_a_t, exo11_a_r))), ['S', 'A', 'B']), "Erreur dans la fonction next_prod : exo11_a - 2 etapes."

print("Test 3 : Fonction prod")
print("---------------------------")

assert eq_set(eq_atom, prod(exo11_b_t,exo11_b_r,eq_atom), ['S', 'A', 'B']), "Erreur dans la fonction prod : exo12_b"

#assert eq_set(eq_atom, next_prod(exo11_b_t, exo11_b_r, eq_atom, next_prod(exo11_b_t, exo11_b_r, eq_atom, prod0(exo11_b_t, exo11_b_r))),['S', 'A', 'B']), "Erreur dans la fonction next_prod : exo11b - 2 etapes."


print("Test 4 : Fonction prod")
print("-----------------------")


assert eq_set(eq_atom, prod(exo12_t,exo12_r,eq_atom), ['S', 'A', 'B', 'C']), "Erreur dans la fonction prod : exo13"

#assert eq_set(eq_atom, next_prod(exo12_t, exo12_r, eq_atom, next_prod(exo12_t, exo12_r, eq_atom, prod0(exo12_t, exo12_r))),['S', 'A', 'B', 'C']), "Erreur dans la fonction next_prod : exo12 - 2 etapes."



print("Test 5 : Fonction prod")
print("-----------------------")


assert eq_set(eq_atom, prod(exo13_t,exo13_r,eq_atom), ['S0', 'S2', 'S3', 'S4', 'S5']), "Erreur dans la fonction prod : exo14"

#assert eq_set(eq_atom, next_prod(exo13_t, exo13_r, eq_atom, next_prod(exo13_t, exo13_r, eq_atom, prod0(exo13_t, exo13_r))), ['S0', 'S2', 'S3', 'S4', 'S5']), "Erreur dans la fonction next_prod : exo13 - 2 etapes."

print("Test 6 : Fonction prod")
print("-----------------------")


assert eq_set(eq_atom, prod(g_red_t,g_red_r,eq_atom), ['S', 'T', 'V']), "Erreur dans la fonction prod"
