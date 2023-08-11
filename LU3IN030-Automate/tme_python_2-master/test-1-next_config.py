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

a2_st = ["q0","q1"]
a2_alph = ["a","b"]
a2_stack_alph = ["Z","A","B"]
a2_t_rel = [(("q0","Z","a"),("q0",["A","Z"])),\
            (("q0","Z","b"),("q0",["B","Z"])),\
            (("q0","A","a"),("q0",["A", "A"])),\
            (("q0","A","b"),("q0",[])),\
            (("q0","B","a"),("q0",[])),\
            (("q0", "B", "b"),("q0", ["B","B"])),\
            (("q0", "Z", None), ("q1", []))]
a2_init_st = "q0"
a2_init_stack = "Z"
a2_accept_mode = 0
a2_final_st = []
a2_eq_st = eq_atom

a1_a = (a1_st,a1_alph,a1_stack_alph,a1_t_rel,a1_init_st,\
        a1_init_stack,a1_accept_mode,a1_final_st,a1_eq_st)
a2_a = (a2_st,a2_alph,a2_stack_alph,a2_t_rel,a2_init_st,\
        a2_init_stack,a2_accept_mode,a2_final_st,a2_eq_st)
#TD, ex 8

a2_a_f = (a2_st,a2_alph,a2_stack_alph,a2_t_rel,a2_init_st,\
        a2_init_stack,1,["q1"],a2_eq_st)

a3_st = ["q","p"]
a3_alph = ["0","1"]
a3_stack_alph = ["Z0","X"]
a3_t_rel = [(("q","Z0","0"),("q",["X","Z0"])),\
            (("q","X","0"),("q",["X","X"])),\
            (("q","X","1"),("q",["X"])),\
            (("q","X",None),("p",[])),\
            (("p","X",None),("p",[])),\
            (("p", "X", "1"),("p", ["X","X"])),\
            (("p", "Z0", "1"), ("p", []))]
a3_init_st = "q"
a3_init_stack = "Z0"
a3_accept_mode = 1
a3_final_st = ["p"]
a3_eq_st = eq_atom

a3_a=(a3_st, a3_alph, a3_stack_alph, a3_t_rel, a3_init_st,\
a3_init_stack, a3_accept_mode, a3_final_st, a3_eq_st)#TD3 ex 9


print("\n\n----------------------------------------------\n\n")
print("Test 1 : Fonction next_configs")
print("------------------------------------------------")
assert eq_set(eq_atom, next_configs(a1_a, ("q0", ["z0"]), ["a","b","c"]), [(("q1",["z0"]), ["a","b","c"]),(("q0", ["z0","z0"]),["b","c"])]), "Erreur dans next_configs"
assert eq_set(eq_atom, next_configs(a1_a, ("q0", ["z0"]), ["b","c"]), [(("q1",["z0"]), ["b","c"])]), "Erreur dans next_configs"
assert eq_set(eq_atom, next_configs(a1_a, ("q0", ["z0"]), []), [(("q1",["z0"]), [])]), "Erreur dans next_configs: lecture du mot vide"
assert eq_set(eq_atom, next_configs(a1_a, ("q0", []), ["a","b","c"]),[]), "Erreur dans next configs: pile vide"
assert eq_set(eq_atom, next_configs(a1_a, ("q1", ["z0"]), ["a","b","c"]), [(("q2",[]), ["a","b","c"])]), "Erreur dans next_configs"
assert eq_set(eq_atom, next_configs(a1_a, ("q1", ["z0","z0","z0"]), ["b","c"]), [(("q1",["z0","z0","z0"]), ["c"]), (("q2", ["z0","z0"]), ["b","c"])]), "Erreur dans next_configs"
assert eq_set(eq_atom, next_configs(a1_a, ("q2", ["z0"]), ["a","b","c"]), []), "Erreur dans next_configs: pas de successeur"
assert eq_set(eq_atom, next_configs(a1_a, ("q2", ["z0"]), ["b","c"]), []), "Erreur dans next_configs: pas de successeur"
assert eq_set(eq_atom, next_configs(a1_a, ("q2", ["z0"]), ["c"]), [(("q2",[]),[])]), "Erreur dans next_configs"


print("\n\n----------------------------------------------\n\n")
print("Test 2 : Fonction next_configs")
print("------------------------------------------------\n\n")
assert eq_set(eq_atom, next_configs(a2_a, ("q0", ["Z"]), ["a","b"]), [(("q1", []), ["a","b"]), (("q0", ["A","Z"]), ["b"])]), "Erreur dans next_configs"
assert eq_set(eq_atom, next_configs(a2_a, ("q0", ["Z"]), ["b","a"]), [(("q1", []), ["b", "a"]), (("q0", ["B","Z"]), ["a"])]), "Erreur dans next_configs"
assert eq_set(eq_atom, next_configs(a2_a, ("q0", ["Z"]),[]), [(("q1",[]),[])]), "Erreur dans next_configs : lecture du mot vide"
assert eq_set(eq_atom, next_configs(a2_a, ("q0", []), ["a","b"]), []), "Erreur dans next_configs : pile vide"
assert eq_set(eq_atom, next_configs(a2_a, ("q0",["A","B","B","A","A","A","A","Z"]),["b","b","a"]), [(("q0",["B","B","A","A","A","A","Z"]),["b","a"])]), "Erreur dans next_configs"
assert eq_set(eq_atom, next_configs(a2_a, ("q0",["B","A","B","B","A","A","A","A","Z"]),["b","b","a"]), [(("q0",["B","B","A","B","B","A","A","A","A","Z"]),["b","a"])]), "Erreur dans next_configs"
assert eq_set(eq_atom, next_configs(a2_a, ("q0",["A","B","B","A","A","A","A","Z"]),[]), []), "Erreur dans next_configs: pas de successeur"
assert eq_set(eq_atom, next_configs(a2_a, ("q1",["A","B","B","A","A","A","A","Z"]),[]), []), "Erreur dans next_configs: pas de successeur"
assert eq_set(eq_atom, next_configs(a2_a, ("q1",["A","B","B","A","A","A","A","Z"]),["b","b","a"]),[]), "Erreur dans next_configs: pas de successeur"
assert eq_set(eq_atom, next_configs(a2_a, ("q1",[]),[]),[]), "Erreur dans next_configs"

print("\n\n----------------------------------------------\n\n")
print("Test 3 : Fonction next_configs")
print("------------------------------------------------\n\n")
assert eq_set(eq_atom, next_configs(a3_a, ("q",["Z0"]),["0"]),[(("q",["X","Z0"]),[])]), "Erreur dans next_configs"
assert eq_set(eq_atom, next_configs(a3_a, ("q",["Z0"]),["1"]),[]), "Erreur dans next_configs: pas de successeur"
assert eq_set(eq_atom, next_configs(a3_a, ("q",["X","Z0"]),["0"]),[(("q",["X","X","Z0"]),[]), (("p",["Z0"]),["0"])]), "Erreur dans next_configs"
assert eq_set(eq_atom, next_configs(a3_a, ("q", ["X","Z0"]), ["1"]), [(("p",["Z0"]),["1"]), (("q", ["X","Z0"]),[])]), "Erreur dans next_configs"
assert eq_set(eq_atom, next_configs(a3_a, ("q",["X","X","Z0"]), ["1"]), [(("q",["X","X","Z0"]),[]),(("p",["X","Z0"]),["1"])]), "Erreur dans next_configs"
assert eq_set(eq_atom, next_configs(a3_a, ("q",["X","X","Z0"]), ["0"]),[(("q",["X","X","X","Z0"]),[]), (("p",["X","Z0"]),["0"])]), "Erreur dans next_configs"
assert eq_set(eq_atom, next_configs(a3_a, ("p",["Z0"]),["1"]),[(("p",[]),[])]), "Erreur dans next_configs"
assert eq_set(eq_atom, next_configs(a3_a, ("p",["Z0"]),["0"]),[]), "Erreur dans next_configs: pas de successeur"
assert eq_set(eq_atom, next_configs(a3_a, ("p",["X","X","Z0"]), ["1"]), [(("p",["X","X","X","Z0"]),[]),(("p",["X","Z0"]),["1"])]), "Erreur dans next_configs"
assert eq_set(eq_atom, next_configs(a3_a, ("p",["X","X","Z0"]), ["0"]),[(("p",["X","Z0"]),["0"])]), "Erreur dans next_configs"
assert eq_set(eq_atom, next_configs(a3_a, ("p",["X"]),[]), [(("p",[]),[])]), "Erreur dans next_configs"
assert eq_set(eq_atom, next_configs(a3_a, ("p",[]),[]), []), "Erreur dans next_configs: pile vide"
