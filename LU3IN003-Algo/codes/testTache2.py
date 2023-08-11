from projet import *

def prog_dyn(x, y):
    """
        renvoie à la fois la distance d(x, y) et un alignement optimal
    """
    T=create_T(x, y)
    #affichera la distance d(x, y) et un alignement optimal
    print('distance de ' + x + ' et ' + y + ' : ' + (str)(dist_1(x, y)))
    print('alignement optimal de' + x + ' et ' + y + ' : ' + (str)(sol_1(T, x, y)))

#pre-Test : pour les mots avec la taille plus grand que 12, le temp d'execution est plus que 10min

#Initialisation
#la liste de ficher pour tracer dans le graphe
listeFic={ 0: "Instances_genome/Inst_0000010_7.adn",
           1: "Instances_genome/Inst_0000010_8.adn",
           2: "Instances_genome/Inst_0000010_44.adn",
           3: "Instances_genome/Inst_0000012_13.adn",
           4: "Instances_genome/Inst_0000012_56.adn",
           5: "Instances_genome/Inst_0000013_45.adn"
         }
tailleMot=[5, 9, 10, 11, 12, 13]        #la création d'une liste pour stocker la taille de |x|
cpuTemps=[]                            #la création d'une liste pour stocker la pourcentage de consommation de temps CPU

for i in range(0, len(listeFic)):
    t0=time.time()
    cp=read_file(listeFic[i])
    prog_dyn(cp[2], cp[3])
    t1=time.time()
    cpuTemps.append(t1-t0)
    print("CPU time :" + (str)(t1-t0) + "\n")

#Tracer le graphique
x=np.arange(20,350)
l1=plt.plot(tailleMot, cpuTemps,'r--',label='CPU')
plt.plot(tailleMot, cpuTemps,'ro-')
plt.title('consommation de temps CPU')
plt.xlabel('|x|')
plt.ylabel('CPU time')
plt.legend()
plt.show()