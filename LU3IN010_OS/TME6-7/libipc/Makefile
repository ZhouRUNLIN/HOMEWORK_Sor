IPCPATH = ./lib 
CFLAGS = -Wall -I$(IPCPATH)

all: Tab1case TabNcases  

Tab1case: Tab1case.o
	gcc -o Tab1case Tab1case.o -L$(IPCPATH) -lIPC

Tab1case.o: Tab1case.c
	gcc -c  $(CFLAGS) Tab1case.c

TabNcase: TabNcase.o
	gcc -o TabNcase TabNcase.o  -L$(IPCPATH) -lIPC

TabNcase.o: TabNcase.c
	gcc -c  $(CFLAGS) TabNcase.c

clean:
	rm -f Tab1case Tab1case.o TabNcase TabNcase.o
