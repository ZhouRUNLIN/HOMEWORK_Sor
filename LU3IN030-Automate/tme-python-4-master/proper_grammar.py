#============================================#
# UE Calculabilite L3                        #
# TME GHC propres                            #
# Mathieu.Jaume@lip6.fr                      #
#============================================#




# Symboles annulables
# -------------------
from ghc import *
import ensembles
def canc0(r):
    # r : liste de productions
    def is_empty_list(l):
        return len(l)==0
    return [s for s,ls in r if exists_such_that(ls,is_empty_list)]

def next_canc(r,eqnt,prev):
    # r : liste de productions
    # eqnt : egalite sur les non terminaux
    # prev : liste de non terminaux de depart
    PNG = prev
    for (s, l) in r:  # l = [["A","S","B"],[]]
        if s not in prev:
            for p in l:  # p = ["A","S","B"]
                if len(p) == 1 and p[0] in prev:
                    PNG = PNG + [s]


    return PNG

def canc(r,eqnt):
    # r : liste de productions
    # eqnt : egalite sur les non terminaux
    def _next_canc(e):
        return next_canc(r,eqnt,e)
    return fixpoint_from(make_eq_set(eqnt),_next_canc,canc0(r))

# Elimination des epsilon-productions
# -----------------------------------

def remove_eps_prod(g):
    # g : ghc
    nt,t,r,si,eqnt = g
    canc_g = canc(r,eqnt)
    def make_new_prod(l):
        if len(l)==0:
            return [[]]
        else:
            res_rec = make_new_prod(l[1:])
            add_first = [[l[0]]+lrec for lrec in res_rec]
            if is_in(eqnt,l[0],canc_g):
                acc = add_first + res_rec
            else:
                acc = add_first
            return acc
    def make_new_prods(ls):
        res = []
        for l in ls:
            new_l = [x for x in make_new_prod(l) if len(x)>0]
            res = union(make_eq_prod(nt,eqnt),new_l,res)
        return res
    new_r = [(s,make_new_prods(ls)) for s,ls in r]
    return (nt,t,new_r,si,eqnt) 

# Egalite sur les paires de symboles non-terminaux

def make_eq_pair_nt(eqnt):
    # eqnt :  egalite sur les non terminaux
    def _eq_pair_nt(p1,p2):
        x1,y1 = p1
        x2,y2 = p2
        return eqnt(x1,x2) and eqnt(y1,y2)
    return _eq_pair_nt


# Paires unitaires
# ----------------

def unit_pair0(nt,r,eqnt):
    # nt : symboles non terminaux
    # r : liste de productions
    # eqnt : egalite sur les non terminaux
    P0G = []
    for (s, l) in r:  # l = [["A","S","B"],[]]
        for p in l:  # p = ["A","S","B"]
            if len(p) == 1 and p[0] in nt:
                P0G = P0G + [(s, p[0])]

    return P0G

def next_unit_pair(nt,r,eqnt,prev):
    # nt : symboles non terminaux
    # r : liste de productions
    # eqnt : egalite sur les non terminaux
    # prev : liste de non terminaux de depart
    def trouve_X(y,l):
        l_X = []
        for e in l:
            if e[1] == y:
                l_X.append(e[0])
        return l_X

    PNG = []
    UNG = prev
    l_Y = []
    for e in prev:
        l_Y.append(e[1])
    for (s, l) in r:  # l = [["A","S","B"],[]]
        if s in l_Y:
            for p in l:  # p = ["A","S","B"]
                if len(p) == 1 and p[0] in nt:
                    PNG = PNG + [(s, p[0])]
    for i in PNG:
        l_X = trouve_X(i[0], prev)
        for j in l_X:
            UNG = UNG + [(j, i[1])]

    return UNG

def unit_pair(nt,r,eqnt):
    # nt : symboles non terminaux
    # r : liste de productions
    # eqnt : egalite sur les non terminaux
    def _next_unit_pair(e):
        return next_unit_pair(nt,r,eqnt,e)
    return fixpoint_from(make_eq_set(make_eq_pair_nt(eqnt)),\
                         _next_unit_pair,unit_pair0(nt, r, eqnt))


# Elimination des paires unitaires
# --------------------------------

def remove_unit_pairs(g):
    # g : ghc
    g_nt, g_t, g_r, g_si, g_eqnt = g
    g1 = remove_eps_prod(g)
    g1_nt, g1_t, g1_r, g1_si, g1_eqnt = g1
    #print("g1",g1_r)
    UNG = unit_pair(g_nt, g_r, g_eqnt)
    #print("ung", UNG)
    def trouve_Y(x,l):
        l_Y = []
        for i in l:
            if i[0] == x:
                l_Y.append(i[1])
        return l_Y
    def trouve_regle(r,s):
        for (s1,l) in r:
            if s1==s:
                return l
    g2_r = []
    for (s, l) in g_r:
        l_copy = l.copy()
        for p in l:  # p = ["A","S","B"]
            if len(p) == 1 and p[0] in g1_nt:
                l_copy.remove(p)

        g2_r.append((s, l_copy))
    g3_r = []
    for(s, l) in g2_r:
        l_y = trouve_Y(s, UNG)
        #print(s, l_y)
        if l_y != []:
            for i in l_y:
                #print("wf", trouve_regle(g2_r, i))
                l = l + trouve_regle(g2_r,i)
            #print(l)
        g3_r.append((s, l))
    #print("r", g3_r)
    return g1_nt, g1_t, g3_r, g1_si, g1_eqnt

# Construction d'une grammaire propre
# -----------------------------------

def make_gp(g):
    # g : ghc
    return remove_unit_pairs(remove_eps_prod(ensembles.reduce_grammar(g)))
