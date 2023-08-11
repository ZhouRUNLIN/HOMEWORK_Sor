from projet import *

listeFic={ 0: "Instances_genome/Inst_0000010_7.adn",
           1: "Instances_genome/Inst_0000010_8.adn",
           2: "Instances_genome/Inst_0000010_44.adn",
           3: "Instances_genome/Inst_0000012_13.adn",
           4: "Instances_genome/Inst_0000012_56.adn",
           5: "Instances_genome/Inst_0000013_45.adn"
         }
tailleMot=[5, 9, 10, 11, 12, 13]        #la création d'une liste pour stocker la taille de |x|
cpuTemps1=[]                            #la création d'une liste pour stocker la pourcentage de consommation de temps CPU
cpuTemps2=[]  

for i in range(0, len(listeFic)):
    cp=read_file(listeFic[i])

    t0=time.time()
    dist_1(cp[2], cp[3])
    t1=time.time()
    cpuTemps1.append(t1-t0)
    print("CPU time for dist_1 :" + (str)(t1-t0) + "\n")

    t0=time.time()
    dist_2(cp[2], cp[3])
    t1=time.time()
    cpuTemps2.append(t1-t0)
    print("CPU time for dist_2 :" + (str)(t1-t0) + "\n")

#Tracer le graphique
x=np.arange(20,350)
l1=plt.plot(tailleMot, cpuTemps1,'r--',label='dist_1')
l2=plt.plot(tailleMot, cpuTemps2,'g--',label='dist_2')
plt.plot(tailleMot, cpuTemps1,'ro-', tailleMot, cpuTemps2,'g+-')
plt.title('comparation entre dist_1 et dist_2')
plt.xlabel('|x|')
plt.ylabel('CPU time')
plt.legend()
plt.show()
