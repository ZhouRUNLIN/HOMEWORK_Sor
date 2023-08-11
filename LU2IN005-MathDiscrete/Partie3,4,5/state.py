# -*- coding: utf-8 -*-
"""
Code à ne pas modifier
"""

class State :
    """
    Classe définissant un état caractérisée par :
        - un identifiant
        - un booleen pour savoir si c'est un état initial
        - un booleen pour savoir si c'est un état final
        - un label utilisé pour les constructions
             ou il faut memoriser d'ou vient l'etat construit
    """

    def __init__ (self, id, init, fin, label=None) :
        """ int x bool x bool x str -> State
        constructeur d'état
        """
        self.id = id
        self.init = init
        self.fin = fin
        if label == None :
            self.label = str(self.id)
        else :
            self.label =label


    def __repr__(self) :
        """ -> str
        renvoie une description de l'état sous la forme d'une chaîne
        de caractères contenant son label puis (init) si c'est un état
        initial puis (fin) si c'est un état final
        elle permet d'utiliser print pour les états
        """
        # ret : str
        ret = str(self.label)
        if self.init :
            ret = ret + "(init)"
        if self.fin :
            ret = ret+ "(fin)"
        return ret


    def insertPrefix(self, prefid, preflab=None):
        """ int x str ->
        met à jour l'identifiant et le label de l'état en les
        préfixant avec la chaîne de caractères pref
        """
        if self.id < 0 :
            tempid = - self.id
        else :
            tempid = self.id
        tempid2 = 10**len(str(tempid))*prefid + tempid
        if self.id < 0 :
            self.id = - tempid2
        else :
            self.id = tempid2
        if preflab == None :
            self.label = str(prefid) + "_" + str(self.label)
        else :
            self.label = str(preflab) + "_" + str(self.label)


    def __eq__(self, other) :
        """ Val -> bool
        rend le booléen vrai si l'état est égal à other, faux sinon
        elle permet que == fonctionne pour les états
        """
        return type(self) == type(other) and self.id == other.id

    def __ne__(self, other) :
        """ Val -> bool
        rend le booléen vrai si l'état est différent de other, faux sinon
        elle permet que != fonctionne pour les états
        """
        return not self.__eq__(other)

    def __hash__(self) :
        """ -> int
        rend un identifiant unique (utile pour les tables de hachage)
        elle permet que les états puissent appartenir à des ensembles
        """
        if type(self.id)== int :
            return self.id
        # s : str
        s=str(self.id)
        # res : str
        res=''.join(str(ord(c)) for c in s)
        return int(res)

    # MJ? on leur fournit ça ???
    @staticmethod
    def isInitialIn(list) :
        """ list[State] -> bool
        rend vrai si l'un des états de list est un état initial, faux sinon
        """
        # s : State
        for s in list :
            if s.init :
                return True
        return False

    # MJ? on leur fournit ça ???
    @staticmethod
    def isFinalIn(list) :
        """ list[State] -> bool
        rend vrai si l'un des états de list est un état final, faux sinon
        """

        for s in list :
            if s.fin :
                return True
        return False
