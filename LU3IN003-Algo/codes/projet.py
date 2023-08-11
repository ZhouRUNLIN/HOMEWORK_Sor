import time
import numpy as np
import matplotlib.pyplot as plt

#pour tacheA ---------------------------------------------------------
def read_file(fileName:str):
	"""
	Lire les dossiers de ADN sous la forme:
	lenX
	lenY
	x1 x2 x3...
	y1 y2 y3...
	(lenX <= lenY)
	retourne: (lenX:int,lenY:int,x:str,y:str)
	"""
	f=open(fileName)
	lenX=int(f.readline())
	lenY=int(f.readline())
	x=""
	y=""
	x0=f.readline()
	for c in x0:
		if c in {'A','T','C','G'}:
			x+=c
	y0=f.readline()
	for c in y0:
		if c in {'A','T','C','G'}:
			y+=c
	return (lenX,lenY,x,y)

# exercice 6
def dist_naif(x:str,y:str):
	"""
	Retourne d(X,Y) en utilisant le pseudo code donnee en exercice 6.
	Nous initialisons dist avec 2147483647 au lieu de l'infini positif.
	"""
	return dist_naif_rec(x,y,0,0,0,2147483647)

def dist_naif_rec(x:str,y:str,i:int,j:int,c:int,dist:int):
	"""
	Entrée : x et y deux mots,
	i un indice dans [0..|x|], j un indice dans [0..|y|],
	c le coût de l'alignement de (x[1..i],y[1..j])
	dist le coût du meilleur alignement de (x, y) connu avant cet appel
	Sortie : dist le coût du meilleur alignement de (x, y) connu après cet appel
	"""
	n=len(x)
	m=len(y)
	if n==i and m==j:
		if c<dist:
			dist=c
	else:
		if i<n and j<m:
			if x[i]==y[j]:
				cSub=0
			elif (x[i]=='C' and y[j]=='G') or (x[i]=='G' and y[j]=='C') or (x[i]=='A' and y[j]=='T') or (x[i]=='T' and y[j]=='A'):
				cSub=3
			else:
				cSub=4
			dist=dist_naif_rec(x,y,i+1,j+1,c+cSub,dist)
		if i<n:
			dist=dist_naif_rec(x,y,i+1,j,c+2,dist)
		if j<m:
			dist=dist_naif_rec(x,y,i,j+1,c+2,dist)
	return dist

#pour tacheB ---------------------------------------------------------
def min_3(a:int,b:int,c:int):
    """
    Trouver le plus petit chiffre entre a,b et c.
    """
    if a>b:
        a=b
    if a>c:
        a=c
    return a

def cost_sub(x:str,y:str,i:int,j:int):
    """
    Calculer le c_sub de x[i] et y[j]
    """
    if x[i-1]==y[j-1]:
        return 0
    elif (x[i-1]=='C' and y[j-1]=='G') or (x[i-1]=='G' and y[j-1]=='C') or (x[i-1]=='A' and y[j-1]=='T') or (x[i-1]=='T' and y[j-1]=='A'):
        return 3
    else:
        return 4

def dist_1(x:str,y:str):
    """
    Calculer la distance de x et y
    """
    n=len(x)
    m=len(y)
    return dist_1_rec(x,y,n,m)

def dist_1_rec(x:str,y:str,i:int,j:int):
    """
    Corps de partie recursion de dist_1
    """
    if i==0 and j==0:
        return 0
    if i==0:
        return j*2
    if j==0:
        return i*2
    a=dist_1_rec(x,y,i-1,j)+2
    b=dist_1_rec(x,y,i,j-1)+2
    c=dist_1_rec(x,y,i-1,j-1)+cost_sub(x,y,i,j)
    return min_3(a,b,c)

def create_T(x:str,y:str):
    """
    La creation de la table T indexé par [0..|x|] * [0..|m|] contenant les valeurs de D
    """
    T=[[0]*(len(y)+1) for i in range(len(x)+1)]
    for i in range(len(x)+1):
        for j in range(len(y)+1):
            T[i][j]=dist_1(x[0:i],y[0:j])
    return T

def sol_1(T:list,x:str,y:str):
    """
    Trouver l'alignement avec le plus petite distance
    """
    n=len(x)
    m=len(y)
    return sol_1_rec(T,x,y,n,m)

def sol_1_rec(T:list,x:str,y:str,i:int,j:int):
    if i==0 and j==0:
        return ("","")
    if i==0:
        return ("-"*j,y[0:j])
    if j==0:
        return (x[0:i],"-"*i)
    a=T[i-1][j-1]+cost_sub(x,y,i,j)
    b=T[i-1][j]+2
    c=T[i][j-1]+2
    if a==T[i][j]:
        al_x,al_y=sol_1_rec(T,x,y,i-1,j-1)
        return (al_x+x[i-1],al_y+y[j-1])
    if b==T[i][j]:
        al_x,al_y=sol_1_rec(T,x,y,i-1,j)
        return (al_x+x[i-1],al_y+"-")
    al_x,al_y=sol_1_rec(T,x,y,i,j-1)
    return (al_x+"-",al_y+y[j-1])

#pour tacheC ---------------------------------------------------------
def dist_2(x:str,y:str):
    """
    Calculer la distance en dessinant la table de la distance.
    """
    n=len(x)
    m=len(y)
    T=[[0]*(m+1) for i in range(2)] #cree un tableau 2*m vide
    for j in range(m+1):
        T[0][j]=j*2

    for i in range(n+1):
        T[1][0]=i*2
        for j in range(m+1):
            T[1][j]=min_3(T[1][j-1]+2, T[0][j]+2, T[0][j-1]+cost_sub(x,y,i,j))

        for j in range(m+1):
           T[0][j]=T[1][j]
    return T[0][m]-2

#pour tacheD ---------------------------------------------------------
def word_gaps(k:int):
    """
    Retourne "-"*k
    """
    return "-"*k

def draw_T_2(x:str,y:str):
    """
    Dessiner la graphe comme dist_2, mais avec les directions
    retourne : list(list(couple(int,str)))
    N,L,U,LU:none, left, up, left up
    """
    n=len(x)
    m=len(y)
    T=[[[0,"N"]]*(len(y)+1) for i in range(len(x)+1)]
    for i in range(n+1):
        for j in range(m+1):
            if i==0 and j==0:
                T[i][j]=[0,"N"]
                continue
            if i==0:
                T[i][j]=[j*2,"L"]
                continue
            if j==0:
                T[i][j]=[i*2,"U"]
                continue
            dist=min_3(T[i-1][j][0]+2,T[i][j-1][0]+2,T[i-1][j-1][0]+cost_sub(x,y,i,j))
            if dist==T[i][j-1][0]+2:
                dir="L"
            elif dist==T[i-1][j-1][0]+cost_sub(x,y,i,j):
                dir="LU"
            else:
                dir="U"
            T[i][j]=[dist,dir]
    return T

def coupure(x:str,y:str,i:int):
    """
    Trouver l'indice de coupure associée à i
    """
    T=draw_T_2(x,y)
    lc=[k for k in range(len(y)+1)]
    lt=[0 for k in range(len(y)+1)]
    for pi in range(i+1,len(x)+1):
        for j in range(len(y)+1):
            if T[pi][j][1]=="L":
                lt[j]=lt[j-1]
            elif T[pi][j][1]=="U":
                lt[j]=lc[j]
            else:
                lt[j]=lc[j-1]
        lc=lt.copy()
    return lt[len(y)]

def align_letter_word(x:str,y:str):
    """
    Q22
    Etant donné y un mot de longueur 1 et x un mot non vide de longueur quelconque, renvoie un meilleur alignement de (x, y).
    """
    dmin=4
    p=0
    for i in range(len(x)):
        if x[i]==y[0]:
            dmin=0
            p=i
        elif (x[i]=='C' and y[0]=='G') or (x[i]=='G' and y[0]=='C') or (x[i]=='A' and y[0]=='T') or (x[i]=='T' and y[0]=='A'):
            if dmin>3:
                dmin=3
                p=i
    return "-"*p+y[0]+"-"*(len(x)-p-1)


def sol_2(x:str,y:str):
    """
    Trouver l'alignement avec le plus petite distance en utilisant le psuedo code de Q24
    Dans le cas où x est plus court que y, on fait l'inverse.
    """
    bool=0
    if len(x)<=len(y): #changer le contenu de x et y 
        bool=1
        str=x
        x=y
        y=str
    
    if len(x)==1 and len(y)==1:
        return (x,y)
    if len(y)==1:
        return (x,align_letter_word(x,y))
    if len(y)==0:
        return (x,word_gaps(len(x)))
    i=len(x)//2
    j=coupure(x,y,i)
    x1,y1=sol_2(x[0:i],y[0:j])
    x2,y2=sol_2(x[i:],y[j:])
    
    if bool==1:
        return(y1+y2, x1+x2)
    return (x1+x2,y1+y2)