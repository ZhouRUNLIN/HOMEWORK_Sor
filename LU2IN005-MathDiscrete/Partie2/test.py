# -*- coding: utf-8 -*-
"""
Code modifiable.
"""

from automate import Automate
from state import State
from transition import Transition
from myparser import *

automate = Automate.creationAutomate("exempleAutomate.txt")
automate.show("exempleAutomate")

s1= State(1, False, False)
s2= State(2, False, False)
print (s1==s2)
print (s1!=s2)
