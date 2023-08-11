# -*- coding: utf-8 -*-

"""
Package: iads
File: Clustering.py
Année: LU3IN026 - semestre 2 - 2022-2023, Sorbonne Université
"""

# ---------------------------
# Fonctions de Clustering

# import externe
import numpy as np
import pandas as pd
import math
import scipy.cluster.hierarchy
import matplotlib.pyplot as plt
import matplotlib.cm as cm


# ------------------------ 
def normalisation(df):
    df = (df - df.min()) / (df.max() - df.min())
    return df

def dist_euclidienne(v1,v2):
    d = 0
    for i in range(len(v1)):
        d += pow(v1[i]-v2[i],2)
    return math.sqrt(d)

def centroide(d):
    return np.mean(d, axis=0)

def dist_centroides(d1,d2):
    return dist_euclidienne(centroide(d1),centroide(d2))

def dist_complete(d1,d2):
    dmax = 0
    for _,row1 in d1.iterrows():
        for _,row2 in d2.iterrows():
            if dist_euclidienne(tuple(row1),tuple(row2)) > dmax:
                dmax = dist_euclidienne(tuple(row1),tuple(row2))
    return dmax

def dist_simple(d1,d2):
    dmin = 255
    for _,row1 in d1.iterrows():
        for _,row2 in d2.iterrows():
            if dist_euclidienne(tuple(row1),tuple(row2)) < dmin:
                dmin = dist_euclidienne(tuple(row1),tuple(row2))
    return dmin

def dist_average(d1,d2):
    dsum = 0
    for _,row1 in d1.iterrows():
        for _,row2 in d2.iterrows():
            dsum += dist_euclidienne(tuple(row1),tuple(row2))
    return dsum/(d1.shape[0]*d2.shape[0])

def initialise_CHA(DF):
    d = dict()
    for i in DF.index:
        d[i]=[i]
    return d

def fusionne(data, part, verbose=False):
    mind = 255
    minkey = (None,None)
    for key1 in part:
        for key2 in part:
            if key1 >= key2:
                continue
            if dist_centroides(data.loc[part[key1]],data.loc[part[key2]]) < mind:
                mind = dist_centroides(data.loc[part[key1]],data.loc[part[key2]])
                minkey = (key1,key2)
    newkey = max(part.keys())+1
    part2 = part.copy()
    part2[newkey] = part2.pop(minkey[0])+part2.pop(minkey[1])
    if verbose:
        print("Distance mininimale trouvée entre",part2[newkey]," = ",mind)
    return part2,minkey[0],minkey[1],mind

def fusionne_d(data, part, func_dist, verbose=False):
    mind = 255
    minkey = (None,None)
    for key1 in part:
        for key2 in part:
            if key1 >= key2:
                continue
            if func_dist(data.loc[part[key1]],data.loc[part[key2]]) < mind:
                mind = func_dist(data.loc[part[key1]],data.loc[part[key2]])
                minkey = (key1,key2)
    newkey = max(part.keys())+1
    part2 = part.copy()
    part2[newkey] = part2.pop(minkey[0])+part2.pop(minkey[1])
    if verbose:
        print("Distance mininimale trouvée entre",part2[newkey]," = ",mind)
    return part2,minkey[0],minkey[1],mind
                
def CHA_centroid(data,verbose=False,dendrogramme=False):
    part = initialise_CHA(data)
    l = []
    while len(part) > 1:
        part,k1,k2,d = fusionne(data,part,verbose)
        l.append([k1,k2,d,len(part[max(part.keys())])])
    if dendrogramme:
        plt.figure(figsize=(30, 15))
        plt.title('Dendrogramme', fontsize=25)    
        plt.xlabel("Indice d'exemple", fontsize=25)
        plt.ylabel('Distance', fontsize=25)
        scipy.cluster.hierarchy.dendrogram(l, leaf_font_size=24., )
        plt.show()
    return l

def CHA_complete(data,verbose=False,dendrogramme=False):
    part = initialise_CHA(data)
    l = []
    while len(part) > 1:
        part,k1,k2,d = fusionne_d(data,part,dist_complete,verbose)
        l.append([k1,k2,d,len(part[max(part.keys())])])
    if dendrogramme:
        plt.figure(figsize=(30, 15))
        plt.title('Dendrogramme', fontsize=25)    
        plt.xlabel("Indice d'exemple", fontsize=25)
        plt.ylabel('Distance', fontsize=25)
        scipy.cluster.hierarchy.dendrogram(l, leaf_font_size=24., )
        plt.show()
    return l 

def CHA_simple(data,verbose=False,dendrogramme=False):
    part = initialise_CHA(data)
    l = []
    while len(part) > 1:
        part,k1,k2,d = fusionne_d(data,part,dist_simple,verbose)
        l.append([k1,k2,d,len(part[max(part.keys())])])
    if dendrogramme:
        plt.figure(figsize=(30, 15))
        plt.title('Dendrogramme', fontsize=25)    
        plt.xlabel("Indice d'exemple", fontsize=25)
        plt.ylabel('Distance', fontsize=25)
        scipy.cluster.hierarchy.dendrogram(l, leaf_font_size=24., )
        plt.show()
    return l 

def CHA_average(data,verbose=False,dendrogramme=False):
    part = initialise_CHA(data)
    l = []
    while len(part) > 1:
        part,k1,k2,d = fusionne_d(data,part,dist_average,verbose)
        l.append([k1,k2,d,len(part[max(part.keys())])])
    if dendrogramme:
        plt.figure(figsize=(30, 15))
        plt.title('Dendrogramme', fontsize=25)    
        plt.xlabel("Indice d'exemple", fontsize=25)
        plt.ylabel('Distance', fontsize=25)
        scipy.cluster.hierarchy.dendrogram(l, leaf_font_size=24., )
        plt.show()
    return l 

def CHA(DF,linkage='centroid', verbose=False,dendrogramme=False):
    """
    Cette fonction effectue une classification hiérarchique ascendante sur un DataFrame en utilisant l'un des quatre
    liens de cluster : centroid, complete, simple ou average.
    
    Args:
        DF (pandas.DataFrame): Le DataFrame contenant les données à clusteriser.
        linkage (str): Le type de lien de cluster à utiliser. Les valeurs possibles sont 'centroid', 'complete',
            'simple' et 'average'. Par défaut, la valeur est 'centroid'.
        verbose (bool): Si True, affiche les détails du processus de clustering. Par défaut, la valeur est False.
        dendrogramme (bool): Si True, affiche un dendrogramme de la hiérarchie de clustering. Par défaut, la valeur est False.
    
    Returns:
        pandas.DataFrame: Un DataFrame contenant les résultats du clustering hiérarchique.
    """
    if linkage == 'centroid':
        return CHA_centroid(DF,verbose,dendrogramme)
    if linkage == 'complete':
        return CHA_complete(DF,verbose,dendrogramme)
    if linkage == 'simple':
        return CHA_simple(DF,verbose,dendrogramme)
    if linkage == 'average':
        return CHA_average(DF,verbose,dendrogramme)
    
def inertie_cluster(Ens):
    c = centroide(Ens)
    inertie = 0
    for _,row in Ens.iterrows():
        distance = dist_euclidienne(row,c)
        inertie += distance ** 2
    return inertie

def init_kmeans(K,Ens):
    ind = np.random.choice(Ens.shape[0], size=K, replace=False)
    c = Ens.iloc[ind]
    return c.values

def plus_proche(Exe,Centres):
    l = [dist_euclidienne(Exe,c) for c in Centres]
    return l.index(min(l))

def affecte_cluster(Base,Centres):
    dic = dict()
    for i in range(Centres.shape[0]):
        dic[i] = []
    for i in range(Base.shape[0]):
        pproche = plus_proche(Base.iloc[i],Centres)
        dic[pproche].append(i)
    return dic

def nouveaux_centroides(Base,U):
    cs = []
    for key in U:
        newBase = Base.iloc[U[key]]
        newC = centroide(newBase)
        cs.append(newC)
    return np.asarray(cs)

def inertie_globale(Base, U):
    in_total = 0
    for key in U:
        newBase = Base.iloc[U[key]]
        in_total += inertie_cluster(newBase)
    return in_total

def kmoyennes(K, Base, epsilon, iter_max,verbose = False):
    cs = init_kmeans(K,Base)
    U = affecte_cluster(Base,cs)
    ine0 = inertie_globale(Base,U)
    i = 0
    while iter_max > i:
        cs = nouveaux_centroides(Base,U)
        U = affecte_cluster(Base,cs)
        ine1 = inertie_globale(Base,U)
        #if verbose:
            #print("iteration ",i+1," Inertie : ",ine1," Difference: ",abs(ine1-ine0))
        if abs(ine1-ine0)<epsilon:
            break
        ine0 = ine1
        i += 1
    return cs,U

def affiche_resultat(Base,Centres,Affect):
    couleurs = cm.tab20(np.linspace(0, 1, 20))
    i = 0
    for key in Affect:
        newBase = Base.iloc[Affect[key]]
        for _,row in newBase.iterrows():
            row = tuple(row)
            x, y = row
            plt.scatter(x, y, color=couleurs[i])
        i += 1
    for x,y in Centres:
        plt.plot(x,y,'rx')
    plt.show()
    
def dunn_index(centroids, dataframe, groups):
    #Calculer la matrice de distance entre tous les deux points
    dist_matrix = np.zeros((len(dataframe), len(dataframe)))
    for i in range(len(dataframe)):
        for j in range(i + 1, len(dataframe)):
            distance = np.sqrt(np.sum((dataframe.iloc[i] - dataframe.iloc[j])**2))
            dist_matrix[i][j] = distance
            dist_matrix[j][i] = distance

    # Calculer la distance la plus courte entre tous les points internes des grappes,
    diameters = []
    for group in groups.values():
        if len(group) > 1:
            group_matrix = dist_matrix[group][:, group]
            diameters.append(np.max(group_matrix))
        else:
            diameters.append(0)
    max_diameter = max(diameters)

    # Calculer la distance entre chaque paire de grappes différentes
    inter_distances = []
    for i, group_i in enumerate(groups.values()):
        for j, group_j in enumerate(groups.values()):
            if i < j:
                group_i_matrix = dist_matrix[group_i][:, group_j]
                inter_distances.append(np.min(group_i_matrix))
    min_distance = min(inter_distances)

    # calculer Dunn's index
    dunn = min_distance / max_diameter

    return dunn

def beni_index(centroids, dataframe, groups):
    # 计算每个聚类的离散度
    dispersions = []
    for group in groups.values():
        group_center = np.mean(dataframe.iloc[group], axis=0)
        group_dispersion = np.mean([np.sqrt(np.sum((dataframe.iloc[i] - group_center)**2)) for i in group])
        dispersions.append(group_dispersion)

    # 计算每个聚类之间的间距
    spacings = []
    for i, center_i in enumerate(centroids):
        for j, center_j in enumerate(centroids):
            if i < j:
                spacing = np.sqrt(np.sum((center_i - center_j)**2))
                spacings.append(spacing)
    avg_spacing = np.mean(spacings)

    # 计算Index de Vie-Beni
    vb_index = sum(dispersions) / avg_spacing

    return vb_index