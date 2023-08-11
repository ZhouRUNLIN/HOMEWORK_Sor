MKLIB=ar rcs

SRC=src
INC=include
OBJ=obj
LIB=lib
BIN=bin
DEMO=algorithms
CC=gcc
CFLAGS=-D_DEBUG_  # Commenter cette ligne pour desactiver les affichages 
STRATEGIES=LFU. Random.  # Liste des strategies

all:libmem main

libmem:libmem. Swapper.
	$(MKLIB) $(LIB)/$@.a $(OBJ)/*.o

libmem. Swapper.:
	$(CC) $(CFLAGS) -c $(SRC)/$@c -I$(INC) -o $(OBJ)/$@o
	
algorithm:$(STRATEGIES)

%.:
	$(CC) $(CFLAGS) -c $(DEMO)/$@c -I$(INC) -o $(OBJ)/$@o

main:libmem algorithm
	$(CC) $(CFLAGS) -I$(INC) -L$(LIB) $(DEMO)/$@.c $(OBJ)/*.o -o $(BIN)/$@ 

clean:
	rm -f {.,$(SRC),$(INC),$(DEMO)}/*~ {$(OBJ),$(BIN),$(LIB)}/*
