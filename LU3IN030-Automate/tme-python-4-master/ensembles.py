# UE Calculabilite
# Representation des ensembles par des listes finies
# Mathieu.Jaume@lip6.fr
#===================================================

# Ensembles finis representes (naivement) par des listes sans doublons
#---------------------------------------------------------------------

# Egalite atomique

def eq_atom(x,y):
    return x==y

# Appartenance d'un element a un ensemble

def is_in(eq,x,E):
    for e in E:
        if eq(x,e):
            return True
    return False

# Inclusion d'ensembles

def is_subset(eq,E1,E2):
    for x in E1:
        if not is_in(eq,x,E2):
            return False
    return True

# Egalite d'ensembles

def eq_set(eq,E1,E2):
    return is_subset(eq,E1,E2) and is_subset(eq,E2,E1)

# >>> eq_set(eq_atom,[1,2,3],[2,1,3])
# True

def make_eq_set(eq):
    def _eq_set(E1,E2):
        return eq_set(eq,E1,E2)
    return _eq_set


# eq_set_atom = make_eq_set(eq_atom)
# >>> eq_set_atom([1,2,3],[2,1,3])
# True
# >>> eq_set_atom([1,2,3],[9,1,3])
# False

# Operations ensemblistes
#------------------------

# Ajout d'un element a un ensemble

def ajout(eq,x,E):
    if is_in(eq,x,E):
        return E
    else:
        return [x]+E

# >>> l1 = [2,4,6]
# >>> ajout(eq_atom,3,l1)
# [3, 2, 4, 6]
# >>> l1
# [2, 4, 6]

# Union de deux ensembles
# remarque : E1 peut contenir des doublons

def union(eq,E1,E2):
    R=E2
    for x in E1:
        R = ajout(eq,x,R)
    return R

# >>> l2 = [5,6,7,8]
# >>> union(eq_atom,l1,l2)
# [4, 2, 5, 6, 7, 8]
# >>> l1
# [2, 4, 6]
# >>> l2
# [5, 6, 7, 8]

# Union des ensembles presents dans une liste d'ensembles

def union_sets(eq,LE):
    r = []
    for E in LE:
        r = union(eq,E,r)
    return r


# Intersection de deux ensembles

def intersection(eq,E1,E2):
    R=[]
    for x in E1:
        if is_in(eq,x,E2):
            R=R+[x]
    return R


# difference d'ensembles

def diff_set(eq,E1,E2):
    return [e for e in E1 if not is_in(eq,e,E2)]

# >>> diff_set(eq_atom,l1,l2)
# [2, 4]
# >>> l1
# [2, 4, 6]
# >>> l2
# [5, 6, 7, 8]

# Produits cartesiens d'un ensemble

def cartesian_product(E,k):
    if k == 0:
        return []
    else:
        if k == 1:
            return [[x] for x in E]
        else:
            prev = cartesian_product(E,k-1)
            return [[x]+p for x in E for p in prev]

# Produit cartesien de n ensembles
# remarque : les elements des ensembles peuvent etre des tuples

def cartesian_prod_n(LE):
    def _cart_prod_sup_2(LL):
        k = len(LL)
        if k==0:
            return []
        else:
            if k==1:
                return LL[0]
            else:
                if LL[0]==[]:
                    return []
                else:
                    tmp = [tuple(list(x)+[y]) for x in LL[0] for y in LL[1]]
                    return _cart_prod_sup_2([tmp]+LL[2:])
    n = len(LE)
    if n==0:
        return []
    else:
        if n==1:
            return LE[0]
        else:
            tmp = [(x,y) for x in LE[0] for y in LE[1]]
            if n==2:
                return tmp
            else:
                return _cart_prod_sup_2([tmp]+LE[2:])

##L1 = [(1,2,3),(2,3,4),(5,6,7)]
##L2 = [("a","b"),("b","c")]
##L3 = [True,False]
##

##>>> cartesian_prod_n([L1,L2,L3])
##[((1, 2, 3), ('a', 'b'), True), ((1, 2, 3), ('a', 'b'), False),
## ((1, 2, 3), ('b', 'c'), True), ((1, 2, 3), ('b', 'c'), False),
## ((2, 3, 4), ('a', 'b'), True), ((2, 3, 4), ('a', 'b'), False),
## ((2, 3, 4), ('b', 'c'), True), ((2, 3, 4), ('b', 'c'), False),
## ((5, 6, 7), ('a', 'b'), True), ((5, 6, 7), ('a', 'b'), False),
## ((5, 6, 7), ('b', 'c'), True), ((5, 6, 7), ('b', 'c'), False)]



# Proprietes sur les elements d'un ensemble
#------------------------------------------

# Existence d'un element satisfaisant une propriete

def exists_such_that(E,p):
    for x in E:
        if p(x):
            return True
    return False

# Propriete verifiee par tous les elements d'un ensemble

def forall_such_that(E,p):
    for x in E:
        if not p(x):
            return False
    return True



# Calcul de point fixe par iteration
# ==================================

def fixpoint_from(eq,f,e):
    fe = f(e)
    if eq(e,fe):
        return e
    else:
        return fixpoint_from(eq,f,fe)

