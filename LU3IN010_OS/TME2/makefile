loopcpu: 
	gcc -c loopcpu.c
	gcc loopcpu.o -o loopcpu

loopsys: 
	gcc -c loopsys.c
	gcc loopsys.o -o loopsys

mytimes: mytimes.o lanceCommande.o
	gcc mytimes.o lanceCommande.o -o mytimes

mytimes2: mytimes2.o lanceCommande.o
	gcc mytimes2.o lanceCommande.o -o mytimes2

lanceCommande.o: lanceCommande.h lanceCommande.c
	gcc -c lanceCommande.c -o lanceCommande.o

mytimes.o: mytimes.c lanceCommande.h
	gcc -c mytimes.c -o mytimes.o

mytimes2.o: mytimes2.c lanceCommande.h
	gcc -c mytimes2.c -o mytimes2.o
clean:
	rm -f *.o loopcpu loopsys mytimes mytimes2