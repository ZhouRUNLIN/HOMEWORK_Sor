# -*- coding: utf-8 -*-
from transition import *
from state import *
import os
import copy
from itertools import product
from automateBase import AutomateBase
from automate import *
#partie 2.1
s0 = State(0,True,False)
s1 = State(1,False,False)
s2 = State(2,False,True)
t1 = Transition(s0,"a",s0)
t2 = Transition(s0,"b",s1)
t3 = Transition(s1,"a",s2)
t4 = Transition(s1,"b",s2)
t5 = Transition(s2,"a",s0)
t6 = Transition(s2,"b",s1)
auto = Automate([t1,t2,t3,t4,t5,t6])
print(auto)
auto.show("A_ListeTrans")

auto1 = Automate([t1,t2,t3,t4,t5,t6], [s0,s1,s2])
print(auto1)
auto1.show("A_ListeTrans1")

auto2 = Automate.creationAutomate("auto.txt")
print(auto2)
auto1.show("A_ListeTrans2")

#partie 2.2
auto.removeTransition(t2)
print(auto)
auto.show("A_ListeTrans0.1")

auto.addTransition(t2)
print(auto)
auto.show("A_ListeTrans0.2")

auto.removeState(s1)
print(auto)
auto.show("A_ListeTrans0.3")

auto.addState(s1)
s2 = State(0,True,False)
auto.addState(s2)
print(auto)
auto.show("A_ListeTrans0.4")

auto1.getListTransitionsFrom(s1)




