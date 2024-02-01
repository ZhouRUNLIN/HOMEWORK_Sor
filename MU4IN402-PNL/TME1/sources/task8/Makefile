.PHONY : all clean check
.ONESHELL:

CHECK_PATCH=./checkpatch.pl --no-tree

all : testComment

%.o : %.c
	gcc -g -Wall -c $<

testComment : testComment.o comment.o
	gcc -g -Wall $^ -o $@

check : 
	for f in *.c *.h ; do
		$(CHECK_PATCH) -f $$f
	done
	
memory :
	valgrind --leak-check=full ./testComment

clean :
	rm -rf *.o *~ testComment
