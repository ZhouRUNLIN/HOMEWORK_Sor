# Rendu 1 - Du boot au premier programme user

**Les réponses en noir sont celles que nous avons obtenues après discussion ou de recherches sur les sites </br>
Les réponse en rouge sont révisées sur les corrigés en moodle**

## Travaux Dirigés
### A1 - Analyse de l'architecture
1. <font color='blue'>Il y a deux mémoires dans almo1 : RAM et ROM. Qu'est-ce qui les distinguent et que contiennent-elles ? </font>
   
    **La RAM** est une mémoire volatile (read and write) : qui stocke temporairement les codes sur lesquels vous travaillez. 
    **La ROM** est une mémoire non volatile (read only) : qui stocke des instructions pour votre ordinateur de manière permanente. <font color='red'> et le code de démarrage du prototype </font>
    
2. <font color='blue'>Qu'est-ce l'espace d'adressage du MIPS ? Quelle taille fait-il ?
    Quelles sont les instructions du MIPS permettant d'utiliser ces adresses ? Est-ce synonyme de mémoire ?</font>
   
   1. <font color='red'> L'espace d'adressage du MIPS est l'ensemble des adresses que peut former le MIPS.</font>
   2. Cours B1, page 7 : $2^{32}$ octets
   3. les instructions qui ont besoin d'accès l'espace adresse, par exemple : lw, lh, lb, sw, sh)
    4. non
   
3. <font color='blue'>Qu'est-ce l'espace d'adressage de l'application ?</font>
    Cours B1, Page 7 : L'espace d'adressage, c'est l'ensemble des adresses accessibles par l'application
    
4. <font color='blue'>Dans quel composant matériel se trouve le code de démarrage et à quelle adresse est-il placé dans l'espace d'adressage et pourquoi à cette adresse ?</font>
   
    1. ROM
    2. <font color='red'> Il commence à l'adresse 0xBFC00000 parce que c'est l'adresse qu'envoie le MIPS au démarrage.
    </font>
    
5. <font color='blue'>Quel composant permet de faire des entrées-sorties dans almo1 ?
    Citez d'autres composants qui pourraient être présents dans un autre SoC ?</font>
    1. Cours B1, Page 26 : la sortie est l'écran, et l'entrée est le clavier 
    2. <font color='red'> un contrôleur de disque, un contrôleur vidéo, un port réseau Ethernet, un port USB, des entrées analogiques </font>
    
6. <font color='blue'>Il y a 4 registres dans le contrôleur de TTY, à quelles adresses sont-ils placés dans l'espace d'adressage ?
    Comme ce sont des registres, est-ce que le MIPS peut les utiliser comme opérandes pour ses instructions (comme add, or, etc.) ?
    Dans quel registre faut-il écrire pour envoyer un caractère sur l'écran du terminal (implicitement à la position du curseur) ?
    Que contiennent les registres TTY_STATUS et TTY_READ ?
    Quelle est l'adresse de TTY_WRITE dans l'espace d'adressage ?</font>
    1. 0xD0200000
    2. non, seulement les instructions read/write sont accessibles.
    3. TTY_WRITE
    4. Cours B1, Page 8: 
        * TTY_READ: 1 mot en lecture seule,le caractère ascii tapé est dans l'octet de poids faible
        * TTY_STATUS: 1 mot en lecture seule, $\ne 0$ s'il y a un caractère ascii en attente dans TTY_READ
    5. 0xD02000n0 avec n est le nombre de TTY 

7. <font color='blue'>S'il y a 2 terminaux (TTY0 et TTY1), à quelle adresse est le registre TTY_READ de TTY1 ?</font>
    Pour TTY1, l'adresse est à partir de 0XD0200010.
    Donc, l'adresse de TTY_READ de TTY1 est 0xD0200018.
    
8. <font color='blue'>Que représentent les flèches bleues sur le schéma ? Pourquoi ne vont-elles que dans une seule direction ?</font>

    1. la demande pour entrer au mémoire
    2. <font color='red'> pour présenter simplement sur un shéma </font>

9. <font color='blue'>Que fait le contrôleur DMA et donner des différences par rapport au contrôleur de TTY ?</font>
    Cours B1, Page 9
    1. Direct Memory Access <font color='red'> Le contrôleur DMA fait des déplacement de mémoire comme memcpy(),mais il le fait plus vite que la fonction memcpy()</font>
    2. <font color='red'>il peut faire des lectures et de écritures dans la mémoire</font>


### A2 - Programmation assembleur
1. <font color='blue'> Réécrivez le code de la question précédente en utilisant la et li</font>

    la $4, __tty_regs_map
    li $5, 'x'
    sb $5, 0($4)

2. <font color='blue'>Est-ce que j label et jr $r permettent-elles d'effectuer un saut à n'importe quelle adresse ?</font>

    Il y a 16 blocs de mémoire, et le bloc dans lequel nous nous trouvons actuellement, et le bloc où se trouve l'étiquette j, sont généralement le même bloc. 
    Si nous voulons sauter à un autre bloc, alors la valeur IMMEDIATE de 26 bits dans l'instruction j n'est pas suffisante dans ce cas, on a besoin d'une nouvelle instruction est nécessaire : jr rs : saut à un registre. 
    rs est un registre pour stocker des adresses et le programme saute directement à l'adresse représentée par la valeur stockée dans rs. La valeur stockée dans rs peut être.

    * n'est pas un multiple de 4, c'est-à-dire que l'adresse n'est pas alignée.
    * être dans la plage de mémoire des privilèges du système et hors de la plage de privilèges de l'utilisateur

3. <font color='blue'>Écrivez le code assembleur créant la section ".mytext" et suivi de l'addition des registres $5 et $6 dans $4</font>
    .section .mytext, "ax" # au format de .section .name, "flags"
        addu $4, $5, $6

4. <font color='blue'>À quoi sert la directive .globl label ? </font>
   
    global label

5. <font color='blue'> Écrivez une séquence de code qui affiche la chaîne de caractère sur TTY0.</font>

    .data
    hello:  .asciiz "Hello"

    .text
        &ensp;&ensp; la $4, hello
        &ensp;&ensp; la $5, __tty_regs_map
    print:&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp; #une boucle et chaque intération peut afficher une caractère sur l'écran
        &ensp;&ensp; lb $8, 0($4)
             sb $8, 0($5) 

    ​		 addiu $4, $4, 1
    ​    &ensp;&ensp; bne $8, $0, print

6. <font color='blue'> En regardant le dessin de l'espace d'adressage du prototype almo1, dites à quelle adresse devra être initialisé le pointeur de pile pour le kernel. </font>

    1. la première adresse de la zone kdata: 0x80400000
    2. <font color='red'> La première chose que fait une fonction, c'est décrémenter le pointeur depile pour écrire $31,etc.Il faut donc que le pointeur ait été défini : la $29, __kdata_end </font>

### A3 - Programmation en C
1. <font color='blue'>Quels sont les usages du mot clé static en C ? </font>

    1. Si un fichier statique est ajouté, il sera caché des autres fichiers sources. Cette fonctionnalité permet de définir des fonctions et des variables portant le même nom dans différents fichiers sans craindre de conflits de noms.
    2. L'initialisation par défaut est 0.
    3. <font color='red'> Déclarer static une variable locale permet de la rendre persistante, c'est-à-dire qu'elle conserve sa valeur entre deux appels.</font>


2. <font color='blue'>Pourquoi déclarer des fonctions ou des variables extern ? </font>
    1. extern modifie les déclarations de variables, les variables qui peuvent être référencées par d'autres modules avec le modificateur extern sont généralement des variables globales
    2. extern modifie les déclarations de fonctions
    3. le modificateur extern peut être utilisé pour indiquer que le code est compilé par le langage C

3. <font color='blue'>Comment déclarer un tableau de structures en variable globale ? </font>

    struct test_s { <br>&ensp;&ensp;int a;
    &ensp;&ensp;int b; <br>};
struct test_s tab[2];



4. <font color='blue'>Quelle est la différence entre #include "file.h" et #include <file.h> ? </font>
   
    #include <> et #include "", les deux recherchent le fichier à l'emplacement défini par l'implémentation et l'incluent. 
    La différence est que si la recherche de #include "" est réussie, elle masque le fichier du même nom que #include <> peut trouver; sinon, elle recherche le fichier de la même manière que #include <>. 
    De même, les en-têtes de la bibliothèque standard sont placés à l'emplacement trouvé par #include <>.

5. <font color='blue'>Comment définir une macro-instruction C uniquement si elle n'est pas déjà définie ? Écrivez un exemple. </font>
   
    #ifndef MACRO
    #define MACRO
    #endif


6. <font color='blue'>Comment être certain de ne pas inclure plusieurs fois le même fichier .h ? </font>

    <font color='red'>En utilisant ce que nous venons de voir dans la question précédente : on peut définir une macro instruction différente au début de chaque fichier .h. On peut alors tester l'existence de cette macro comme condition d'inclusion du fichier.</font>

7. <font color='blue'>Écrivez une fonction C int getchar(void) bloquante qui attend un caractère tapé au clavier sur le TTY0. </font>

    int getchar(void) {
&ensp;&ensp; while (__tty_regs_map[0].status == 0);
&ensp;&ensp; return __tty_regs_map[0].read; 
}

8. <font color='blue'>Savez-vous à quoi sert le mot clé volatile ? </font>



### A4 - Compilation 
1. <font color='blue'>Complétez les lignes de déclaration des variables pour la région kdata_region</font>

     __kdata_origin = 0x80020000 ; 
     __kdata_length = 0x003E0000 ;



2. <font color='blue'>Le fichier contient ensuite la déclaration des régions (dans MEMORY{...}) qui vont être remplies par les sections trouvées dans les fichiers objets. Comment modifier cette partie (la zone [... question 2 ...]) pour ajouter les lignes correspondant à la déclaration de la région kdata_region ?</font>

     kdata_region : ORIGIN = __kdata_origin, LENGTH = __kdata_length

3. <font color='blue'>Complétez les lignes correspondant à la description du remplissage de la région ktext_region.</font>

    .ktext : { <br>&ensp;&ensp; *(.text)
    } > ktext_region

4. <font color='blue'>Quelle est la différence entre =, ?= et += ?</font>
    1. = : affectation
    2. ?= : Assignez la valeur après le signe égal si elle n'a pas été assignée avant.
    3. += : (a+=b) == (a=a+b)

5. <font color='blue'>Où est utilisé CFLAGS ? Que fait -DNTTYS=$(NTTY) et pourquoi est-ce utile ici ?</font>
   
    

6. <font color='blue'>Si on exécute make sans cible, que se passe-t-il ? </font>

    on entre à la guide de commande make

7. <font color='blue'>À quoi servent @ et - au début de certaines commandes ? </font>

    <font color='red'> 1. @ : permet de ne pas afficher la commande avant son exécution. On peut rendre ce comportement systématique en ajoutant la règle .SILENT: n'importe où dans le fichier.
    2. - : permet de ne pas stopper l'exécution des commandes même si elles rendent une erreur,c'est-à-dire une valeur de sortie différente de 0.</font>

### A5 - Les modes d'exécution du MIPS

1. <font color='blue'> Le MIPS propose deux modes d'exécution, rappelez quels sont ces deux modes et à quoi ils servent?</font>

   ​	mode kernel : utilisé par le noyau

   ​	mode user : utilisé par l'application

2. <font color='blue'> Commencez par rappeler ce qu'est l'espace d'adressage du MIPS et dîtes ce que signifie « une adresse X est mappée en mémoire ». Dîtes si une adresse X mappée en mémoire est toujours accessible (en lecture ou en écriture) quelque soit le mode d'exécution du MIPS.</font>

   L'espace d'adressage est un ensemble d'adresses utilisées par MIPS.

   « une adresse X est mappée en mémoire » signifie que l'adresse X existe en mémoire.

   En mode noyau, toutes les adresses sont accessibles. Mais seuls 0x00000000 à 0x7FFFFFFF sont accessibles en mode utilisateur.

3. <font color='blue'> Le MIPS propose des registres à usage général (GPR General Purpose Register) pour les calculs ($0 à $31). Le MIPS propose un deuxième banc de registres à l'usage du système d'exploitation, ce sont les registres système (dit du coprocesseur 0). Comment sont-ils numérotés? Chaque registre porte un nom correspondant à son usage, quels sont ceux que vous connaissez: donner leur nom, leur numéro et leur rôle? Peut-on faire des calculs avec des registres? Quelles sont les instructions qui permettent de les manipuler?</font>

   $0 - $31

   $8 c0_bar : L'adresse malformée

   $9 c0_count : Réservé à l'assembleur pour les macro-instructions

   $12 c0_sr : Registre d'état utilisé pour indiquer le numéro de service syscall

   $13 c0_cause : Cause d'appel du noyau utilisés pour le passage des arguments de fonctions

   $14 c0_epc : L'adresse de retour ou de l'instruction fautive

   $15 c0_procid : Le numéro du core

   Non

   mtc0 : move to c0 ; mfc0 : move from c0

4. <font color='blue'>  Le registre status est composé de plusieurs champs de bits qui ont chacun une fonction spécifique. Décrivez le contenu du registre status et le rôle des bits de l'octet 0 (seulement les bits vus en cours).</font>

   hwi5 hwi4 hwi3 hwi2 hwi1 hwi0 0 0 0 0 0 um 0 erl exl ie

   hwi0-5 : irq masque

   um : user mode=1, kernel mode=0

   erl : error level, erl=1 au reset et quand le MIPS est dans un état incohérent

   exl : exception level

5. <font color='blue'>  Le registre cause est contient la cause d'appel du kernel. Dites à quel endroit est stockée cette cause et donnez la signification des codes 0, 4 et 8</font>

   La partie XCODE

   0 : int, interruption

   4 : adel, adresse illégale en lecture

   8 : sys, appel syscall

6. <font color='blue'>  Le registre C0_EPC est un registre 32 bits qui contient une adresse. Vous devriez l'avoir décrit dans la question 2. Expliquez pourquoi ce doit être l'adresse de l'instruction qui provoque une exception qui doit être stockée dans C0_EPC ?</font>

   Lorsqu'il n'y a pas d'exception, il est utilisé pour enregistrer l'adresse renvoyée après l'exécution de la commande syscall. Exceptionnellement, il contient l'adresse de la mauvaise instruction.

7. <font color='blue'>  Nous avons vu trois instructions utilisables seulement lorsque le MIPS est en mode kernel, lesquelles? Que font-elles? Est-ce que l'instruction syscall peut-être utilisée en mode user?</font>

   mtc0/mfc0 : move to/from c0

   eret : exception return

   Oui

8. <font color='blue'>  Quelle est l'adresse d'entrée dans le noyau?</font>

   0x80000180

9. <font color='blue'>  Que se passe-t-il quand le MIPS entre dans le noyau, après l'exécution de l'instruction syscall ?</font>

   EPC <- PC

   c0_sr.EXL <- 1

   c0_cause.XCODE <- 8

   PC <- 0x80000180

10. <font color='blue'>  Quelle instruction utilise-t-on pour sortir du noyau et entrer dans l'application ? Dîtes précisément ce que fait cette instruction dans le MIPS.</font>

    eret

    PC <- EPC

    c0_SR.EXL <- 0

### A6 -  Langage C pour la programmation système

1. <font color='blue'> Quelle était la syntaxe de cet attribut (regardez sur le slide 37).</font>

   \__attribute__ ((section (".crt0")))

2. <font color='blue'>     Expliquez ce que font les lignes 11, 12 et 15</font>

   l11 : Remplisser l'espace après avec 0 jusqu'à ce que l'adresse soit un multiple de 4 mots.

   l12 : Créer la variable __bss_origin avec l'adresse actuelle comme valeur.

   l15 : Créer la variable __bss_end avec l'adresse actuelle comme valeur.

3. <font color='blue'> À quoi servent les mots clés extern et volatile ? Si NTTYS est une macro dont la valeur est 2 , quelle est l'adresse en mémoire __tty_regs_map[1].read ?</font>

   extern : Lorsqu'une variable globale est déclarée en externe, le mot clé extern est nécessaire. Si la variable n'est pas modifiée par extern et n'a pas d'initialisation explicite, elle devient également la définition de la variable. Par conséquent, extern doit être ajouté à ce moment, et le compilateur marque ici l'espace de stockage. Lorsqu'il est chargé et initialisé à 0.

   volatile : Lorsqu'une variable est déclarée volatile, elle n'est pas stockée dans un registre.

   adresse = 0xd0200000 + d(16+8) = 0xd0200018

   

4. <font color='blue'> Que doivent contenir les registres $4 à $7 et comment doit-être la pile?</font>

   $4-$7 contiennent les paramètres de la fonction.

   <font color='red'>      Le pointeur de pile doit pointer sur la case réservée pour le premier argument et les cases suivantes sont réservées arguments suivants. Ce n'est pas rappelé ici, mais il y a au plus 4 arguments (entier ou pointeur) pour tous les syscalls. En conséquence, le pointeur de pile pointe au début d'une zone vide de 4 entiers.</font>

5. <font color='blue'> Combien d'arguments a la fonction syscall() ? Comment la fonction syscall() reçoit-elle ses arguments ? A quoi sert la ligne 3 de la fonction syscall() et que se passe-t-il si on la retire ? Expliquer la ligne 5 de la fonction syscall() . Aurait-il été possible de mettre le code de la fonction syscall() dans un fichier .S ?</font>

   syscall a 5 paramètres : int a0, int a1, int a2, int a3, int syscall_code.

   Il reçoit les quatre premiers arguments via $4-$7 et <font color='red'> obtient le cinquième argument sur la pile.</font>

   La troisième ligne déclare que la fonction syscall est globale. Sans cette ligne de code, l'éditeur de liens ne peut pas le trouver.

   <font color='red'>Le noyau attend le numéro de service dans $2 . Or le numéro du service est le 5e argument de la fonction syscall() . La ligne 5 permet d'aller le chercher dans la pile.</font>

   Il peut être mis dans un fichier .S.

### A7 -  Passage entre les modes kernel et user

1. <font color='blue'>     Comment imposer le placement d'adresse d'une fonction ou d'une variable en mémoire?</font>

   Utiliser volatile

   <font color='red'>C'est l'éditeur de lien qui est en charge du placement en mémoire du code et des données, et c'est dans le fichier ldscript kernel.ld ou user.ld que le programmeur peut imposer ses choix. Pour placer une fonction à une place, la méthode que vous avez vu consiste à créer une section grâce à la directive .section en assembleur ou à la directive \__attribute__((section())) en C puis à positionner la section créée dans la description des SECTIONS du ldscrip</font>

2. <font color='blue'>Expliquez pour chacun la valeur affectée.</font>

   c0_epc : devient l'adresse de __start

   c0_sr : = 0b00010010, UM = 1, IE = 0, EXL = 1, mode kernel avec interruptions masquées

   $29 : devient l'adresse du bit le plus haut de la pile

3. <font color='blue'>   Que faire avant l'exécution de la fonction main() du point de vue de l'initialisation? Et au retour de la fonction main() ?</font>

   Au début : Initialiser toutes les variables globales non initialisées
   Au retour : appeler exit()

4. <font color='blue'>   Nous avons vu que le noyau est sollicité par des événements, quels sont-ils? Nous rappelons que l'instruction syscall initialise le registre c0_cause , comment le noyau fait-il pour connaître la cause de son appel?</font>

   syscall, interruption, exception

   XCODE dans c0_cause enregistre la raison de l'appel.

5. <font color='blue'>La directive .org (ligne 16) permet de déplacer le pointeur de remplissage de la section courante du nombre d'octets donnés en argument, ici 0x180 . Pouvez-vous dire pourquoi ? Expliquer les lignes 25 à 28.</font>

   l25 : La valeur de c0_cause est copiée dans $26

   l26 : Les 3e à 6e bits de 26 $ sont réservés et le reste devient 0.

   l27 : $27 devient 0x20

   l28 : Si $26 et $27 ne sont pas égaux, sauter à la position de l'étiquette kpanic.

6. <font color='blue'>      Les lignes 47 à 54 sont chargées d'allouer de la place dans la pile.
* Dessinez l'état de la pile après l'exécution de ces instructions. 
* Que fait l'instruction ligne 55 et quelle conséquence cela a-t-il? - Que font les lignes 57 à 62 ? 
* Et enfin que font les lignes 64 à 70 ? Les commentaires ont été laissés, vous devez juste mettre à quoi ça sert, sans détailler ligne à ligne.</font>

   <font color='red'>État de la pile après l'exécution des lignes 36 à 43 </font>

   <font color='red'>+----------+</font>

   <font color='red'> | $31 | Nous allons exécuter jal un peu plus et perdre $31, il faut le sauver </font>

   <font color='red'>+----------+</font>

   <font color='red'> | C0_EPC |</font>

   <font color='red'> C'est l'adresse de retour du syscall</font>

   <font color='red'>+----------+ </font>

   <font color='red'>| C0_SR | le registre status est modifié plus loin, il faut le sauver pour le restaurer </font>

   <font color='red'>+----------+ </font>

   <font color='red'>| $2 | C'est le numéro de syscall qui pourra être accédé par la fonction appelée en 5e argument </font>

   <font color='red'>+----------+ </font>

   <font color='red'>| | place réservée pour le 4e argument actuellement dans $7 </font>

   <font color='red'>+----------+ </font>

   <font color='red'>| | place réservée pour le 3e argument actuellement dans $6 </font>

   <font color='red'>+----------+ </font>

   <font color='red'>| | place réservée pour le 2e argument actuellement dans $5 </font>

   <font color='red'>+----------+ </font>

   <font color='red'>$29 → | | place réservée pour le 1e argument actuellement dans $4 </font>

   <font color='red'>+----------+ </font>

   <font color='red'>L'instruction ligne 44 met 0 dans le registre c0_sr . Ce qui a pour conséquence de mettre à 0 les bits UM , EXL et IE . On est donc en mode kernel avec interruptions masquées.</font>

   <font color='red'>Commentaire du code lignes 46 à 53 Ligne 46 : $26 ← l'adresse du tableau syscall_vector ⟶ On s'apprête à y faire un accès indexé par le registre $2 Ligne 47 : $2 ← $2 & 0x1F ⟶ pour éviter de sortir du tableau si l'utilisateur à mis n'importe quoi dans $2 Ligne 48 : $2 ← $2 * 4 ⟶ Les cases du tableau sont des pointeurs et font 4 octets Ligne 49 : $2 ← $26 + $2 ⟶ $2 contient désormais l'adresse de la case contenant la fonction correspondante au service n° $2 Ligne 50 : $2 ← MEM[ $2 ] ⟶ $2 contient l'adresse de la fonction à appeler Ligne 51 : jal $2 ⟶ appel de la fonction de service On rappelle que $4 à $7 et qu'il y a de place pour ces arguments dans la pile. Les lignes 53 à 59 restaurent l'état des registres $31 , c0_status , c0_epc et le pointeur de pile puis on sort du noyau avec l'instruction eret .</font>

### A8 - Génération du code exécutable

1. <font color='blue'>   Rappelez à quoi sert un Makefile?</font>

   Le Makefile construit le fichier cible à travers une série d'instructions shell

2. <font color='blue'>   Dans cet extrait, quelles sont la cible finale, les cibles intermédiaires et les sources ? A quoi servent les variables automatiques de make? Dans ces deux règles, donnez-en la valeur.</font>

   cible finale : kernel.x

   cibles intermédiaires : kernel.ld, obj/hcpu.o, obj/kinit.o, obj/klibc.o, obj/harch.o

   cible source : hcpua.s

   kernel.x : 

   ​	$@ = cible = kernel.x 

   ​	$^ = kernel.ld , obj/hcpu.o , obj/kinit.o , obj/klibc.o, obj/harch.o

   obj/hcpua.o :

   ​	$@ = cible = obj/hcpu.o

   ​	$< = la première des dépendances = hcpua.s

3. <font color='blue'>Ecrivez la règle compil du fichier 04_libc/Makefile .</font>

   compil:

   ​	make -C kernel compil

   ​	make -C ulib compil

   ​	make -C uapp compil

## Travaux Pratiques

### B1 - Saut dans la fonction kinit() du noyau en langage C
* Question :
    1. <font color='blue'>Dans quel fichier se trouve la description de l'espace d'adressage du MIPS ? Que trouve-t-on dans ce fichier ?</font>
        1. kernel.ld
        2. il y a trois parties dans le ficher :
            * definition pour des segments mémoires (par exemple : __boot_origin)
            * <font color='red'> déclaration des région mémoire (pour ici, on juste ne sais pas comment explique) </font>
            * définition pour des sections de sortie

    2. <font color='blue'>Dans quel fichier se trouve le code de boot et pourquoi, selon vous, avoir nommé ce fichier ainsi ?</font>

        hapua.s <font color='red'> Il a été nommé ainsi parce que c'est du code qui dépend du hardware et qu'il concerne le cpu </font>
    
    3. <font color='blue'>À quelle adresse démarre le MIPS ? Où peut-on le vérifier ?</font>

        1. 0xBFC00000 
        2. sur *kernel.ld*  
       __boot_origin    = 0xbfc00000 ; /* first byte address of boot region */

    4. <font color='blue'>Que produit gcc quand on utilise l'option -c ?</font>

        *gcc* peut produit les fichers *objet*, au format de \*.o

    5.  <font color='blue'>Que fait l'éditeur de liens ? Comment est-il invoqué ?</font>

        1. Rassemblez les fichers objet pour créer une exécutable complet au format de *elf*, <font color='red'> conformément au fichier ldscript. </font>
        2. par gcc <font color='red'> ou directement par .ld </font>

    6.   <font color='blue'>De quels fichiers a besoin l'éditeur de liens pour fonctionner ?</font>
   
           les fichers objets <font color='red'>et les fichers ldscript</font>
   
    7. <font color='blue'>Dans quelle section se trouve le code de boot ? (la réponse est dans le code assembleur)</font>

        Dans *SECTIONS* qui définit la région de .boot

    8. <font color='blue'>Dans quelle région de la mémoire le code de boot est-il placé?(la réponse est dans kernel.ld)</font>

        boot_region

    9. <font color='blue'>Comment connaît-on l'adresse du registre de sortie du contrôleur de terminal TTY ?</font>

        Dans chaque terminal, il y a 4 segments : TTY_WRITE(0), TTY_STATUS(4), TTY_READ(8), TTY_CONFIG ( C); donc TTY_WRTIE(sortie de TTY) est le premier registre.
        Dnas kernel.ld, on peut voir la définition de *__tty_regs_map*. Donc le premier registre dans le map(__tty_regs_map) est l'adresse de TTY_WRITE

    10.  <font color='blue'>Quand faut-il initialiser la pile ? Dans quel fichier est-ce ? Quelle est la valeur du pointeur initial?</font>
        1. <font color='red'> Il faut initialiser le pointeur avant d'appeler kinit()</font>
        2. hcpua.s
        3. $29 <- __kdata_end; avec l'adresse 0x80400000 = __kdata_origin + __kdata_length
    
    11. <font color='blue'> Dans quel fichier le mot clé volatile est-il utilisé ? Rappeler son rôle.</font>
    dans kinit.c
    
* Exercice : 
    1. <font color='blue'>Exécutez le programme en lançant le simulateur avec make exec, qu'observez-vous ?</font>
    
        un message : "hello world"
    
    2. <font color='blue'>Exécutez le programme en lançant le simulateur avec make debug.
        1. Ouvrez trace.0.s et repérez ce qui est cité ici</font>
        
            K    12:     <boot> ------------------------------------------------------- hcpua.S
        K    12:     0xbfc00000	0x3c1d8040 	lui	sp,0x8040
        K    13:     0xbfc00004	0x27bd0000 	addiu	sp,sp,0
        K    14:     0xbfc00008	0x3c1a8000 	lui	k0,0x8000
        K    15:     0xbfc0000c	0x275a0028 	addiu	k0,k0,40
        K    26:     0xbfc00010	0x03400008 	jr	k0
        K    27:     0xbfc00014	0x00000000 	nop
        K    37:     <kinit> ------------------------------------------------------ kinit.c
        K    37:     0x80000028	0x27bdffe8 	addiu	sp,sp,-24
        K    38:     0x8000002c	0xafbf0014 	sw	ra,20(sp)
        K    39:     <-- WRITE MEMORY @ 0x803ffffc BE=1111 <-- 0       
        K    48:     0x80000030	0x3c048002 	lui	a0,0x8002
        K    49:     0x80000034	0x0c000000 	jal	80000000 <puts>
        K    50:     0x80000038	0x24840000 	addiu	a0,a0,0
        K    60:     <puts> ------------------------------------------------------- kinit.c
        K    60:     0x80000000	0x80820000 	lb	v0,0(a0)
        K    61:     0x80000004	0x10400006 	beqz	v0,80000020 <puts+0x20>
        K    70:     --> READ  MEMORY @ 0x80020000 BE=---1 --> 0x6c6c6548
        K    72:     0x80000008	0x3c03d020 	lui	v1,0xd020
        K    73:     0x8000000c	0xac620000 	sw	v0,0(v1)
        K    74:     <-- WRITE MEMORY @ 0xd0200000 BE=1111 <-- 0x48
        K    83:     0x80000010	0x24840001 	addiu	a0,a0,1
        K    84:     0x80000014	0x80820000 	lb	v0,0(a0)
        K    85:     --> READ  MEMORY @ 0x80020000 BE=--1- --> 0x6c6c6548
        K    85:     0x80000018	0x1440fffc 	bnez	v0,8000000c <puts+0xc>
        K    87:     0x8000001c	0x00000000 	nop
        K    88:     0x8000000c	0xac620000 	sw	v0,0(v1)
        K    89:     <-- WRITE MEMORY @ 0xd0200000 BE=1111 <-- 0x65
        K    89:     0x80000010	0x24840001 	addiu	a0,a0,1
        K    90:     0x80000014	0x80820000 	lb	v0,0(a0)
        K    91:     --> READ  MEMORY @ 0x80020000 BE=-1-- --> 0x6c6c6548
        K    91:     0x80000018	0x1440fffc 	bnez	v0,8000000c <puts+0xc>
        K    93:     0x8000001c	0x00000000 	nop
        K    94:     0x8000000c	0xac620000 	sw	v0,0(v1)
        K    95:     <-- WRITE MEMORY @ 0xd0200000 BE=1111 <-- 0x6c
        K    95:     0x80000010	0x24840001 	addiu	a0,a0,1
        K    96:     0x80000014	0x80820000 	lb	v0,0(a0)
        
        2. <font color='blue'>Ouvrez le fichier label0.s et interprétez ce que vous voyez</font>
        
            K    12:     <boot> ------------------------------------------------------- hcpua.S
            K    37:     <kinit> ------------------------------------------------------ kinit.c
            K    60:     <puts> ------------------------------------------------------- kinit.c
        
        3. <font color='blue'>vous exécutez à nouveau votre programme en mode debug, qu'observez-vous dans la trace d'exécution ?</font>
        

    3. <font color='blue'>Notez l'adresse de kinit dans les deux fichiers, sont-ce les mêmes ? Sont-elles dans les mêmes sections ? Expliquez pourquoi.</font>

        dans kinit.o.s : 00000028 <kinit>:
        dans kernel.x.s : 80000028 <kinit>:
        
    4. <font color='blue'>Modifiez le code de kinit.c et afficher un second message ?</font>
    
### B2. Premier petit pilote pour le terminal
* Question : 
    1. <font color='blue'>La structure décrivant la carte des registres du TTY est déclarée dans le .c. Pourquoi avoir fait ainsi ?</font>
    
    <font color='red'> Le noyau n'a pas besoin de savoir comment sont organisés les registres dans le TTY. Il a juste besoin de savoir comment écrire ou lire un message. Plus c'est cloisonné, moins il y a de risque de problèmes. En outre, cela simplifie un hypothétique portage sur une autre architecture.</font>
    
    2. <font color='blue'>Le MIPS dispose d'un compteur de cycles internes. Ce compteur est dans un banc de registres accessibles uniquement quand le processeur fonctionne en mode kernel. Nous verrons ça au prochain cours, mais en attendant nous allons quand même exploiter ce compteur. Pourquoi avoir mis la fonction dans hcpua.S ? Rappeler, pourquoi avoir mis .globl clock</font>



    3. <font color='blue'>Compilez et exécutez le code avec make exec. Observez. Ensuite ouvrez le fichier kernel.x.s et regardez où a été placée la fonction clock(). Est-ce un problème si kinit() n'est plus au début du segment ktext ? </font>

* Exercice : 
    <font color='blue'>Ecrire une fonction void Capitalize(void) appelée par la fonction kinit() qui lit une phrase terminée par un \n et la réécrit en ayant mis en majuscule la première lettre de chaque mot. </font>
    ```
    void Capitalize(void){
	    for(int i=0; i < 64; i++){
		    if(name[i] != 0 && name[i] != '\n')
			    name[i] -= 32;
        }
	}
    ```
    enfin on ajoute "Capitalize();" entre "tty_puts(0, hello);" (ligne 84) et "tty_puts(0, name);" (ligne 85)
    
### B3. Ajout d'une bibliothèque de fonctions standards pour le kernel (klibc)
* Question : 
    1. <font color='blue'>Ouvrez le fichier Makefile, En ouvrant tous les fichiers dessiner le graphe de dépendance de kernel.x vis-à-vis de ses sources?</font>
    
    ![](https://i.imgur.com/zBeCuG7.jpg)

    2. <font color='blue'>Dans quel fichier se trouvent les codes dépendant du MIPS ?</font>
    
    hcpua.s
    
* Exercice : 
    <font color='blue'>Le numéro du processeur est dans les 12 bits de poids faible du registre $15 du coprocesseur système. Ajoutez la fonction int cpuid(void) qui lit le registre c0_cpuid et qui rend un entier contenant juste les 12 bits de poids faible.
    Ecrivez un programme de test</font>
    
    sur hcpua.s (ajoutez à la fin du document)
    ```
    .globl cpuid
    
    cpuID:
        mfc0 $2, $15
        andi $2, $2, 0xFFF 
        jr $31
    ```
    
    sur hcpu.h (modifiez la ligne 16)
    ```
    extern unsigned cpuID (void);
    ```
    
    sur kinit.c (modifiez la ligne 23)
    kprintf ("[%d] Hello World!\n", cpuID());
    
### B4. Ajout de la librairie C pour l'utilisateur

* Question : 
    1. <font color='blue'>Pour ce petit système, dans quel fichier sont placés tous les prototypes des fonctions de la libc? Est-ce ainsi pour POSIX sur LINUX?</font>
    
        1. libc.h
        2. <font color='red'> Non, pour POSIX, les prototypes de fonctions de la libc sont répart:q:qis dans plusieurs fichiers suivant leur rôle. Il y a stdio.h ,string.h ,stdlib.h ,etc. Nous n'avons pas voulu ajouter cette complexité.</font>
        
    2. <font color='blue'>Dans quel fichier se trouve la définition des numéros de services tels que SYSCALL_EXIT ?</font>
        
        common/syscalls.h
    3. <font color='blue'>Dans quel fichier se trouve le vecteur de syscall, c'est-à-dire le tableau syscall_vector[] contenant les pointeurs sur les fonctions qui réalisent les services correspondants aux syscall ?</font>
        
        <font color='red'> Il est dans le fichier kernel/ksyscall.c. </font>
        
    4. <font color='blue'>Dans quel fichier se trouve le gestionnaire de syscalls ?</font>
        
        kernel/hcpua.S
        
* Exercice : 
     <font color='blue'>vous allez juste ajouter une boucle d'affichage des caractères ASCII au début de la fonction main() en utilisant la fonction de la libc fputc.

Ensuite, quand ça marche, exécutez le programme en mode débug et ouvrez le fichier trace0.s. A quel cycle, commence la fonction main() ?

Recompilez le kernel en utilisant le mode -O0(lettre 0 suivie du chiffre zéro), réexécutez l'application en mode debug et regardez à nouveau à quelle cycle commence la fonction main() ?

Pour finir, recompilez à nouveau le noyau en utilisant le mode -O3, réexécutez encore l'application en mode debug et regardez combien de cycles sont nécessaires pour exécuter la fonction fputc(). Pour ça, vous ouvrez le fichier trace0.s, vous cherchez le premier appel de fputc() et vous faites la différence ?
    Refaites le calcul pour le deuxième appel de fputc(), que constatez-vous ? Avez-vous une explication ?</font>