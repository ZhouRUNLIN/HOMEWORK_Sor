# Auteur : ZHOU runlin
# Num Etu : 28717281

# Code du Ex 1

from utils import *

def code_to_mesure_KEY(fileName, size):
    """ le block du code a execute pour comparer le temp qu'il besoin """
    with open(fileName, "r") as file:
        # lire la contenu
        lines = file.readlines()
    
    for line in lines:
        while len(line) < 35:
            line += 'a'
        # process la contenu
        key = Key(size)
        key.storeNum(line)
        key.outputNum()
        
class Key:
    def __init__(self, size=4):
        self.size = size
        self.data = []

    def storeNum(self, char):
        self.data = charToInt(char, self.size)

    def outputNum(self):
        return intToChar(self.data)
    
    def __lt__(self, other):
        if isinstance(other, Key):
            for i in range(self.size):
                if (self.data[i] < other.data[i]):
                    return True
                if (self.data[i] > other.data[i]):
                    return False
            return False
        
    def __gt__(self, other):
        if isinstance(other, Key):
            for i in range(self.size):
                if (self.data[i] > other.data[i]):
                    return True
                if (self.data[i] < other.data[i]):
                    return False
            return False
    
    def __eq__(self, other):
        if isinstance(other, Key):
            for i in range(self.size):
                if (self.data[i] != other.data[i]):
                    return False
            return True
    
    def __le__(self, other):
        if isinstance(other, Key):
            return self.__lt__(other) or self.__eq__(other)
    
    def __ge__(self, other):
        if isinstance(other, Key):
            return self.__gt__(other) or self.__eq__(other)

# test pour la fonction inf et eg
if __name__ == '__main__':
    print("test pour la fonction inf et eg")
    key1 = Key(4)
    key2 = Key(4)
    key3 = Key(4)

    key1.storeNum("0x334150c645555e0108300aee0b62678a")
    key2.storeNum("0x1e106252b5a625cef8c7be5aaf67afaf")
    key3.storeNum("0x1e106252b5a625cef8c7be5aaf67afaf")

    print("key1 == key2 is : " + str(key1 == key2))
    print("key2 == key3 is : " + str(key2 == key3))
    print("key1 < key2 is : " + str(key1 < key2))
    print("key2 < key3 is : " + str(key2 >= key3))
    print(key1.data)

    # init pour experience
    filePath = "cles_alea"
    fileList = list_files(filePath)
    fileList.sort()
    size = 1
    data = np.zeros((6, 8)) # data est init a une matrix vide avec la taille de 6*6

    for i in range(0, 6):
        cpt = 0
        for fileName in fileList:
            # en utilisent timeit pour mesure de temps consomme par ce code block
            code_block = lambda: code_to_mesure_KEY(fileName, size)
            data[i][cpt % 8] += timeit.timeit(stmt=code_block, number=1)
            cpt += 1
        size *= 2

    produceGraphe(data, x_axis = [1000, 10000, 120000, 20000, 200000, 5000, 50000, 80000], title = "Find The Best Size", xlabel = "number of key", ylabel="time used")
