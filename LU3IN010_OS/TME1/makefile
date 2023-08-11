findLargestNum:
	gcc -c findLargestNum.c
	gcc findLargestNum.o -o findLargestNum

main: mainTab.o tab.o
	gcc mainTab.o tab.o -o mainTab

tab.o: tab.c tab.h
	gcc -c tab.c -o tab.o

mainTab.o: mainTab.c tab.h
	gcc -c mainTab.c -o mainTab.o

clean:
	rm -f *.o findLargestNum mainTab