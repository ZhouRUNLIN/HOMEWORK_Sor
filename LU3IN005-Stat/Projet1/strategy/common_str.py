from model.bataille import *

class Strategy:
	def __init__(self):
		self.bat=Bataille()
		
	def peut_jouer(self,position):
		return self.bat.record[position[0]][position[1]]==0
	
	def generer_alea(self):
		x=numpy.random.randint(0,10)
		y=numpy.random.randint(0,10)
		if self.bat.record[x][y]==0:
			return (x,y)
		return self.generer_alea()