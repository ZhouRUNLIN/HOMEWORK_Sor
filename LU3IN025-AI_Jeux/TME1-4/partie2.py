import partie1
import random
import time
import matplotlib.pyplot as plt

def generate_pref_etu(n:int):
    """
    Input :
        n int : nombre d'étudiants
    Return : 
        list (int ** 2) : Un tableau (n*9) aléatoire à deux dimensions représentant les préférences des étudiants
    """
    pref_etu = []
    for i in range(n):
        l = [0, 1, 2, 3, 4, 5, 6, 7, 8]
        random.shuffle(l)
        pref_etu.append(l)
    return pref_etu

def generate_pref_spe(n:int):
    """
    Input :
        n int : nombre d'étudiants
    Return : 
        list (int ** 2) : Un tableau (9*(n+1)) aléatoire à deux dimensions représentant les préférences des parcours. Attention : La première colonne est la capacité des parcours
    """
    pref_spe = []
    for i in range(9):
        l = [j for j in range(n)]
        random.shuffle(l)
        pref_spe.append([1]+l)
    for i in range(n-9):
        pref_spe[random.randint(0,8)][0] += 1
    return pref_spe

def analyse_running_time_HA(n:int):
    """
    Input :
        n int : nombre d'étudiants
    Return :
        float : Temps d'exécution de la fonction hospital_algorithm avec 9 parcours et n étudiants
    """
    pref_etu = generate_pref_etu(n)
    pref_spe = generate_pref_spe(n)
    time_passed = time.time()
    assignment = partie1.hospital_algorithm(pref_etu,pref_spe)
    time_passed = time.time() - time_passed
    return time_passed

def analyse_running_time_HAHO(n:int):
    """
    Input :
        n int : nombre d'étudiants
    Return :
        float : Temps d'exécution de la fonction hospital_algorithm_Hoptimized avec 9 parcours et n étudiants
    """
    pref_etu = generate_pref_etu(n)
    pref_spe = generate_pref_spe(n)
    time_passed = time.time()
    assignment = partie1.hospital_algorithm_Hoptimized(pref_etu,pref_spe)
    time_passed = time.time() - time_passed
    return time_passed

def analyse_running_time_graph(n1:int,n2:int,step:int):
    """
    Enregistrer le temps d'exécution moyen des deux fonctions pour n∈[n1,n2] et afficher une graphe linéaire.
    Input : 
        n1, n2 int : Plage de nombre d'étudiants
        step int : La quantité n augmente à chaque cycle
    Return :
        None
    """
    y1 = []
    y2 = []
    for n_value in range(n1,n2+1,step):
        y1_temp=0
        y2_temp=0
        for index_n in range(10):
            y1_temp += analyse_running_time_HA(n_value)/10.0
            y2_temp += analyse_running_time_HA(n_value)/10.0
        y1.append(y1_temp)
        y2.append(y2_temp)
    x = [i for i in range(n1,n2+1,step)]
    l1 = plt.plot(x,y1,'r--',label="Patient-optimized")
    l2 = plt.plot(x,y2,'b--',label="Hospital-optimized")
    plt.xlabel('n')
    plt.ylabel('running time')
    plt.legend()
    plt.show()
    return None

def hospital_algorithm_record_iteration(mat_patient,mat_hospital):
    """
    Cet GS algorithme est prioritaire pour les étudiants/patients.
    Input : 
        mat_patient list (int ** 2) : Tableau 2D des préférences des étudiants/patients
        mat_hospital list (int ** 2) : Tableau 2D des préférences des parcours/hôpitals. Attention : La première colonne est la capacité
    Return : 
        int : Le nombre de boucles que la fonction exécute
    """
    nb_iter = 0
    list_patient_free = [i for i in range(len(mat_patient))] # La liste qui contient les internes libres
    list_next_patient_choice = [0 for i in range(len(mat_patient))] # list_next_patient_choice[patient] : le prochain hôpital choisi par l'interne
    list_hospital = [[] for i in range(len(mat_hospital))] # L'affectation des internes
    while len(list_patient_free) != 0:
        patient = list_patient_free[0]
        hospital = mat_patient[patient][list_next_patient_choice[patient]]
        if len(list_hospital[hospital]) < mat_hospital[hospital][0]: # Si H n’a pas encore atteint sa capacité max
            list_hospital[hospital]=partie1.insert_patient(list_hospital[hospital],patient,mat_hospital[hospital][1:])
            list_patient_free.pop(0)
        else:
            patient_compared = list_hospital[hospital][-1]
            if mat_hospital[hospital][1:].index(patient) < mat_hospital[hospital][1:].index(patient_compared): # Si H préfère I à I′
                list_patient_free.pop(0)
                list_patient_free.append(patient_compared)
                list_hospital[hospital].remove(patient_compared)
                list_hospital[hospital] = partie1.insert_patient(list_hospital[hospital],patient,mat_hospital[hospital][1:])
        list_next_patient_choice[patient] += 1
        nb_iter += 1
    return nb_iter

def hospital_algorithm_Hoptimized_record_iteration(mat_patient,mat_hospital):
    """
    Cet GS algorithme est prioritaire pour les parcours/hôpitals.
    Input : 
        mat_patient list (int ** 2) : Tableau 2D des préférences des étudiants/patients
        mat_hospital list (int ** 2) : Tableau 2D des préférences des parcours/hôpitals. Attention : La première colonne est la capacité
    Return : 
        int : Le nombre de boucles que la fonction exécute
    """
    nb_iter = 0
    list_next_hospital_choice = [1 for i in range(len(mat_hospital))] # list_next_hospital_choice[hospital] : la prochaine interne choisi par l'hôpital
    list_patient_free = [i for i in range(len(mat_patient))] # La liste que contient les internes libres
    list_hospital_free = [i for i in range(len(mat_hospital))] # La liste que contient les hôpitals libres
    list_hospital = [[] for i in range(len(mat_hospital))] # L'affectation des internes
    while len(list_hospital_free) != 0:
        hospital = list_hospital_free[0]
        patient = mat_hospital[hospital][list_next_hospital_choice[hospital]]
        if patient in list_patient_free: # Si l'interne est libre aussi
            list_patient_free.remove(patient)
            list_hospital[hospital] = partie1.insert_patient(list_hospital[hospital],patient,mat_hospital[hospital][1:])
            if len(list_hospital[hospital]) == mat_hospital[hospital][0]:
                list_hospital_free.remove(hospital)
        else:
            hospital_compared = -1
            for i in range(len(list_hospital)):
                if patient in list_hospital[i]:
                    hospital_compared = i
                    break
            if mat_patient[patient].index(hospital) < mat_patient[patient].index(hospital_compared):
                list_hospital[hospital] = partie1.insert_patient(list_hospital[hospital],patient,mat_hospital[hospital][1:])
                if len(list_hospital[hospital]) == mat_hospital[hospital][0]:
                    list_hospital_free.remove(hospital)
                list_hospital[hospital_compared].remove(patient)
                if hospital_compared not in list_hospital_free:
                    list_hospital_free.append(hospital_compared)
        list_next_hospital_choice[hospital] += 1
        nb_iter += 1
    return nb_iter

def analyse_iterations_graph(n1:int,n2:int,step:int):
    """
    Enregistrer les itérations d'exécution moyen des deux fonctions pour n∈[n1,n2] et afficher une graphe linéaire.
    Input : 
        n1, n2 int : Plage de nombre d'étudiants
        step int : La quantité n augmente à chaque cycle
    Return :
        None
    """
    y1 = []
    y2 = []
    for n_value in range(n1,n2+1,step):
        y1_iter=0
        y2_iter=0
        for index_n in range(10):
            pref_etu = generate_pref_etu(n_value)
            pref_spe = generate_pref_spe(n_value)
            y1_iter += hospital_algorithm_record_iteration(pref_etu,pref_spe)/10.0
            y2_iter += hospital_algorithm_Hoptimized_record_iteration(pref_etu,pref_spe)/10.0
        y1.append(y1_iter)
        y2.append(y2_iter)
    x = [i for i in range(n1,n2+1,step)]
    l1 = plt.plot(x,y1,'r--',label="Patient-optimized")
    l2 = plt.plot(x,y2,'b--',label="Hospital-optimized")
    plt.xlabel('n')
    plt.ylabel('iterations executed')
    plt.legend()
    plt.show()
    return None