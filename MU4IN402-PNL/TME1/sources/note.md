# TME1

## task1

2 byte major

8 byte minor

1 byte flag

in_unstable initiale:

return 1 & ((char *)v)[sizeof(unsigned short)];

```
(gdb) x /32b &v
0x7fffffffde10:	4	0	-1	-1	-1	127	0	0
0x7fffffffde18:	0	0	0	0	0	0	0	0
0x7fffffffde20:	0	16	0	0	0	0	0	0
0x7fffffffde28:	0	96	112	96	109	-69	-35	-39
```
pour la première ligne, 6 byte arbitraire;

pour la troisième ligne, 7 byte arbitraire.

ptype /o v
```
(gdb) ptype /o v
/* offset      |    size */  type = struct version {
/*      0      |       2 */    unsigned short major;
/* XXX  6-byte hole      */
/*      8      |       8 */    unsigned long minor;
/*     16      |       1 */    char flags;
/* XXX  7-byte padding   */

                               /* total size (bytes):   24 */
                             }
```

optimisation du structure:

```
struct version {
	char flags;
	unsigned short major;
	unsigned long minor;
};

```

son mémoire:
```
(gdb) ptype /o v
/* offset      |    size */  type = struct version {
/*      0      |       1 */    char flags;
/* XXX  1-byte hole      */
/*      2      |       2 */    unsigned short major;
/* XXX  4-byte hole      */
/*      8      |       8 */    unsigned long minor;

                               /* total size (bytes):   16 */
                             }
```

x /32b &v
```
(gdb) x /32b &v
0x7fffffffde10:	0	-30	4	0	-1	127	0	0
0x7fffffffde18:	0	0	0	0	0	0	0	0
0x7fffffffde20:	0	16	0	0	0	0	0	0
0x7fffffffde28:	0	-114	-128	54	75	31	-106	4
```

## task 2
```
0------------commit, id

8------------version

24-----------comment

32-----------next

40-----------prev

48-----------*la fin de structure*

```

## task 4

**LIST_HEAD**: définit et initialise une list_head en même temps

**INIT_LIST_HEAD**: initialise une list head après son définition

**list_del**:supprimer une list head dans la cercle

**__list_del_entry**:relier la précédent et prochaine list head mais pas supprimer list head ici

**__list_cut_position**：déplacer tous les points avant `entry` (list head = `head`, entry inclus ) 
à la nouvelle liste commence  par `list`

**list_entry**:retourne la structure que list_head embarqué

**list_next_entry**:retourne la prochaine structure

**list_for_each**:iterer la list_head (direction -> next)

**list_for_each_prev**:iterer la list_head (direction -> prev)

**list_for_each_entry**:(pos: structure type head:list_head member:list_head name in structure type)
iterer sur la structure embarqué


## task 7

Avant créer l'interface struct commit_ops:
```
(gdb) ptype /o tmp
type = struct commit {
/*      0      |       8 */    unsigned long id;
/*      8      |      16 */    struct version {
/*      8      |       1 */        char flags;
/* XXX  1-byte hole      */
/*     10      |       2 */        unsigned short major;
/* XXX  4-byte hole      */
/*     16      |       8 */        unsigned long minor;

                                   /* total size (bytes):   16 */
                               } version;
/*     24      |       8 */    char *comment;
/*     32      |      16 */    struct list_head {
/*     32      |       8 */        struct list_head *next;
/*     40      |       8 */        struct list_head *prev;

                                   /* total size (bytes):   16 */
                               } minor_list;
/*     48      |      16 */    struct list_head {
/*     48      |       8 */        struct list_head *next;
/*     56      |       8 */        struct list_head *prev;

                                   /* total size (bytes):   16 */
--Type <RET> for more, q to quit, c to continue without paging--
                               } major_list;
/*     64      |       8 */    struct commit *parent;
/*     72      |       8 */    void (*display)(struct commit *);
/*     80      |       8 */    void (*extract)(struct commit *);

                               /* total size (bytes):   88 */
                             } *

```

Après créer l'interface struct commit_ops:
```
(gdb) ptype /o tmp
type = struct commit {
/*      0      |       8 */    unsigned long id;
/*      8      |      16 */    struct version {
/*      8      |       1 */        char flags;
/* XXX  1-byte hole      */
/*     10      |       2 */        unsigned short major;
/* XXX  4-byte hole      */
/*     16      |       8 */        unsigned long minor;

                                   /* total size (bytes):   16 */
                               } version;
/*     24      |       8 */    char *comment;
/*     32      |      16 */    struct list_head {
/*     32      |       8 */        struct list_head *next;
/*     40      |       8 */        struct list_head *prev;

                                   /* total size (bytes):   16 */
                               } minor_list;
/*     48      |      16 */    struct list_head {
/*     48      |       8 */        struct list_head *next;
/*     56      |       8 */        struct list_head *prev;

                                   /* total size (bytes):   16 */
                               } major_list;
/*     64      |       8 */    struct commit *parent;
/*     72      |      16 */    struct commit_ops {
/*     72      |       8 */        void (*display)(struct commit *);
/*     80      |       8 */        void (*extract)(struct commit *);

                                   /* total size (bytes):   16 */
                               } ops;

                               /* total size (bytes):   88 */
                             } *
```

## task 8


Avant modification:
```
(gdb) print *c
$1 = {title_size = 1431655769, title = 0x9d6d8d2cae2d6b81 <error: Cannot access memory at address 0x9d6d8d2cae2d6b81>, author_size = 13, author = 0x555555559300 "\271", <incomplete sequence \307>, 
  text_size = 10, text = 0x555555559320 <incomplete sequence \306>}
```
```
(gdb) ptype /o c
type = struct comment {
/*      0      |       4 */    int title_size;
/* XXX  4-byte hole      */
/*      8      |       8 */    char *title;
/*     16      |       4 */    int author_size;
/* XXX  4-byte hole      */
/*     24      |       8 */    char *author;
/*     32      |       4 */    int text_size;
/* XXX  4-byte hole      */
/*     40      |       8 */    char *text;

                               /* total size (bytes):   48 */
                             } *
```

Au cours de modification:
```
(gdb) print *c
$4 = {title = 0x555555559300, author = 0x5555555592e0, text = 0x5555555592c0}
(gdb) print *c->author
$5 = {size = -8949906, author = 0x5555555592e4 "D. Ritchie"}
(gdb) print *c->text
$6 = {size = 11, text = 0x5555555592c4 "better now"}
(gdb) print *c->title
$7 = {size = 1919251825, title = 0x555555559304 " the world with this languag the world with this language from D. Ritchie \"better now\"\n\244"}
```

Après modification:
```
(gdb) print *c
$1 = {title = 0x5555555592c0, author = 0x5555555592e0, text = 0x555555559300}
(gdb) print *c->title
$2 = {size = 14, title = 0x5555555592c4 "first release"}
(gdb) print *c->author
$3 = {size = 13, author = 0x5555555592e4 "B. Kernighan"}
(gdb) print *c->text
$4 = {size = 42, text = 0x555555559304 "let's conquer the world with this languageRitchie \"better now\"\nborn\"\n"}
```
```
(gdb) ptype /o c
type = struct comment {
/*      0      |       8 */    struct title *title;
/*      8      |       8 */    struct author *author;
/*     16      |       8 */    struct text *text;

                               /* total size (bytes):   24 */
                             } *
```




