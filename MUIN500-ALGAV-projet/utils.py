# Auteur : ZHOU runlin
# Num Etu : 28717281

import os
import numpy as np
import matplotlib.pyplot as plt
import timeit
import time
import math
import random

def charToInt(data, size):
    """
        Arguments : data est la chaine de caractere en base 16, size est la taille de liste que on retourne
        la fonction retourne une liste de type int, avec la taille size
        et la lsite est compose par les entiers pour representer le cle en 128 bits 
    """
    data = data[2:]
    res = []
    currIndex = 0
    sliceLen = int(len(data) / size)
    for i in range(0, size):
        sum = 0
        for char in data[currIndex:currIndex+sliceLen]:
            if char != '\n':
                sum = int(char, 16) + 16*sum
        res.append(sum)
        currIndex += sliceLen
    return res

def intToChar(data):
    """
        Arguments : data est une list de type int
        la fonction renvoie la chaine de caractere en base 16
    """
    hexStr = "0x"
    for num in data:
        decNum = num
        while decNum != 0:
            decNum = int(decNum/16)
        hexStr += hex(num)[2:]
    return hexStr

def list_files(startpath):
    """
        la fonction renvoie les noms de tous les fichiers de ce dossier
    """
    res = []
    for root, dirs, files in os.walk(startpath):
        for file in files:
            res.append(os.path.join(root, file))
    return res

def produceGraphe(matrix, x_axis, title="title of matrix", xlabel="x", ylabel="y"):
    rows, _ = matrix.shape

    # Tracer des graphiques linéaires
    for i in range(rows):
        plt.plot(x_axis, matrix[i, :], label=f'Row {i + 1}')

    # Ajout d'étiquettes et de légendes
    plt.title(title)
    plt.xlabel(xlabel)
    plt.ylabel(ylabel)
    plt.legend()

    # affiche le graphe
    plt.show()

def log2Up(num):
    if math.pow(2, int(math.log2(num))) == num:
        return int(math.log2(num))
    return int(math.log2(num)) + 1