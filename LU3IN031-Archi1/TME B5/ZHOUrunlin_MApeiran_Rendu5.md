# Rendu 5 - Cache L1 à correspondance directe - performance

**Les réponses en noir sont celles que nous avons obtenues après discussion ou de recherches sur les sites </br>
Les réponse en rouge sont révisées sur les corrigés en moodle**

## Auteurs
- ZHOU runlin 28717281
- MA peiran 28717249

## A. Questions de cours
1. <font color="blue">Que signifie « taux de MISS » ? De quoi dépend-il ? Est-il stable ? Quelle est sa valeur typique ?</font>
    Cours 5: Page 8
    1.  Le Taux de MISS de cache = Nombre de MISS / Nombre total d'accès-mémoire
    2. Le taux de MISS de cache dépend de la taille du cache, de l'usage de la mémoire
    3. n'est pas stable
    4. Pour le cache d'instruction le taux de MISS de cache < 2%, et pour le cache de données le taux de MISS de cache < 5%

2. <font color="blue">Qu'est-ce que le coût d'un MISS ? De quoi dépend-il ? Quelle est sa valeur typique ?</font>
    Cours 4: Page 13
    1. chaque ligne de chache manquante, il y a un MISS
    2. Ce coût dépend de l'architecture, du bus, du nombre de MIPS, de la taille des autres niveaux de cache, de la longueur des lignes, etc.
    3. 10 à 100 cycles

3. <font color="blue">Que signifient CPI et IPC ? Quel est le rapport entre les deux ?</font>
    Cours 4: Page 6
    1. CPI (Cycle Per Instruction) et IPC (Instruction Per Cycle)
    2. CPI * IPC = 1
    
4. <font color="blue">Sur un processeur capable de démarrer une instruction par cycle, pourquoi on n'a pas un CPI de 1 ?</font>
    Cours 5: Page 6
    Car il y a des dépendances entre les instructions obligeant à introduire des bulles dans le pipeline
    
5. <font color="blue">Finalement, quelle est l'équation définissant le CPI réel en fonction du CPI0 (avec un cache parfait, donc un taux de MISS de 0%) et de taux de MISS réel et des coûts de MISS ?</font>
    Cours 5: Page 6, 8
    On a:
    $$CPI = CPI0 + \Delta CPI_{ins} + \Delta CPI_{data}$$
    $$\Delta CPI=Taux\_de\_MISS * Cout\_de\_MISS$$
    Donc, on peut déduire que :
    $$ CPI = CPI0 + Taux\_de\_MISS_{ins} * Cout\_de\_MISS_{ins} + Taux\_de\_MISS_{data} * Cout\_de\_MISS_{ins}$$
    
6. <font color="blue">Qu'est-ce qu'une perte de cohérence pour un cache de niveau 1 ? Est-ce grave ?</font>
    Cours 5: Page 26
    1. Deux copies contient les données différente 
    2. OUI
    
7. <font color="blue">Est-ce qu'il y a un risque de perte de cohérence si on a un seul processeur ? Et s'il y en a plus qu'un ?</font>
    Cours 5: Page 26
    Les caches contiennent des copies de lignes de cache, s'il y a plusieurs cœurs MIPS dans un processeur chacun a ses caches, une même ligne de cache peut alors être copiée dans plusieurs caches.
    Donc : 
    - s'il y a un seul processeur, il n'y a pas des copies des caches, alors pas de risque de confilt entre deux copies (parce que il y a juste une seule copy)
    - s'il y en a plus, oui, il y a un risque de perte de cohérence
    
8. <font color="blue">Un moyen d'éviter le problème de cohérence, c'est le snooping, qu'est-ce que c'est ?</font>
    Cours 5: Page 29
    - Toutes les écritures sont envoyées vers la mémoire. 
    - Les caches L1 peuvent snooper (espionner) le bus et s'ils voient des écritures dans des lignes dont ils ont une copie, il la mette à jour (flèche bleue sur le schéma)
    
9. <font color="blue">Quels autres moyens connaissez-vous pour traiter le problème de cohérence ?</font>
    Cours 5: Page 29
    directory based (basé sur un répertoire des copies) : 
    - C'est le cache L2 qui donne les copies de lignes aux caches L1. Il peut alors se souvenir dans quel cache L1, il a copié chaque ligne.
    - C'est lui qui est responsable des mises à jour (flèche verte)
    - C'est plus long et il y a une perte temporaire de cohérence, mais cela n'oblige pas les caches L1 de faire de l'espionnage car parfois c'est impossible à cause du BUS système.
    
10. <font color="blue">Qu'est-ce qu'un faux partage ?</font>
    Cours 5: Page 30
    C'est un phénomène qui se produit lorsque deux threads ou processus accèdent à des données distinctes mais qui partagent la même ligne de cache. 
    
11. <font color="blue">Est-ce que le faux partage est un problème grave ?</font>
    Cours 5: Page 30
    Oui, car il peut ralentir considérablement l'exécution des programmes
    Les conflits de cohérence de cache causés par le faux partage peuvent entraîner des temps d'attente excessifs pour les threads qui doivent attendre la fin de l'invalidation de la ligne de cache.  
    
## B. Influence des mémoires cache sur les performances
### B.1 Système mémoire parfait
1. <font color="blue">Calculez la valeur CPI0 (correspondant à un système mémoire parfait) en supposant que 50% des instructions de lecture de donnée sont en dépendance avec l'instruction suivante ? (Il faut faire une somme des durées pondérées par leur taux d'occurence)</font>
    ![](https://i.imgur.com/oELwaEu.jpg)

2. <font color="blue">Quel est le pourcentage de gel dans les conditions précédentes avec un système mémoire parfait.</font>
    ![](https://i.imgur.com/JnxclC5.jpg)

### B.2 Estimation du coût du MISS
1. <font color="blue">Calculez la valeur de DCPI_ins, en utilisant le taux de MISS défini dans l'énoncé (4%), et le coût du MISS (30 cycles).</font>
    ![](https://i.imgur.com/NtxniWQ.jpg)

2. <font color="blue">Calculez la valeur de DCPI_data, en utilisant le taux de MISS défini dans l'énoncé (8%) et le coût du MISS de 30 cycles.</font>
    ![](https://i.imgur.com/73W8V4R.jpg)
    
3. <font color="blue">Sachant que 10% des instructions sont des écritures, expliquez pourquoi les écritures n'entraînent pas d'augmentation directe du CPI, bien que toute écriture entraîne un accès au bus système ?</font>
    Étant donné qu'il y a un tampon d'écritures postées disponible, le processeur n'est que rarement bloqué lorsqu'il exécute une instruction d'écriture. Le tampon permet de stocker les données à écrire et celles-ci sont effectivement écrites plus tard par l'automate contrôleur du cache, lorsque le bus est disponible. Ainsi, le temps d'écriture est considéré comme étant d'un seul cycle et le processeur peut continuer à exécuter d'autres instructions sans être ralenti par l'opération d'écriture.
    
4. <font color="blue">Faut-il traiter comme un cas particulier les situations où le processeur émet simultanément (i.e. au même cycle) des requêtes d'instructions et de données qui font à la fois MISS sur le cache d'instructions et MISS sur le cache de données ?</font>
    Comme le bus système ne peut effectuer qu'une seule transaction à la fois, le processeur est bloqué pendant 30 cycles deux fois de suite lorsque la même instruction provoque une absence de cache (MISS) à la fois dans le cache d'instructions et dans le cache de données. Ainsi, le processeur doit attendre que les deux transactions soient terminées avant de pouvoir continuer à exécuter d'autres instructions.
    
5. <font color="blue">Quelle est finalement la valeur du nombre moyen de cycles par instruction ?</font>
    ![](https://i.imgur.com/DVrUlTR.jpg)

6. <font color="blue">Calculez la valeur du CPI lorsqu'on désactive les caches L1. Que se passe-t-il si on désactive aussi le cache L2 et que la durée d'accès est disons 400 cycles ?</font>

### C. Travaux pratiques
### C.1 Caches de faible capacité
1. <font color="blue">Quel est le CPI (nombre moyen de Cycles Par Instruction) pour ces tailles de cache ?</font>
    5.763113 (dans le ficher stats.txt)
    
2. <font color="blue">Quel est le temps d'exécution de l'application (en nombre de cycles) ?</font>
    ![](https://i.imgur.com/e1ACD3o.png)
    1836850 Cycles
    
3. <font color="blue">Que constatez vous concernant le temps d'exécution et le CPI ?</font>
    - le temps d'exécution reste le même
    - le CPI devient la moitié de ce qu'il était
    
    caches à 16 octets :
    ![](https://i.imgur.com/3oIBVi1.png)
    caches à 32 octets :
    ![](https://i.imgur.com/tubH8QD.png)

### C.2 Caches de capacité "normale"
1. <font color="blue">Faites varier la taille du cache d'instructions de 4 à 1024 cases par puissance de 2, et notez à chaque fois le CPI et le temps de calcul obtenus. </font>
    |  NDCACHESET   | CPI  |
    |  ----  | ----  |
    | 4  | 0.184424 |
    | 8  | 0.124582 |
    | 16 | 0.093420 |
    | 32 | 0.040539 |
    | 64 | 0.035142 |
    | 128 | 0.035141 |
    | 256  | 0.009446 |
    | 512  | 0.009442 |
    | 1024  | 0.009440 |
    

2. <font color="blue">Faites varier la taille du cache de données de 1 à 1024 cases par puissance de 2, et notez à chaque fois le CPI et le temps de calcul obtenus. </font>
    |  NICACHESET   | CPI  |
    |  ----  | ----  |
    | 4  | 0.234639 |
    | 8  | 0.153791 |
    | 16 | 0.117535 |
    | 32 | 0.101793 |
    | 64 | 0.045926 |
    | 128 | 0.009987 |
    | 256  | 0.006688 |
    | 512  | 0.006150 |
    | 1024  | 0.006090 |

3. <font color="blue">Quelles tailles de caches pensez-vous qu'il faille choisir pour obtenir les taux de MISS correspondant aux hypothèses faites plus haut (4% pour le cache instruction et 8% pour le cache de données) ?</font>
    Selon les deux tableaux, on doit choisir : 
    NDCACHESET = 16
    NICACHESET = 64 
    
### C.3 Caches désactivés
1. <font color="blue">Quelle est la durée d'exécution du programme ?</font>
    30s
    
2. <font color="blue">Quelle est la valeur du CPI ?</font>
    8.93534
    ![](https://i.imgur.com/QYiqMRg.jpg)

### C.4 Représentation graphique
1. Utilisez l'outil gnuplot pour visualiser la courbe 
    un fichier texte (nommée par donnee.txt) pour reprenant des résultats de l'exercice C.2
    ```
    4 0.234639 0.184424
    8 0.153791 0.124582
    16 0.117535 0.093420
    32 0.101793 0.040539
    64 0.045926 0.035142
    128 0.009987 0.035141
    256 0.006688 0.009446
    512 0.006150 0.009442
    1024 0.006090 0.009440
    ```
    Une série de commande pour créer les courbe (nommée par commande.txt)
    ```
    plot "donnees.txt" using 1:2 title 'CPI et NDCACHESET' with lines
    replot "donnees.txt" using 1:3 title 'CPI et NICACHESET' with lines
    set term postscript portrait
    set size 0.7, 0.7
    ```
    
    pour voir le résultat, on exécute la commande : gnuplot -p < commande.txt
    ![](https://i.imgur.com/TYLlCny.png)
