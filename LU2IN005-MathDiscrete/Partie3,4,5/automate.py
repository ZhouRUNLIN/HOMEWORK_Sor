# -*- coding: utf-8 -*-
from transition import *
from state import *
import os
import copy
from itertools import product
from automateBase import AutomateBase
import itertools


class Automate(AutomateBase):
        
    def succElem(self, state, lettre):
        """State x str -> list[State]
        rend la liste des états accessibles à partir d'un état
        state par l'étiquette lettre
        """
        successeurs = []
        # t: Transitions
        for t in self.getListTransitionsFrom(state):
            if t.etiquette == lettre and t.stateDest not in successeurs:
                successeurs.append(t.stateDest)
        return successeurs


    def succ (self, listStates, lettre):
        """list[State] x str -> list[State]
        rend la liste des états accessibles à partir de la liste d'états
        listStates par l'étiquette lettre
        """
        successeurs = []
        for s in listStates:
            for t in self.getListTransitionsFrom(s):
                if t.etiquette == lettre and t.stateDest not in successeurs:
                    successeurs.append(t.stateDest)
        return successeurs
        



    """ Définition d'une fonction déterminant si un mot est accepté par un automate.
    Exemple :
            a=Automate.creationAutomate("monAutomate.txt")
            if Automate.accepte(a,"abc"):
                print "L'automate accepte le mot abc"
            else:
                print "L'automate n'accepte pas le mot abc"
    """
    @staticmethod
    def accepte(auto,mot) :
        """ Automate x str -> bool
        rend True si auto accepte mot, False sinon
        """
        for s0 in auto.listStates:
          if s0.init:
            list = Automate.succElem(auto,s0,mot[0])
            for i in mot[1:]:
                list = Automate.succ(auto,list,i)
            for j in list:
                if j.fin == True:
                    return True
        return False


    @staticmethod
    def estComplet(auto,alphabet) :
        """ Automate x str -> bool
         rend True si auto est complet pour alphabet, False sinon
        """
        for s in auto.listStates:
            list=auto.getListTransitionsFrom(s)
            ls=[]
            for k in list:
                if k.etiquette not in ls:
                    ls.append(k.etiquette)
            if len(ls) != len(alphabet):
                return False
        return True

        
    @staticmethod
    def estDeterministe(auto) :
        """ Automate  -> bool
        rend True si auto est déterministe, False sinon
        """
        for s in auto.listStates:
            list=auto.getListTransitionsFrom(s)
            ls=[]
            for k in list:
                if k.etiquette not in ls:
                    ls.append(k.etiquette)
                else: 
                    return False 
        return True
        

       
    @staticmethod
    def completeAutomate(auto,alphabet) :
        """ Automate x str -> Automate
        rend l'automate complété d'auto, par rapport à alphabet
        """
        if(Automate.estComplet(auto,alphabet)):
            return auto
        auto_c=copy.deepcopy(auto)
        sn=State("new",False,False)
        auto_c.addState(sn)
        for s in auto_c.listStates:
            list=auto.getListTransitionsFrom(s)
            ls=[]
            for k in list:
                if k.etiquette not in ls:
                    ls.append(k.etiquette)
            for i in alphabet:
                if i not in ls:
                    tn=Transition(s,i,sn)
                    auto_c.listTransitions.append(tn)          
        return auto_c

       

    @staticmethod
    def rec(auto,li_s,li_t,state0) :
        while(state0 not in li_s):
            alpha=set()
            for s in state0:
                list=auto.getListTransitionsFrom(s)
                for k in list:
                    if k.etiquette not in alpha:
                        alpha.add(k.etiquette)            
            for l in alpha:
                state1=set()
                for s in state0:
                    li=auto.getListTransitionsFrom(s)
                    li_t_l=[]
                    for t in li:
                        if(t.etiquette==l):
                            li_t_l.append(t)
                    if li_t_l==[]:
                        state1.add(s)
                    else:
                        for t in li_t_l:
                            if t.stateDest not in state1:
                                state1.add(t.stateDest)
                s0=State(state0,False,False)
                s1=State(state1,False,False)
                t=Transition(s0,l,s1)
                li_t.append(t)
                li_s.append(state0)
                li_t=Automate.rec(auto,li_s,li_t,state1)
        return li_t

    @staticmethod
    def determinisation(auto) :
        """ Automate  -> Automate
        rend l'automate déterminisé d'auto
        """
        state0=set()
        for s in auto.listStates:
            if ((s.init==True) and (s.fin==False)):
                state0.add(s)       
        li_t=Automate.rec(auto,[],[],state0)
        auto_d=Automate(li_t)
        return auto_d
    @staticmethod
    def complementaire(auto):
        """ Automate -> Automate
        rend  l'automate acceptant pour langage le complémentaire du langage de a
        """
        alphabet=set()
        list=auto.listTransitions
        for k in list:
            if k.etiquette not in alphabet:
                alphabet.add(k.etiquette)  
        autom=Automate.completeAutomate(auto,alphabet)
        autom_c=Automate([])
        for st in autom.listStates:            
            if (st.fin==True):
                stn=State(st.id,True,False)
            else:
                stn=State(st.id,False,True) 
            autom_c.addState(stn)
        for s in autom.listStates:
            if (s.fin==True):
                sn=State(s.id,True,False)
            else:
                sn=State(s.id,False,True) 
            t_deja=[]
            for t in autom.listTransitions:
                if t not in t_deja:
                    if (t.stateSrc==s) and (t.stateDest!=s):
                        tn=Transition(sn,t.etiquette,t.stateDest)
                        autom_c.addTransition(tn)
                        t_deja.append(t) 
                    if (t.stateSrc!=s) and (t.stateDest==s):
                        tn=Transition(t.stateSrc,t.etiquette,sn)
                        autom_c.addTransition(tn)
                        t_deja.append(t) 
                    if (t.stateSrc==s) and (t.stateDest==s):
                        tn=Transition(sn,t.etiquette,sn)
                        autom_c.addTransition(tn)
                        t_deja.append(t) 
            li=autom_c.listTransitions
            for t in li:
                    if (t.stateSrc==s) and (t.stateDest!=s):
                        tn=Transition(sn,t.etiquette,t.stateDest)
                        autom_c.removeTransition(t)
                        autom_c.addTransition(tn)
                    if (t.stateSrc!=s) and (t.stateDest==s):
                        tn=Transition(t.stateSrc,t.etiquette,sn)
                        autom_c.removeTransition(t)
                        autom_c.addTransition(tn)
        return autom_c
   
    @staticmethod
    def rec2 (auto0, auto1,li_s,li_t,li):
        alpha=[]
        for t in auto0.listTransitions:
            if t.etiquette not in alpha:
                alpha.append(t.etiquette)
        for (s0,s1) in li:
            while (s0,s1) not in li_s:
                if s0.init and s1.init :
                    s_i=State((s0,s1),True,False)
                if s0.fin and s1.fin :
                    s_i=State((s0,s1),False,True)
                else:
                    s_i=State((s0,s1),False,False)
                li_s.append((s0,s1))
                for l in alpha:
                    li_s_f0=Automate.succElem(auto0, s0, l)
                    li_s_f1=Automate.succElem(auto1, s1, l)
                    li_s_f=list(itertools.product(li_s_f0, li_s_f1))
                    for (s_f0,s_f1) in li_s_f:
                        if s_f0.init and s_f1.init :
                            s_f=State((s_f0,s_f1),True,False)
                        if s_f0.fin and s_f1.fin :
                            s_f=State((s_f0,s_f1),False,True)
                        else:
                            s_f=State((s_f0,s_f1),False,False)
                        t=Transition(s_i, l, s_f)
                        li_t.append(t)
                    li_t=Automate.rec2(auto0,auto1,li_s,li_t,li_s_f)
        return li_t
    @staticmethod
    def intersection (auto0, auto1):
        """ Automate x Automate -> Automate
        rend l'automate acceptant pour langage l'intersection des langages des deux automates
        """
        init0=[];init1=[];
        fin0=[];fin1=[];
        for s in auto0.listStates:
            if s.init:
                init0.append(s)
            if s.fin:
                fin0.append(s)
        for s in auto1.listStates:
            if s.init:
                init1.append(s)
            if s.fin:
                fin1.append(s)
        init=list(itertools.product(init0, init1))
        li_t=Automate.rec2(auto0,auto1,[],[],init)
        auto=Automate(li_t)
        return auto

    @staticmethod
    def union (auto0, auto1):
        """ Automate x Automate -> Automate
        rend l'automate acceptant pour langage l'union des langages des deux automates
        """
        return
    @staticmethod
    def concatenation (auto1, auto2):
        """ Automate x Automate -> Automate
        rend l'automate acceptant pour langage la concaténation des langages des deux automates
        """
        li_t=[]
        for s in auto1.listStates:
            if s.fin:
                s_f1=State(s.id,False,False)         
        for s in auto2.listStates:
            if s.init:
                s_i2=State(s.id,False,False)   
        for t in auto1.listTransitions:
            if (((t.stateDest).fin) and (not ((t.stateSrc).fin))):
                tc=Transition(t.stateSrc,t.etiquette,s_i2)
                li_t.append(tc)
            if (((t.stateDest).fin) and ((t.stateSrc).fin)):
                tc=Transition(s_f1,t.etiquette,s_i2)
                li_t.append(tc)
            if (((t.stateDest).fin) and (not ((t.stateSrc).fin))):
                tn=Transition(t.stateSrc,t.etiquette,s_f1)
                li_t.append(tn)
            elif (((not ((t.stateDest).fin))) and ((t.stateSrc).fin)):
                tn=Transition(s_f1,t.etiquette,t.stateDest)
                li_t.append(tn)
            elif (((t.stateDest).fin) and ((t.stateSrc).fin)):
                tn=Transition(s_f1,t.etiquette,s_f1)
                li_t.append(tn)
            else:
                li_t.append(t)
        for t in auto2.listTransitions:
            if (not (t.stateDest).init) and (t.stateSrc).init:
                tn=Transition(s_i2,t.etiquette,t.stateDest)
                li_t.append(tn)
            elif (t.stateDest).init and not (t.stateSrc).init:
                tn=Transition(t.stateSrc,t.etiquette,s_i2)
                li_t.append(tn)
            elif (t.stateDest).init and (t.stateSrc).init:
                tn=Transition(s_i2,t.etiquette,s_i2)
                li_t.append(tn)
            else:
                li_t.append(t)
        auto=Automate(li_t)
        return auto


    @staticmethod
    def etoile (auto):
        """ Automate  -> Automate
        rend l'automate acceptant pour langage l'étoile du langage de a
        """
        li_t=auto.listTransitions
        for s in auto.listStates:
            if s.init:
                init=s
        for t in auto.listTransitions:
            if (t.stateDest).fin:
                tn=Transition(t.stateSrc,t.etiquette,init)
                li_t.append(tn)
        auto2=Automate(li_t)
        return auto2




