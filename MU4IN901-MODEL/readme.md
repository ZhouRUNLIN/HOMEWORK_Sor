### organisation de archive

1. archive /src: les fichers .c et .h qui definissent les fonctions de nessaire 
    - utils : définition et gestion des objects
        - deux struct : numComplex et polynomial
        - arithmétique élémentaire pour des entiers complex
        - ajouter, supprimer élément dans l'object 
    -  fastFourierTrans : algorithems de fft et fft inverse
        - la précision du algo est en $2^{-42}$
    - multPoly : implementation de mult de poly en methode naïve et en base de fft
2. archive /bin et /obj : Placement des exécutables et des fichiers .o
3. benchmark_results.csv : liste des donnees

### lancement pour le projet
**Requirements** : 
* en python : pandas, pyplot
* en C : time.h, math.h

**Pour lancer le programme** : <br>
``` make test && ./bin/test``` : une petite exemple du projet, qui conclue les tests du fonction

```  make leak ``` : Utilisé pour voir s'il y a une fuite de mémoire dans le test

```  make clean ``` : vider tous les fichers dans /obj et /bin

``` make compare && ./bin/compare numTest``` : produit un ficher benchmark_result.csv, pour stocker le temps consomme par l'algo du deux version de multPoly. **numTsest** est un argument passé qui indique la degree maximum du polynomial

``` python3 traceGraph.py ``` : produit un graphe linéaire pour analyse complexité de l'algo
    
