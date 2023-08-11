m: main 

t:test

main: main.o biblioLC.o entreeSortieLC.o
	gcc main.o biblioLC.o entreeSortieLC.o -o main

test: test.o biblioLC.o biblioH.o entreeSortieLC.o
	gcc test.o biblioLC.o biblioH.o entreeSortieLC.o -o test

main.o: main.c biblioLC.h entreeSortieLC.h 
	gcc -c main.c -o main.o

test.o: test.c biblioLC.h entreeSortieLC.h
	gcc -c test.c -o test.o

biblioLC.o: biblioLC.c biblioLC.h
	gcc -c biblioLC.c -o biblioLC.o

biblioH.o: biblioH.c biblioH.h 
	gcc -c biblioH.c -o biblioH.o 

entreeSortieLC.o: entreeSortieLC.c entreeSortieLC.h
	gcc -c entreeSortieLC.c -o entreeSortieLC.o
	
clean:
	rm -f *.o main test

