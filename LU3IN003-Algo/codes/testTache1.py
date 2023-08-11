from projet import *

#test pour Inst_0000010_44.adn
cp=read_file("Instances_genome/Inst_0000010_44.adn")
t0=time.time()
print('distance de ' + cp[2] + ' et ' + cp[3] + ' : ' + (str)(dist_2(cp[2],cp[3])))
t1=time.time()
print('Time taken for execute dist_naif : ' + (str)(t1-t0))

#test pour Inst_0000010_7.adn
cp=read_file("Instances_genome/Inst_0000010_7.adn")
t0=time.time()
print('distance de ' + cp[2] + ' et ' + cp[3] + ' : ' + (str)(dist_2(cp[2],cp[3])))
t1=time.time()
print('Time taken for execute dist_naif : ' + (str)(t1-t0))

#test pour Inst_0000010_8.adn
cp=read_file("Instances_genome/Inst_0000010_8.adn")
t0=time.time()
print('distance de ' + cp[2] + ' et ' + cp[3] + ' : ' + (str)(dist_2(cp[2],cp[3])))
t1=time.time()
print('Time taken for execute dist_naif : ' + (str)(t1-t0))

#les instances fournies en moins d’une minute
cp=read_file("Instances_genome/Inst_0000012_13.adn")
t0=time.time()
print('distance de ' + cp[2] + ' et ' + cp[3] + ' : ' + (str)(dist_2(cp[2],cp[3])))
t1=time.time()
print('Time taken for execute dist_naif : ' + (str)(t1-t0))

cp=read_file("Instances_genome/Inst_0000013_56.adn")
t0=time.time()
print('distance de ' + cp[2] + ' et ' + cp[3] + ' : ' + (str)(dist_2(cp[2],cp[3])))
t1=time.time()
print('Time taken for execute dist_naif : ' + (str)(t1-t0))       