import numpy
import matplotlib.pyplot as plt
import matplotlib.cm as cm

class Zone_Cotiere:
	def __init__(self):
		#on crée une tableau avec 20 blocs
		self.listY=numpy.zeros(20)
		self.listPi=Zone_Cotiere.generate_pi()
		self.ps=0.2		#on définir la valeur de ps
		
		"""
		Pour la boucle dans while, on ajoute les valeurs de Pi_i de chaque bloc.
		Après, nous insérons l'objet dans une cellule au moyen d'un nombre aléatoire généré
		"""
		while self.listY.sum()==0:
			py=numpy.random.rand()*self.listPi.sum()
			for i in range(19,-1,-1):
				if self.listPi[0:i+1].sum()<=py:
					self.listY[i]=1
					break
		
	def generate_pi():
		l=numpy.zeros(20)
		l[-1]=(numpy.random.rand()+1)/2
		for i in range(18,-1,-1):
			l[i]=l[i+1]*(numpy.random.rand()+10)/11
		return l

	def scan_SS(self):
		"""
		retourne 1 si le senseur détecte l'objet avec la probabilité de ps
		et 0 sinon
		"""
		p=numpy.unravel_index(numpy.argmax(self.listPi),self.listPi.shape)[0]
		if self.listY[p]==0 or numpy.random.rand()>self.ps:
			self.listPi[p]*=1-self.ps
			return 0
		return 1
	
	def action_prop(self):
		num=0
		while self.scan_SS()!=1:
			num+=1
		return num
	
	def action_traverse(self):
		num=0
		while True:
			for i in range(19,-1,-1):
				if self.listY[i]==0 or numpy.random.rand()>self.ps:
					num+=1
				else:
					return num
	
	def reset(self):
		self.__init__()
	
	def affiche_prop(self):
		"""affiche le graph de fréquence"""
		data=[]
		esp=0.0
		for i in range(1000):
			data.append(self.action_prop())
			esp+=data[-1]
			self.reset()
		plt.hist(data,bins=100)
		plt.xlabel("nb steps")
		plt.ylabel("frequency")
		plt.title("esperiance : "+str(esp/1000))
		plt.savefig('./figures/SS_prop.jpg')
		
	def affiche_traverse(self):
		"""affiche le graph de fréquence"""
		data=[]
		esp=0.0
		for i in range(1000):
			data.append(self.action_traverse())
			esp+=data[-1]
			self.reset()
		plt.hist(data,bins=100)
		plt.xlabel("nb steps")
		plt.ylabel("frequency")
		plt.title("esperiance : "+str(esp/1000))
		plt.savefig('./figures/SS_traverse.jpg')
	
z=Zone_Cotiere()
z.affiche_prop()
#z.affiche_traverse()
