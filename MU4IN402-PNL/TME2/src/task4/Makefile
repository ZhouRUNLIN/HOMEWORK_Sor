.PHONY : all clean check
.ONESHELL:

all : cron_func

%.o : %.c
	gcc -Wall -c $<

cron_func : cron_func.o nothing.o
	gcc -Wall $^ -o $@

clean :
	rm -rf *~ *.o cron_func
