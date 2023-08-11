# -*- coding: utf-8 -*-
from transition import *
from state import *
import os
import copy
from itertools import product
from automateBase import AutomateBase
from automate import *

s0 = State(0,True,False)
s1 = State(1,False,False)
s2 = State(2,False,True)

t1 = Transition(s0,"a",s0)
t2 = Transition(s0,"b",s1)
t3 = Transition(s0,"a",s1)
t4 = Transition(s1,"a",s2)
t5 = Transition(s1,"b",s2)
t6 = Transition(s2,"a",s0)
t7 = Transition(s2,"b",s1)

auto = Automate([t1,t2,t3,t4,t5,t6,t7])
print("----------------------------")
print("------------auto-----------")
print(auto)
auto.show("A_ListeTrans")

print("test de succ et succElem")
r1 = Automate.succElem(auto, s0, "a")
print(r1)
li = [s0,s1]
r2 = Automate.succ (auto, li, "a")
print(r2)

print("'ab' est accept par auto?")
print(Automate.accepte(auto,"ab"))
print("'bab' est accept par auto?")
print(Automate.accepte(auto,"bab"))

print("auto est complet?")
print(Automate.estComplet(auto,"ab"))
print("auto est deterministe?")
print(Automate.estDeterministe(auto))

s3 = State(3,True,False)
s4 = State(4,False,False)
s5 = State(5,False,True)

t21 = Transition(s3,"a",s4)
t22 = Transition(s3,"a",s3)
t23 = Transition(s3,"b",s3)
t24 = Transition(s4,"a",s4)
t25 = Transition(s4,"b",s5)
t26 = Transition(s5,"a",s3)
auto2 = Automate([t21,t22,t23,t24,t25,t26])
print("----------------------------")
print("------------auto2-----------")
print(auto2)
auto2.show("A_ListeTrans2")
print("auto2 est complet?")
print(Automate.estComplet(auto2,"ab"))
print("auto2 est deterministe?")
print(Automate.estDeterministe(auto2))

print("----------------------------")
print("-------auto2_complete-------")
auto2_c=Automate.completeAutomate(auto2,"ab")
print(auto2_c)
auto2_c.show("A_ListeTrans2_c")

print("----------------------------")
print("---auto2_determinisation----")
auto2_d=Automate.determinisation(auto2)
print(auto2_d)
auto2_d.show("A_ListeTrans2_d")

print("----------------------------")
print("----auto2_complementaire----")
auto2_complementaire=Automate.complementaire(auto2)
print(auto2_complementaire)
auto2_complementaire.show("A_ListeTrans2_com")

print("----------------------------")
print("--------intersection--------")
intersection=Automate.intersection(auto,auto2)
print(intersection)
intersection.show("A_ListeTrans2_inter")

print("----------------------------")
print("--------concatenation-------")
concatenation=Automate.concatenation(auto,auto2)
print(concatenation)
concatenation.show("A_ListeTrans2_conca")

print("----------------------------")
print("------------etoile----------")
etoile2=Automate.etoile(auto2)
print(etoile2)
etoile2.show("A_ListeTrans2_etoile")
print("ab est accepte par etoile2?")
print(Automate.accepte(etoile2,"ab"))
print("abab est accepte par etoile2?")
print(Automate.accepte(etoile2,"abab"))
print("ababab est accepte par etoile2?")
print(Automate.accepte(etoile2,"ababab"))
print("abababab est accepte par etoile2?")
print(Automate.accepte(etoile2,"abababab"))
