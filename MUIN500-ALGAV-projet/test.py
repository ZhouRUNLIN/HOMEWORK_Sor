from ArbreRecherche import *
from tasPriorite import *
from utils import *
from setKey import *
from tabHachage import *

from ordered_set import OrderedSet

if __name__ == '__main__':
    # Question 6.14
    fileNames = list_files("Shakespeare")
    fileNames.sort()
    
    tousMotHach = {}                # pour stocker tous les mots qui ont une collision
    tousMotListe = OrderedSet()     # pour stocker l'ordre d'ocurrence de mot
    # lire tous les mot de Shakspeare
    print("start to process all the book of Shakespeare !")
    for fileName in fileNames:
        with open(fileName, "r") as f:
            lines = f.readlines()

            for line in lines:
                # transforme le mot vers la chaine de MD5 
                mot = "0x" + Md5(line[:-1].encode('ascii'))
                # inserer les chaines dans les deux sets
                tousMotListe.add(mot)
                if mot not in tousMotHach.keys():
                    tousMotHach[mot] = [line[:-1]]
                else:
                    if line[:-1] not in tousMotHach[mot]:
                        tousMotHach[mot].append(line[:-1])
        print("finish process the book : " + fileName)
    
    # creation du ABR
    tousMotTree = BTree()
    tousMotTree.InsertIteratifs(tousMotListe)

    # for hexMot in tousMotListe:
    cpt = 0
    for key in tousMotHach.keys():
        # le cas qui il existe une collision
        if len(tousMotHach[key]) > 1:
            cpt += len(tousMotHach[key])
            print(tousMotHach[key])
    print("Nb of collision by using MD5 : " + str(cpt))
    

