from proper_grammar import *


g2_nt = ['S', 'A3', 'A6', 'A9']
g2_r = [('S', [['A3']]), ('A3', [['a', 'A3', 'A9'], ['A6']]), ('A6', [['b', 'A6'], []]), ('A9', [['c']])]
g2_eqnt = eq_atom
g2_t =['a', 'b', 'c']
g2_s = 'S'
g2_g = ( g2_nt, g2_t, g2_r, g2_s, g2_eqnt)


g3_nt = ['S', 'A3', 'A6', 'A9']
g3_r = [('S', [['A3']]), ('A3', [['A6'], ['a', 'A9'], ['a', 'A3', 'A9']]), ('A6', [['b'], ['b', 'A6']]), ('A9', [['c']])]
g3_eqnt = eq_atom
g3_t =['a', 'b', 'c']
g3_s = 'S'
g3_g = ( g3_nt, g3_t, g3_r, g3_s, g3_eqnt)

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

C1 = remove_unit_pairs(g1_g)
C2 = remove_unit_pairs(g2_g)
C3 = remove_unit_pairs(g3_g)
C11_a = remove_unit_pairs(exo11_a_g)
C11_b = remove_unit_pairs(exo11_b_g)
C12 = remove_unit_pairs(exo12_g)
C13 = remove_unit_pairs(exo13_g)


SC1 = (['S', 'A1', 'A2', 'A3', 'A4', 'A5', 'A6', 'A7', 'A8', 'A9'], ['a', 'b', 'c'], [('A1', [['b', 'A4'], ['a', 'A1', 'A1'], ['a', 'A2', 'A4'], ['a', 'A3', 'A7']]), ('A2', [['b', 'A5'], ['a', 'A1', 'A2'], ['a', 'A2', 'A5'], ['a', 'A3', 'A8']]), ('A3', [[], ['b', 'A6'], ['a', 'A1', 'A3'], ['a', 'A2', 'A6'], ['a', 'A3', 'A9']]), ('A4', [['b', 'A4']]), ('A5', [['b', 'A5']]), ('A6', [['b', 'A6'], []]), ('A9', [['c']]), ('S', [['a', 'A3', 'A7'], ['a', 'A2', 'A4'], ['a', 'A1', 'A1'], ['a', 'A3', 'A8'], ['a', 'A2', 'A5'], ['a', 'A1', 'A2'], ['a', 'A3', 'A9'], ['a', 'A2', 'A6'], ['a', 'A1', 'A3'], ['b', 'A4'], ['b', 'A5'], [], ['b', 'A6']])], 'S', eq_atom)

SC2 = (['S', 'A3', 'A6', 'A9'], ['a', 'b', 'c'], [('A3', [[], ['b', 'A6'], ['a', 'A3', 'A9']]), ('A6', [['b', 'A6'], []]), ('A9', [['c']]), ('S', [['a', 'A3', 'A9'], [], ['b', 'A6']])], 'S', eq_atom)

SC3 =(['S', 'A3', 'A6', 'A9'], ['a', 'b', 'c'], [('A3', [['b', 'A6'], ['b'], ['a', 'A9'], ['a', 'A3', 'A9']]), ('A6', [['b'], ['b', 'A6']]), ('A9', [['c']]), ('S', [['a', 'A3', 'A9'], ['a', 'A9'], ['b', 'A6'], ['b']])], 'S', eq_atom)

SC11_a = (['S', 'A', 'B'], ['a', 'b'], [('S', [['A', 'S', 'B'], []]), ('A', [['a', 'A', 'S'], ['a']]), ('B', [['a'], ['a', 'A', 'S'], ['S', 'b', 'S'], ['b', 'b']])], 'S', eq_atom)

SC11_b =(['S', 'A', 'B'], ['a', 'b'], [('S', [[], ['A', 'B'], ['a', 'A'], ['A', 'A', 'A']]), ('A', [['a', 'A'], ['A', 'B'], []]), ('B', [[], ['A', 'B'], ['a', 'A']])], 'S', eq_atom)

SC12 = (['S', 'A', 'B', 'C'], ['a', 'b'], [('S', [['A', 'B'], ['B', 'C']]), ('A', [['B', 'A'], ['a']]), ('B', [['C', 'C'], ['b']]), ('C', [['A', 'B'], ['a']])], 'S', eq_atom)


SC13 = (['S0', 'S1', 'S2', 'S3', 'S4', 'S5'], ['a', 'b', 'c'], [('S0', [['b'], [], ['c', 'S4'], ['S1', 'S2'], ['S3', 'S4']]), ('S1', [['S1', 'S1'], ['S1', 'S4']]), ('S2', [['S5', 'a', 'S3'], [], ['c', 'S4'], ['a', 'S2']]), ('S3', [['a', 'S2'], [], ['c', 'S4'], ['S5', 'a', 'S3']]), ('S4', [['c', 'S4'], []]), ('S5', [[], ['c', 'S4'], ['b']])], 'S0', eq_atom)


def test_unit_pair(C, SC):
    for i in [0,1,3]:
        assert eq_set(eq_atom, C[i], SC[i]), "Erreur dans la fonction remove_unit_pair"
    for r1, r2 in [ ( C[ 2 ], SC[ 2 ] ), ( SC[ 2 ], C[ 2 ] ) ]:
        assert all( [
            eq_set( eq_atom, productions, prods_s( r1, eq_atom, non_terminal) )
            for non_terminal, productions in r2
        ] )
    

print("\n\n----------------------------------------------\n\n")
print("Test 0 : Fonction remove_unit_pair")
print("---------------------------")

test_unit_pair(C3,SC3)

print("Test 1 : Fonction remove_unit_pair")
print("---------------------------")

test_unit_pair(C1,SC1)
   

print("Test 2 : Fonction remove_unit_pair")
print("---------------------------")

test_unit_pair(C2,SC2)

print("Test 3 : Fonction remove_unit_pair")
print("---------------------------")

test_unit_pair(C11_a,SC11_a)


print("Test 4 : Fonction remove_unit_pair")
print("---------------------------")

test_unit_pair(C11_b,SC11_b)



print("Test 5 : Fonction remove_unit_pair")
print("---------------------------")

test_unit_pair(C12,SC12)

print("Test 6 : Fonction remove_unit_pair")
print("---------------------------")

test_unit_pair(C13,SC13)
