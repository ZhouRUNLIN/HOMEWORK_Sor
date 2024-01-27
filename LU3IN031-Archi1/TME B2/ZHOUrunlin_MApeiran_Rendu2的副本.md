# # Rendu 2 - Gestion des interruptions

**Les réponses en noir sont celles que nous avons obtenues après discussion ou de recherches sur les sites </br>
Les réponse en rouge sont révisées sur les corrigés en moodle**

## Travaux Dirigés
### A - Questions
1. <font color='blue'>À quoi servent les interruptions ? </font>

    Une interruption, c'est la suspension de l'exécution de l'application en cours sur le processeur pour accomplir un service de plus haute priorité (dans le kernel) (cours p9)

    <font color='red'>Les interruptions sont un mécanisme permettant de traiter au plus vite un événement matériel ou une demande d'un autre processeur.</font>

2. <font color='blue'>Une interruption en informatique est à la fois une suspension temporaire d'un programme et un signal électrique. Comment s'appelle le signal d'interruption et comment s'appelle le code permettant de la traiter ?</font>
   
   IRQ, ISR
   
3. <font color='blue'>Est-ce que tous les composants génèrent des signaux d'interruption ? Si la réponse est non, donnez un exemple ?</font>
    Non. Exemples : RAM, ROM

4. <font color='blue'>Est-ce qu'un composant peut produire plusieurs signaux d'interruption ?</font>

    Oui. 8 TTYS peut produire 8 IRQs

5. <font color='blue'>Est-ce qu'une application utilisateur sait quand elle va être interrompue ?</font>

    Non. 

6. <font color='blue'>Est-ce qu'une application utilisateur sait quand elle a été interrompue ?</font>

    Non.

7. <font color='blue'>Que signifie IPI et à quoi ça sert ? (il n'y a pas de slides dans le cours sur ça...)</font>
    IPI signifie Inter-Processor Interrupt, c'est un mécanisme permettant à un programme d'interrompre un autre programme s'exécutant sur un autre processeur. Il faut un composant matériel spécifique contenant des registres dont l'écriture provoque des IRQ.

8. <font color='blue'>Est-ce qu'un programme utilisateur peut interdire les interruptions en général ?</font>

    Non. Seule la modification de c0_sr ou ICU_MASK peut empêcher l'interruption.

9. <font color='blue'>Est-ce que le noyau du système d'exploitation peut interdire les interruptions en général ?</font>
    Oui. Il peut modifier c0_sr. <font color='red'>Il le fait soit définitivement quand un périphérique n'est pas utilisé, ou temporairement pour les sections critiques.</font>

10. <font color='blue'>Quand un signal d'interruption s'active est ce que le noyau sait toujours quoi faire ?</font>

    Oui. Il accédera à c0_cause.<font color='red'>Oui, c'est défini par le vecteur d'interruption, à chaque IRQ, il y a une ISR.</font>

11. <font color='blue'>Que signifie l'expression «vol de cycles» ?</font>

     Une fois l'application interrompue, son temps d'exécution est volé.

     <font color='red'>C'est une expression pour dire que l'application en cours a été interrompu le noyau pour exécuter une ISR.</font>

12. <font color='blue'>Est-ce l'application qui a provoqué l'activation d'un signal d'interruption qui est volée ?</font>

     Non. Si le système exécute plusieurs applications, celle interrompue n'est généralement pas celle qui a fait la commande ! ⇒ le gestionnaire d'interruption vole des cycles à l'application en cours. (Cours P22)

13. <font color='blue'>Pour le composant TTY, à quel moment produit-il un signal d'interruption ?</font>

     En appuyant sur le clavier

14. <font color='blue'>Pour le composant TTY, comment fait-on pour acquitter une d'interruption ?</font>

     Lors de la lecture de TTY_READ

15. <font color='blue'>Si plusieurs caractères ASCII sont en attente dans d'être lus dans un TTY, quelle conséquence cela a-t-il sur le signal d'interruption ?</font>

     Il restera actif jusqu'à ce que le dernier caractère soit lu.

16. <font color='blue'>À quoi sert le composant TIMER ?</font>

     Il envoie périodiquement des IRQ.

17. <font color='blue'>Comment fait-on pour le configurer ?</font>

     TIMER_MODE : 

     ​	bit0 -> 1 → timer en marche (décompte) ; 0 → timer arrêté

     ​	bit1 -> 0 → pas d'IRQ quand le compteur atteint 0

     TIMER_PERIOD : période demandée entre 2 IRQ

18. <font color='blue'>Comment fait-on pour acquitter une interruption pour le composant TIMER ?</font>

     TIMER_RESETIRQ : écrire n'importe quoi à cette adresse acquitte l'IRQ

19. <font color='blue'>Est-cequeleregistre TIMER_VALUE peut activer (on dit aussi lever) un signal d'interruption?</font>

     Non. C'est un compteur.

20. <font color='blue'>À quelles adresses dans l'espace d'adressage sont placés les registres des 3 contrôleurs de périphériques de la plateforme et comment le kernel les connaît ?</font>

     ttys : 0xd0200000

     icu : 0xd2200000

     timers : 0xd3200000

     Dans kernel.ld

21. <font color='blue'>Que signifie l'acronyme I.R.Q. ?</font>

     Interrupt request

22. <font color='blue'>Une IRQ est un signal électrique, combien peut-il avoir d'états ?</font>

     2, actif et inactif

23. <font color='blue'>Qu'est-ce qui provoque une IRQ ?</font>

     Il est généré par l'application qui a demandé l'interruption.

     <font color='red'>C'est un événement matériel sur le contrôleur de périphérique, comme la fin d'une commande ou l'arrivée d'une donnée.</font>

24. <font color='blue'>Les IRQ relient des composants sources et des composants destinataires, quels sont ces composants ? Donnez un exemple.</font>

     Source : TTYs, TIMERs

     Destinaire : MIPS

25. <font color='blue'>Que signifie masquer une IRQ ?</font>

     Bloquer cette requête, cette requête restera active jusqu'à ce qu'elle soit débloquée et traitée.

26. <font color='blue'>Quels composants peuvent masquer une IRQ ?</font>

     ICU_MASK et c0_sr

27. <font color='blue'>Est-ce qu'une application utilisateur peut demander le masquage d'une IRQ ?</font>

     Non. mtc0 est une commande du noyau, elle ne peut donc pas modifier c0_sr. De plus, elle ne peut pas accéder à ICU.

28. <font color='blue'>Que signifie l'acronyme I.S.R. ?</font>

     interrupt service routine

29. <font color='blue'>Dans la plateforme des TPs, sur quelles entrées de l'ICU sont branchées les IRQ venant des TTYs et du TIMER ?</font>

     TTYS : 10, 11, 12, 13

     Timer : 0

30. <font color='blue'>Quelle valeur faut il avoir dans le registre ICU_MASK si on veut recevoir seulement les IRQ venant des 4 TTYs, dans le cas de la plateforme utilisée en TP ? Donnez le nombre en binaire et en hexadécimal.</font>

     0b00000000000000000011110000000000

     0x00003C00

31. <font color='blue'>L'écriture dans ICU_MASK n'est pas possible, comment modifier ce registre pour mettre à 1 le bit 0 ?</font>

     ICU_SET[0] = 1

32. <font color='blue'>Sur une plateforme (autre que celle des TP) sur laquelle on aurait un TTY0 sur l'entrée 5, un TIMER sur l'entrée 2, et un autre TTY1 sur l'entrée 14. Que doit-on faire pour que seuls le TTY1 et le TIMER soient démasqués et que TTY0 soit masqué ? Siles3IRQselèventaumêmecycle,quellesserontlesvaleursdesregistres ICU_STATE, ICU_MASK et ICU_HIGHEST ?</font>

     ICU_SET = 0b00000000000000000100000000000100

     ICU_CLEAR = 0b00000000000000000000000000100000

     ICU_STATE = 0x00000000000000000100000000100100

33. <font color='blue'>Dans quel mode est le processeur quand il traite une IRQ ?</font>

     Mode kernel

34. <font color='blue'>À quel moment doit-on initialiser le vecteur d'interruption ?</font>

     Quand la fonction kinit() est exécutée

35. <font color='blue'>En quoi consiste la liaison des interruptions (*interrupt binding* en anglais) ?</font>

     Un IRQ correspond à un ISR

     <font color='red'>C'est le fait de lier une IRQ de périphérique et une ISR, c'est fait par le vecteur d'interruption.</font>

36. <font color='blue'>Comment le noyau sait-il que la cause de son invocation est une interruption ?</font>

     C0_cause.XCODE

37. <font color='blue'>Quelle instruction permet de sortir du noyau pour revenir dans le code interrompu ? et que fait-elle précisément ?</font>

     eret

     c0_sr.EXL ← 0

     PC ← EPC (Cours p8)

38. <font color='blue'>Rappeler la différence entre un registre temporaire et un registre persistant.</font>

     La valeur des registres temporaires peut changer après l'appel d'une fonction, contrairement aux registres persistants.

39. <font color='blue'>Pour qu'une IRQ soit effectivement prise en compte, il faut que le périphérique la lève et qu'elle ne soit pas masquée. Il y a plusieurs endroits où on peut masquer une IRQ, lesquels ?</font>

     ICU_MASK et c0_sr

40. <font color='blue'>Que fait le processeur lorsqu'il reçoit une IRQ masquée ?</font>

     None. Car la IRQ est masquée

41. <font color='blue'>Que signifie acquitter une IRQ ?</font>

     <font color='red'>Cela signifie demander au contrôleur de périphérique concerné de baisser (désactiver) le signal IRQ.</font>

42. <font color='blue'>Qui demande l'acquittement à qui ?</font>

     ISR -> registres du contrôleur de périphérique

43. <font color='blue'>Comment demande-t-on l'acquittement ?</font>

     <font color='red'>Chaque périphérique a sa propre méthode d'acquittement, il faut lire la documentation de chaque composant pour le savoir. Pour le TTY, l'acquittement est fait en lisant le registre TTY_READ, pour le TIMER, l'acquittement est fait en écrivant n'importe quelle valeur dans le registre TIMER_RSTIRQ.</font>

44. <font color='blue'>Est-ce qu'une IRQ peut se désactiver sans intervention du processeur ?</font>

     Non, une fois qu'une IRQ est activé, elle ne peut pas se désactiver par l'application.

45. <font color='blue'>Est-ce qu'une IRQ peut ne pas être attendue par le noyau ?</font>

     Non, le noyau doit fournir l'ISR.

46. <font color='blue'>Quelle est la valeur du champ XCODE du registre c0_cause à l'entrée dans le noyau en cas d'interruption?</font>

     0

     <font color='red'>Il y a 0 , et pour rappel c0_cause.XCODE = 8 pour un syscall, les autres valeurs sont des numéros d'exception (division par 0, violation de privilège, etc.)</font>

47. <font color='blue'>Quelle est la valeur écrite dans le registre c0_EPC à l'entrée dans le noyau en cas d'interruption?</font>

     EPC ← PC ou PC+4

     PC adresse de l'instruction courante pour syscall et exception

     PC+4 adresse suivante pour interruption (cf cours)

     Donc c'est PC+4

48. <font color='blue'>Que se passe-t-il dans le registre c0_status à l'entrée dans le noyau en cas d'interruption et quelle est la conséquence?</font>

     c0_status.EXL = 1

     Toutes les IRQ sont masquées.

49. <font color='blue'>La routine kentry (entrée du kernel à l'adresse 0x80000180) appelle le gestionnaire d'interruption quand le MIPS reçoit une IRQ non masquée, que fait ce gestionnaire d'interruption ?</font>

     <font color='red'>Le gestionnaire d'interruption doit déterminer le numéro de l'IRQ en lisant dans le registre ICU_HIGHEST de l'ICU et il doit appeler la fonction ISR trouvée dans le tableau IRQ_VECTOR_ISR du vecteur d'interruption dans la case du numéro de l'IRQ,en lui donnant en argument le numéro du périphérique (DEVice) trouvé dans le tableau IRQ_VECTOR_DEV[] dans la case du numéro de l'IRQ.</font>

50. <font color='blue'>À l'entrée dans le noyau, kentry analyse le champ XCODE du registre de c0_cause etsic'est 0 alors il saute au code donné ci-après (ce n'est pas exactement le code que vous pouvez voir dans les fichiers sources pour que ce soit plus facile à comprendre). Pourquoi, ne pas sauver les registres persistants ?</font>

     Il n'est pas nécessaire de sauvegarder les registres permanents, mais les registres temporaires doivent persister après une interruption, donc les registres temporaires doivent être sauvegardés.

51. <font color='blue'>La fonction irq_handler() a pour mission d'appeler la bonne ISR. Dans le code qui suit (extrait du fichier kernel/harch.c), on voit d'abord la déclaration de la structure qui décrit les registres présents dans l'ICU. En fait c'est un tableau de structures parce qu'il y a autant d'instances d'ICU que de processeurs (donné par NCPUS), ici, il y a un seul processeur MIPS, donc NCPUS=1. La déclaration extern volatile struct icu_s \__icu_regs_map[NCPUS]; informe le compilateur que le symbole \__icu_regs_map est défini ailleurs et que c'est un tableau de structures de type struct icu_s . Ainsi, le compilateur gcc sait comment utiliser la variable \__icu_regs_map .Dans quel fichier est défini \__icu_regs_map ? Que font les fonctions icu_get_highest() , icu_set_mask() et irq_handler() ? Comment s'appelle le couple de tableaux irq_vector_isr[irq] et irq_vector_dev[irq] ? Combien ont-il de cases ?</font>

     kernel/kernel.ld

     icu_get_highest() : Obtenir la commande IRQ la plus prioritaire.

     icu_set_mask() : Modifirer la valeur de icu_mask par __icu_mask or paramètre__

     irq_handler() : gère les requêtes IRQ et fournit les fonctions ISR

     Liaison (appelé binding) des couples (n° IRQ → ISR) et (n° IRQ → n°instance) en écrivant dans les tableaux IRQ_VECTOR_ISR[] et IRQ_VECTOR_DEV[]

52. <font color='blue'>Si ICU_HIGHEST contient 10 (dans le cas de notre plateforme) que doit faire la fonction irq_handler()</font>

     TTY0 : Appeler l'ISR correspondant au TTY

53. <font color='blue'> Que fait la fonction icu_set_mask (int icu, int irq) ?</font>

     ICU_MASK[irq] = 1 pour le no.icu ICU

54. <font color='blue'>Les registres du TIMER sont définis dans le code du noyau de la façon suivante. Écrivez le code de la fonction static void timer_init (int timer, int tick) qui initialise la période du timer n° timer avec l'entier nommé tick etactive les IRQ si la période donnée est non nulle.</font>

     static void timer_init (int timer, int tick) {

     ​	__timer_regs_map[timer].period = tick;

     ​	__timer_regs_map[timer].mode = (tick)?3:0; // timer ON with IRQ only if (tick != 0) 

     }

55. <font color='blue'>La configuration des périphériques et des interruptions est faite dans la fonction arch_init() appelée par kinit().
      Écrivez les instructions C permettant d'ajouter le TIMER dans le noyau avec un tick de 1000000 (1 million de cycles). Il faut (1) initialiser le timer ; (2) démasquer l'IRQ venant du timer dans l'ICU, elle connectée sur son entrée n°0 ; (3) initialiser le vecteur d'interruption avec la fonction timer_isr pour ce timer 0 .</font>

     int tick = 1000000;

     timer_init (0, tick);

     icu_set_mask (0, 0);

     irq_vector_isr [0] = timer_isr;

     irq_vector_dev [0] = 0;

## Travaux Pratiques

1. <font color='blue'>La plateforme
    La plateforme que nous allons utiliser contient :
    * un processeur
    * une mémoire multisegment pour le code et les     données du noyau et de l'utilisateur. 
    * une ROM pour le boot
    * un contrôleur MULTITTY
    * un timer
    * une icu
    La manière dont sont routées les IRQ n'est pas modifiable par logiciel, les IRQ sont des signaux électriques câblés par les architectes. Sur almo1 :
    * L'IRQ du timer entre sur l'entrée n°0 de l'ICU.
    * Les IRQ de TTY entrent respectivement sur les entrées 10, 11, 12 et 13 de l'ICU.
    
    <strong> Question : </strong>
    faire un dessin représentant la plateforme avec les signaux IRQ. </font>
    ![](https://i.imgur.com/pQo2Jy0.jpg)
    
2. <font color='blue'>Game over simple
    1. Essayez le jeu</font>
        En entrant dans le jeu, le programme demande au joueur d'entrer son nom. Ensuite, le joueur doit deviner un nombre (un nombre entier entre 0 et 99) choisi par le programme. Lorsque le joueur a deviné le nombre correspondant, le jeu se termine.
    
    2. <font color='blue'>Dans la version précédente du gestionnaire de syscall, nous avions masqué les IRQ en écrivant 0 dans le registre c0_status. Il faut modifier ça.</font>
    ```
    li $26, 0x401 
    mtc0 $26, $12 
    mtc0 $0, $12
    ```
    3. <font color='blue'>Dans archi_init(), on a une paramètre  qui va servir de période d'horloge. Le simulateur de la plateforme sur les machines de la PPTI va environ à 3.5MHz. Combien de secondes demande-t-on dans ce code ?</font>
    ```
    arch_init(30*3500000) //ligne 26
    ```
    30*3 500 000, donc 30s
    
    4. <font color='blue'>vous allez devoir remplir 3 fonctions pour configurer le timer: arch_init(), timer_init() et timer_isr() </font>
    ```
    void arch_init (int tick) {
        // TODO A remplir avec 4 lignes :
        // 1) appel de la fonction timer_init(pour le timer 0 avec tick comme période
        timer_init (0, tick);  
        // 2) mise à 1 du bit 0 du registre ICU_MASK en utilisant la fonction icu_set_mask()
        icu_set_mask (0, 0);
        // 3) initialisation de la table irq_vector_isr[] vecteur d'interruption avec timer_isr()
        irq_vector_isr [0] = timer_isr;
        // 4) initialisation de la table irq_vector_dev[] vecteur d'interruption avec 0
        irq_vector_dev [0] = 0; 
    }
    
    static void timer_init (int timer, int tick) {
        // TODO A remplir avec 2 lignes :
        // 1) initialiser le registre period du timer "timer" avec la periode tick
        __timer_regs_map[timer].period = tick; 
        // 2) initialiser le registre mode   du timer "timer" avec 3
        __timer_regs_map[timer].mode = (tick)?3:0; 
    }
    
    static void timer_isr (int timer) {
        // TODO A remplir avec 3 lignes :
        // 1) Acquiter l'interruption du timer en ecrivant n'importe quoi dans le registre resetirq
        __timer_regs_map[timer].resetirq = 1;  
        // 2) afficher un message "Game Over" avec kprintf()
        kprintf ("\nGame Over\n");
        // 3) appeler la fonction kernel exit() 
        exit(1);
    }

3. <font color='blue'>Game over avec décompteur
    Dans cette nouvelle version, l'ISR du timer décrémente un compteur alloué dans une variable globale du noyau puis elle revient dans l'application tant que ce compteur est différent de 0. Donc, dans l'ISR du timer si le compteur est différent de 0, elle affiche un message avec la valeur du compteur, sinon elle affiche "game over!" et stoppe l'application</font>
    fini cette partie par le corrigé :(

4. <font color='blue'>Évaluation de la durée d'une ISR
    En utilisant le mode debug et le fichier trace0.S, déterminez la durée en cycles du traitement par le noyau d'une IRQ du timer. Ce n'est pas exactement la même durée pour toutes les IRQ.</font>
    La commande make debug ne s'exécute pas correctement 
    Le terminal indique que () est introuvable.