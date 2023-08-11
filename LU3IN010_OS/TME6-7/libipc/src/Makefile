# Nom du repertoire ou generer la libIPC
REP        = ../lib
 
# Nom du fichier a generer :
#                                                       <---
 
LIBRAIRIE = $(REP)/libIPC.a
 
# Fichiers C necessaires a la compilation :
#                                                       <---
CFILES    = shmem.c sem.c
 
#                                                       <-
#INCDIR    =  -I- 
 
# Fichiers objets :
#                                                       <
CFLAGS    = $(GFLAGS) $(INCDIR) -Wall 
OFILES    = $(CFILES:.c=.o)
CC        = gcc
 
$(LIBRAIRIE) : $(OFILES)
	@echo "Building $@ ... "
	ar r $@ $(OFILES); ranlib $@
	@echo "$@ done"
 
clean:;         rm -f $(OFILES) $(LIBRAIRIE) core
 
sem.o: sem.c
	$(CC) -c sem.c $(CFLAGS)
shmem.o: shmem.c
	$(CC) -c shmem.c $(CFLAGS)
 
 
