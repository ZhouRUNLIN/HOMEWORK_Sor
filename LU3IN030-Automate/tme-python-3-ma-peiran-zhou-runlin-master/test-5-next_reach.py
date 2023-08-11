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

NR1 = next_reach(g1_nt,g1_r,eq_atom,[g1_s])
NR1b = [next_reach(g1_nt,g1_r,eq_atom,[s] ) for s in NR1]
SNR1 = ['S', 'A3', 'A2', 'A1']
SNR1b = [['S', 'A3', 'A2', 'A1'], ['A9', 'A6', 'A2', 'A3', 'A1'], ['A8', 'A3', 'A5', 'A2', 'A1'], ['A7', 'A3', 'A4', 'A2', 'A1']]


#ex
g_red_nt = ["S","T","U","V"]
g_red_t=["a","b"]
g_red_r=[("S",[["a","T"],["b","T","U"],["a","b","T","S"],["U","V"]]),\
		("T",[["a","U"],["b","T"],["a"]]),\
		("U",[["a","U"],["b","U"]]),\
		("V",[["a","T"],["b","S"],["a"]])]
g_red_s="S"
g_red=(g_red_nt, g_red_t, g_red_r, g_red_s)

#exprod
g_red2_nt = ["S","T","V"]
g_red2_t=["a","b"]
g_red2_r=[("S",[["a","T"],["a","b","T","S"]]),\
		("T",[["b","T"],["a"]]),\
		("V",[["a","T"],["b","S"],["a"]])]
g_red2_s="S"
g_red2=(g_red2_nt, g_red2_t, g_red2_r, g_red2_s)

eq_set_atom = make_eq_set(eq_atom)


print("\n\n----------------------------------------------\n\n")
print("Test 1 : Fonction next_reach")
print("---------------------------")

assert eq_set(eq_atom, NR1, SNR1), "Erreur dans la fonction next_reach"
assert eq_set(eq_set_atom, NR1b ,SNR1b), "Erreur dans la fonction next_reach"

g2_nt = ['S', 'A3', 'A6', 'A9']
g2_r = [('S', [['A3']]), ('A3', [['a', 'A3', 'A9'], ['A6']]), ('A6', [['b', 'A6'], []]), ('A9', [['c']])]
g2_eqnt = eq_atom
prev0_g2 = ['S']

NR2 = next_reach(g2_nt,g2_r,g2_eqnt,prev0_g2)
NR2b = [next_reach(g2_nt,g2_r,g2_eqnt,[s]) for s in NR2]
SNR2 = ['S', 'A3']
SNR2b = [['S', 'A3'], ['A6', 'A9', 'A3']]

print("Test 2 : Fonction next_reach")
print("---------------------------")

assert eq_set(eq_atom, NR2, SNR2), "Erreur dans la fonction next_reach"
assert eq_set(eq_set_atom, NR2b, SNR2b), "Erreur dans la fonction next_reach"


NR11_a= next_reach(exo11_a_nt,exo11_a_r,eq_atom,[exo11_a_s])
NR11_ab = [next_reach(exo11_a_nt,exo11_a_r,eq_atom,[s] ) for s in NR11_a]
SNR11_a = ['B', 'S', 'A']
SNR11_ab = [['B', 'A', 'S'], ['B', 'S', 'A'], ['S', 'A']]


print("Test 3 : Fonction next_reach")
print("---------------------------")

assert eq_set(eq_atom, NR11_a, SNR11_a), "Erreur dans la fonction next_reach"
assert eq_set(eq_set_atom, NR11_ab, SNR11_ab), "Erreur dans la fonction next_reach"


NR11_b= next_reach(exo11_b_nt,exo11_b_r,eq_atom,[exo11_b_s])
NR11_bb = [next_reach(exo11_b_nt,exo11_b_r,eq_atom,[s] ) for s in NR11_b]
SNR11_b = ['S', 'B', 'A']
SNR11_bb = [['S', 'B', 'A'], ['B', 'A'], ['B', 'A']]

print("Test 4 : Fonction next_reach")
print("---------------------------")

assert eq_set(eq_atom, NR11_b, SNR11_b), "Erreur dans la fonction next_reach"
assert eq_set(eq_set_atom, NR11_bb, SNR11_bb), "Erreur dans la fonction next_reach"


NR12= next_reach(exo12_nt,exo12_r,eq_atom,[exo12_s])
NR12_b = [next_reach(exo12_nt,exo12_r,eq_atom,[s] ) for s in NR12]
SNR12 = ['S', 'C', 'B', 'A']
SNR12_b = [['S', 'C', 'B', 'A'], ['C', 'B', 'A'], ['B', 'C'], ['A', 'B']]

print("Test 5 : Fonction next_reach")
print("---------------------------")

assert eq_set(eq_atom, NR12, SNR12), "Erreur dans la fonction next_reach"
assert eq_set(eq_set_atom, NR12_b, SNR12_b), "Erreur dans la fonction next_reach"



NR13= next_reach(exo13_nt,exo13_r,eq_atom,[exo13_s])
NR13_b = [next_reach(exo13_nt,exo13_r,eq_atom,[s] ) for s in NR13]
SNR13 = ['S0', 'S5', 'S4', 'S3', 'S2', 'S1']
SNR13_b = [['S0', 'S5', 'S4', 'S3', 'S2', 'S1'], ['S5', 'S4'], ['S4'], ['S3', 'S5', 'S4', 'S2'], ['S3', 'S2'], ['S4', 'S1']]




print("Test 6 : Fonction next_reach")
print("---------------------------")

assert eq_set(eq_atom, NR13, SNR13), "Erreur dans la fonction next_reach"
assert eq_set(eq_set_atom, NR13_b, SNR13_b), "Erreur dans la fonction next_reach"


NRred= next_reach(g_red_nt,g_red_r,eq_atom,[g_red_s])
NRred_b = [next_reach(g_red_nt,g_red_r,eq_atom,[s] ) for s in NRred]
SNRred = ['S', 'T', 'U', 'V']
SNRred_b = [['S', 'T', 'U', 'V'], ['T', 'U'], ['U'], ['S', 'T', 'V']]




print("Test 7 : Fonction next_reach")
print("---------------------------")

assert eq_set(eq_atom, NRred, SNRred), "Erreur dans la fonction next_reach"
assert eq_set(eq_set_atom, NRred_b, SNRred_b), "Erreur dans la fonction next_reach"


NR2red= next_reach(g_red2_nt,g_red2_r,eq_atom,[g_red2_s])
NR2red_b = [next_reach(g_red2_nt,g_red2_r,eq_atom,[s] ) for s in NR2red]
SNR2red = ['S', 'T']
SNR2red_b = [['S', 'T'], ['T']]




print("Test 8 : Fonction next_reach")
print("---------------------------")

assert eq_set(eq_atom, NR2red, SNR2red), "Erreur dans la fonction next_reach"
assert eq_set(eq_set_atom, NR2red_b, SNR2red_b), "Erreur dans la fonction next_reach"


