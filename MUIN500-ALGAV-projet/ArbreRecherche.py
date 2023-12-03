# Auteur : ZHOU runlin
# Num Etu : 28717281

# code du exec 5
import numpy as np

from utils import *
from setKey import *

class TreeNode(object):
    def __init__(self, data=None, left=None, right=None):
        self.data = data
        self.left = left
        self.right = right
        self.height = 0

class BTree(object):
    def __init__(self, root=None):
        self.root = root
    
    def height(self, r):
        if r == None:
            return -1
        return r.height
    
    def Insert(self, data):
        self.root = self.__insert(data, self.root)
        # print(self.Show())
        return self.root

    def InsertIteratifs(self, lsData):
        for el in lsData:
            self.root = self.__insert(el, self.root)
        return self.root

    def Delete(self, data):
        self.root = self.__delete(data, self.root)

    def __insert(self, data, r):
        # cas particulières
        if r == None:
            return TreeNode(data)
        
        # comparer avec la valeur de node actuel
        if data == r.data:
            return r
        elif data < r.data:
            r.left = self.__insert(data, r.left)
        else:
            r.right = self.__insert(data, r.right)
        r.height = max(self.height(r.left), self.height(r.right)) + 1
    
        if self.height(r.left) - self.height(r.right) > 1 or self.height(r.left) - self.height(r.right) < -1:
            return self.equilibre(r, data)

        return r

    def __delete(self, data, r):
        if r == None:
            return r

        if r.data == data:
            if r.left == None:      # s'il n'exite pas le fils gauche
                return r.right
            elif r.right == None:   # s'il n'exite pas le fils droite
                return r.left
            else:                   # si les deux fils sont tous existe, on doit choisir un nouveau root de l'arbre
                if self.height(r.left) > self.height(r.right):  # si le fils gauche est plus haut que le droite
                    node = r.left
                    while(node.right != None):                  # trouver l'element le plus gauche (l'element plus petite a gauche)
                        node = node.right                       # on met cet element comme le root
                else:                     
                    node = r.right 
                    while node.left != None:                    # trouver l'element le plus droite (l'element plus grande a droite)
                        node = node.left                        # on met cet element comme le root
                r = self.__delete(node.data, r)
                r.data = node.data
                return r
        elif data < r.data:     # supprimer l'elements dans le fils guache
            r.left = self.__delete(data, r.left)
        else:     # supprimer l'elements dans le fils droite
            r.right = self.__delete(data, r.right)
        
        # mise a jour le hauteur
        r.height = max(self.height(r.left), self.height(r.right)) + 1
        return self.equilibre(r, data)

    def equilibre(self, r, data):
        # le fils gauche est plus haut que le droite 
        if self.height(r.left) - self.height(r.right) > 1:
            if data <= r.left.data:
                return self.rotationGauche(r)
            else:
                return self.rotationGaucheDroite(r)
        # le fils droite est plus haut que le gauche
        elif self.height(r.right) - self.height(r.left) > 1:
            if data >= r.right.data:
                return self.rotationDroite(r)
            else:
                return self.rotationDroiteGauche(r)
        else:
            return r

    def Show(self):
        """ en utilisent traversée hiérarchique, la fonction renvoie une liste, 
                la taille de liste est la niveau de l'arbre
                les éléments de liste sont aussi une liste, qui contients tous les elements de ce niveau """
        res = []
        if self.root == None:
            return res
        
        cur = [self.root]
        # si la liste est vide, on sort la boucle
        while cur:
            nxt = []
            vals = []
            for node in cur:
                vals.append(node.data)
                if node.left:  nxt.append(node.left)
                if node.right: nxt.append(node.right)
            cur = nxt
            res.append(vals)
        return res
    
    def rotationGauche(self, r):
        # entree : <p, <q, u, v>, w>   ->   renvoie : <q, u, <p, v, w>>
        node = r.left
        r.left = node.right
        node.right = r
        # mise a jour le hauteur 
        r.height = max(self.height(r.right), self.height(r.left)) + 1
        node.height = max(self.height(node.right), self.height(node.left)) + 1
        return node

    def rotationDroite(self, r):
        # entree : <q, u, <p, v, w>>   ->   renvoie : <p, <q, u, v>, w>
        node = r.right
        r.right = node.left
        node.left = r
        # mise a jour le hauteur 
        r.height = max(self.height(r.right), self.height(r.left)) + 1
        node.height = max(self.height(node.right), self.height(node.left)) + 1
        return node

    def rotationDroiteGauche(self, r):
        r.right = self.rotationGauche(r.right)
        return self.rotationDroite(r)

    def rotationGaucheDroite(self, r):
        r.left = self.rotationDroite(r.left)
        return self.rotationGauche(r)

# Exec 5
if __name__ == '__main__':
    avl = BTree()
    for i in range(20):
        avl.Insert(i)
        print(i)
    avl.Delete(2)
    
    # init pour experience
    data = np.zeros((1, 1000)) # data est init a une matrix vide avec la taille de 6*6

    cpt = 0
    # en utilisent timeit pour mesure de temps consomme par ce code block
    with open("cles_alea/jeu_1_nb_cles_1000.txt", "r") as file:
    # lire la contenu
        lines = file.readlines()
    
    avlTest = BTree()
    for i in range(1, 1000):
        for j in range((i-1)*10,i*10-1):
            avlTest.Insert(j)
        for k in range(100):
            code_block = lambda: avlTest.Insert(i*10)
            data[0][i] += timeit.timeit(stmt=code_block, number=1) /100
            avlTest.Delete(i*10)

    produceGraphe(data, x_axis = [i*100 for i in range(1000)], title = "Complexite of insert", xlabel = "number of node", ylabel="time used")
