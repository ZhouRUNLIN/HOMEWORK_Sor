# -*- coding: utf-8 -*-
"""
Code à ne pas modifier
"""
class Transition :
    """Classe définissant une transition caractérisée par :
        - une étiquette
        - un état source
        - un état de destination"""

    
    def __init__ (self,  stateSrc, etiquette, stateDest) :
        """ State x str x State -> Transition
        constructeur de transition
        """
        self.etiquette = str(etiquette)
        self.stateSrc = stateSrc
        self.stateDest = stateDest
    
    
    def __eq__(self, other) :
        """ Val -> bool
        rend le booléen vrai si la transition est égale à other, faux sinon
        elle permet que == fonctionne pour les transitions
        """
        return type(self)==type(other) and self.etiquette == other.etiquette and self.stateDest ==other.stateDest and self.stateSrc == other.stateSrc


    def __ne__(self, other) :
        """ Val -> bool
        rend le booléen vrai si la transition est différente de other, faux sinon
        elle permet que != fonctionne pour les transitions
        """
        return not self.__eq__(other)
    
    def __repr__(self) :
        """ -> str
        renvoie une description de la transition sous la forme d'une
        chaîne de caractères contenant, entre crochets, l'état de
        départ, un tiret, l'étiquette de transition, une flèche et
        l'état final
        elle permet d'utiliser print pour les transitions
        """
        return '['+str(self.stateSrc)+'-'+str(self.etiquette)+'->'+str(self.stateDest)+']'

        

    
