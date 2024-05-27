obj-m += ouichefs.o
ouichefs-objs := fs.o super.o inode.o file.o dir.o

KERNELDIR ?= $(HOME)/pnl_workplace/linux-6.5.7
SHARELDIR ?= $(HOME)/pnl_workplace/share

all:
	make -C $(KERNELDIR) M=$(PWD) modules

debug:
	make -C $(KERNELDIR) M=$(PWD) ccflags-y+="-DDEBUG -g" modules

clean:
	make -C $(KERNELDIR) M=$(PWD) clean
	rm -rf *~

upload:
	cp ouichefs.ko $(SHARELDIR)

.PHONY: all clean
