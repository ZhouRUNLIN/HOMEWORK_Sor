import partie1
import partie2
import partie3
import os
from subprocess import run, DEVNULL

pref_etu = partie2.generate_pref_etu(11)
pref_spe = partie2.generate_pref_spe(11)

#Q11
k_min = partie3.find_minimum_k(pref_etu, pref_spe, False, True)
print("Q11 : kmin = "+str(k_min))

#Q10
partie3.generate_pl_egalitarian_criterion(pref_etu,pref_spe,3,True)
run("gurobi_cl ResultFile=affectation.sol 3-premier_choix.lp", stdout=DEVNULL, stderr=DEVNULL, shell=True)
print("Q10 : solution when k=3")
f = open("affectation.sol")
l = f.readline()
if len(l) == 0:
	print("No solution")
else:
	print(l)

#Q12
partie3.generate_pl_max_utility(pref_etu,pref_spe)
run("gurobi_cl ResultFile=affectation_max_utility.sol max_utility.lp", stdout=DEVNULL, stderr=DEVNULL, shell=True)
print("Q12 : Maximum utility:")
f = open("affectation_max_utility.sol")
l = f.readline()
print(l)
f.close()

#Q13
partie3.generate_pl_egalitarian_criterion(pref_etu,pref_spe,k_min,True)
run("gurobi_cl ResultFile=affectation_max_utility.sol "+str(k_min)+"-premier_choix.lp", stdout=DEVNULL, stderr=DEVNULL, shell=True)
print("Q13 : Maximum utility when k=k*:")
f = open("affectation_max_utility.sol")
l = f.readline()
print(l)
f.close()
