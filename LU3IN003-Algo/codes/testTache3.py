from projet import *
#pre-Test : pour les mots avec la taille plus grand que 50000, le temp d'execution est plus que 10min

#Initialisation
#la liste de ficher pour tracer dans le graphe
listeFic={ 0: "Instances_genome/Inst_0000010_7.adn", 1: "Instances_genome/Inst_0000012_13.adn",
           2: "Instances_genome/Inst_0000013_56.adn", 3: "Instances_genome/Inst_0000014_23.adn",
           4: "Instances_genome/Inst_0000015_76.adn", 5: "Instances_genome/Inst_0000020_32.adn",
           6: "Instances_genome/Inst_0000050_77.adn", 7: "Instances_genome/Inst_0000100_44.adn",
           8: "Instances_genome/Inst_0000500_88.adn", 9: "Instances_genome/Inst_0001000_2.adn",
           10: "Instances_genome/Inst_0002000_44.adn", 11: "Instances_genome/Inst_0003000_45.adn",
           12: "Instances_genome/Inst_0005000_33.adn", 13: "Instances_genome/Inst_0008000_54.adn",
           14: "Instances_genome/Inst_0010000_7.adn", 15: "Instances_genome/Inst_0015000_20.adn",
           16: "Instances_genome/Inst_0020000_77.adn" }
tailleMot=[]        #la création d'une liste pour stocker la taille de |x|
cpuTemps=[]       #la création d'une liste pour stocker la pourcentage de consommation de temps CPU

for i in range(0, len(listeFic)):
    t0=time.time()
    cp=read_file(listeFic[i])
    print('distance de taille '+ (str)(cp[0]) +' : ' + (str)(dist_2(cp[2], cp[3])))
    t1=time.time()
    tailleMot.append(cp[0])
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