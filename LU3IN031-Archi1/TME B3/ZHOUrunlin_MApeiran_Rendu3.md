# Rendu 3 - Gestion des Threads

**Les réponses en noir sont celles que nous avons obtenues après discussion ou de recherches sur les sites </br>
Les réponse en rouge sont révisées sur les corrigés en moodle**

## Travaux Dirigés
### A1 - Questions générales
1. <font color='blue'>Dites-en une phrase ce qu'est un processus informatique (selon Wikipédia)</font>

     (cf. Cours p4) 

    Un processus est une programme en cours d'exécution.

    Pour l'OS, c'est le conteneur des ressources nécessaires pour exécuter
     un programme (application), il contient :

    ●  un espace d'adressage pour le code du programme, les données globales et les piles d'exécution.

    ●  des ressources d'entrées-sorties : fichiers (disque et devices) et sockets (réseau)

    ●  des propriétés (état, parenté, droits, ...)

    ●  une console (terminal)

    ●  et des fils d'exécution nommés threads

2. <font color='blue'>Est-ce qu'un processus utilisateur s'exécute toujours dans le mode utilisateur du MIPS ?</font>

   oui (cf. Cours p6) 

   Tous les threads partagent le même espace d'adressage

   – les mêmes propriétés du processus : état, __droit__, parenté, etc.

   <font color='red'>Non, la majorité du code s'exécute en mode utilisateur (user), mais lorsqu'il fait un appel système, il entre dans le noyau pour exécuter les fonctions rendant le service avec le droit du root, et c'est toujours le processus utilisateur qui s'exécute, mais avec les droits du root.</font>

3. <font color='blue'>Nous avons vu qu'un processus utilisateur peut faire des appels système, c'est-à-dire demander des services au noyau du système d'exploitation. Est-ce qu'un processus peut faire des interruptions et des exceptions ?</font>
    Il ne peut pas provoquer d'interruption, l'IRQ doit être activée par un périphérique externe. Mais il peut déclencher des exceptions lorsqu'il rencontre un problème qu'il ne peut pas gérer.

4. <font color='blue'>Un processus dispose d'un espace d'adressage pour s'exécuter, qu'y met-il ?</font>

    Il stocke du code, de la pile et des variables globales.

5. <font color='blue'>Dans un fichier exécutable, avant qu'il ne soit chargé en mémoire, on trouve le code du programme et les données globales. Est-ce qu'il y a aussi les piles d'exécution des threads ? Justifiez votre réponse.</font>

    <font color='red'>Non, en principe non, puisque les piles sont créées à la volée à chaque création des threads. Mais, dans la version actuelle du système, qui n'a pas encore de service de mémoire dynamique, les piles des threads sont dans des variables globales de type struct thread_s , alors on peut se demander si elles sont dans le fichier. La réponse est non, parce que les variables globales thread de type struct thread_s ne sont pas initialisées.Elles sont donc dans une section BSS qui est définie (position et taille) dans le fichier, mais elle n'occupe pas de place pour les données puisque que c'est le programme qui fait la mise à 0 du segment.</font>

6. <font color='blue'>Un thread de processus informatique représente un fil d'exécution de ce processus. Il est défini par une pile d'exécution pour ses fonctions, un état des registres du processeur et des propriétés comme un état d'exécution (RUNNING, READY, DEAD, et d'autres que nous verront plus tard). Combien de threads a-t-on par processus au minimum et au maximum ?</font>

    Chaque processus a au moins un thread, et le nombre maximum de threads dépend de la taille de mémoire.

7. <font color='blue'>Tous les threads d'un processus se partagent le même espace d'adressage, et donc le même code, les mêmes variables globales, les mêmes variables dynamiques (nous les verrons dans un prochain cours). Est-ce qu'ils se partagent aussi les piles ?</font>
    Non. (cf. Cours p6)

    Tous les threads partagent le même espace d'adressage

    – les mêmes données globales (mais ils ne devraient pas partager les piles)

8. <font color='blue'>Lorsque l'on crée un nouveau thread (un nouveau fil d'exécution du processus), il faut indiquer sa fonction principale, c'est-à-dire la fonction par laquelle qu'il doit exécuter. Est-ce que le nouveau thread pourra appeler d'autres fonctions ?</font>

    Oui. Il peut appeler la plupart des autres fonctions normalement.

9. <font color='blue'>Est-ce qu'on peut créer deux threads avec la même fonction principale ?</font>
    Oui. Par exemple:

    p = fork()

    if (p>0):

    ​	fork()

10. <font color='blue'>Combien d'arguments la fonction principale d'un thread peut-elle prendre et de quel type ?</font>

    Un thread peut passer jusqu'à 4 paramètres ($4, $5, $6, $7). Les paramètres restants qui doivent être passés doivent être stockés dans le pile. Il peut s'agir d'un mot, d'un half ou d'un octet.

     <font color='red'>Le prototype de la fonction principale d'un thread est imposé par la fonction thread_create(). Une fonction principale de thread prend un seul argument de type void * et elle rend un void * . Si on veut passer plusieurs arguments, il faut les mettre dans une structure et passer le pointeur sur cette structure.</font>

    <font color='red'>Il y a quand même une exception pour le thread main() créé systématiquement au démarrage du processus. Comme vous les avez, la fonction main() prend jusqu'à 3 arguments : int argc , char *argv[] et un moins connu char *arge[] (aussi nommé char *env[] ) qui contient les définitions de variables d'environnement du shell). Dans notre système main() n'a pas d'arguments,alors que normalement c'est grâce au shell quel'utilisateur peut définir les arguments argc, argv et arge, mais nous n'avons pas encore de shell. Lafonction main() rend un int etnon pas un void * .</font>

11. <font color='blue'>Que se passe-t-il lorsqu'on sort de la fonction principale d"un thread ?</font>

    Une fois l'application interrompue, son temps d'exécution est volé.

    <font color='red'>C'est une expression pour dire que l'application en cours a été interrompu le noyau pour exécuter une ISR.</font>

12. <font color='blue'>L'exécution en temps partagé est un mécanisme permettant d'exécuter plusieurs threads à tour de rôle sur le même processeur. Comment s'appelle le service du noyau chargé du changement de thread ?</font>

    L'ordonnanceur. L'ordonnanceur est responsable de la décision de quel thread doit être exécuté ensuite et pour combien de temps.

13. <font color='blue'>La phase de changement de thread a une certaine durée, c'est un temps perdu du point de vue de l'application. Comment nomme-t-on cette phase pour indiquer que c'est un temps perdu ?</font>

    Overhead. Cette phase est considérée comme une perte de temps car elle représente le temps nécessaire pour passer d'un thread à un autre, ce qui n'apporte aucune valeur directe à l'application elle-même.

14. <font color='blue'>Pour l'exécution en temps partagé, le noyau applique une politique, laquelle définit l'ordre d'exécution. Si les threads sont toujours prêts à être exécutés et que le noyau les exécute à tour de rôle de manière équitable, comment se nomme cette politique ?</font>

    Round Robin. 

15. <font color='blue'>Dans cette politique équitable, quelle est la fréquence type de changement de thread ? Donnez une justification.</font>

    La fréquence typique de changement de thread est déterminée par le temps alloué à chaque thread. Cette durée est généralement fixe et peut être de l'ordre de quelques millisecondes. Un temps trop court entraînera une surcharge élevée en raison de la fréquence élevée des changements de thread, tandis qu'un temps trop long entraînera une accaparement des ressources CPU pendant une longue période, ce qui peut entraîner des temps d'attente élevés pour les autres threads.

16. <font color='blue'>Comment nomme-t-on la durée entre deux interruptions d'horloge ? Ici, c'est le temps d'une instance d'exécution d'un thread.</font>

     Quantum. 

     Le quantum est la durée allouée à chaque thread pour s'exécuter dans un système d'exploitation utilisant l'exécution en temps partagé, et correspond à la durée d'une instance d'exécution d'un thread avant que le noyau ne passe au thread suivant.

     <font color='red'>●  C'est le tick. Un tick d'horloge est la durée entre deux IRQ du timer. Dans l'état actuel du code, on fait une commutation de thread à chaque tick. </font>

     <font color='red'>●  Dans un système plus évolué, on a la notion de quantum qui correspond à un nombre fini de ticks, par exemple 1 quantum = 10 ticks. Ce quantum peut varier au cours du temps pour donner plus de temps à certains threads (au démarrage par exemple ou pour des tâches que l'on veut favoriser) </font>

17. <font color='blue'>Le mécanisme de changement de thread (dont vous avez donné le nom précédemment) se déroule en 3 étapes, quelle que soit la politique suivie. Quelles sont ces étapes ?</font>

    1. Sélection du thread suivant à exécuter
2. Sauvegarde de l'état du thread en cours d'exécution
    3. Restauration de l'état du thread sélectionné

18. <font color='blue'>Comment se nomme la fonction qui provoque la perte du processeur par le thread en cours au profit d'un nouveau thread ?</font>

    thread_yield()

19. <font color='blue'>Qu'est-ce qui provoque un changement de thread sans que le thread n'en fasse lui-même la demande ?</font>

     Les interruptions matérielles ou la fin d'exécution du thread

     <font color='red'>C'est l'IRQ du timer pour respecter la politique round robin, mais pas seulement, on retire le processeur aux threads bloqués, parce qu'ils ont demandé une ressource au noyau, mais que cette ressource n'est pas disponible. Le thread peut aussi demander à rendre le processeur.</font>

20. <font color='blue'>Dans le mécanisme de changement de thread, l'une des étapes est la sauvegarde du contexte, est-ce la même chose qu'un contexte de fonction ? Dites de quoi il est composé.</font>

     Non.

     Le contexte d'un thread est composé des éléments suivants :

     1. Les registres de processeur
     2. Les descripteurs de fichiers
     3. Les indicateurs de statut
     4. La pile d'exécution
     5. Les structures de données de processus

     Le contexte de fonction est généralement composé des éléments suivants :

     1. Les variables locales
     2. Les paramètres de fonction
     3. L'adresse de retour
     4. Le pointeur de pile

21. <font color='blue'>Où est sauvé le contexte d'un thread ? Que pouvez-vous dire de la fonction de sauvegarde ? (langage, prototype, valeur de retour, etc.)</font>

     Le contexte d'un thread est généralement sauvegardé dans la structure thread_s. Elle contient toutes les informations nécessaires pour restaurer l'état du thread en cours d'exécution lorsqu'il est repris ultérieurement.

     La fonction de sauvegarde est généralement appelée par le noyau du système d'exploitation lorsque le thread en cours d'exécution doit être suspendu pour laisser la place à un autre thread. La fonction de sauvegarde doit être suffisamment rapide et efficace pour minimiser le surcoût associé au changement de thread.

      <font color='red'>C'est une fonction en assembleur parce qu'elle est spécifique au processeur, on ne pourrait pas l'écrire en C. Elle prend en argument un pointeur vers le tableau de sauvegarde. C'est un prototype générique qui fait partie de la HAL (Hardware Abstraction Layer). Elle rend 1 quand elle vient juste de faire la sauvegarde du contexte du thread en cours.</font>

22. <font color='blue'>Chaque thread dispose de sa propre pile d'exécution, doit-on aussi sauver la pile lors des changements de thread ?</font>

    Non, nous avons juste besoin de garder un pointeur vers la pile.

23. <font color='blue'>Après qu'un thread a été élu et que son contexte a été chargé dans le processeur, donnez le nom de la fonction responsable du chargement et dites où elle retourne ?</font>

     1. Si le thread nouvellement élu n'a jamais été élu auparavant, la fonction "thread_load()" retourne au jr $31, qui conduit à la fonction "thread_bootstrap()". Cette fonction a pour but de lancer le thread en allant chercher les informations nécessaires dans la structure de thread, notamment la fonction de démarrage "_start()" ou "thread_start()", la fonction principale du thread et l'argument du thread.
     2. Si le thread nouvellement élu avait déjà été élu auparavant, la fonction "thread_load()" retourne à la fonction "thread_save()", qui avait été appelée lors de la perte du processeur par ce thread. En conséquence, "thread_save()" sera exécutée pour sauvegarder le contexte du thread courant avant de charger le contexte du thread nouvellement élu. La valeur de retour de "thread_save()" est testée pour savoir s'il faut revenir dans "sched_switch()" ou sortir de la fonction.

### A2 - Questions sur l'implémentation

1. <font color='blue'>Quelles sont les fonctions de l'API utilisateur des threads et les états de threads ? Indiquer les changements d'état provoqué par l'appel des fonctions de cette API. Regardez les transparents pour répondre.</font>

    Création : thread_create

    ​	état -> READY

    Cession : thread_yield

    ​	thread courant état : RUNNING -> READY

    ​	thread élu état : READY -> RUNNING

    Terminaison : thread_exit

    ​	état -> DEAD

2. <font color='blue'> La structure thread_s rassemble les propriétés du thread, sa pile et le tableau de sauvegarde de son contexte. Cette structure est, dans l'état actuel du code` entièrement dans dans le segment des données globales de l'application. Pouvez-vous justifier cette situation et en discuter ?</font>

     La structure thread_s est entièrement stockée dans le segment des données globales de l'application pour simplifier l'accès aux propriétés du thread, mais cela peut avoir des inconvénients en termes de performance et de gestion de la mémoire. Stocker la pile du thread dans l'espace utilisateur est nécessaire, tandis que stocker le contexte du thread et ses propriétés dans l'espace noyau nécessiterait deux structures par thread, ce qui est difficile à implémenter sans un allocateur de mémoire dynamique. La décision actuelle est donc un choix simplificateur temporaire.

3. <font color='blue'>Le tableau de sauvegarde du contexte d'un thread est initialisé avec des valeurs qui seront chargées dans les registres du processeur au premier chargement du thread. Tous les registres n'ont pas besoin d'être initialisés avec une valeur. Seuls les registres $c0_sr ( $12 du coprocesseur système) , $sp ( $29 des GPR) et $ra ( $31 des GPR) ont besoin d'avoir une valeur choisie. Pourquoi ?</font>

    - $c0_sr ($12 du coprocesseur système) est un registre du processeur MIPS utilisé pour stocker l'état de l'interruption et des exceptions. Il est utilisé pour activer ou désactiver les interruptions et pour stocker des informations sur l'état des exceptions.
    - $sp ($29 des GPR) est un registre du processeur MIPS utilisé pour stocker le pointeur de pile. Il pointe vers l'emplacement actuel de la pile d'exécution et est utilisé pour allouer et désallouer de l'espace sur la pile lors de l'appel de fonctions ou de la création de nouveaux threads.
    - $ra ($31 des GPR) est un registre du processeur MIPS utilisé pour stocker l'adresse de retour. Il est utilisé pour stocker l'adresse de l'instruction suivant l'appel de fonction actuel, de sorte que le programme puisse revenir à l'instruction suivante une fois que la fonction est terminée.

    Ce qui précède constitue les paramètres nécessaires à l'exécution de la fonction de démarrage thread_bootstrap().

4. <font color='blue'>$c0_sr est initialisé avec 0x413 , dite pourquoi.</font>

    0x413 = 0b10000010011

    HWI0=1, IE=1(autoriser les interruptions), UM=1, (mode user), EXL=1(masquer les interruptions)

5. <font color='blue'>La fonction sched_switch() appelle d'abord l'électeur de thread qui choisit le thread entrant (qui gagne le processeur), puis sched_switch() sauve le contexte du thread sortant (qui perd le processeur) et charge le contexte du thread entrant, enfin sched_switch() change l'état du thread entrant à RUNNING . sched_switch() est appelée par thread_yield() . Pouvez-vous expliquer pourquoi avoir créé sched_switch() ? </font>

    La fonction sched_switch() a été créée pour gérer le changement de contexte entre les threads et passer la main au thread nouvellement élu. Sans cette fonction, le programme ne pourrait pas passer le contrôle d'exécution d'un thread à un autre et il n'y aurait pas d'exécution en temps partagé.

    <font color='red'>Un principe de fonctionnement de l'ordonnanceur, c'est que l'ordonnanceur ignore la raison pour laquelle un thread perd le processeur. Son travail, c'est d'élire un nouveau thread entrant, sauver le contexte du thread sortant et charger le contexte du thread entrant. Il change également l'état du thread entrant à RUNNING parce qu'il sait que c'est le bon état (il vient de charger un contexte et donc le thread entrant est nécessairement le RUNNING thread). Comme vous pouvez le voir, dans ces fonctions, on commence par changer l'état du thread et après on appelle sched_switch() . Pour le moment, c'est un simple changement d'état avant l'appel à sched_switch() , mais plus tard, il y aura d'autres opérations, pour la gestion des listes d'attente des ressources partagées ou la gestion des terminaisons de threads dont d'autres attendent la terminaison (avec thread_join ).</font>

6. <font color='blue'>Quand un thread est élu pour la première fois, à la sortie de thread_load() , on appelle la fonction thread_bootstrap() . Retrouvez dans les transparents du cours les étapes qui vont mener à l'exécution de la fonction principale du thread élu, et expliquez-les.</font>

    1. Initialiser le pointeur de pile ($sp) pour le nouveau thread avec la valeur de la variable thread_stack de la structure thread_s correspondante.
    2. Récupèrer les informations de la structure thread_s, notamment la fonction de démarrage (thread_start), la fonction principale du thread et l'argument du thread.
    3. Appeler thread_start pour initialiser les segments de données et de bss.
    4. Appeler la fonction principale du thread avec son argument.
    5. Une fois la fonction principale terminée, elle retourne la valeur de retour qui est stockée dans la structure thread_s correspondante.
    6. Appeler la fonction thread_exit() pour terminer le thread.

7. <font color='blue'>Un thread peut perdre le processeur pour 3 raisons (dans la version actuelle du code), quelles sont ces raisons ?</font>

    thread_yield() , l'interruption d'horloge, thread_exit()

8. <font color='blue'>Quand un thread TS perd le processeur pour une raison X à la date T , il entre dans le noyau par kentry, puis il y a une séquence d'appel de fonction jusqu'à la fonction thread_load() du thread entrant TE. Lorsqu'on sort de ce thread_load() , on est dans le nouveau thread TE. Plus tard, le thread TS sera élu à son tour et gagnera à nouveau le processeur en sortant lui aussi d'un thread_load() . En conséquence, on sortira de la séquence des appels qu'il y avait eu à la date T . Expliquez, en vous appuyant sur la description du comportement précédent, pourquoi on ne sauve pas les registres temporaires dans le contexte des threads.</font>

    Lorsqu'un thread rend le processeur, il le reprendra plus tard et reviendra précisément dans la fonction où il l'avait perdu, sauf si c'est une sortie définitive avec thread_exit().

    Les fonctions C supposent que les registres persistants conservent leur valeur, c'est-à-dire qu'ils ne sont pas modifiés par la fonction appelée. La fonction thread_save() ne peut modifier que les registres temporaires et doit sauvegarder les registres persistants pour qu'ils soient restaurés plus tard par la fonction thread_load() qui sortira de thread_save() sans modification des registres persistants.

    Ces deux points sont importants pour garantir la cohérence de l'exécution des threads et éviter les conflits entre eux. Ils permettent également de gérer efficacement la sauvegarde et la restauration des contextes des threads pour assurer une exécution en temps partagé.

9. <font color='blue'>Où sont définis les symboles __bss_origin , __bss_end , __main_thread , _start et quel est leur type ?</font>

    <font color='red'>C'est le fichier kernel.ld qui définit la position de __bss_origin et __bss_end dans la section .kdata . Ce sont des adresses qui dépendent des variables globales. C'est aussi le fichier kernel.ld qui définit les adresses __main_thread et _start . Par convention, __main_thread est au tout début de la section .data de l'utilisateur et _start est au tout début de la section .text de l'utilisateur. Cette convention est nécessaire pour que le kernel sache comment lancer le premier thread de l'application.</font>

10. <font color='blue'>Dites ce que sont les arguments 2 et 3 de thread_create_kernel() dans le code de kinit() et pourquoi, ici, on les met à 0 ?</font>

    2 : la taille de la pile allouée pour le nouveau thread

    La taille de la pile est fixée à l'avance pour chaque thread dans la structure thread_s. Il n'est donc pas nécessaire de spécifier une taille de pile lors de la création du thread.

    3 : la priorité du nouveau thread

    La priorité du thread n'est pas importante pour kinit(), car il s'agit d'un thread système qui ne sera jamais mis en attente par un autre thread. Il peut donc avoir une priorité minimale sans affecter les performances du système.

11. <font color='blue'> Dans la fonction kinit() , que se passe-t-il quand on sort de thread_load() et pourquoi avoir mis l'appel à kpanic() ?</font>

     Dans la fonction kinit(), quand on sort de thread_load(), cela signifie que le thread kinit() a été élu pour la première fois et que sa fonction principale thread_start() va être exécutée.

     L'appel à kpanic() a été ajouté pour gérer les erreurs qui pourraient survenir pendant l'exécution de kinit(). Si une erreur critique se produit, le système est dans un état instable et doit être arrêté. kpanic() permet d'afficher un message d'erreur et de redémarrer le système en cas d'erreur critique.

12. <font color='blue'>Dans quelle pile s'exécute la fonction kinit() ? Dans quelle section est-elle ? Pourquoi n'est-elle que temporaire ?</font>

     La fonction kinit() s'exécute sur la pile noyau (kernel stack). Elle est située dans la section .text de l'image exécutable du noyau. Elle n'est que temporaire car elle est appelée une seule fois lors du démarrage du système et n'a plus besoin d'être utilisée une fois que le système est initialisé.

13. <font color='blue'>Pour le chargement de thread main() avec thread_load (_main_thread.context) , on initialise les registres $16 à $23 , $30 , $c0_EPC , est-utile ? Si oui pourquoi ? Sinon, pourquoi faire ces initialisations ?</font>

     Oui. Les registres $16 à $23 sont utilisés pour le passage des arguments de fonction, et $30 est le pointeur de pile (stack pointer). $c0_EPC est le registre qui contient l'adresse de la prochaine instruction à exécuter en cas d'exception ou d'interruption.

     Ces initialisations permettent de s'assurer que le thread main() a tous les registres nécessaires initialisés pour fonctionner correctement.

14. <font color='blue'>Quelle(s) conséquence(s) voyez-vous pour les appels système ?</font>

     <font color='red'>Si les appels système ne sont plus interruptibles, ils ne doivent plus être bloquants.</font>

15. <font color='blue'> 1) Que doit-faire le noyau si un thread lui demande une ressource qu'il n'a pas ? Il ne peut pas attendre la ressource, alors il a deux possibilités, les voyez-vous ? Mettez-vous à sa place si vous devez gérer des ressources, par exemple des places dans un restaurant, que vous avez des clients qui se présentent et que toutes les places sont occupées. Que faites-vous ?</font>

     Le noyau doit mettre le thread en état de blocage (blocked state) et planifier un autre thread à exécuter. Le thread bloqué restera dans cet état jusqu'à ce que la ressource soit disponible.

     <font color='red'>Deux possibilités : </font>

     1. <font color='red'>Soit le noyau abandonne le service et rend une erreur au thread pour l'informer que le service ne peut pas être rendu, et donc que le thread doit retenter sa chance plus tard ou faire autre chose. Pour l'analogie, vous dites à votre client de partir et de, s'il veut, revenir plus tard ou pas. </font>
     2. <font color='red'>Soit le noyau demande un changement de thread avec thread_yield() pour faire quelque chose d'utile pour un autre thread. Pour l'analogie, vous dites à votre client d'attendre et vous allez faire autre chose. Le client attend et tente de rentrer dès qu'un autre client sort du restaurant. Notez que dans cette analogie, il n'y a pas de file d'attente, le dernier client arrivé sera peut-être le premier servi.</font>

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