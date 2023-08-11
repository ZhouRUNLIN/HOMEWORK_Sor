CFLAGS = -Wall -g

mymalloc : affiche_tas.o  mymalloc.o main.c
	gcc $(CFLAGS) -o mymalloc affiche_tas.o mymalloc.o main.c
   

mymalloc.o : mymalloc.h mymalloc.c
	gcc $(CFLAGS) -c mymalloc.c

affiche_tas.o : affiche_tas.h affiche_tas.c
	gcc $(CFLAGS) -c affiche_tas.c

clean:
	rm -f *.o mymalloc 

