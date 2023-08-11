#=====================================================================#
# UE Calculabilite L3                                                 #
# TME Machines de Turing : Machines de Turing deterministes           #
# Mathieu.Jaume@lip6.fr                                               #
#=====================================================================#
import ensembles as es

#=====================================================================#
# Machines de Turing deterministes a 1 bande                          #
#=====================================================================#


# Fonction associee a une liste representant une fonction sur un domaine fini
#----------------------------------------------------------------------------

def assoc_f(lf,x):
    """ list[alpha*beta] * alpha -> beta """
    for (xf,yf) in lf:
        if xf == x:
            return yf
    return None

# Machine de Turing deterministe a 1 bande
#-----------------------------------------
#
# M = (d,q0,qok,qko)
# d = ((q,a),(q',a',m))
#

# exemple

l_M_ex1 = [((0,"A"),(1,"A","R")), ((0,"a"),(3,"a","R")), ((0,"b"),(3,"b","R")),
           ((0,"B"),(3,"B","R")), ((1,"A"),(3,"A","R")), ((1,"B"),(2,"B","R")),
           ((1,"a"),(1,"b","R")), ((1,"b"),(1,"a","R"))]

M_ex1 =(l_M_ex1,0,2,3)


# Affichage d'une configuration pour une machine de Turing a 1 bande
#-------------------------------------------------------------------

def print_config_1(L,t,q,qok,qko):
    for s in L[:t]:
        print(s,end='')
    print("|",end='')
    if q == qok:
        print("ok",end='')
    elif q == qko:
        print("ko",end='')
    else:
        print(q,end='')
    print("|",end='')
    for s in L[t:]:
        print(s,end='')
    print(" ")


# Execution d'une machine de Turing deterministe a 1 bande
#---------------------------------------------------------

def exec_MT_1(M,L,i0):
    # M : machine de Turing deterministe a 1 bande
    # L : liste representant la bande initiale
    # i0 : position initiale de la tete de lecture
    _,q0,_,_ = M
    return exec_MT_1_rec(M,L,q0,i0)

def exec_MT_1_rec(M,L,q,i0):
    d,_,qok,qko = M
    print_config_1(L,i0,q,qok,qko)
    if es.eq_atom(q,qok):
        return (True,i0,L.copy())
    if es.eq_atom(q,qko):
        return (False,i0,L.copy())
    tr_next = assoc_f(d,(q,L[i0]))
    if tr_next == None:
        return (False,i0,L.copy())
    q_next,c_next,dir = tr_next
    L_next = L.copy()
    L_next[i0] = c_next
    if es.eq_atom(dir,"R"):
        i0 += 1
        if i0 == len(L):
            L_next.append("Z")
    if es.eq_atom(dir,"L"):
        if i0 == 0:
            L_next = ["Z"] + L_next
        else:
            i0 -= 1
    return exec_MT_1_rec(M,L_next,q_next,i0)

l_ex2 = [((0,"a"),(1,"X","R")),((1,"a"),(1,"a","R")),((1,"X"),(1,"X","R")),((1,"Y"),(1,"Y","R")),((1,"b"),(2,"Y","L")),((1,"Z"),(6,"Z","L")),
         ((0,"b"),(4,"Y","R")),((4,"b"),(4,"b","R")),((4,"X"),(4,"X","R")),((4,"Y"),(4,"Y","R")),((4,"a"),(2,"X","L")),((4,"Z"),(6,"Z","L")),
         ((2,"a"),(2,"a","L")),((2,"b"),(2,"b","L")),((2,"X"),(2,"X","L")),((2,"Y"),(2,"Y","L")),((2,"Z"),(3,"Z","R")),
         ((3,"X"),(3,"X","R")),((3,"Y"),(3,"Y","R")),((3,"Z"),(5,"Z","L")),((3,"a"),(1,"X","R")),((3,"b"),(4,"Y","R")),((0,"Z"),(5,"Z","R"))]
M_ex2 =(l_ex2,0,5,6)

d_isneg = [((0, '0'), (1, '0', 'R')), ((0, '1'), (2, '1', 'R')), ((1, '0'), (1,'0', 'R')), ((1, '1'), (2, '1', 'R')), ((1, 'Z'), (3, 'Z', 'L')), ((2, '0'), (1, '0', 'R')), ((2, '1'), (2, '1', 'R')), ((2, 'Z'), (4, 'Z', 'L')), ((3, '0'), (3, '0', 'L')), ((3, '1'), (3, '1', 'L')), ((3, 'Z'), (5, 'Z', 'R')), ((4, '0'), (4, '0', 'L')), ((4, '1'), (4, '1', 'L')), ((4, 'Z'), (6, 'Z', 'R'))]
M_isneg =(d_isneg,0,6,5)

#============================================================#
# Composition de machines de Turing                          #
#============================================================#

# Machines de Turing utiles pour le TME
#---------------------------------------

# complement binaire et repositionnement de la tete de lecture au debut
# bande = mot binaire  se terminant par Z

d_compl_bin = [((0,"0"),(0,"1","R")), ((0,"1"),(0,"0","R")),\
               ((0,"Z"),(1,"Z","L")), ((1,"0"),(1,"0","L")),\
               ((1,"1"),(1,"1","L")), ((1,"Z"),(2,"Z","R"))]

M_compl_bin = (d_compl_bin,0,2,3)

# successeur d'un entier en representation binaire (bit de poids faibles a gauche)
# et repositionnement de la tete de lecture sur le bit de poids faible
# bande = mot binaire avec bits de poids faibles a gauche et se terminant par Z

d_succ_bin = [((0,"0"),(1,"1","L")), ((0,"1"),(0,"0","R")),\
              ((0,"Z"),(2,"1","R")), ((1,"0"),(1,"0","L")),\
              ((1,"1"),(1,"1","L")), ((1,"Z"),(3,"Z","R")),\
              ((2,"Z"),(1,"Z","L"))]

M_succ_bin = (d_succ_bin,0,3,4)

# fonction identite

M_id =([],0,0,1)

# Fonction qui construit une machine de Turing permettant de determiner
# si le symbole sous la tete de lecture est le caractere x et ne modifie
# pas la position de la tete de lecture
# C'est la MT qui accepte le langage { x }

def make_test_eq(c,alphabet):
    d = []
    for x in alphabet:
        if c==x:
            d = d + [((0,c),(1,c,"R"))]
        else:
            d = d + [((0,x),(2,x,"R"))]
        d = d + [((1,x),(3,x,"L")), ((2,x),(4,x,"L"))]
    M = (d,0,3,4)
    return M

# exemple

M_eq_0 = make_test_eq("0",["0","1","Z"])

def make_test_neq(c,alphabet):
    (d,q0,qok,qko) = make_test_eq(c,alphabet)
    return (d,q0,qko,qok)

M_neq_1 = make_test_neq("1",["0","1","Z"])

# deplacement de la tete de lecture a droite :

def make_MTright(alphabet):
    d = []
    for a in alphabet:
        d = d + [((0,a),(1,a,"R"))]
    M = (d,0,1,2)
    return M

M_Right_bin = make_MTright(["0","1","Z"])

# (propagation du bit de signe) : duplication du dernier bit d'un mot binaire


d_prop1 = [((0,"0"),(1,"0","R")), ((0,"1"),(2,"1","R")), \
           ((1,"0"),(1,"0","R")), ((1,"1"),(2,"1","R")), ((1,"Z"),(3,"0","L")),\
           ((2,"0"),(1,"0","R")), ((2,"1"),(2,"1","R")), ((2,"Z"),(3,"1","L")),\
           ((3,"0"),(3,"0","L")), ((3,"1"),(3,"1","L")), ((3,"Z"),(4,"Z","R"))]


M_prop1 =(d_prop1,0,4,5)

# Composition de machines de Turing : sequence
#---------------------------------------------

def exec_seq_MT_1(M1,M2,L,i1):
    (b,i2,L2)=exec_MT_1(M1,L,i1)
    if b:
        return exec_MT_1(M2,L2,i2)
    else:
        return (b,i2,L2)

def make_seq_MT(M1,M2):
    # M1,M2 : machines de Turing deterministes a 1 bande
    d1,q01,qok1,qko1 = M1
    d2,q02,qok2,qko2 = M2
    d = []
    for tr in d2:
      (q1,a1),(q2,a2,dpie) = tr
      d.append((((2,q1),a1),((2,q2),a2,dpie)))
      if q1 == q02 and q01 == qok1:
        d.append((((1,q01),a1),((2,q2),a2,dpie)))
    for tr in d1:
      (q1,a1),(q2,a2,dpie) = tr
      if q2 != qko1 and q2 != qok1:
        d.append((((1,q1),a1),((1,q2),a2,dpie)))
      elif q2 == qko1:
        d.append((((1,q1),a1),((2,qko2),a2,dpie)))
      elif q2 == qok1:
        d.append((((1,q1),a1),((2,q02),a2,dpie)))
    return (d,(1,q01),(2,qok2),(2,qko2))
  
M_opp_int_bin = make_seq_MT(M_compl_bin, M_succ_bin)

# Composition de machines de Turing : conditionnelle
#---------------------------------------------------

def exec_cond_MT_1(MC,M1,M2,L,i0):
    (bc,ic,Lc)=exec_MT_1(MC,L,i0)
    if bc:
        return exec_MT_1(M1,Lc,ic)
    else:
        return exec_MT_1(M2,Lc,ic)


def make_cond_MT(MC,M1,M2):
    # MC, M1, M2 : machines de Turing deterministes a 1 bande
    dC,q0C,qokC,qkoC = MC
    d1,q01,qok1,qko1 = M1
    d2,q02,qok2,qko2 = M2
    d = []
    for tr in d2:
      (q1,a1),(q2,a2,dpie) = tr
      d.append((((2,q1),a1),((2,q2),a2,dpie)))
    for tr in dC:
      (q1,a1),(q2,a2,dpie) = tr
      if q2 != qkoC and q2 != qokC:
        d.append((((0,q1),a1),((0,q2),a2,dpie)))
      elif q2 == qkoC:
        d.append((((0,q1),a1),((2,q02),a2,dpie)))
      elif q2 == qokC:
        d.append((((0,q1),a1),((1,q01),a2,dpie)))
    for tr in d1:
      (q1,a1),(q2,a2,dpie) = tr
      if q2 != qko1 and q2 != qok1:
        d.append((((1,q1),a1),((1,q2),a2,dpie)))
      elif q2 == qko1:
        d.append((((1,q1),a1),((2,qko2),a2,dpie)))
      elif q2 == qok1:
        d.append((((1,q1),a1),((2,qok2),a2,dpie)))
        if q0C == qokC and q1 == q01:
          d.append((((0,q0C),a1),((1,q2),a2,dpie)))
    return (d,(0,q0C),(2,qok2),(2,qko2))

# Composition de machines de Turing : boucle
#-------------------------------------------

def exec_loop_MT_1(MC,M,L,i0):
    (bc,ic,Lc)=exec_MT_1(MC,L,i0)
    if bc:
        (bM,iM,LM) = exec_MT_1(M,Lc,ic)
        if bM:
            return exec_loop_MT_1(MC,M,LM,iM)
        else:
            return (False,iM,LM)
    else:
        return (True,ic,Lc)

def make_loop_MT(MC,M):
    # MC,M : machines de Turing deterministes a 1 bande
    dC,q0C,qokC,qkoC = MC
    dM,q0M,qokM,qkoM = M
    d = []
    for tr in dC:
      (q1,a1),(q2,a2,dpie) = tr
      if q2 != qokC:
        d.append((((0,q1),a1),((0,q2),a2,dpie)))
      elif q2 == qokC:
        d.append((((0,q1),a1),((1,q0M),a2,dpie)))

    for tr in dM:
      (q1,a1),(q2,a2,dpie) = tr
      if q2 != qokM:
        d.append((((1,q1),a1),((1,q2),a2,dpie)))
      elif q2 == qokM:
        d.append((((1,q1),a1),((0,q0C),a2,dpie)))
    return (d,(0,q0C),(0,qkoC),(1,qkoM))

d_foo1 = [((0, '0'), (1, '0', 'R')), ((1, '0'), (3, '0', 'L')), ((2, '0'), (4, '0', 'L')), ((0, '1'), (2, '1', 'R')), ((1, '1'), (3, '1', 'L')), ((2, '1'), (4, '1', 'L')), ((0, 'Z'), (2, 'Z', 'R')), ((1, 'Z'), (3, 'Z', 'L')), ((2, 'Z'), (4, 'Z', 'L'))]
m_foo1 = (d_foo1,0,3,4)
M_foo1 = make_loop_MT(m_foo1, M_Right_bin)
M_foo0 = make_cond_MT(M_isneg,M_prop1,M_id)
M_foo2 = make_seq_MT(M_foo0,M_foo1)
d_eq_Z = [((0, 'Z'), (1, 'Z', 'R')),((0, '1'), (2, '1', 'R')),((0, '2'), (2, '2', 'R')),((1, '1'), (3, '1', 'L')),((1, '2'), (3, '2', 'L')),((1, 'Z'), (3, 'Z', 'L')),((2, '1'), (4, '1', 'L')),((2, '2'), (4, '2', 'L')),((2, 'Z'), (4, 'Z', 'L'))]
M_eq_Z = (d_eq_Z,0,3,4)
M_foo3 = make_seq_MT(M_Right_bin,M_compl_bin)
M_foo4 = make_cond_MT(M_eq_Z,M_id,M_foo3)
M_foo = make_seq_MT(M_foo2,M_foo4)
#======================================================================#
# Machines de Turing deterministes a k bandes                          #
#======================================================================#

# Machine de Turing deterministe a k bandes
#
# M = (d,q0,qok,qko)
#
# d = [((q,(a1,...,an)),(q',(a'1,...,a'n),(m1,...,mn))),...]
#
# bandes : L = [L1,...,Ln]
#

# Affichage d'une configuration pour une machine de Turing a k bandes
#--------------------------------------------------------------------

def print_config_k(L,T,q,qok,qko,k):
    for i in range(k):
        print_config_1(L[i],T[i],q,qok,qko)


def exec_MT_k(M,k,L,T):
    # M : machine de Turing deterministe a k bandes
    # k : nombre de bandes
    # L : liste des representations des bandes initiales
    # T : positions initiales des k tetes de lecture
    _,q0,_,_ = M
    return exec_MT_k_rec(M,k,q0,L,T)
  
def exec_MT_k_rec(M,k,q,L,T):
    d,_,qok,qko = M
    print_config_k(L,T,q,qok,qko,k)
    if es.eq_atom(q,qok):
        return (True,T,L.copy())
    if es.eq_atom(q,qko):
        return (False,T,L.copy())
    l_position = []
    for indice in range(k):
      l_position.append(L[indice][T[indice]])
    tr_next = assoc_f(d,(q,tuple(l_position)))
    if tr_next == None:
        return (False,T,L.copy())
    q_next,c_next,dir = tr_next
    L_next = L.copy()
    T_next = T.copy()
    for indice in range(k):
        L_next[indice][T_next[indice]] = c_next[indice]
        if es.eq_atom(dir[indice],"R"):
            T_next[indice] += 1
            if T_next[indice] == len(L[indice]):
                L_next[indice].append("Z")
        if es.eq_atom(dir[indice],"L"):
            if T_next[indice] == 0:
                L_next[indice] = ["Z"] + L_next[indice]
            else:
                T_next[indice] -= 1
        if es.eq_atom(dir[indice],"S"):
            pass
    return exec_MT_k_rec(M,k,q_next,L_next,T_next)

d2_ex1 = [((0,("a","Z")),(1,("a","X"),("R","R"))),
           ((0,("b","Z")),(2,("b","X"),("R","R"))),
  ((0,("Z","Z")),(3,("Z","Z"),("S","S"))),((1,("a","X")),(1,("a","X"),("R","R"))),
  ((1,("a","Z")),(1,("a","Z"),("R","R"))),
  ((1,("b","Z")),(1,("b","Z"),("R","L"))),
  ((1,("b","X")),(2,("b","X"),("R","R"))),
  ((1,("Z","X")),(3,("Z","X"),("S","S"))),
  ((2,("a","X")),(1,("a","X"),("R","R"))),
  ((2,("a","Z")),(2,("a","Z"),("R","L"))),
  ((2,("b","Z")),(2,("b","Z"),("R","R"))),
  ((2,("b","X")),(2,("b","X"),("R","R"))),
  ((2,("Z","X")),(3,("Z","X"),("S","S")))]
M2_ex1 = (d2_ex1,0,3,4)


# mots sur {a,b} contenant autant de a que de b
#

d2_ex1 = [((0,("a","Z")),(1,("a","X"),("R","R"))),\
          ((0,("b","Z")),(2,("b","X"),("R","R"))),\
          ((0,("Z","Z")),(3,("Z","Z"),("S","S"))),\
          ((1,("a","X")),(1,("a","X"),("R","R"))),\
          ((1,("a","Z")),(1,("a","Z"),("R","R"))),\
          ((1,("b","Z")),(1,("b","Z"),("R","L"))),\
          ((1,("b","X")),(2,("b","X"),("R","R"))),\
          ((1,("Z","X")),(3,("Z","X"),("S","S"))),\
          ((2,("a","X")),(1,("a","X"),("R","R"))),\
          ((2,("a","Z")),(2,("a","Z"),("R","L"))),\
          ((2,("b","Z")),(2,("b","Z"),("R","R"))),\
          ((2,("b","X")),(2,("b","X"),("R","R"))),\
          ((2,("Z","X")),(3,("Z","X"),("S","S")))]


M2_ex1 = (d2_ex1,0,3,4)
  
d2_palin_bin = [((0,("Z","Z")),(1,("Z","Z"),("L","S"))),((0,("0","Z")),(0,("0","0"),("R","R"))),((0,("1","Z")),(0,("1","1"),("R","R"))),
  ((1,("0","Z")),(1,("0","Z"),("L","S"))),((1,("1","Z")),(1,("1","Z"),("L","S"))),((1,("Z","Z")),(2,("Z","Z"),("R","L"))),
  ((2,("Z","Z")),(3,("Z","Z"),("S","S"))),((2,("1","1")),(2,("X","X"),("R","L"))),((2,("0","0")),(2,("X","X"),("R","L"))),
  ((2,("1","0")),(4,("1","0"),("S","S"))),((2,("0","1")),(4,("0","1"),("S","S"))),
  ((-1,("Z","Z")),(-2,("Z","Z"),("R","R"))),((-2,("Z","Z")),(3,("Z","Z"),("L","L"))),((-1,("0","Z")),(0,("0","Z"),("S","S"))),((-1,("1","Z")),(0,("1","Z"),("S","S")))]
M2_palin_bin =(d2_palin_bin,-1,3,4)

d2_un_to_bin = [((0,("$","Z")),(1,("$","0"),("R","R"))),((1,("Z","Z")),(2,("Z","Z"),("S","L"))),((2,("Z","0")),(2,("Z","0"),("S","L"))),
  ((2,("Z","1")),(2,("Z","1"),("S","L"))),((2,("Z","Z")),(3,("Z","$"),("S","R"))),((1,("I","Z")),(4,("I","Z"),("S","L"))),((4,("I","0")),(5,("I","1"),("S","R"))),((4,("I","1")),(4,("I","0"),("S","L"))),((4,("I","Z")),(5,("I","1"),("S","R"))),((5,("I","0")),(5,("I","0"),("S","R"))),((5,("I","1")),(5,("I","1"),("S","R"))),((5,("I","Z")),(1,("I","Z"),("R","S"))),
  ((-1,("$","Z")),(-1,("$","Z"),("R","S"))),((-1,("Z","Z")),(-2,("Z","$"),("S","R"))),((-2,("Z","Z")),(3,("Z","0"),("S","S"))),
  ((-1,("I","Z")),(-3,("I","Z"),("R","S"))),((-3,("I","Z")),(-4,("I","Z"),("L","S"))),((-4,("I","Z")),(0,("I","Z"),("L","S"))),
  ((-3,("Z","Z")),(-5,("Z","$"),("S","R"))),((-5,("Z","Z")),(3,("Z","1"),("S","R")))]

M2_un_to_bin =(d2_un_to_bin,-1,3,None)