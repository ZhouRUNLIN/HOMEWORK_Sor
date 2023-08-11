from automates_a_pile import *

a1_st = ["q0","q1","q2"]
a1_alph = ["a","b","c"]
a1_stack_alph = ["z0"]
a1_t_rel = [(("q0","z0","a"),("q0",["z0","z0"])),\
             (("q0","z0",None),("q1",["z0"])),\
             (("q1","z0","b"),("q1",["z0"])),\
             (("q1","z0",None),("q2",[])),\
             (("q2","z0","c"),("q2",[]))]
a1_init_st = "q0"
a1_init_stack = "z0"
a1_accept_mode = 0
a1_final_st = []
a1_eq_st = eq_atom
a1_a = (a1_st,a1_alph,a1_stack_alph,a1_t_rel,a1_init_st,\
         a1_init_stack,a1_accept_mode,a1_final_st,a1_eq_st)

print(is_in_LA(a1_a,[]))
print(is_in_LA(a1_a,["a", "c"]))
print(is_in_LA(a1_a,["a", "a", "b", "b", "b", "b", "c"]))
print(is_in_LA(a1_a,["a", "a", "a", "c", "c", "c"]))
print(is_in_LA(a1_a,["a", "a", "b", "b", "b", "b", "b", "b", "b", "c", "c"]))
print(is_in_LA(a1_a,["b", "b", "a", "a", "a", "c", "c", "c"]))
