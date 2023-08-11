from ensembles import *
from automates_finis import *
from automates_finis_det import *

ex_A = ([0,1,2,3,4],\
        [(0,None,2),(0,None,3),(1,"b",1),(1,"a",2),(1,"b",3), \
         (1,"b",4),(3,"a",1),(3,"b",1),(3,None,2),(4,"a",0),(4,None,0)],\
        [0],[2],eq_atom)

(ex_S,ex_T,ex_I,ex_F,ex_eqS) = ex_A

print("test : eps_cl")
print(eps_cl(ex_eqS,0,ex_T))
print(eps_cl(ex_eqS,1,ex_T))
print(eps_cl(ex_eqS,4,ex_T))
print(eps_cl_set(ex_eqS,[0,1],ex_T))

ex_RcoR = ([0,1,2,3,4,5],[(0,"a",1), (0,"a",2), (1,"a",1), (1,"b",5),\
                           (2,None,3), (3,"a",3), (3,"b",3), (4,"b",3),\
                           (4,"a",5)],\
            [0],[1,5],eq_atom)

print("test : reach_A")         
print(reach_A(ex_RcoR))
print("test : co_reach_A")
print(co_reach_A(ex_RcoR))

print("test : is_deterministic")
print(is_deterministic(ex_A))

print("test : make_det")
print(make_det(ex_A))

#exe 2.1
ex_BA = (["2D","2A","G","1"],\
        [("2D","R","1"),("2D","D","G"),("2D","A","2A"),("2A","A","2D"),("2A","D","2A"),("2A","R","1"),("2G","R","1"),("2G","D","2A"),("2G","A","2A"),("1","A","1"),("1","D","1"),("1","R","2A"),("1","R","2D")],\
                ["2D","2A","1"],["G"],eq_atom)
print(make_det(ex_BA))