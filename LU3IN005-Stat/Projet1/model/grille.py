#Partie 1 Modélisation et fonctions simples

import numpy
import matplotlib.pyplot as plt
import matplotlib.cm as cm

class Grille:
    """
    Signification et type des paramètres:
        grille | Matrix : Zone de jeu, taille : 10*10
        bateau | int : type des bateaux
        position | (int,int) : coordonnées de la tête de bateau
        direction | int : 1 pour horizontale et 2 pour verticale
    """
    
    def __init__(self):
        #Initialisation d'une matrice vide
        #Numéroter la grille de gauche à droite de haut en bas, en utilisant les chiffres de 0 à 9
        self.grille = numpy.zeros((10, 10))

    def bat_longueur(bateau):
        """
        Définir la taille de chaque type de bateau et retourne le longueur
        """
        if bateau == 1:
            length = 5
        elif bateau == 2:
            length = 4
        elif bateau == 3 or bateau == 4:
            length = 3
        elif bateau == 5:
            length = 2
        else:
            length = 0
        
        return length

    def peut_placer(grille, bateau, position, direction):
        """
        retourne True si il est possible de placer la bateau et tous, False sinon
        """
        
        # tester si les coordonnées de tête sont dans la grille
        if position[0]<0 or position[0]>9 or position[1]<0 or position[1]>9:
            return False

        # calculer le longueur de bateau
        length = Grille.bat_longueur(bateau)
        if length==0:
            print("Err : wrong ship type number !")
            return False

        if direction==1:
           if position[0]+length<11:
                for i in range(position[0], position[0]+length):
                    if grille[i][position[1]] != 0:
                       return False
                return True
        
        elif direction==2:
           if position[1]+length<11:
                for i in range(position[1], position[1]+length):
                    if grille[position[0]][i] != 0:
                       return False
                return True
        
        return False
    
    def place(self, grille, bateau, position, direction):
        """
        retourne True si cette opération est valide, False sinon
        """
        
        #vérifier la opération est vailde à l'avance et puis on continue d'insertion de bateau
        if not Grille.peut_placer(grille, bateau, position, direction):
            return False
        
        length = Grille.bat_longueur(bateau)

        if direction == 1:
            for i in range(position[0], position[0]+length):
                self.grille[i][position[1]] = bateau
        
        if direction == 2:
            for i in range(position[1], position[1]+length):
                self.grille[position[0]][i] = bateau

        return True
    
    def place_1(self, grille, bateau, position, direction):
        """
        retourne True si cette opération est valide, False sinon
        """
        
        #vérifier la opération est vailde à l'avance et puis on continue d'insertion de bateau
        if not Grille.peut_placer(grille, bateau, position, direction):
            return False
        
        length = Grille.bat_longueur(bateau)
        
        if direction == 1:
            for i in range(position[0], position[0]+length):
                self.grille[i][position[1]] = 1
                
        if direction == 2:
            for i in range(position[1], position[1]+length):
                self.grille[position[0]][i] = 1
                
        return True

    def generer_position(self):
        """
        la création des coordonnées de la tête de bateau et sa direction au hasard
        """
        
        x=numpy.random.randint(0,10)
        y=numpy.random.randint(0,10)
        d=numpy.random.randint(1,3)
        
        return ((x,y),d)

    def place_alea(self, grille, bateau):
        """
        Effectuer une boucle jusqu'à ce que le navire soit inséré avec succès
        """
        pos,dir = self.generer_position()
        while not Grille.peut_placer(grille, bateau, pos, dir):
            pos,dir = self.generer_position()
        self.place(self.grille,bateau,pos,dir)
    
    def affiche(self, grille):
        fig = plt.figure()
        ax2 = fig.add_subplot(122)
        ax2.imshow(grille, interpolation='nearest', cmap=cm.Greys_r)
        plt.show()
    
    def eq(grilleA, grilleB):
        """
        retourne True si grilleA == grilleB (tous les points des deux matrice sont éqals), false sinon
        """
        return numpy.array_equal(grilleA,grilleB)

    def genere_grille(self):
        """
        Inserez 5 bateau (Un de chaque espèce) dans la grille
        """
        for i in range(1,6):
            self.place_alea(self.grille,i)

    def genere_grille_list(self,bateaux):
        """
        Inserer les bateaux dans une liste
        """
        for i in bateaux:
            self.place_alea(self.grille,i)
    
    def place_alea_restricted(self, grille, bateau, max):
        """
        Effectuer une boucle jusqu'à ce que le navire soit inséré avec succès
        """
        pos,dir = self.generer_position()
        while max>0 and not Grille.peut_placer(grille, bateau, pos, dir):
            pos,dir = self.generer_position()
            max-=1
        if max==0:
            return 0
        self.place_1(self.grille,bateau,pos,dir)
        return 1