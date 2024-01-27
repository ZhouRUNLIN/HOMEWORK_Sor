# Rendu 4 - Principe d'un cache de premier niveau

**Les réponses en noir sont celles que nous avons obtenues après discussion ou de recherches sur les sites </br>
Les réponse en rouge sont révisées sur les corrigés en moodle**

## Travaux Dirigés
### A - Questions de cours
1. <font color="blue">Que signifie bus système ?</font>
    Cours 4: Page 3
    Le bus système est un ensemble de connexions physiques, par exemple : CPU, RAM, etc.
    Les accès des MIPS vers la mémoire passent par un BUS système qui les relie à la mémoire.
    
2. <font color="blue">À quoi sert l'arbitre du bus système ?</font>
    Cours 4: Page 3
    - Pour éviter l'absence de famine.
    - Si il y a plusieurs demande du MIPS, l'arbitre peut choisir l'ordre d'exècution (comme un cycle) 
    
3. <font color="blue"> Qu'est un contrôleur mémoire ?</font>
    Cours 4: Page 4
    le rôle principal du contrôleur est de gérer l'accès aux barrettes de RAM
    
4. <font color="blue"> La "localité spatiale" et la "localité temporelle" sont deux propriétés des programmes, que représentent-elles ?</font>
    Cours 4: Page 8
    - localité temporelle : Lorsqu'une instruction ou une donnée est lue, la probabilité qu'elle le soit à nouveau est très élevée.
    - localité spatiale : Lorsqu'une instruction ou une donnée est lue, la probabilité que ces voisines le soit aussi est très élevée.

5. <font color="blue"> Où sont placés les caches ?</font>
    Cours 4: Page 10 (sur le schéma)
    Caches L1 : entre le bus et le MIPS (les processeurs)
    Caches L2 : entre le bus et les mémoires <font color="red"> (le contrôleur de mémoire externe)</font>
    
6. <font color="blue"> Peut-on avoir plusieurs niveaux de cache ?</font>
    oui, sur le schéma dans la page 10, il y a deux nieaux de cache.
    <font color="red">
    - Oui, on peut en avoir 1, 2, 3 voire 4.
    - En fait, à chaque fois qu'une donnée se trouve sur un support lent, on peut ajouter un cache qui va accélérer les accès, à la condition que les propriétés de localité spatiale et temporelle soient présentent. Sinon on ne gagne rien. </font>

7. <font color="blue">Que signifient "caches séparés" et "cache unifié" ?</font>
    Cours 4: Page 11
    - cache unifié : c.-à-d. que données et instructions sont mélangées
    - cache séparés : Le contraire de cache unifié
    
8. <font color="blue">Quelle est la répartition des types d'instructions dans un programme ?
 </font>
     Cours 4: Page 12
    - 40% : Arithmétiques et logiques (add, or, etc.)
    - 30% : Branchements (bne, jal, j, etc.)
    - 20% : Loads (lw, lh, lb)
    - 10% : Stores (sw, sh, sb)
 
9. <font color="blue">Pourquoi les lectures sont bloquantes et les écritures sont non bloquantes ?
 </font>
    Cours 4: Page 12
    - Les lectures sont toujours bloquantes
        - Si la donnée ou l'instruction n'est pas disponible il y a un gel du cœur de processeur
    - Les écritures ne sont pas bloquantes
        - Le cœur envoie une donnée vers la mémoire et il continue l'exécution du programme.

10. <font color="blue">Qu'est un taux de MISS et quels sont les taux normaux ? </font>
    Cours 4: Page 13
    - Lorsque le cœur demande une instruction ou une donnée:
        - si le cache correspondant possède l'instruction ou la donnée demandée, alors c'est un succès nommé HIT de cache,
        - sinon c'est un échec nommé MISS de cache.
    - les taux normaux :
        - Pour le cache d'instruction le taux de MISS de cache < 2% (2 MISS et 98 HIT)
        - Pour le cache de données le taux de MISS de cache < 5% (5 MISS et 95 HIT) 
    
11. <font color="blue">Qu'est-ce qu'une "ligne de cache" ? </font>
    Cours 4 : Page 15
    Une ligne de cache est l'unité d'échange en lecture entre la mémoire et un cache, c'est à dire que lorsqu'un cache lit la mémoire, alors il lit une ligne de cache entière
    
    Une ligne de cache est un segment de 2n octets, n est compris en 3 et 7
12. <font color="blue">Qu'est-ce qu'une "case de cache" ?
 </font>
    Cours 4 : Page 17
    une case de cache est un contenant pour stocker une ligne de cache
    
13. <font color="blue">Que veut dire cache à correspondance directe ?
 </font>
    Cours 4: Page 19
    Le cache à correspondance directe, est un type de mémoire cache 
    Dans un cache à correspondance directe, chaque bloc de données de la mémoire principale (RAM) est mappé à un emplacement spécifique dans le cache. Le mappage est réalisé en utilisant une fonction de hachage simple basée sur l'adresse mémoire du bloc de données. Cette fonction attribue à chaque bloc de données un emplacement unique dans le cache.
    
14. <font color="blue">Que sont un numéro de ligne, un tag, un index et un offset de cache ? </font>
    Cours 4: Page 20
    - L'adresse octet dans une ligne s'appelle offset
    - Le numéro de la case utilisé par la ligne s'appelle index
    - Le complément de l'index dans le numéro de ligne s'appelle l'étiquette (tag)
    
15. <font color="blue">Qu'est-ce que le tampon d'écriture ? </font>
    <font color="red">C'est une petite mémoire qui contient les requêtes d'écriture faites par le processeur à destination de la mémoire, plus généralement de l'espace d'adressage, mais le plus souvent pour écrire dans la mémoire.
</font>
16. <font color="blue">Quel est le principal inconvénient des caches à correspondance directe ?
 </font>
     Cours 4: Page 24
     Dès lors que le numéro de la case est déterminé par le numéro de ligne et qu'il est unique, cela va créer des "collisions" entre les lignes qui sont en compétition pour une case.
     
### B.1. Remplissage des cases de cache
<font color="blue">Soit un processeur MIPS32 associé à un cache d'instruction set à un cache de données séparés. Les deux caches ont une capacité de stockage de 32 octets et sont à correspondance directe. La largeur d'une ligne de cache est de 8 octets (soit 2 mots).</font>

1. <font color="blue">Dites comment le contrôleur du cache interprète une adresse :
    - quel est le nombre de bits de l'index ?
    - quel est le nombre de bits du déplacement (offset) ? 
    - quel est le nombre de bits de l'étiquette (tag) ?</font>
    Cours 4: Page 20
    ![](https://i.imgur.com/SovGx6I.jpg)
    
<br>

2. <font color="blue">Remplir les formules : </font>
    Cours 4: Page 15, 16, 21, 24<br>
    ![](https://i.imgur.com/CyUwubj.jpg)
    <br>![](https://i.imgur.com/SpiBo61.jpg)

### B.2. Cas de collision de lignes
1. <font color="blue">Donnez les éléments des tableaux X et Y qui peuvent occuper le mot n°0 de la cas en °3 du cache de données.</font>
    ![](https://i.imgur.com/RQYNUBG.jpg)

2. <font color="blue">Calculez le taux d'échecs dans le cache de données pour la boucle suivante (on suppose que la variables calaire S estcontenue dans un registre - donc jamais d'échec de cache lors de sa lecture) :</font>
![](https://i.imgur.com/PzuhbXQ.jpg)

3. <font color="blue">Si l'on suppose que le tableau Y est maintenant rangé à l'adresse 0x00014020, calculez le nouveau taux d'échecs.
</font>

# Travaux pratiques
### C.1 Calcul du taux de MISS dans le cache d'instructions
1. <font color="blue">Ouvrez le fichier src/kinit.c et expliquez ce que fait la fonction kinit() dans le cadre de ce TP ? </font>
    Dans le ficher kinit.c
    - ligne 13-20 : il déclaire un tableau vide (type :int) et sa taille est 15. 
    - ligne 24-41 : chaque case du tableau se augemente pour 1000 fois. Et la valeur est égale à leurs index.
    - ligne 45-46 : affichage du tableau avec le format : tab[i] = value 
    
2. <font color="blue">Lancez l'exécution du Makefile (make compil), puis examinez le code assembleur correspondant à l'application logicielle (kernel.x.s).</font>
    insert photo
    C'est une capture de ficher kernel.x.s
    La partie blache est l'initialisation de programme, et la partie bleu est les boucles
    - <font color="blue">Déterminez les adresses de début et de fin de la boucle de calcul (seconde boucle for).</font>
    on peut voir que la boucle est commence avec "lw v0, 24(sp)"
    donc, pour la seconde boucle for, l'adresse de début est 0x8000 0274, et l'adresse de fin est 0x8000 027c 
    
    - <font color="blue">Combien d'instructions sont exécutées à chaque itération de cette boucle ?</font>
    51 instructions 
    
    - <font color="blue">Toutes les instructions de la boucle de calcul peuvent-elles être simultanément stockées dans le cache ?</font>
    non, parce que le cache a 8 cases, et chaque case peut stocker 4 instructions au maximum. En total, c'est 32 instrcutions.
    
    - <font color="blue">Que pouvez-vous en conclure ?</font>
    chaque boucle de for a 51 instrction qui est supérieur au nombre max d'un chache peut stocker donc il exsite des MISS (dans chaque for).
    
3. <font color="blue"> Déterminez, pour chaque instruction de la boucle de calcul, dans quelle case du cache sera rangée la ligne de cache à laquelle cette instruction appartient.</font>
    Par exemple:
    ```
    80000210 <kinit>:
    # lignes de cache: 0
    80000210:	27bdffa0 	addiu	sp,sp,-96
    80000214:	afbf005c 	sw	ra,92(sp)
    80000218:	afa00014 	sw	zero,20(sp)
    8000021c:	10000009 	b	80000244 <kinit+0x34>
    
    # lignes de cache: 1
    80000220:	00000000 	nop
    80000224:	8fa20014 	lw	v0,20(sp)
    80000228:	00021080 	sll	v0,v0,0x2
    8000022c:	27a30010 	addiu	v1,sp,16
    
    # lignes de cache: 2
    80000230:	00621021 	addu	v0,v1,v0
    80000234:	ac400008 	sw	zero,8(v0)
    80000238:	8fa20014 	lw	v0,20(sp)
    8000023c:	24420001 	addiu	v0,v0,1
    
    # lignes de cache: 3
    80000240:	afa20014 	sw	v0,20(sp)
    80000244:	8fa20014 	lw	v0,20(sp)
    80000248:	2c42000f 	sltiu	v0,v0,15
    8000024c:	1440fff5 	bnez	v0,80000224 <kinit+0x14>
    
    # lignes de cache: 4
    80000250:	00000000 	nop
    80000254:	afa00010 	sw	zero,16(sp)
    80000258:	10000030 	b	8000031c <kinit+0x10c>
    8000025c:	00000000 	nop
    80000260:	8fa20018 	lw	v0,24(sp)
    
    # lignes de cache: 5
    80000264:	afa20018 	sw	v0,24(sp)
    80000268:	8fa2001c 	lw	v0,28(sp)
    8000026c:	24420001 	addiu	v0,v0,1
    80000270:	afa2001c 	sw	v0,28(sp)
    
    # lignes de cache: 6
    80000274:	8fa20020 	lw	v0,32(sp)
    80000278:	24420002 	addiu	v0,v0,2
    8000027c:	afa20020 	sw	v0,32(sp)
    80000280:	8fa20024 	lw	v0,36(sp)
    
    ... etc
    ```
    Après le numéro de cache arrive à 7, le numéro suivant va mettre à 0 (car il y a seulement 8 cache en total)
    
4. <font color="blue">Évaluez le nombre de MISS instruction lors de l'exécution de la première itération ? Lors de la deuxième itération ?  </font>
    - prelière itération: 
        - charge 13 lignes de cache : 6, 7, 0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, donc il y a 13 MISS
    - <font color="red">Itérations suivantes : Au début de la 2e itération, les instructions contenues dans les cases 6, 7, 0, 1, 2 font MISS, car elles
ont été écrasées. Les instructions contenues dans les cases 3, 4, 5 ne font pas MISS. À la fin de l'itération, les instructions
contenues dans les cases 0, 1, 2 font de nouveau MISS. On a donc 10 MISS pour 51 instructions lors de la 2e itération, et il
en va de même pour les itérations suivantes. Ceci correspond à un taux de MISS de 10/51, légèrement inférieur à 20%. </font>

### C.2 Analyse de trace 
1. <font color="blue">A quelle numéro de cycles s'affiche le message EXIT</font>
    ![](https://i.imgur.com/SSUxrWv.png)
    230892

2. <font color="blue">
    À quel cycle est chargée dans le cache d'instructions la première instruction de la fonction kinit() ? </font>
    <br>
    <br><font color="blue">À quel cycle est chargée la première ligne de cache contenant des instructions du corps de la boucle de calcul ? (on peut la repérer parce la boucle de calcul fait beaucoup de fois la même chose)
    - À quel cycle cette première ligne est-elle évincée par le chargement d'une autre ligne de cache ?
    - À quel cycle cette première ligne est-elle rechargée pour exécuter la deuxième itération de la boucle ? Et à quelle cycle cette première instruction est ré-exécutée pour la seconde itération ?
    - Quelle est la durée (en nombre de cycles) de la première itération?
    - À quel cycle est-elle ré-exécutée à la troisième itération?
    - Quelle est la durée des itérations suivantes?
</font>

### C.3 Mesure du taux de MISS 
1. <font color="blue">Comment expliquez-vous l'évolution du taux de MISS au cours du temps ?</font>
    Selon la définition du taux de MISS, sa vleur dépend : 
    - de la taille du cache
    - de l'usage de la mémoire
        
    Au début de la boucle, la cache est vide. 
    Pendant l'exécution du programme, la cache est remplie, ce qui affecte la variation de taux de MISS.
    
### C.4 Optimisation du code pour minimiser le taux de MISS
1. <font color="blue">Comment expliquez-vous l'évolution du taux de MISS pour cette nouvelle version de l'application ?</font>
    Lorsque le programme commence à s'exécuter, les comportements des deux versions ne présentent pas de différence notable. Cependant, lorsqu'ils entrent dans la boucle "for", les deux versions commencent à se différencier, et le taux de MISS de la courbe verte est de 0.
