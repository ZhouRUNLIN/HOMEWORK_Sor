# Chemin ou est present la libipc et libipc.h
IPCPATH = ../lib
CFLAGS = -Wall -I$(IPCPATH)

all: demo-ipc 

demo-ipc: demo-ipc.o
	gcc -o demo-ipc demo-ipc.o -L$(IPCPATH) -lIPC

demo-ipc.o: demo-ipc.c
	gcc -c  $(CFLAGS) demo-ipc.c
clean:
	rm -f demo-ipc demo-ipc.o
