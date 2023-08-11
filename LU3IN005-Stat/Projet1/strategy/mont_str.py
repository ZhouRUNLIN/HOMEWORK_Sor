from strategy.common_str import *

class Mont_str(Strategy):
	def __init__(self):
		super().__init__()
	
	def jouer(self):
		nb=0
		while not self.bat.victoire():
			pos=self.salle_de_representation_dartillerie_modifiee()
			p=self.bat.joue(pos)
			nb+=1
		return nb
	
	def affiche_stat(self,fois):
		data=[]
		esp=0.0
		for i in range(fois):
			print(i)
			self.bat.reset()
			data.append(self.jouer())
			esp+=data[-1]
		plt.hist(data,bins=100)
		plt.xlabel("nb steps")
		plt.ylabel("frequency")
		plt.title("esperiance : "+str(esp/fois))
		plt.savefig('./figures/mont.jpg')
		
	def salle_de_representation_dartillerie_modifiee(self):
		prop_mat=numpy.zeros((10,10))
		for attempt in range(100):
			m=self.case_matrix()
			while m.min()<0:
				m=self.case_matrix()
			prop_mat+=m
		prop_mat*=1-self.bat.toucher_cible
		if Grille.eq(prop_mat,numpy.zeros((10,10))):
			return super().generer_alea()
		return numpy.unravel_index(numpy.argmax(prop_mat),prop_mat.shape)
		
	def case_matrix(self):
		gTemp=Grille()
		remain=self.bat.ships_remain()
		mat_sunk=numpy.zeros((10,10))
		for x in range(10):
			for y in range(10):
				if self.bat.g1.grille[x][y] not in remain+[0]:
					mat_sunk[x][y]=1
		gTemp.grille=self.bat.record-self.bat.toucher_cible+mat_sunk
		for bateau in remain:
			if gTemp.place_alea_restricted(gTemp.grille,bateau,1000)==0:
				return self.case_matrix()
		if (gTemp.grille-self.bat.toucher_cible).min()<0:
			try:
				return self.case_matrix()
			except Exception as e:
				return gTemp.grille-self.bat.toucher_cible
		return gTemp.grille-(self.bat.record-self.bat.toucher_cible+mat_sunk)