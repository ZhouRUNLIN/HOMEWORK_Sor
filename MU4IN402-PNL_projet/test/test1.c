#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <string.h>
#include <sys/ioctl.h>

#include "command.h"

#define FILE_SIZE 1024
#define DATA_SIZE 256

static char buf[1024]={0};

int copy_file(int fd, char *path) 
{
    clock_t start, end;
    lseek(fd, 0, SEEK_SET);
    int file = open(path, O_CREAT | O_WRONLY, 0600);
    if (file < 0) {
        perror("Error opening file");
        return 1;
    }
    start = clock();
    char data[DATA_SIZE];
    int read_bytes;
    while ((read_bytes = read(fd, data, sizeof(data))) > 0) {
        write(file, data, read_bytes);
    }
    end = clock();
    close(file);
    printf("Copy time: %f at %s\n", ((double) (end - start)) / CLOCKS_PER_SEC, path);
    return 0;
}


int insert_file(char* message, char *path, int offset) 
{   
    struct stat st;
    if (stat(path, &st) < 0) {
        perror("Error getting file size");
        return 1;
    }
    printf("File size before insertion: %ld\n", st.st_size);
    if ((off_t)offset > st.st_size) {
        perror("Offset is greater than file size");
        return 1;
    }
    clock_t start, end;
    int file_w = open(path, O_RDWR, 0600);
    if ((file_w) < 0) {
        perror("Error opening file");
        return 1;
    }
    start = clock();
    lseek(file_w, offset, SEEK_SET);
    if(write(file_w, message, strlen(message)) < 0) {
        perror("Error writing file 1");
        return 1;
    }
    end = clock();
    printf("Insert time : %f at %s \n", ((double) (end - start)) / CLOCKS_PER_SEC, path);
    close(file_w);
    if (stat(path, &st) < 0) {
        perror("Error getting file size");
        return 1;
    }
    printf("File size after insertion: %ld\n", st.st_size);
    return 0;
}

int read_file(char *path) 
{
    int fd = open(path, O_RDONLY);
    clock_t start, end;
    start = clock();
    lseek(fd, 0, SEEK_SET);
    char data[DATA_SIZE];
    int read_bytes;
    while ((read_bytes = read(fd, data, sizeof(data))) > 0);
    end = clock();
    printf("Read time: %f at %s\n", ((double) (end - start)) / CLOCKS_PER_SEC, path);
    close(fd);
    return 0;
}

int main() 
{
    int file = open("./test.txt", O_RDONLY);
    if (file < 0) {
        perror("Error opening file");
        return 1;
    }
    copy_file(file, "/mnt/ouichefs/file1");
    int file1_before  = open("/mnt/ouichefs/file1", O_RDONLY);
    if (file1_before < 0) {
        perror("Error opening file");
        return 1;
    }
    if(ioctl(file1_before, LIST_USED_BLOCKS, buf)==-1)
        perror("Failed to system call ioctl in LIST_USED request\n");
    printf("resultat of ioctl LIST_USED_BLOCKS: %s\n", buf);
    

    int file1  = open("/mnt/ouichefs/file1", O_RDONLY);
    if (file1 < 0) {
        perror("Error opening file");
        return 1;
    }

    copy_file(file, "./copy.txt");
    if(insert_file("Hello World", "/mnt/ouichefs/file1", 10)){
        perror("Failed to insert file");
        return 1;
    }
    if(insert_file("blablablabla", "/mnt/ouichefs/file1", 4098)){
        perror("Failed to insert file");
        return 1;
    }

    if(insert_file("random_message", "/mnt/ouichefs/file1", 5000)){
        perror("Failed to insert file");
        return 1;
    }
    if(insert_file("Hello World", "./copy.txt", 10)){
        perror("Failed to insert file local");
        return 1;
    }

    read_file("/mnt/ouichefs/file1");
    read_file("./copy.txt");


    // ioctl tests
    memset(buf, 0, 1024);
    printf("before fragmentation\n");
    if(ioctl(file1, USED_BLOCKS, buf)==-1)
        perror("Failed to system call ioctl in USED_BLOCKS request\n");
    printf("resultat of ioctl USED_BLOCKS: %s\n", buf);
    if(ioctl(file1, PARTIALLY_BLOCKS, buf)==-1)
        perror("Failed to system call ioctl in PARTIALLY_BLOCKS request\n");
    printf("resultat of ioctl PARTIALLY_BLOCKS: %s\n", buf);
    if(ioctl(file1, WASTED_BYTES, buf)==-1)
        perror("Failed to system call ioctl in  WASTED_BYTES request\n");
    printf("resultat of ioctl WASTED_BYTES: %s\n", buf);
    if(ioctl(file1, LIST_USED_BLOCKS, buf)==-1)
        perror("Failed to system call ioctl in LIST_USED request\n");
    printf("resultat of ioctl LIST_USED_BLOCKS: %s\n", buf);

    printf("\nafter fragmentation\n");
    if(ioctl(file1, DEFRAGEMENTATION, buf)==-1)
        perror("Failed to system call ioctl in DEFRAGEMENTATION request\n");
    if(ioctl(file1, USED_BLOCKS, buf)==-1)
        perror("Failed to system call ioctl in USED_BLOCKS request\n");
    printf("resultat of ioctl USED_BLOCKS: %s\n", buf);
    if(ioctl(file1, PARTIALLY_BLOCKS, buf)==-1)
        perror("Failed to system call ioctl in PARTIALLY_BLOCKS request\n");
    printf("resultat of ioctl PARTIALLY_BLOCKS: %s\n", buf);
    if(ioctl(file1, WASTED_BYTES, buf)==-1)
        perror("Failed to system call ioctl in  WASTED_BYTES request\n");
    printf("resultat of ioctl WASTED_BYTES: %s\n", buf);
    if(ioctl(file1, LIST_USED_BLOCKS, buf)==-1)
        perror("Failed to system call ioctl in LIST_USED request\n");
    printf("resultat of ioctl LIST_USED_BLOCKS: %s\n", buf);
    
    close(file);
    close(file1);
    close(file1_before);
    return 0;
}