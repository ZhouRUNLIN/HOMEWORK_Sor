import sp
import re
import os
from sp import *
from re import *

"""
Construction du parser pour les fichiers contenant les descriptions
textuelles des automates
"""

class MyParser :
    @staticmethod
    def Auto():
        etat = R(r'\d+')
        #etat = R(r'\w+')

        with Separator(r'[\s\n\r]+'):
            trans = '(' & etat & R(r'\w') & etat & ')'
            listeEtats =  etat[:]
            listeInit = etat[:]
            listeFin = etat[:]
            listeTrans = trans[:]
            auto = '#E:' & listeEtats & '#I:' & listeInit & '#F:'& listeFin & '#T:' & listeTrans



        return auto



    @staticmethod
    def parseFromFile (nomFichier) :
        fichier = open (nomFichier)
        my_parser = MyParser.Auto()
        s=fichier.read()
        result = my_parser(s)
        fichier.close()
        return result
