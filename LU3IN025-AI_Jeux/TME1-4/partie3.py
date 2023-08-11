import partie1
import partie2
from subprocess import run, DEVNULL
import os

def generate_pl_egalitarian_criterion(pref_etu,pref_spe,k:int,maximize_u = True):
    """
    Générer un fichier PL pour juger s'il existe une solution lorsque les étudiants et le parcours ne choisissent que les k meilleures préférences. 
    Si maximize_u = True, le résultat d'exécution du fichier PL renvoie le cas où l'utilitaire est le plus grand, sinon c'est le plus petit.
    Input :
        mat_patient list (int ** 2) : Tableau 2D des préférences des étudiants/patients
        mat_hospital list (int ** 2) : Tableau 2D des préférences des parcours/hôpitals. Attention : La première colonne est la capacité
        k int : Choisir les k meilleures préférences
        maximize_u Boolean : True = Efficacité ; False = Equité
    Return :
        None
    """
    n_etu = len(pref_etu)
    n_spe = len(pref_spe)
    f = open(str(k)+"-premier_choix.lp","w+")
    if maximize_u:
        f.write("Maximize"+"\n") #Maximize
    else:
        f.write("Minimize"+"\n") #Minimize
    # generate the object to be minimized
    str_temp = ""
    for i in range(n_etu):
        for j in range(n_spe):
            str_temp += str(n_spe-pref_etu[i].index(j)-1)+" "+"c"+str(i)+"_"+str(j)+" + "+str(n_etu-pref_spe[j][1:].index(i)-1)+" "+"c"+str(i)+"_"+str(j)+" + "
    str_temp = str_temp[0:-3]
    f.write(str_temp+"\n") #Σij cij(jmax-j + imax-i)
    f.write("Subject To"+"\n")
    # part ST
    res_count = 1
    for i in range(n_etu):
        str_temp1 = "c"+str(res_count)+": "
        str_temp2 = "c"+str(res_count+1)+": "
        res_count += 2
        for j in range(n_spe):
            if pref_etu[i].index(j)<k:
                str_temp1 += "c"+str(i)+"_"+str(j)+" + "
            else:
                str_temp2 += "c"+str(i)+"_"+str(j)+" + "
        str_temp1 = str_temp1[0:-3]+" = 1"
        str_temp2 = str_temp2[0:-3]+" = 0"
        f.write(str_temp1+"\n")
        f.write(str_temp2+"\n")
    
    for j in range(n_spe):
        str_temp1 = "c"+str(res_count)+": "
        str_temp2 = "c"+str(res_count+1)+": "
        res_count += 2
        for i in range(n_etu):
            if pref_spe[j][1:].index(i)<k:
                str_temp1 += "c"+str(i)+"_"+str(j)+" + "
            else:
                str_temp2 += "c"+str(i)+"_"+str(j)+" + "
        str_temp1 = str_temp1[0:-3]+" = "+str(pref_spe[j][0])
        str_temp2 = str_temp2[0:-3]+" = 0"
        f.write(str_temp1+"\n")
        f.write(str_temp2+"\n")
    
    f.write("Binary"+"\n")
    # part Binary
    str_temp = ""
    for i in range(n_etu):
        for j in range(n_spe):
            str_temp += "c"+str(i)+"_"+str(j)+" "
    f.write(str_temp[0:-1]+"\n")

    f.write("End"+"\n")
    f.close()
    
def find_minimum_k(pref_etu,pref_spe,keep_files = True, maximize_u = True):
    """
    Trouver le plus petit ktel qu'il existe une solution
    Input :
        mat_patient list (int ** 2) : Tableau 2D des préférences des étudiants/patients
        mat_hospital list (int ** 2) : Tableau 2D des préférences des parcours/hôpitals. Attention : La première colonne est la capacité
        keep_files Boolean : Si False, supprimer les fichiers sol et lp générés après l'exécution
        maximize_u Boolean : True = Efficacité ; False = Equité
    Return :
        int : k minimal
    """
    k = 1
    os.system("rm -f *.lp")
    os.system("rm -f *.sol")
    while k < 9:
        generate_pl_egalitarian_criterion(pref_etu, pref_spe, k, True)
        run("gurobi_cl ResultFile=affectation.sol " + str(k) + "-premier_choix.lp", stdout=DEVNULL, stderr=DEVNULL, shell=True)
        f = open("affectation.sol")
        l = f.readline()
        if len(l)!=0:
            if not keep_files:
                os.system("rm -f *.lp")
                os.system("rm -f *.sol")
            return k
        k += 1
    if not keep_files:
        os.system("rm -f *.lp")
        os.system("rm -f *.sol")
    return -1

def generate_pl_max_utility(pref_etu,pref_spe):
    """
    Générer un fichier PL pour trouver l'utilité maximale parmi toutes les combinaisons
    Input :
        mat_patient list (int ** 2) : Tableau 2D des préférences des étudiants/patients
        mat_hospital list (int ** 2) : Tableau 2D des préférences des parcours/hôpitals. Attention : La première colonne est la capacité
    Return :
        None
    """
    n_etu = len(pref_etu)
    n_spe = len(pref_spe)
    f = open("max_utility.lp","w+")
    f.write("Maximize"+"\n") #Maximize
    # generate the object to be minimized
    str_temp = ""
    for i in range(n_etu):
        for j in range(n_spe):
            str_temp += str(n_spe-pref_etu[i].index(j)-1)+" "+"c"+str(i)+"_"+str(j)+" + "+str(n_etu-pref_spe[j][1:].index(i)-1)+" "+"c"+str(i)+"_"+str(j)+" + "
    str_temp = str_temp[0:-3]
    f.write(str_temp+"\n") #Σij cij(jmax-j + imax-i)
    f.write("Subject To"+"\n")
    # part ST
    res_count = 1
    for i in range(n_etu):
        str_temp = "c"+str(res_count)+": "
        res_count += 1
        for j in range(n_spe):
            str_temp += "c"+str(i)+"_"+str(j)+" + "
        str_temp = str_temp[0:-3]+" = 1"
        f.write(str_temp+"\n")
        
    for j in range(n_spe):
        str_temp = "c"+str(res_count)+": "
        res_count += 1
        for i in range(n_etu):
            str_temp += "c"+str(i)+"_"+str(j)+" + "
        str_temp = str_temp[0:-3]+" = "+str(pref_spe[j][0])
        f.write(str_temp+"\n")
        
    f.write("Binary"+"\n")
    # part Binary
    str_temp = ""
    for i in range(n_etu):
        for j in range(n_spe):
            str_temp += "c"+str(i)+"_"+str(j)+" "
    f.write(str_temp[0:-1]+"\n")
    
    f.write("End"+"\n")
    f.close()