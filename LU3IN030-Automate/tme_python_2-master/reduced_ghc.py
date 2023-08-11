#============================================#
# UE Calculabilite L3                        #
# TME GHC reduites                           #
# Mathieu.Jaume@lip6.fr                      #
#============================================#

from ghc import *

# Symboles productifs
# -------------------

def prod0(t,r):
    # t : symboles terminaux
    # r : liste de productions
    l = []
    for (pile, trans) in r:
    	for ptr in trans:
    		if False not in [ tr in t for tr in ptr ]:
    			l.append(pile)
    			break
    return l

def next_prod(t,r,eqnt,prev):
    # t : symboles terminaux
    # r : liste de productions
    # eqnt : egalite sur les non terminaux
    # prev : liste de non terminaux de depart
    # A COMPLETER
    l = prev[:]
    for (pile, trans) in r:
    	if pile in l:
    		continue
    	for ptr in trans:
    		if False not in [ tr in t+prev for tr in ptr ]:
    			l.append(pile)
    			break
    return l


def prod(t,r,eqnt):
    # t : symboles terminaux
    # r : liste de productions
    # eqnt : egalite sur les non terminaux
    def prod_rec(prev):
    	new_prod = next_prod(t,r,eqnt,prev)
    	if eq_set(eqnt,prev,new_prod):
    		return prev
    	return prod_rec(new_prod)
    return prod_rec(prod0(t,r))

# Elimination des symboles non productifs d'une GHC
# -------------------------------------------------

# elimination de non-terminaux n'appartenant pas a l'ensemble ent (ainsi que
# des productions contenant un symbole n'appartenant pas a ent

def remove_nt(g,ent):
    # g : ghc
    # ent : symboles non terminaux
    nt,t,r,si,eqnt = g
    def is_terminal(x):
        return is_in(eq_atom,x,t)
    def is_in_ent(x):
        return is_in(eqnt,x,ent)
    def is_in_ent_or_terminal(x):
        return is_in_ent(x) or is_terminal(x)
    def all_in_ent_or_terminal(l):
        return forall_such_that(l,is_in_ent_or_terminal)
    new_r = [(s,[l for l in ls if all_in_ent_or_terminal(l)]) \
             for s,ls in r if is_in_ent(s)]
    if is_in_ent(si):
        return (ent,t,new_r,si,eqnt)
    else:
        return (ent,t,new_r,None,eqnt)


def remove_non_prod(g):
    # g : ghc
    # A COMPLETER
    nt,t,r,si,eqnt = g
    return remove_nt(g,prod(t,r, eqnt))

# Symboles accessibles
# --------------------

def next_reach(nt,r,eqnt,prev):
    # nt : symboles non terminaux
    # r : liste de productions
    # eqnt : egalite sur les non terminaux
    # prev : liste de non terminaux de depart
    # A COMPLETER
    l = prev[:]
    for p in prev:
    	for (pile,trans) in r:
    		if p==pile:
    			for ptr in trans:
    				for c in ptr:
    					if c not in l and c in nt:
    						l.append(c)
    return l

def reach(nt,r,si,eqnt):
    # nt : symboles non terminaux
    # r : liste de productions
    # si : symbole de depart
    # eqnt : egalite sur les non terminaux
    # A COMPLETER
    def reach_rec(prev):
    	new_reach = next_reach(nt,r,eqnt,prev)
    	if eq_set(eqnt,prev,new_reach):
    		return prev
    	return reach_rec(new_reach)
    return reach_rec([si])

# Elimination des symboles non accessibles d'une GHC
# -------------------------------------------------

def remove_non_reach(g):
    # g : ghc
    # A COMPLETER
    nt,t,r,si,eqnt = g
    return remove_nt(g,reach(nt,r,si,eqnt))

# Reduction d'une grammaire hors-contexte
# ---------------------------------------

def reduce_grammar(g):
    # g : ghc
    # A COMPLETER
    return remove_non_reach(remove_non_prod(g))
