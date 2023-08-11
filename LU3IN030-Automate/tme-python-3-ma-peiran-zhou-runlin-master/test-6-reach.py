from reduced_ghc import *


g2_nt = ['S', 'A3', 'A6', 'A9']
g2_r = [('S', [['A3']]), ('A3', [['a', 'A3', 'A9'], ['A6']]), ('A6', [['b', 'A6'], []]), ('A9', [['c']])]
g2_eqnt = eq_atom
prev0_g2 = 'S'

NR2 = reach(g2_nt,g2_r, prev0_g2, g2_eqnt)
SNR2 = g2_nt


# G12a
# -----

exo11_a_nt = ["S","A","B"]
exo11_a_t = ["a","b"]
exo11_a_r = [("S",[["A","S","B"],[]]),\
             ("A",[["a","A","S"],["a"]]),\
             ("B",[["S","b","S"],["A"],["b","b"]])]
exo11_a_s = "S"
exo11_a_g = (exo11_a_nt,exo11_a_t,exo11_a_r,exo11_a_s,eq_atom)

NR11_a= reach(exo11_a_nt,exo11_a_r,exo11_a_s,eq_atom)
SNR11_a = ['B', 'S', 'A']

# G12b
#-----

exo11_b_nt = ["S","A","B"]
exo11_b_t = ["a","b"]
exo11_b_r = [("S",[["A","A","A"],["B"]]),\
             ("A",[["a","A"],["A","B"],[]]),\
             ("B",[["A"]])]
exo11_b_s = "S"
exo11_b_g = (exo11_b_nt,exo11_b_t,exo11_b_r,exo11_b_s,eq_atom)

NR11_b = reach(exo11_b_nt, exo11_b_r, exo11_b_s, eq_atom)
SNR11_b = ["S","A","B"]

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

NR12 = reach(exo12_nt, exo12_r, exo12_s, eq_atom)
SNR12 = ["S","A","B","C"]

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

NR13 = reach(exo13_nt, exo13_r, exo13_s, eq_atom)
SNR13 = exo13_nt



#ex
g_red_nt = ["S","T","U","V"]
g_red_t=["a","b"]
g_red_r=[("S",[["a","T"],["b","T","U"],["a","b","T","S"],["U","V"]]),\
		("T",[["a","U"],["b","T"],["a"]]),\
		("U",[["a","U"],["b","U"]]),\
		("V",[["a","T"],["b","S"],["a"]])]
g_red_s="S"
g_red=(g_red_nt, g_red_t, g_red_r, g_red_s, eq_atom)


NRred = reach (g_red_nt, g_red_r, g_red_s, eq_atom)
SNRred = g_red_nt

#exprod
g_red2_nt = ["S","T","V"]
g_red2_t=["a","b"]
g_red2_r=[("S",[["a","T"],["a","b","T","S"]]),\
		("T",[["b","T"],["a"]]),\
		("V",[["a","T"],["b","S"],["a"]])]
g_red2_s="S"
g_red2=(g_red2_nt, g_red2_t, g_red2_r, g_red2_s, eq_atom)

NRred2 = reach (g_red2_nt, g_red2_r, g_red2_s, eq_atom)
SNRred2 = ["S","T"]

eq_set_atom = make_eq_set(eq_atom)


print("\n\n----------------------------------------------\n\n")
print("Test 1 : Fonction reach")
print("---------------------------")
NR1 = reach(g1_nt,g1_r,g1_s,eq_atom)
SNR1 = g1_nt

assert eq_set(eq_atom, NR1, SNR1), "Erreur dans la fonction reach"



print("Test 2 : Fonction reach")
print("---------------------------")

assert eq_set(eq_atom, NR2, SNR2), "Erreur dans la fonction reach"






print("Test 3 : Fonction reach")
print("---------------------------")

assert eq_set(eq_atom, NR11_a, SNR11_a), "Erreur dans la fonction reach"




print("Test 4 : Fonctionreach")
print("---------------------------")

assert eq_set(eq_atom, NR11_b, SNR11_b), "Erreur dans la fonction reach"




print("Test 5 : Fonction reach")
print("---------------------------")

assert eq_set(eq_atom, NR12, SNR12), "Erreur dans la fonction reach"




print("Test 6 : Fonction reach")
print("---------------------------")

assert eq_set(eq_atom, NR13, SNR13), "Erreur dans la fonction reach"


print("Test 7 : Fonction reach")
print("---------------------------")

assert eq_set(eq_atom, NRred, SNRred), "Erreur dans la fonction reach"


print("Test 8 : Fonction reach")
print("---------------------------")

assert eq_set(eq_atom, NRred2, SNRred2), "Erreur dans la fonction reach"






