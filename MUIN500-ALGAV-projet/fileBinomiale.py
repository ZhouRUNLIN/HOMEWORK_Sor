# Auteur : ZHOU runlin
# Num Etu : 28717281

# code du Exec 3
from utils import *

class Tournoi:
    def __init__(self, val=None, fils=None, frere=None):
        self.val = val
        self.firstChild = fils      # premier enfant
        self.nextSibling = frere    # frere suivant

    # les primitives du Tournois
    def EstVide(self):
        """TournoiB -> booleen
            Renvoie vrai ssi le tournoi est vide."""
        return self.firstChild == None and self.nextSibling == None
        
    def Degre(self):
        """TournoiB -> entier
            Renvoie le degre de la racine du tournoi."""
        if self.firstChild == None:
            return 0
        return 1 + self.firstChild.Degre()

    def Union2Tid(self, other):
        """TournoiB * TournoiB -> TournoiB
            Renvoie l'union de 2 tournois de meme taille."""
        if self.Degre() == other.Degre():
            # compare le root des deux arbres, mettre le root plus petit comme le nouveau root
            if self.val <= other.val:
                self.firstChild, other.nextSibling = other, self.firstChild
                return self
            else:
                other.firstChild, self.nextSibling = self, other.firstChild
                return other
        print("Err : two objects are not at the same size ! ")
        return self
        
    def Decapite(self):
        """TournoiB -> FileB
            Renvoie la file binomiale obtenue en supprimant la racine du tournoi 
                T_k -> <T_{k-1},T_{k-2}, ... ,T_1,T_0>."""
        fb = fileBinomial(self.Degre())
        root = self.firstChild
        for i in range(fb.maxSize):
            fb.rootList[fb.maxSize-1-i] = root
            root = root.nextSibling
            fb.rootList[fb.maxSize-1-i].nextSibling = None
        return fb

    def File(self):
        """TournoiB -> FileB
            Renvoie la file binomiale reduite au tournoi T_k -> <T_k>."""
        fb = fileBinomial(self.Degre()+1)
        fb.rootList[self.Degre()] = self
        return fb
        
    # les méthodes spéciales
    def __str__(self):
        string = ""
        if self.val != None:
            string = str(self.val) + " "
        if self.EstVide():
            return string
        if self.nextSibling != None:
            string += self.nextSibling.__str__()
        # string += "\n"
        return string + self.firstChild.__str__()

class fileBinomial:
    """
        representer comme une suite de Tournois, les donnees sont stockee dans une liste de python
    """
    # Creer un BF tel qu'il ait au maximum max tournois.
    def __init__(self, max):
        # tous les elements de la liste sont init a None
        # pour representer que cette FB est vide
        self.maxSize = max
        self.rootList = [None for i in range(max)]

    # les primitives du file binomial
    def EstVide(self):
        """FileB -> booleen
            Renvoie vrai ssi la file est vide."""
        for root in self.rootList:
            if root != None:
                return False
        return True
    
    def MinDeg(self):
        """FileB -> (int, tournois)
            Renvoie un tuple : le tournois min et index de liste (rootList)"""
        if self.EstVide():
            return None

        minimal = self.maxSize
        index = -1
        for i in range(self.maxSize):
            if self.rootList[i] != None:
                if minimal > self.rootList[i].Degre():
                    minimal = self.rootList[i].Degre()
                    index = i
        return self.rootList[index]
    
    def Reste(self):
        """FileB -> Tournoi
            supprime le tournoi de degre minimal, et Renvoie ce tournoi."""
        ptr = self.MinDeg()
        for i in range(self.maxSize):
            if self.rootList[i] != None:
                if self.rootList[i] == ptr:
                    self.rootList[i] = None
        return self
        
    def AjoutMin(self, tb):
        """Tournoi * FileB -> FileB"""
        self.rootList[tb.Degre()] = tb
        return self
    
    # Exec 3.2
    def Union(self, other):
        return self.UFret(other, None)
    
    def UFret(self, other, tb): 
        if tb == None: # pas de tournoi en retenue
            if self.EstVide():
                return other
            if other.EstVide():
                return self
            T1 = self.MinDeg()
            T2 = other.MinDeg()
            if T1.Degre() < T2.Degre():
                return self.Reste().Union(other).AjoutMin(T1)
            elif T1.Degre() > T2.Degre():
                return other.Reste().Union(self).AjoutMin(T2)
            else:
                return self.Reste().UFret(other.Reste(), T1.Union2Tid(T2))
        else: # T tournoi en retenue
            if self.EstVide():
                return tb.File().Union(other)
            if other.EstVide():
                return tb.File().Union(self)
            T1 = self.MinDeg()
            T2 = other.MinDeg()
            if tb.Degre() <  T1.Degre() and tb.Degre() < T2.Degre():
                return self.Union(other).AjoutMin(tb)
            elif tb.Degre() ==  T1.Degre() and tb.Degre() == T2.Degre():
                return self.Reste().UFret(other.Reste(), T1.Union2Tid(T2)).AjoutMin(tb)
            elif tb.Degre() ==  T1.Degre() and tb.Degre() < T2.Degre():
                return self.Reste().UFret(other, T1.Union2Tid(tb))
            else:
                return other.Reste().UFret(self, T2.Union2Tid(tb))
    
    def Construction(self, listeEl):
        degre = log2Up(len(listeEl))
        if degre != self.maxSize:
            self.__init__(degre)
        for el in listeEl:
            self = self.Ajout(el)
        return self
    
    def SupprMin(self):
        # init le min
        minimal = self.rootList[self.maxSize-1].val # un val assze grand
        index = self.maxSize-1
        # parcours pour trouver le min index
        for i in range(self.maxSize-1):
            if self.rootList[i] != None:
                if self.rootList[i].val < minimal:
                    minimal = self.rootList[i].val
                    index = i
        # supprime le root de ce tournois a l'appel de Decapite
        fb = self.rootList[index].Decapite()
        if self.maxSize > fb.maxSize:
            self.rootList[fb.maxSize] = None
        return self.Union(fb)
    
    def Ajout(self, el):
        return self.Union(Tournoi(el).File())

    # les méthodes spéciales
    def __str__(self):
        lsStr = []
        for i in range(self.maxSize):
            lsStr.append(str(i) + ": " + str(self.rootList[i]))
        return "\n".join(lsStr)

    def reset(self):
        self.__init__(self.maxSize)

    
if __name__ == '__main__' :
    print("test for Union2Tid")
    # une suite de union un par un
    # les tournois de degre 0
    lsTB_0 = [Tournoi(i) for i in range(8)] 
    # les tournois de degre 1
    lsTB_1 = [lsTB_0[i].Union2Tid(lsTB_0[7-i]) for i in range(4)]
    # les tournois de degre 2
    lsTB_2 = [lsTB_1[i].Union2Tid(lsTB_1[3-i]) for i in range(2)]
    tb = lsTB_2[0].Union2Tid(lsTB_2[1])
    print("--------------")
    print(tb)

    print("test for File")
    fb = tb.File()
    print("--------------init")
    print(fb)

    print("test for Decapite")
    fb = tb.Decapite()
    print("--------------after removing the min element")
    print(fb)

    fb.Reste()
    print("--------------after removing the min degre TB")
    print(fb)
    fb.AjoutMin(lsTB_0[7])
    print("--------------after adding a min degree TB")
    print(fb)

    print("test for cons et union")
    fb1 = fileBinomial(3)
    fb2 = fileBinomial(3)
    fb1 = fb1.Construction([1, 3, 5, 7, 9, 11, 13, 15, 17, 19])
    fb2 = fb2.Construction([0, 2, 10, 12, 14, 16, 18])
    print("fb1 :")
    print(fb1)
    print("fb2 :")
    print(fb2)
    print("fb3 = fb1 union fb2 :")
    fb3 = fb1.Union(fb2)
    print(fb3)

    for i in range(8):
        fb3 = fb3.SupprMin()
        print("--------------after deleting a min from fb3")
        print(fb3)

    # analyse graphiquement 
    # pour deux fonctions : Construction et Union
    # Dans cette expérience, le nombre d'éléments stockés dans fileBinomial croît exponentiellement
    # Si vous ne voulez pas attendre trop longtemps, réduisez la valeur de size
    size = 23   
    data1 = np.zeros((1, size))
    data2 = np.zeros((1, size))
    # init pour experience

    for i in range(1, 23):
        fb_test_1 = fileBinomial(1)
        fb_test_2 = fileBinomial(1)
        lsEls_1 = [random.randint(1, 100) for a in range(int(math.pow(2, i))-1)]
        lsEls_2 = [random.randint(1, 100) for a in range(int(math.pow(2, i))-1)]

        start = time.time()
        fb_test_1 = fb_test_1.Construction(lsEls_1)
        end = time.time()
        data1[0][i] = end - start

        fb_test_2 = fb_test_2.Construction(lsEls_2)
        start = time.time()
        fb_test_1 = fb_test_1.Union(fb_test_2)
        end = time.time()
        data2[0][i] = end - start
        print(i)
        fb_test_1.reset()
        fb_test_2.reset()

    produceGraphe(data1, x_axis = [(math.pow(2, i))-1 for i in range(size)], title = "Representation Graphique", xlabel = "number of node", ylabel="time used")
    produceGraphe(data2, x_axis = [(math.pow(2, i))-1 for i in range(size)], title = "Representation Graphique", xlabel = "number of node", ylabel="time used")

