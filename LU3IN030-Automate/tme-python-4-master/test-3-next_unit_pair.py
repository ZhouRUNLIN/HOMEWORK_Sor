from proper_grammar import *

g2_nt = ['S', 'A3', 'A6', 'A9']
g2_r = [('S', [['A3']]), ('A3', [['a', 'A3', 'A9'], ['A6']]), ('A6', [['b', 'A6'], []]), ('A9', [['c']])]
g2_eqnt = eq_atom
g2_t =['a', 'b', 'c']
g2_s = 'S'


g3_nt = ['S', 'A3', 'A6', 'A9']
g3_r = [('S', [['A3']]), ('A3', [['A6'], ['a', 'A9'], ['a', 'A3', 'A9']]), ('A6', [['b'], ['b', 'A6']]), ('A9', [['c']])]
g3_eqnt = eq_atom
g3_t =['a', 'b', 'c']
g3_s = 'S'

# Exemples / TESTS A REALISER
#----------------------------

# G11a
# -----

exo11_a_nt = ["S","A","B"]
exo11_a_t = ["a","b"]
exo11_a_r = [("S",[["A","S","B"],[]]),\
             ("A",[["a","A","S"],["a"]]),\
             ("B",[["S","b","S"],["A"],["b","b"]])]
exo11_a_s = "S"
exo11_a_g = (exo11_a_nt,exo11_a_t,exo11_a_r,exo11_a_s,eq_atom)
# sol11_a_nt,sol11_a_t,sol11_a_r,sol11_a_s,sol11_a_eqnt = make_gp(exo11_a_g)

# G11b
#-----

exo11_b_nt = ["S","A","B"]
exo11_b_t = ["a","b"]
exo11_b_r = [("S",[["A","A","A"],["B"]]),\
             ("A",[["a","A"],["A","B"],[]]),\
             ("B",[["A"]])]
exo11_b_s = "S"
exo11_b_g = (exo11_b_nt,exo11_b_t,exo11_b_r,exo11_b_s,eq_atom)


# G12
#----


exo12_nt = ["S","A","B","C"]
exo12_t = ["a","b"]
exo12_r = [("S",[["A","B"],["B","C"]]),\
           ("A",[["B","A"],["a"]]),\
           ("B",[["C","C"],["b"]]),\
           ("C",[["A","B"],["a"]])]
exo12_s = "S"
exo12_g = (exo12_nt,exo12_t,exo12_r,exo12_s,eq_atom)

# is_in_LG(exo12_g,?)

# G13
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
C1 = unit_pair(g1_nt,g1_r,eq_atom)
C2 = unit_pair(g2_nt,g2_r,g2_eqnt)
C3 = unit_pair(g3_nt,g3_r,g3_eqnt)
C11_a = unit_pair(exo11_a_nt,exo11_a_r,eq_atom)
C11_b = unit_pair(exo11_b_nt,exo11_b_r,eq_atom)
C12 = unit_pair(exo13_nt,exo13_r,eq_atom)
C13 = unit_pair(exo13_nt,exo13_r,eq_atom)


SC1 = [('S', 'A6'), ('S', 'A5'), ('S', 'A4'), ('A3', 'A6'), ('A2', 'A5'), ('A1', 'A4'), ('S', 'A3'), ('S', 'A2'), ('S', 'A1')]
SC2 = [('S', 'A6'), ('A3', 'A6'), ('S', 'A3')]
SC3 = [('S', 'A6'), ('A3', 'A6'), ('S', 'A3')]
SC11_a = [('B', 'A')]
SC11_b = [('S', 'A'), ('B', 'A'), ('S', 'B')]
SC12 = [('S3', 'S3'), ('S2', 'S2'), ('S2', 'S4'), ('S0', 'S4'), ('S5', 'S4'), ('S3', 'S4'), ('S3', 'S2'), ('S2', 'S3'), ('S0', 'S5')]
SC13 =[('S3', 'S3'), ('S2', 'S2'), ('S2', 'S4'), ('S0', 'S4'), ('S5', 'S4'), ('S3', 'S4'), ('S3', 'S2'), ('S2', 'S3'), ('S0', 'S5')]


print("\n\n----------------------------------------------\n\n")
print("Test 0 : Fonction unit_pair")
print("---------------------------")

assert eq_set(eq_atom, C3, SC3), "Erreur dans la fonction next_unit_pair"



print("Test 1 : Fonction unit_pair")
print("---------------------------")

assert eq_set(eq_atom, C1, SC1), "Erreur dans la fonction next_unit_pair"


print("Test 2 : Fonction unit_pair")
print("---------------------------")

assert eq_set(eq_atom, C2, SC2), "Erreur dans la fonction next_unit_pair"

print("Test 3 : Fonction unit_pair")
print("---------------------------")

assert eq_set(eq_atom, C11_a, SC11_a), "Erreur dans la fonction next_unit_pair"


print("Test 4 : Fonction unit_pair")
print("---------------------------")

assert eq_set(eq_atom, C11_b, SC11_b), "Erreur dans la fonction next_unit_pair"


print("Test 5 : Fonction unit_pair")
print("---------------------------")

assert eq_set(eq_atom, C12, SC12), "Erreur dans la fonction next_unit_pair"

print("Test 6 : Fonction unit_pair")
print("---------------------------")

assert eq_set(eq_atom, C13, SC13), "Erreur dans la fonction next_unit_pair"
