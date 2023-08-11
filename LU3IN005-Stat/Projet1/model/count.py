#Partie 2 Combinatoire du jeu

from model.grille import *

def count_possible(bateau):
	"""
	permet de calculer le nombre de façons de placer un bateau donné sur une grille vide.
	"""
	gTemp=Grille()
	count=0
	for i in range(0,10):
		for j in range(0,10):
			for d in range(1,3):
				if Grille.peut_placer(gTemp.grille, bateau, (i,j), d):
					count+=1
	return count

def count_possible_mult(list_bateau):
	"""
	permet de calculer le nombre de façon de placer une liste de bateaux sur une grille vide
	"""
	gTemp=Grille()
	return count_possible_rec(gTemp.grille, list_bateau)

def count_possible_rec(grille, list_bateau):
	if len(list_bateau)==0:
		return 1
	count=0
	gTmp=Grille()
	for d in range(1,3):
		for i in range(0,10):
			for j in range(0,10):
				if Grille.peut_placer(grille,list_bateau[0],(i,j),d):
					#print(str(i)+" "+str(j)+" "+str(d))
					gTmp.grille=grille.copy()
					gTmp.place(gTmp.grille,list_bateau[0],(i,j),d)
					count+=count_possible_rec(gTmp.grille, list_bateau[1:])
	return count

def generer_grille_equiproque(grille):
	g0=Grille()
	count=0
	while not Grille.eq(grille,g0.grille):
		g0.grille=numpy.zeros((10, 10))
		g0.genere_grille()
		count+=1
	return count

def approximation(precision):
	g0=Grille()
	g0.genere_grille()
	avg=0
	for i in range(precision):
		avg+=generer_grille_equiproque(g0.grille)
	return avg/precision
