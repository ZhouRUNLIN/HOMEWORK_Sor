#Partie 3  Modélisation probabiliste du jeu
#la création de la class Bataille

from model.grille import *

class Bataille:
	def __init__(self):
		self.g1=Grille()
		self.g1.genere_grille()			 	#une matrice pour stocker les coordonnées des bateaux
		self.record = numpy.zeros((10, 10)) 		#une nouvelle matrice pour stocker les point touchée
		self.toucher_cible = numpy.zeros((10, 10))  #Une matrice de cibles touchées
		self.count=0 					#le compteur pour entregister le nombre d'action qui touche le bateaux
		
	def joue(self, position):
		"""
		tenter de toucher un bateau, si réussit le compteur se augment à 1
		"""
		self.record[position[0]][position[1]]=1
		if self.g1.grille[position[0]][position[1]]!=0:
			self.count+=1
			self.toucher_cible[position[0]][position[1]]=1
			return 1
		return 0
	
	def victoire(self):
		"""
		vérifier si tous les point de chaque bateau sont touchés.
		retourne 1 si nous gagnons la bataille, et le jeu se termine, retourne 0 sinon
		"""
		if self.count==17:
			return 1
		return 0
	
	def reset(self):
		"""
		Redémarrer un jeu
		"""
		self.__init__()
		
	def ships_remain(self):
		"""
		Renvoie le numéro de navire survivant
		"""
		l=[]
		s=(1-self.record)*self.g1.grille
		for i in range(1,6):
			if i in s:
				l.append(i)
		return l
		
		