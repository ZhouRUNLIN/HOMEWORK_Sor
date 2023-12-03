# Auteur : ZHOU runlin
# Num Etu : 28717281

# code du Ex 2
from utils import *

# struct via un tableau
def indexPere(index):
    return int((index-1)/2)

def indexFilsGauche(index):
    return 2*index + 1
    
def indexFilsDroite(index):
    return 2*(index + 1)

class TasTab:
    def __init__(self):
        self.size = 0
        self.data = []
    
    def Ajout(self, el):
        # ajoute l'element dans la queue de la tableau
        self.data.append(el)
        self.size += 1
        self.fix_up()
        # self.affiche()
        
    def SupprMin(self):
        # supprimer le premier element de tableau
        res = self.data.pop(0)
        self.size -= 1
        # mettre le dernier element dans le debut de tableau
        last = self.data.pop()
        self.data.insert(0, last)
        # 
        self.fix_down()
        # self.affiche()
        return res
    
    def AjoutsIteratifs(self, listEls): 
        """ 
            Ajouter un ensemble de l'elements 
        """
        for el in listEls:
            self.Ajout(el)
    
    def Construction(self, lsKey):
        # inserer tous les elements directment
        if self.size != 0:
            print("Err : tas est deja init !")
            return -1
        self.data = lsKey
        self.size = len(lsKey)

        # trier les elements
        for i in range(self.size//2-1, -1, -1):
            self.heaplify(i)

    def Union(self, other):
        if isinstance(other, TasTab):
            # 合并两个堆的数组
            self.data += other.data
            self.size += other.size
    
            # 从最后一个非叶子节点开始，依次进行下沉操作，保持最小堆性质
            for i in range(self.size//2 - 1, -1, -1):
                self.heaplify(i)
    
    def heaplify(self, p):
        """
            fix-down entre seulement deux niveau (faire un echange au maximum)
            la fonction retourne 1 si on fait un swap, et 0 sinon
        """
        if indexFilsGauche(p) < self.size:
            fils = indexFilsGauche(p)           
            if fils+1 < self.size:
                if self.data[fils] > self.data[fils + 1]:   
                    fils += 1                               
            
            if self.data[p] > self.data[fils]:          
                self.data[p], self.data[fils] = self.data[fils], self.data[p]
                return 1
        return 0

    def fix_up(self):
        fils = self.size - 1            # index du dernier element 
        while indexPere(fils) >= 0:     # il existe le pere
            pere = indexPere(fils)      # index du pere
            if self.data[pere] <= self.data[fils]: 
                break
            self.data[pere], self.data[fils] = self.data[fils], self.data[pere] # fait le swap
            fils = pere
    
    def fix_down(self):
        """
            将data中，以data[p]为根的子堆进行调整为tasmin
        """  
        pere = 0     # init du index du pere
        while indexFilsGauche(pere) < self.size:  # il existe le fils gauche
            if self.heaplify(pere) == 1:
                pere = indexFilsGauche(pere)
            else:
                break
        # while indexFilsGauche(pere) < self.size:            # il existe le fils gauche
        #     fils = indexFilsGauche(pere)
        #     if fils < self.size - 1:                        # il existe le fils droite
        #         if self.data[fils] > self.data[fils + 1]:   # comparasion des deux fils
        #             fils += 1
        #     if self.data[pere] <= self.data[fils]:          # si la valeur de pere est plus petite que le fils
        #         break                                       # le pere est a la place, donc on sort la boucle
        #     self.data[pere], self.data[fils] = self.data[fils], self.data[pere] # fait le swap
        #     pere = fils
    def degree(self):
        return log2Up(self.size)
    
    def reset(self):
        self.__init__()


if __name__ == '__main__':
    print("A simple emexple of TasTab :")
    # creation du objet
    tasMin = TasTab()
    tasMin2 = TasTab()
    # des exmeple de l'application du fonction
    # exemple sur le cours
    tasMin.Construction([2, 6, 5, 10, 13, 7, 8, 12, 15, 14])
    tasMin2.Construction([1, 3, 9, 11, 16, 17, 18])
    # tasMin.AjoutsIteratifs([2, 6, 5, 10, 13, 7, 8, 12, 15, 14])
    # tasMin2.AjoutsIteratifs([1, 3, 9, 11, 16, 17, 18])
    tasMin.Ajout(4)
    tasMin.SupprMin()
    tasMin.affiche()
    tasMin.Union(tasMin2)
    tasMin.affiche()

    tas1 = TasTab()
    tas2 = TasTab()
    tas3 = TasTab()
    data = np.zeros((3, 1000))
    for numEl in range(0, 1000):
        # liste d'elements pour rajouter
        ls1 = [2*i for i in range(numEl*100)]
        ls2 = [2*i+1 for i in range(numEl*100)]
        # comparaision 
        code_block = lambda: tas1.AjoutsIteratifs(ls1)
        tas3.Construction(ls1)
        data[0][numEl] += timeit.timeit(stmt=code_block, number=1)
        code_block = lambda: tas2.Construction(ls2)
        data[1][numEl] += timeit.timeit(stmt=code_block, number=1)
        if (tas1.data != tas3.data):
            print("err :")
            break
        code_block = lambda: tas1.Union(tas2)
        data[2][numEl] += timeit.timeit(stmt=code_block, number=1)
        
        # reinit
        tas1.reset()
        tas2.reset()
        tas3.reset()

    produceGraphe(data, x_axis = [i*100 for i in range(1000)], title = "Comparasion of different functions", xlabel = "number of node", ylabel="time used")
