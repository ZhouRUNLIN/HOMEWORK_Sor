m: main 

mp5: mainPartie5

t:test

testp: testProofwork

main: main.o partie1.o partie2.o partie3.o partie4.o
	gcc main.o partie1.o partie2.o partie3.o partie4.o -o main

mainPartie5: mainPartie5.o partie1.o partie2.o partie3.o partie4.o partie5.o 
	gcc mainPartie5.o partie1.o partie2.o partie3.o partie4.o partie5.o -o mainPartie5 -lssl -lcrypto

test: test.o partie1.o partie2.o partie3.o partie4.o partie5.o
	gcc test.o partie1.o partie2.o partie3.o partie4.o partie5.o -o test -lssl -lcrypto

testProofwork: testProofwork.o partie1.o partie2.o partie3.o partie4.o partie5.o
	gcc testProofwork.o partie1.o partie2.o partie3.o partie4.o partie5.o -o testProofwork -lssl -lcrypto

main.o: main.c partie1.h partie2.h partie3.h partie4.h
	gcc -c main.c -o main.o

mainPartie5.o: mainPartie5.c partie1.h partie2.h partie3.h partie4.h partie5.h
	gcc -c mainPartie5.c -o mainPartie5.o

test.o: test.c partie1.h partie2.h partie3.h partie4.h partie5.h
	gcc -c test.c -o test.o

testProofwork.o: testProofwork.c partie1.h partie2.h partie3.h partie4.h partie5.h
	gcc -c testProofwork.c -o testProofwork.o

partie1.o: partie1.c partie1.h
	gcc -c partie1.c -o partie1.o

partie2.o: partie2.c partie2.h
	gcc -c partie2.c -o partie2.o

partie3.o: partie3.c partie3.h
	gcc -c partie3.c -o partie3.o

partie4.o: partie4.c partie4.h
	gcc -c partie4.c -o partie4.o

partie5.o: partie5.c partie5.h
	gcc -c partie5.c -o partie5.o

clean:
	rm -f *.o *.txt main test mainPartie5

