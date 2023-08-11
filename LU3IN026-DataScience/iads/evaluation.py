# -*- coding: utf-8 -*-

"""
Package: iads
File: evaluation.py
Année: LU3IN026 - semestre 2 - 2022-2023, Sorbonne Université
"""

# ---------------------------
# Fonctions d'évaluation de classifieurs

# import externe
import numpy as np
import pandas as pd

# ------------------------ 
#TODO: à compléter  plus tard
# ------------------------ 
def analyse_perfs(L):
    """ L : liste de nombres réels non vide
        rend le tuple (moyenne, écart-type)
    """
    return (np.mean(L),np.std(L))

def crossval(X, Y, n_iterations, iteration, init_seed=42):
    np.random.seed(init_seed)
    index = np.random.permutation(len(X))
    Xtest = np.asarray([ X[index[j]] for j in range(int(len(X)*iteration/n_iterations),int(len(X)*(iteration+1)/n_iterations))])
    Ytest = np.asarray([ Y[index[j]] for j in range(int(len(Y)*iteration/n_iterations),int(len(Y)*(iteration+1)/n_iterations))])
    Xapp = np.asarray([ X[index[j]] for j in range(0,int(len(X)*iteration/n_iterations))]+[ X[index[j]] for j in range(int(len(X)*(iteration+1)/n_iterations),len(X))])
    Yapp = np.asarray([ Y[index[j]] for j in range(0,int(len(Y)*iteration/n_iterations))]+[ Y[index[j]] for j in range(int(len(Y)*(iteration+1)/n_iterations),len(Y))])
    return Xapp, Yapp, Xtest, Ytest

def crossval_strat(X, Y, n_iterations, iteration):
    valY = np.unique(Y)
    Xn = []
    for val in range(len(valY)):
        Xn.append(np.asarray([X[i] for i in range(len(X)) if Y[i]==valY[val]]))
    Xapp, Yapp, Xtest, Ytest = [],[],[],[]
    for val in range(len(valY)):
        XappTmp, YappTmp, XtestTmp, YtestTmp = crossval(Xn[val],np.asarray([valY[val]]*len(Xn[val])),n_iterations, iteration)
        Xapp.append(XappTmp.tolist())
        Yapp.append(YappTmp.tolist())
        Xtest.append(XtestTmp.tolist())
        Ytest.append(YtestTmp.tolist())
    XappF, YappF, XtestF, YtestF = [],[],[],[]
    for obj in Xapp:
        XappF += obj
    for obj in Yapp:
        YappF += obj
    for obj in Xtest:
        XtestF += obj
    for obj in Ytest:
        YtestF += obj
    return np.asarray(XappF), np.asarray(YappF), np.asarray(XtestF), np.asarray(YtestF)

def validation_croisee(C, DS, nb_iter):
    """ Classifieur * tuple[array, array] * int -> tuple[ list[float], float, float]
    """
    import copy
    X, Y = DS   
    perf = []
    
    newC = copy.deepcopy(C)
    
    ########################## COMPLETER ICI 
    for i in range(nb_iter):
        Xapp,Yapp,Xtest,Ytest = crossval_strat(X,Y,nb_iter,i)
        newC.train(Xapp,Yapp)
        perf.append(newC.accuracy(Xtest,Ytest))
    ##########################
    (perf_moy, perf_sd) = analyse_perfs(perf)
    return (perf, perf_moy, perf_sd)
