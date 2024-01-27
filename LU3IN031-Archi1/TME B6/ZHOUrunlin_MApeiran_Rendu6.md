# # Rendu 6 - Allocation dynamique de mémoire

**Les réponses en noir sont celles que nous avons obtenues après discussion ou de recherches sur les sites </br>
Les réponse en rouge sont révisées sur les corrigés en moodle**

## Travaux Dirigés
### A - Questions de cours
1. <font color='blue'>Quels sont les besoins d'allocation de l'application et du noyau ? </font>

    Variables globales, locales et dynamiques

2. <font color='blue'>L'allocation dynamique est confrontée au problème de fragmentation de l'espace libre. Il y a deux types de fragmentation, définissez-les.</font>

   Perte par fragmentation externe : Espace insuffisant pour l'allocation entre deux mémoires allouées

   Perte par fragmentation interne : La partie entre deux mémoires allouées, qui est suffisante pour l'allocation, mais ne peut pas être allouée car ce n'est pas une cadre complète.

3. <font color='blue'>Pourquoi l'API `list` propose-t-elle un double chaînage pour ses éléments ?</font>
    Car il a besoin de:

    - insérer un élément au début, à la fin ou au milieu
    - extraire un élément au début, à la fin ou au milieu
    - parcourir tous les éléments
    - compter le nombre d'éléments

4. <font color='blue'>Comment est-il possible de trouver le pointeur sur la structure à partir du pointeur sur l'un de ses champs ? Comment se nomme la macro (ce n'est pas une fonction) permettant ce service (la réponse est dans les slides du cours)</font>

    list_item(list_t * item, typeof item_s, member)

    - rout_t * racine = list_foreach ( &(racine->root), item)

5. <font color='blue'>À quoi sert l'allocateur de piles user ? Qui demande l'allocation ? Qui utilise les piles ? Est-ce que ces piles ont une taille variable ?</font>

    Les threads ; les processus ; le kernel ; non. 

6. <font color='blue'>Où sont allouées les piles user ? Peut-on en allouer autant que l'on veut ? dites pourquoi.</font>

    En haut de .data

    Non car l'espace est limité.

7. <font color='blue'>Est-ce que ces piles peuvent déborder ? Si oui, est-ce vraiment un problème et que propose kO6 pour ce problème ?</font>
    Oui. car il n'existe aucun mécanisme pour empêcher ce comportement.

    Non car le nombre MAGIC_STACK est écrit en haut et en bas et rendre le haut de pile (cours p19)

8. <font color='blue'>Que signifie que les objets alloués sont alignés sur les lignes de cache ? Et quels sont les bénéfices de cette contrainte ?</font>

    Leur adresse de départ est un multiple de la ligne de cache.

    Les objets alloués sont alignés sur des lignes de cache pour : 

    (1) éviter les faux partages (pb cohérence de cache), 

    (2) limiter la fragmentation externe et 

    (3) améliorer un peu l'efficacité des caches - localité spatiale (cours p22)

9. <font color='blue'>L'allocateur d'objets (nommés blocs dans le rappel de cours au-dessus) pour l'application utilise une politique *first-fit*. Qu'est-ce que cela signifie ? Quels sont les autres ? Existe-t-il une politique meilleure que les autres et pour quel critère ?</font>

    First Fit choix de la première zone libre assez grande pour répondre à la demande.

    Next Fit politique First fit mais en commençant là où on a fait la dernière allocation.

    Best Fit Recherche de la zone dont la taille est la plus proche de la taille demandée

    First Fit / Next Fit sont plus rapides mais font perdre les grands segments, Next Fit permet d'étaler les objets dans le tas.

    Best Fit est plus lent, mais il préserve les grands segments, toutefois il engendre plus de petits segments.(cours p23)

10. <font color='blue'>Rappeler le nom des deux fonctions de l'API utilisateur de cet allocateur. Est-ce que ces fonctions font des appels système à chaque fois ? Si oui, quand et pourquoi ?</font>

    –  void * alloc (size_t * size) → rend un pointeur sur l'objet ou NULL

    –  void free (void * ptr) → prend un pointeur alloué par alloc()

    Non. La libc peut demander au noyau de changer la taille du tas avec les appels systèmes brk() et sbrk() qui déplacent le pointeur uheap_end de fin de tas (cours p22)

11. <font color='blue'>Pour libérer un objet alloué par l'allocateur de l'application, la fonction `free()` reçoit juste le pointeur rendu par `malloc()`. Comment la fonction `free()` connaît-elle la taille qui avait été allouée ?</font>

      Le premier mot du bloc alloué est utilisé pour stocker la taille du bloc.

12. <font color='blue'>L'allocateur d'objets du noyau utilise un mécanisme d'allocation par dalles ou `slab` en anglais, nommé `slab allocator`. Qu'est-ce qu'un slab ? Quelle est la taille d'un slab ? Quel est l'intérêt des slabs?</font>

     La taille des objets est un multiple d'une ligne de cache et au plus 1 page de 4kO. Pour limiter la fragmentation, tous les objets de même taille sont alloués dans des grands segments de taille fixe que l'on nomme slab. (Cours p30)

     Les slabs ont une taille de 2n pages de 4kO

13. <font color='blue'>L'allocateur d'objets du noyau gère des listes d'objets libres. Quel rapport y a-t-il entre les objets alloués et les slabs ? À quel moment les slabs sont-ils alloués ? À quel moment les slabs sont-ils libérés ? </font>

     Les slabs contiennent les objets libres et donc les futurs objets alloués.

     L'allocateur alloue un slab quand on lui demande un objet d'une certaine taille.

     L'allocateur recherche les slabs entièrement remplis d'objets libres et dans ce cas, il en retire tous les objets et rend le slab.

14. <font color='blue'>Lorsqu'on libère le dernier objet d'un slab, celui-ci est libéré, pensez-vous que cela puisse être un problème ? Si oui, avez-vous une solution ?</font>

     <font color='red'>La politique de rendre un slab dès qu'il est plein (qu'il ne contient que des objets libres) n'est pas forcément efficace.
      En effet, si on alloue un objet d'une taille T, alors on ouvre un slab (on l'alloue) et si on le libère, alors on ferme le slab (on le libère). Si on fait ça en boucle, l'ouverture/fermeture (allocation/libération) des slabs est une perte de temps.
      Il est sans doute préférable d'avoir toujours des listes d'objets libres non vides. On peut donc définir un seuil d'objets libres en dessous duquel ne pas descendre.</font>

15. <font color='blue'>Les objets alloués par l'allocateur d'objets de kO6 font au maximum 4kO, pourquoi cette limite ? Est-ce un problème selon vous ?</font>

     Les slab font toujours 1 et 1 seule page de 4kO

     Les objets sont tous des multiples d'1 ligne 65 de cache mais au minimum 16 octets (4 mots)

     Il y a donc au plus 256 objets d'1 ligne (16o) / slab (cours p31)

16. <font color='blue'>Pour libérer un objet alloué par l'allocateur d'objets du noyau, on utilise la fonction `kfree()` qui prend en argument le pointeur alloué par `kmalloc()` et la taille allouée. Pourquoi demander la taille ? Est-ce une contrainte ?</font>

     nbline ← le nombre de lignes de size (arrondi par excès) pour trouver la liste où rechercher (cours p33)

17. <font color='blue'>Le premier usage des allocateurs est fait par la gestion des threads. Sur les trois allocateurs décrits ici, quels sont ceux qu’il utilise?</font>

     kernel : l'allocateur slab

     Application : l'allocateur pile

18. <font color='blue'>Chaque thread a désormais deux piles. Quelles tailles ont-elles ? À quoi servent-elles et pourquoi sont-elles utiles ? À quel moment bascule-t-on de l'une à l'autre ?</font>

     pile dans .data : 64kb, pour les threads et processeurs

     pile dans .kdata : <4kb, pour les threads dans mode kernel

     Nous basculons lors de l'entrée et de la sortie du mode noyau.

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