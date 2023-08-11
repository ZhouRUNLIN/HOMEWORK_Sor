# -*- coding: utf-8 -*-

"""
Package: iads
File: utils.py
Année: LU3IN026 - semestre 2 - 2022-2023, Sorbonne Université
"""


# Fonctions utiles pour les TDTME de LU3IN026
# Version de départ : Février 2023

# import externe
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

# ------------------------ 
# genere_dataset_uniform:
def genere_dataset_uniform(p, n, binf=-1, bsup=1):
    """ int * int * float^2 -> tuple[ndarray, ndarray]
        Hyp: n est pair
        p: nombre de dimensions de la description
        n: nombre d'exemples de chaque classe
        les valeurs générées uniformément sont dans [binf,bsup]
    """
    if binf > bsup:
        raise NotImplementedError("Please Implement this method")
    return np.random.uniform(binf,bsup,(2*n,p)), np.asarray([-1 for i in range(0, n)] + [1 for i in range(0, n)])
# genere_dataset_gaussian:
def genere_dataset_gaussian(positive_center, positive_sigma, negative_center, negative_sigma, nb_points):
    """ les valeurs générées suivent une loi normale
        rend un tuple (data_desc, data_labels)
    """
   
    desc_negative = np.random.multivariate_normal(negative_center, negative_sigma, nb_points)
    desc_positive = np.random.multivariate_normal(positive_center, positive_sigma, nb_points)

    desc = np.concatenate((desc_negative, desc_positive))
    
    labels = np.asarray([-1 for i in range(0, nb_points)] + [+1 for i in range(0, nb_points)])
    
    return desc, labels

def genere_train_test(desc_set, label_set, n_pos, n_neg):
    """ permet de générer une base d'apprentissage et une base de test
        desc_set: ndarray avec des descriptions
        label_set: ndarray avec les labels correspondants
        n_pos: nombre d'exemples de label +1 à mettre dans la base d'apprentissage
        n_neg: nombre d'exemples de label -1 à mettre dans la base d'apprentissage
        Hypothèses: 
           - desc_set et label_set ont le même nombre de lignes)
           - n_pos et n_neg, ainsi que leur somme, sont inférieurs à n (le nombre d'exemples dans desc_set)
    """
    desc_pos = [desc_set[i] for i in range(len(desc_set)) if label_set[i]>=0]
    desc_neg = [desc_set[i] for i in range(len(desc_set)) if label_set[i]<0]
    pos1 = random.sample(desc_pos, n_pos)
    neg1 = random.sample(desc_neg, n_neg)
    for i in pos1:
        for j in range(len(desc_pos)):
            if (i==desc_pos[j]).all():
                desc_pos.pop(j)
                break
    for i in neg1:
        for j in range(len(desc_neg)):
            if (i==desc_neg[j]).all():
                desc_neg.pop(j)
                break
    desc_appr = np.asarray(pos1+neg1)
    label_appr = np.asarray([1]*n_pos+[-1]*n_neg)
    desc_test = np.asarray(desc_pos+desc_neg)
    label_test = np.asarray([1]*len(desc_pos)+[-1]*len(desc_neg))
    return (desc_appr,label_appr),(desc_test,label_test)
        

# plot2DSet:
def plot2DSet(desc,labels):    
    """ ndarray * ndarray -> affichage
        la fonction doit utiliser la couleur 'red' pour la classe -1 et 'blue' pour la +1
    """
    data2_negatifs = desc[labels == -1]
    data2_positifs = desc[labels == +1]
    plt.scatter(data2_negatifs[:,0],data2_negatifs[:,1],marker='o', color="red") # 'o' rouge pour la classe -1
    plt.scatter(data2_positifs[:,0],data2_positifs[:,1],marker='x', color="blue") # 'x' bleu pour la classe +1
# plot_frontiere:
def plot_frontiere(desc_set, label_set, classifier, step=30):
    """ desc_set * label_set * Classifier * int -> NoneType
        Remarque: le 4e argument est optionnel et donne la "résolution" du tracé: plus il est important
        et plus le tracé de la frontière sera précis.        
        Cette fonction affiche la frontière de décision associée au classifieur
    """
    mmax=desc_set.max(0)
    mmin=desc_set.min(0)
    x1grid,x2grid=np.meshgrid(np.linspace(mmin[0],mmax[0],step),np.linspace(mmin[1],mmax[1],step))
    grid=np.hstack((x1grid.reshape(x1grid.size,1),x2grid.reshape(x2grid.size,1)))
    
    # calcul de la prediction pour chaque point de la grille
    res=np.array([classifier.predict(grid[i,:]) for i in range(len(grid)) ])
    res=res.reshape(x1grid.shape)
    # tracer des frontieres
    # colors[0] est la couleur des -1 et colors[1] est la couleur des +1
    plt.contourf(x1grid,x2grid,res,colors=["darksalmon","skyblue"],levels=[-1000,0,1000])
    
# plot_frontiere:
def plot_frontiere_high_dimension(desc_set, label_set, classifier, nbdim, dim1, dim2, step=30):
    """ desc_set * label_set * Classifier * int -> NoneType
        Remarque: le 4e argument est optionnel et donne la "résolution" du tracé: plus il est important
        et plus le tracé de la frontière sera précis.        
        Cette fonction affiche la frontière de décision associée au classifieur
    """
    mmax=desc_set.max(0)
    mmin=desc_set.min(0)
    moys=[ desc_set[i].mean() for i in range(nbdim) ]
    x1grid,x2grid=np.meshgrid(np.linspace(mmin[dim1],mmax[dim1],step),np.linspace(mmin[dim2],mmax[dim2],step))
    grid=np.hstack((x1grid.reshape(x1grid.size,1),x2grid.reshape(x2grid.size,1)))
    
    # calcul de la prediction pour chaque point de la grille
    grid2=[moys[:] for i in range(len(grid))]
    for g in range(len(grid)):
        grid2[g][dim1]=grid[g][0]
        grid2[g][dim2]=grid[g][1]
    res=np.array([classifier.predict(grid2[i]) for i in range(len(grid2)) ])
    res=res.reshape(x1grid.shape)
    # tracer des frontieres
    # colors[0] est la couleur des -1 et colors[1] est la couleur des +1
    plt.contourf(x1grid,x2grid,res,colors=["darksalmon","skyblue"],levels=[-1000,0,1000])

def create_XOR(n, var):
    """ int * float -> tuple[ndarray, ndarray]
        Hyp: n et var sont positifs
        n: nombre de points voulus
        var: variance sur chaque dimension
    """
    desc1,label1 = genere_dataset_gaussian(np.array([1,1]),np.array([[var,0],[0,var]]),np.array([0,0]),np.array([[var,0],[0,var]]),n)
    desc2,label2 = genere_dataset_gaussian(np.array([0,1]),np.array([[var,0],[0,var]]),np.array([1,0]),np.array([[var,0],[0,var]]),n)
    return np.concatenate((desc1,desc2),axis=0),np.asarray([-1]*len(desc1)+[1]*len(desc2))

def crossval(X, Y, n_iterations, iteration, init_seed=42):
    np.random.seed(init_seed)
    index = np.random.permutation(len(X))
    Xtest = np.asarray([ X[index[j]] for j in range(int(len(X)*iteration/n_iterations),int(len(X)*(iteration+1)/n_iterations))])
    Ytest = np.asarray([ Y[index[j]] for j in range(int(len(Y)*iteration/n_iterations),int(len(Y)*(iteration+1)/n_iterations))])
    Xapp = np.asarray([ X[index[j]] for j in range(0,int(len(X)*iteration/n_iterations))]+[ X[index[j]] for j in range(int(len(X)*(iteration+1)/n_iterations),len(X))])
    Yapp = np.asarray([ Y[index[j]] for j in range(0,int(len(Y)*iteration/n_iterations))]+[ Y[index[j]] for j in range(int(len(Y)*(iteration+1)/n_iterations),len(Y))])
    return Xapp, Yapp, Xtest, Ytest

def crossval_strat(X, Y, n_iterations, iteration, init_seed=42):
    X1 = np.asarray([X[i] for i in range(len(X)) if Y[i]==1])
    X0 = np.asarray([X[i] for i in range(len(X)) if Y[i]==-1])
    Xapp1, Yapp1, Xtest1, Ytest1 = crossval(X1,np.asarray([1]*len(X1)),n_iterations, iteration, init_seed)
    Xapp0, Yapp0, Xtest0, Ytest0 = crossval(X0,np.asarray([-1]*len(X0)),n_iterations, iteration, init_seed)
    Xapp, Yapp, Xtest, Ytest = np.concatenate((Xapp0,Xapp1)), np.concatenate((Yapp0,Yapp1)), np.concatenate((Xtest0,Xtest1)), np.concatenate((Ytest0,Ytest1))
    return Xapp, Yapp, Xtest, Ytest