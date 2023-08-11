LIBPATH=../
INC=$(LIBPATH)/include
CC=gcc
CFLAGS=-Wall -D_DEBUG_  # Commenter cette ligne pour desactiver les affichages 
STRATEGIES=LRU. Fifo.  # Liste des strategies

all: mainFifo mainLRU

	
algorithm:$(STRATEGIES)

%.:
	$(CC) $(CFLAGS) -c $@c -I$(INC) -o $@o

mainLRU: LRU.
	$(CC) $(CFLAGS) -I$(INC) $@.c *.o -o $@ -L$(LIBPATH)/lib -lmem
	
mainFifo: Fifo.
	$(CC) $(CFLAGS) -I$(INC) $@.c *.o -o $@ -L$(LIBPATH)/lib -lmem

clean:
	rm -f *~ *.o mainLRU mainFifo 
