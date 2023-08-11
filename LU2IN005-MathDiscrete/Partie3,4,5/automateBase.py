# -*- coding: utf-8 -*-
"""
Code à ne pas modifier
"""

from transition import *
from state import *
import os
import sys
from copy import copy
from sp import *
from myparser import *
from itertools import product



"""Classe définissant un automate caractérise par :
    - une liste de transitions
    - optionnellement une liste d'etats
    - optionnellement un label
"""
class AutomateBase :


    def __init__ (self, listTransitions, listStates=None, label=None) :
        """list[Transition] x list[State] x list[str] -> Automate
        construit l'automate construit à partir de la liste de
        transitions fournies, et optionnellement la liste d'états et
        l'étiquette
        """
        self.label = label
        if listStates == None :
            self.listStates = []
        else :
            self.listStates = copy(listStates)
        self.listTransitions = copy(listTransitions)
        self.listStates = self.__getListStates()



    def getAlphabetFromTransitions(self):
        """ -> list[str]
        rend la liste des labels d'étiquettes présents dans l'automate
        """
        # recuperation des etiquettes sous forme de set et reconversion en liste
        return list(set([t.etiquette for t in self.listTransitions]))


    def __getListStates(self):
        """  -> list[State]
        rend la liste des états - usage interne
        """
        # states : set[State]
        states = set(self.listStates)
        # t : Transition
        for t in self.listTransitions :
            states = states | {t.stateSrc, t.stateDest}
        return list(states)



    def addTransition(self, transition) :
        """Transition -> Bool
        fait la mise à jour de l'automate en lui ajoutant la
        transition, en ajoutant les états impliqués dans l'automate
        s'ils en sont absents
        rend True si l'ajout a eu lieu, False sinon (si t était déjà
        présente dans l'automate)
        """
        if transition not in self.listTransitions :
            self.listTransitions.append(transition)
            if transition.stateSrc not in self.listStates :
                self.listStates.append(transition.stateSrc)
            if transition.stateDest not in self.listStates :
                self.listStates.append(transition.stateDest)
            return True
        return False


    def removeTransition(self, transition) :
        """Transition -> Bool
        fait la mise à jour de l'automate en lui enlevant la
        transition, sans modifier les états
        rend True si la suppression a eu lieu, False sinon (si t était
        absente de l'automate)
        """
        if transition in self.listTransitions :
            self.listTransitions.remove(transition)
            return True
        return False


    def addState(self, state) :
        """State -> Bool
        fait la mise à jour de l'automate en lui ajoutant l'état state
        rend True si l'ajout a eu lieu, False sinon (si s était déjà
        présent dans l'automate)
        """
        if state not in self.listStates :
                self.listStates.append(state)
                return True
        return False


    def removeState(self, state) :
        """State -> Bool
        fait la mise à jour de l'automate en lui supprimant l'état
        ainsi que toutes les transisitions entrantes et sortantes
        rend True si la suppression a eu lieu, False sinon  (si s
        était absent de l'automate)
        """
        if state in self.listStates :
            copyT=[t for t in self.listTransitions]
            for t in copyT :
                if t.stateSrc == state or t.stateDest == state :
                    self.removeTransition(t)
            self.listStates.remove(state)
            return True
        return False



    def getListInitialStates (self) :
        """ -> list[State]
        rend la liste des états initiaux
        """
        # initStates : list[State]
        initStates = []
        # i : State
        for i in self.listStates :
            if i.init :
                initStates.append(i)
        return initStates


    def getListFinalStates (self) :
        """ -> list[State]
        rend la liste des états finals
        """
        # finStates : list[State]
        finStates = []
        # i : State
        for i in self.listStates :
            if i.fin :
                finStates.append(i)
        return finStates



    def getListTransitionsFrom (self, state) :
        """ State -> list[Transition]
        rend la liste des transitions sortant de l'état state dans l'automate
        """
        # list : list[Transition]
        list = []
        # state : State
        if state in self.listStates :
            # t : Transition
            #filtre listTransitions sur les elements t tels que t.StateSrc==state
            list+=filter(lambda t:t.stateSrc==state, self.listTransitions)
        return list



    def toDot (self) :
        """-> str
        rend une description de l'automate au format dot qui sera
        appelée par la fonction show
        """
        # ret : str
        ret = "digraph a {\n rankdir=LR\n"
        # state : State
        for state in self.listStates :
            ret += str(state.id) + "[ label =\"" + str(state.label) + "\","
            #Test pour savoir si l'etat est initial et/ou final
            if state.init :
               ret += " color=red "
            if state.fin :
               ret += "peripheries=2 "
            ret += "];\n"

            #Ecriture des transitions depuis l'etat
            # liste : list[Transition]
            liste = list( self.getListTransitionsFrom(state))
            # trans : liste[Transition]
            for trans in liste :
                # etiq : str
                etiq = trans.etiquette
                # listToRemove : list[Transition]
                listToRemove = []
                # t : Transition
                for t in  liste :
                    if t.stateDest.id == trans.stateDest.id and t.etiquette != trans.etiquette :
                        etiq = etiq + " , " + t.etiquette
                        listToRemove.append(t)
                for t in listToRemove :
                    liste.remove(t)
                ret += str(trans.stateSrc.id) +" -> " + str(trans.stateDest.id)
                ret += " [ label = \"" + etiq + "\" ];\n"
        #Fin de l'automate
        ret += "}\n"
        #print(ret)
        return ret
    
    #def show (self, nomFichier) :
        """ str ->
        Produit un fichier pdf donnant une représentation graphique de l'automate
        Erreur si l'impression s'est mal passée
        """
        """   
        try :
            # fichier : File
            fichier = open(nomFichier + ".dot", "w")
            fichier.write(self.toDot())
            fichier.close()
            #os.system("dot -Tps "+ nomFichier + ".dot -o " + nomFichier + ".ps" )
            #os.system("ps2pdf " + nomFichier + ".ps " + nomFichier + ".pdf")

            os.system("dot -Tpdf "+nomFichier + ".dot -o " + nomFichier + ".pdf")
            #WINDOWS
            #os.system("start " + nomFichier + ".pdf")
            #MAC
            os.system("open " + nomFichier + ".pdf")
            #LINUX
            #os.system("evince " + nomFichier + ".pdf &")
            os.system("rm " + nomFichier + ".dot " + nomFichier + ".ps")


        except IOError :
            print("Impossible de creer le fichier .dot" )
        """

    def show (self, nomFichier) :
        """ str ->
        Produit un fichier pdf donnant une représentation graphique de l'automate
        Erreur si l'impression s'est mal passée
        """
        try :
            # fichier : File
            fichier = open(nomFichier + ".dot", "w")
            fichier.write(self.toDot())
            fichier.close()
            #os.system("dot -Tps "+ nomFichier + ".dot -o " + nomFichier + ".ps" )
            #os.system("ps2pdf " + nomFichier + ".ps " + nomFichier + ".pdf")

            os.system("dot -Tpdf "+nomFichier + ".dot -o " + nomFichier + ".pdf")
            #WINDOWS
            #os.system("start " + nomFichier + ".pdf")
            if sys.platform.startswith("win32") or sys.platform.startswith("cygwin"):
                os.system("start " + nomFichier + ".pdf")
            #MAC
            #os.system("open " + nomFichier + ".pdf")
            elif sys.platform.startswith("darwin"):
                os.system("open " + nomFichier + ".pdf")
            #LINUX
            #os.system("evince " + nomFichier + ".pdf &")
            elif sys.platform.startswith("linux"):
                os.system("xdg-open " + nomFichier + ".pdf &")

            if os.name == "posix":
                os.system("rm " + nomFichier + ".dot")
            elif os.name == "nt":
                os.system("del " + nomFichier + ".dot")
            # os.system("rm " + nomFichier + ".dot " + nomFichier + ".ps")


        except IOError :
            print("Impossible de creer le fichier .dot" )



    def __repr__(self) :
        """ -> str
        rend une description textuelle de l'automate
        elle permet d'utiliser print pour les Automate"""
        # ret : str
        if self.label is not None :
            ret= "Automate " + self.label + " Etats :"
        else :
            ret= "Etats :"
        # s : State
        for s in self.listStates:
                ret = ret + str(s) + "\n"
        ret = ret + "Transitions :"
        # t : Transition
        for t in self.listTransitions :
           ret = ret + str(t) + "\n"
        return ret

    """Fonction permettant d'initialiser un automate depuis un fichier
       Exemple :
           a = Automate.creationAutomate("testAutomate.txt")
    """

    @classmethod
    def creationAutomate (cls, nomFichier) :
        """  str -> Automate
        rend l'automate construit en lisant le contenu du fichier dont
        le nom est fourni en argument
        Exemple :
        a = Automate.creationAutomate("testAutomate.txt")
        """
        # listeResultat : list[str]
        listeResultat = MyParser.parseFromFile(nomFichier)
        #On recupere les listes des etats
        #listeInit : list[str]
        listeInit = listeResultat[1]
        #print("listeInit" + str(listeInit))
        # listeFin : list[str]
        listeFin = listeResultat[2]
        #print(listeFin)
        #listeEtats: list[str]
        listeEtats = listeResultat[0]
        #print(listeEtats)
        # listeE : list[State]
        listeE=[]
        # listeT : list[Transition]
        listeT=[]
        # e: str
        for e in listeInit: # on commence par ajouter les états initiaux
            # s: State
            s=State(int(e), True, False)
            if e in listeFin :#on vérifie si l'état est à la fois initial et final
                s.fin = True
            if s not in listeE :
                listeE.append(s)
        for e in listeFin : # on ajoute ensuite les états finals
            s=State(int(e), False, True)
            if e not in listeInit : #sinon on a deja ajoute e dans la boucle précédente
                if s not in listeE :
                    listeE.append(s)
        for e in listeEtats : #on ajoute les états ni initiaux ni finals
            s=State(int(e), False, False)
            if e not in listeInit and e not in listeFin :
                if s not in listeE :
                    listeE.append(s)
        #print("liste Etats"+ str(listeE))

        #on récupère la liste des transitions
        #listeTrans : list[str]
        listeTrans = listeResultat[3]
        # t: str
        for t in listeTrans :
            #stateSrc : State
            stateSrc = State(int(t[0]), False, False)
            #stateDest : State
            stateDest = State(int(t[2]), False, False)
            if stateSrc in listeE : # si l'état est déjà dans la liste
                # d'états, on récupère le vrai état,
                #sinon c'est par défaut un état ni initial ni final
                stateSrc = listeE[listeE.index(stateSrc)]
            if stateDest in listeE : # si l'état est déjà dans la
                # liste d'états, on récupère le vrai état,
                #sinon c'est par défaut un état ni initial ni final
                stateDest = listeE[listeE.index(stateDest)]
            # trans : Transition
            trans=Transition(stateSrc,t[1],stateDest)
            if trans not in listeT:
                listeT.append(trans)
        #automate : Automate
        automate = cls(listeT,listeE)
        return automate

        """ Fonction inserant un prefixe a chaque identifiant et label d'etat
        Le prefixe doit être un nombre entier"""
    def prefixStates(self, prefixe):
        """ int ->
        modifie le nom de tous les états de l'automate en les
        préfixant par prefixe
        HYPOTHESE le préfixe est positif
        """
        # state : State
        for state in self.listStates :
                state.insertPrefix(prefixe)
                
    
    """Fonction testant l'égalité champ par champ de deux automates. 
    AutomateBase x AutomateBase -> Bool
    """
    def equals(self, other):
        if isinstance (other, self.__class__):
            return self.label == other.label and self.listTransitions == other.listTransitions and self.listStates == other.listStates
        return False
