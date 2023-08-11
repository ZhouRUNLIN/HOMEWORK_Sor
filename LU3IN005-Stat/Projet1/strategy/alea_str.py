from strategy.common_str import *

class Alea_str(Strategy):
	def __init__(self):
		super().__init__()
	
	def jouer(self):
		nb=0
		while not self.bat.victoire():
			self.bat.joue(super().generer_alea())
			nb+=1
		return nb
	
	def affiche_stat(self,fois):
		data=[]
		esp=0.0
		for i in range(fois):
			self.bat.reset()
			data.append(self.jouer())
			esp+=data[-1]
		plt.hist(data,bins=100)
		plt.xlabel("nb steps")
		plt.ylabel("frequency")
		plt.title("esperiance : "+str(esp/fois))
		plt.savefig('./figures/alea.jpg')