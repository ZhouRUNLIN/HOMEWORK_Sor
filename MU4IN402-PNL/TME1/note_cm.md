# CM 1

## Noyau: 
Partie système d'exploitation s'exécute en mode s.

## Système d'exploitation: 
Gestionnaire d'interruption.

## Noyau monolitique:
Routeur : nécessit une partie de fonctionnement

Définition: Toutes les fonctions intégrée dans une seule espace d'exécution.

## Micro-noyau:
Problème: Plusieur appel-système demandé par le noyau, très couteux.

But: Service indépendante, chaque bloc de service s'interagit par communication (processus)

## Noyau modulaires monolitiques:
Ressemble librairie dynamique. Sert également monolitique spécifique.

But: Certaine service réalisé par module,plus de fléxilité et extension, mais appel-système et noyau sont
toujours dans une même espace d'adressage.

## Retenir jusqu'à 1990: histoire des systèmes Unix
### Année important: 
1970: Le permier compilateur C

1971:

1976:

19

## Chronologie noyau Linux:
1996: PowerPC, Multiprocesseur

1999: IPv6

2003: 2.6, préemptible, communication processus pendans appel-système.

2007: KVM

2011: 3.0 

### Personne important:

Ken Thompson et 

### Compagnie:

AT&T

# CM2

## inline
+ Économiser le cout d'appel fonction

- Augmenter la taille du code, plus de cache miss.

## Annotation de prédiction
Permet à gcc(pas POSIX)d'optimiser le branchement.

likely(): indique à gcc que plus de chance d'être exécuter

unlikely():indique à gcc que moins de chance d'être exécuter

Dans la condition if, on utilise souvent unlikey() pour évider erreur.

## Annotation de passage paramètre
amslinkage:

Indiquer gcc de chercher/déplacer paramètre dans la pile au lieu de registres.

Très important pour appel système.

## union
un type pour stocker différents types de données dans un même espace mémoire

chaque type est un alias de la même d'espace mémoire

e.g
```
union{
    short x;
    long y;
    float z;
}monUnion;

```

## structure avec tableau de taille variable
La taille devient un attribut dans la structure

e.g
```
struct buf {
    int length;
    char *contents;
};
struct buf *thisBuf = (struct buf *) malloc (sizeof (struct buf));
thisBuf->contents = (char *) malloc (SIZE_XXX);
thisBuf->length = SIZE_XXX;
```
limite:
1. deux fois libération
2. la duplication de structure pour la copie n'est pas suffisant.

solution: le struct hack ou tail-padded structures
```
struct buf {
    int length;
    char contents[0];
};
struct buf *thisBuf = (struct buf *) malloc (sizeof (struct buf) + SIZE_XXX);
thisBuf->length = SIZE_XXX;
struct buf anotherTab = { 3, { 'a', 'b', 'c' } };
```
moins d'espace mémoire

## vecteur & pointeur
compilation:

gcc main.c && ./a.out
```
gcc main.c: compiler main.c
&& : tester la commande avant est valid
./a.out : executer la executable (.out est une forme standard)
```
ici est une démonstration de l'ambiguité des vecteurs.

```
void main (void) {
    char *yes = "da";
    char oui[4];
    printf("%p - %p", yes, &yes);
    printf("%p - %p", oui, &oui);
}
```

```
gcc main.c && ./a.out
0x4005e4 - 0x7fff629b2b28
0x7fff629b2b20 - 0x7fff629b2b20
```
i.e. un identificateur d'un vecteur est une constante symbolique

## pointeur des fonctions
prototype:

type_de_retour(* nom_du_pointeur) (liste_des_arguments);

e.g

void (*monPointeur)(void);

int (*monPointeur)(int, char);

callback:

un mécanisme qui utilise le pointeur de fonction en paramètre

```
void myRelease (struct elem *elem){
    ...
}

void elem_put(struct elem *elem, void (* release)(struct elem *)){
    elem->refcount--;
    if (!elem->refcount)
        release(elem);
}

void main (void) {
    struct elem myElem;
    elem_put(myElem, myRelease);
}
```
Le pointeur de fonction peut être aussi une type de retour

prototype:

type_de_retour_de_la_fonction_retournee (* ma_fonction (liste_arg)) (liste_arg_fonction_retournee)

## Reconmandation de programmation
1. évider gros allocation et fonction récursive en pile noyau.
2. évider flottant dans le code de noyau
3. utiliser type générique dans *linux/type.h*
e.g
```
u8 : unsigned byte (8 bits)
u16 : unsigned word (16 bits)
u32 :unsigned 32-bit value
u64 : unsigned 64-bit value
s8 : signed byte (8 bits)
s16 : signed word (16 bits)
s32 : signed 32-bit value
s64 : signed 64-bit value
```

## API noyau
### printk
1. définit avec préfix de **KERN_EMREG** à **KERN_DEBUG**.
2. le flux récupérer par klogd(/var/log/kern.log)

e.g

printk(KERN_DEBUG "Au retour de f() : i=%i", i);

### kmalloc
1. Allocation par taille de 2n n k (k : quelques octets de gestion)
2. Allocation de mémoire contigue en RAM
3. Possible d'allouer zone mémoire supérieur à RAM via *mémoire haut* (**ZONE_HIGHMEM**)
4. Options définitent dans include/linux/gfp.h
    
    GFP: Get Free Page
5. options supplémentaire avec | plus macro

### La mémoire par page
```
unsigned long get_zeroed_page(int flags) : Retourne un pointeur vers une page libre et la remplit avec des zéros

unsigned long __get_free_page(int flags) : Identique, mais le contenu n’est pas initialisé

unsigned long __get_free_pages(int flags, unsigned long order) : Retourne un pointeur sur une zone mémoire de plusieurs pages continues en mémoire physique avec order= log2(nombre de pages).
```

### vmalloc
allouer une zone de mémoire contigue vituel, même si les pages ne pas être contigue en mémoire physique
```
void *vmalloc(unsigned long size);
void vfree(void *addr);
```

### ioremap()
correspondre le segment donné en mémoire physique dans l'espace d'adressage virtuel.
```
void *ioremap(unsigned long phys_addr, unsigned long size);
void iounmap(void *address);
```

### wait queue
une mécanisme que les processus s'endormis en attente une ressource.

deux fonctions s'endormis le processus:
```
sleep_on(queue)
interruptible_sleep_on(queue)
```
avec interruptible_sleep_on peut être réveille par le signal mais l'autre que par la ressource demandée.

réveil par son handler en appelant `wake_upqueue`



e.g

data = kmalloc(sizeof(*data), GFP_KERNEL);

Cette mécanisme permettre de `schedular` les listes des tâche (avec priorité),

```
struct tache{
    void (* pointeur_fonction sur tache)(argument) tache;
    void * donnée;
}
```

la liste de tache sur wait queue va exécuter une à l'autre, avec son donnée et argument.

## Retour de fonction
Dans les cas normale, on retourne une valeur positif ou nul en cas succès et négatif en cas d'erreur.

Si le retour est un pointeur:

**ERR_PTR()**: pointeur d'erreur 
**IS_ERR()**: détermine est-ce que la valeur de retour est une erreur.
**PTR_ERR()**:extraire l'information d'erreur.

```
int _torture_create_kthread(int (*fn)(void *arg), void *arg, 
char *s, char *m, char *f, struct task_struct **tp)
{
    int ret = 0;
    VERBOSE_TOROUT_STRING(m);
    *tp = kthread_run(fn, arg, "%s", s);
    if (IS_ERR(*tp)) {
        ret = PTR_ERR(*tp);
        VERBOSE_TOROUT_ERRSTRING(f);
    *tp = NULL; 
    }
    torture_shuffle_task_register(*tp);
    return ret;
}
EXPORT_SYMBOL_GPL(_torture_create_kthread);
```


















# CM 6
```
Type            Sens         Synchronisation       Taille data      Type        Simplicité
                                                                                kernel|User
Syscall         U->K                Oui           nbr registres     mots       moyen|complex/libc    //À évider sauf fonction 
                                                                                                     //universel
ioctl           U->K                Oui                                             |plus simple
                                                                   4 octet
Param Module    U->K                Non           Taille param    Type param   Plus simple|Simple R/W fichier

Procfs          U->K                Oui(Write)      1 page ou       Brut       Plus simple|Simple R/W fichier   //Réservé avec 
                                                                                                        //data sur le Kernel
Sysfs           K->U                Non(Read)        Segfile
         

```