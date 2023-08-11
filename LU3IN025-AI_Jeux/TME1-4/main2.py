import partie1
import partie2

# Partie 2

# Q5
print(partie2.generate_pref_etu(11))
print(partie2.generate_pref_spe(11))

# Q6
print(partie2.analyse_running_time_HA(9))
print(partie2.analyse_running_time_HAHO(9))
partie2.analyse_running_time_graph(200,2000,200)

# Q7: complexity = O(n2³-n1³) 
# La tendance à la croissance du temps nécessaire à l'exécution est conforme à nos spéculations.

# Q8: Les résultats montrent que sa croissance est linéaire, ce qui n'est pas conforme à nos spéculations.
#partie2.analyse_iterations_graph(200,2000,200)
