import partie1

# Partie 1

# Q1
mat_etu = partie1.read_pref_etu("PrefEtu.txt")
mat_spe = partie1.read_pref_spe("PrefSpe.txt")

# Q2
assignment = partie1.hospital_algorithm(mat_etu,mat_spe)
print(assignment)

# Q3
assignment2 = partie1.hospital_algorithm_Hoptimized(mat_etu,mat_spe)
print(assignment2)

# Q4
print(partie1.stability_verification(assignment,mat_etu,mat_spe))
print(partie1.stability_verification(assignment2,mat_etu,mat_spe))
print(partie1.stability_verification([[5, 3], [4], [1], [8], [10], [0], [9], [7], [6, 2]],mat_etu,mat_spe)) # contre-exemple
