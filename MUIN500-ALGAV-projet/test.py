from ArbreRecherche import *
from tasPriorite import *
from utils import *
from setKey import *
from tabHachage import *
from fileBinomiale import *

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
    print("Nb of different mot : " + str(len(tousMotListe)))
    print("Nb of collision by using MD5 : " + str(cpt))

    tas = TasTab()
    fb = fileBinomial()
    tas_Union = TasTab()
    fb_Union = fileBinomial()
    fileNames = ["cles_alea/jeu_1_nb_cles_1000.txt", "cles_alea/jeu_1_nb_cles_5000.txt", 
                 "cles_alea/jeu_1_nb_cles_10000.txt", "cles_alea/jeu_1_nb_cles_20000.txt", 
                 "cles_alea/jeu_1_nb_cles_50000.txt", "cles_alea/jeu_1_nb_cles_80000.txt", 
                 "cles_alea/jeu_1_nb_cles_120000.txt", "cles_alea/jeu_1_nb_cles_200000.txt"]
    fileNames2 = ["cles_alea/jeu_2_nb_cles_1000.txt", "cles_alea/jeu_2_nb_cles_5000.txt", 
                 "cles_alea/jeu_2_nb_cles_10000.txt", "cles_alea/jeu_2_nb_cles_20000.txt", 
                 "cles_alea/jeu_2_nb_cles_50000.txt", "cles_alea/jeu_2_nb_cles_80000.txt", 
                 "cles_alea/jeu_2_nb_cles_120000.txt", "cles_alea/jeu_2_nb_cles_200000.txt"]
    size = len(fileNames)
    data_cons = np.zeros((3, size))
    data_SupprMin = np.zeros((2, size))
    data_Ajout = np.zeros((2, size))
    data_Union = np.zeros((3, size))
    # comparaison de SupprMin, Ajout, Construction
    for i in range(size):
        lsKeys = []
        with open(fileNames[i], "r") as f:
            lines = f.readlines()
            for line in lines:
                key = Key(4)
                key.storeNum(line)
                lsKeys.append(key)
        
        lsKeysUnion = []
        with open(fileNames[i], "r") as f:
            lines = f.readlines()
            for line in lines:
                key = Key(4)
                key.storeNum(line)
                lsKeysUnion.append(key)
        
        # estimate le temps consomme par file binomiale
        # stoker les donnees de cons
        start = time.time()
        fb = fb.Construction(lsKeys)
        end = time.time()
        fb_Union = fb_Union.Construction(lsKeysUnion)
        data_cons[0][i] = end - start
        # stoker les donnees de SupprMin
        start = time.time()
        fb = fb.SupprMin()
        end = time.time()
        data_SupprMin[0][i] = end - start
        # stoker les donnees de Ajout
        randomKey = Key(4)
        randomKey.randomKey()
        start = time.time()
        fb = fb.Ajout(randomKey)
        end = time.time()
        data_Ajout[0][i] = end - start
        # stoker les donnees de SupprMin
        start = time.time()
        fb = fb.Union(fb_Union)
        end = time.time()
        data_Union[0][i] = (end - start)*100

        # estimate le temps consomme par tas
        # stoker les donnees de cons
        start = time.time()
        tas.Construction(lsKeys)
        end = time.time()
        data_cons[1][i] = end - start
        # stoker les donnees de SupprMin
        start = time.time()
        tas.SupprMin()
        end = time.time()
        data_SupprMin[1][i] = end - start
        # stoker les donnees de Ajout
        randomKey = Key(4)
        randomKey.randomKey()
        start = time.time()
        tas.Ajout(randomKey)
        end = time.time()
        data_Ajout[1][i] = end - start
        # stoker les donnees de Union
        tas_Union.Construction(lsKeysUnion)
        start = time.time()
        tas.Union(tas_Union)
        end = time.time()
        data_Union[1][i] = end - start

        # reinit dans la boucle
        tas.reset()
        fb.reset()
        tas_Union.reset()
        fb_Union.reset()
        print("finish process the book : " + fileNames[i])
    
    # produire les graphes
    produceGraphe(data_cons, x_axis = [1000, 5000, 10000, 20000, 50000, 80000, 120000, 200000], title = "Comparaison of file binomial and tas - Constructions", xlabel = "number of node", ylabel="time used")
    produceGraphe(data_SupprMin, x_axis = [1000, 5000, 10000, 20000, 50000, 80000, 120000, 200000], title = "Comparaison of file binomial and tas - SupprMin", xlabel = "number of node", ylabel="time used")
    produceGraphe(data_Ajout, x_axis = [1000, 5000, 10000, 20000, 50000, 80000, 120000, 200000], title = "Comparaison of file binomial and tas - Ajout", xlabel = "number of node", ylabel="time used")
    produceGraphe(data_Union, x_axis = [1000, 5000, 10000, 20000, 50000, 80000, 120000, 200000], title = "Comparaison of file binomial and tas - Union", xlabel = "number of node", ylabel="time used")

