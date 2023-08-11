# Mettre dans SCHEDPATH le repertoire ou se trouve libsched.a
# et sched.h

CFLAGS = -Wall -g 
EXEC = test_dir test_list_FAT test_cat test_mv_file test_del_file test_create_file test_append_file  test_append2_file  test_read_dir

all: $(EXEC)

fat.o: fat.c
	gcc $(CFLAGS) -c fat.c
test_dir: test_dir.o func_FS_FAT.o fat.o
	gcc $(CFLAGS) -o test_dir test_dir.o func_FS_FAT.o fat.o 

test_list_FAT: test_list_FAT.o func_FS_FAT.o fat.o
	gcc $(CFLAGS) -o test_list_FAT test_list_FAT.o func_FS_FAT.o fat.o 

test_cat: test_cat.o func_FS_FAT.o fat.o
	gcc $(CFLAGS) -o test_cat test_cat.o func_FS_FAT.o fat.o 

test_del_file: test_del_file.o func_FS_FAT.o fat.o 
	gcc $(CFLAGS) -o test_del_file test_del_file.o func_FS_FAT.o fat.o 

test_mv_file: test_mv_file.o func_FS_FAT.o fat.o
	gcc $(CFLAGS) -o test_mv_file test_mv_file.o func_FS_FAT.o fat.o 

test_create_file: test_create_file.o func_FS_FAT.o fat.o
	gcc $(CFLAGS) -o test_create_file test_create_file.o func_FS_FAT.o fat.o 

test_append_file: test_append_file.o func_FS_FAT.o fat.o
	gcc $(CFLAGS) -o test_append_file test_append_file.o func_FS_FAT.o fat.o 

test_append2_file: test_append2_file.o func_FS_FAT.o fat.o
	gcc $(CFLAGS) -o test_append2_file test_append2_file.o func_FS_FAT.o fat.o 

test_read_dir: test_read_dir.o func_FS_FAT.o fat.o
	gcc $(CFLAGS) -o test_read_dir test_read_dir.o func_FS_FAT.o fat.o 

test_dir.o: test_dir.c fat.h 
	gcc $(CFLAGS) -c test_dir.c

test_list_FAT.o: test_list_FAT.c fat.h
	gcc $(CFLAGS) -c test_list_FAT.c

test_cat.o: test_cat.c fat.h
	gcc $(CFLAGS) -c test_cat.c

test_mv_file.o: test_mv_file.c fat.h
	gcc $(CFLAGS) -c test_mv_file.c

test_del_file.o: test_del_file.c fat.h
	gcc $(CFLAGS) -c test_del_file.c

test_create_file.o: test_create_file.c fat.h
	gcc $(CFLAGS) -c test_create_file.c

test_append_file.o: test_append_file.c fat.h
	gcc $(CFLAGS) -c test_append_file.c

test_append2_file.o: test_append2_file.c fat.h
	gcc $(CFLAGS) -c test_append2_file.c

test_read_dir.o: test_read_dir.c fat.h
	gcc $(CFLAGS) -c test_read_dir.c

func_FS_FAT.o: func_FS_FAT.c fat.h
	gcc $(CFLAGS) -c func_FS_FAT.c

clean: 
	rm -f $(EXEC) fat.o test_*.o func_FS_FAT.o
