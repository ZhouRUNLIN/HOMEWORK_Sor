OBJ := obj/
BIN := bin/

GCC-FVERSION := $(shell gcc --version | head -n 1)
GCC-VREGEX   := s/^(.*\s+)?(\d+)\.\d+\.\d+(\s+.*)?$$/$$2/
GCC-VERSION  := $(shell echo '$(GCC-FVERSION)' | perl -wple '$(GCC-VREGEX)')
GCC-OLD      := $(shell test $(GCC-VERSION) -lt 7 && echo 1 || echo 0)


CC      := gcc

ifeq ($(GCC-OLD),1)
  CCFLAGS := -Wall -Wextra -O2 -g -mcmodel=large -Ixed -Iinclude/monitor  -fpie
  LDFLAGS := -Wl,-z,max-page-size=0x1000 -Wl,-Ttext-segment=0x10000 \
             -Wl,-Tbss=0x80000000 -Lxed -lxed
else
  CCFLAGS := -Wall -Wextra -O2 -g -Ixed -Iinclude/monitor  -fpie
  LDFLAGS := -no-pie -Wl,-z,max-page-size=0x1000 -Wl,-Ttext-segment=0x10000 \
             -Wl,-Tbss=0x80000000 -Lxed -lxed
endif


GUEST_AS      := gcc
GUEST_ASFLAGS := -Wall -Wextra -O2 -nostdlib -nodefaultlibs -Iinclude/guest
GUEST_CC      := gcc
GUEST_CCFLAGS := -Wall -Wextra -O2 -nostdlib -nodefaultlibs -fno-builtin \
                 -fno-stack-protector -Wno-implicit-fallthrough -mno-sse \
                 -mno-mmx -Iinclude/guest
GUEST_LD      := ld
GUEST_LDFLAGS := -z max-page-size=0x1000

monitor-objects := $(patsubst %, $(OBJ)monitor/%.o, \
  intercept memory monitor multiboot2 shadow state)

monitor-headers := $(wildcard include/monitor/*)

guest-objects := $(patsubst %, $(OBJ)guest/%.o, \
  entry idt main memory printk trap vga)

guest-headers := $(wildcard include/guest/*)


V ?= 1
ifneq ($(V),0)
  define cmd-print
    @echo '$(1)'
  endef
endif
ifneq ($(V),2)
  Q := @
  ISOPREFIX := !
  ISOSUFFIX :=  2>&1 | grep -E -v \
            -e 'xorriso .* libburnia project.' \
            -e '(Drive|Media) (current|status|summary) ?:' \
            -e 'xorriso : (UPDATE|NOTE)' \
            -e 'Added to ISO image:' \
            -e 'ISO image produced' \
            -e 'Writ(ten|ing) to' \
            -e '^[[:space:]]*$$'
endif


all: $(BIN)monitor $(BIN)guest.elf


check: $(BIN)monitor $(BIN)guest.elf
	$(call cmd-print,  EXEC    $<)
	$(Q)LD_LIBRARY_PATH=xed ./$^

qemu: $(BIN)guest.iso
	$(call cmd-print,  QEMU    $<)
	$(Q)qemu-system-x86_64 -smp 1 -m 4G \
            -drive file=$<,format=raw -monitor stdio


$(BIN)monitor: $(monitor-objects) | $(BIN)
	$(call cmd-print,  LD      $@)
	$(Q)$(CC) $^ -o $@ $(LDFLAGS)

$(BIN)guest.elf: guest/guest.ld $(guest-objects) | $(BIN)
	$(call cmd-print,  LD      $@)
	$(Q)$(GUEST_LD) -o $@ -T $< $(GUEST_LDFLAGS) $(filter %.o, $^)


$(BIN)guest.iso: $(OBJ)iso | $(BIN)
	$(call cmd-print,  MKISO   $@)
	$(Q)$(ISOPREFIX) grub2-mkrescue -d /usr/lib/grub/i386-pc -o $@ \
            --modules=multiboot2 $< $(ISOSUFFIX)

$(OBJ)iso: guest/grub.cfg $(BIN)guest.elf | $(OBJ)
	$(call cmd-print,  BUILD   $@)
	$(Q)rm -rf $@ 2> /dev/null || true
	$(Q)mkdir $@
	$(Q)mkdir $@/boot
	$(Q)mkdir $@/boot/grub
	$(Q)cp $< $@/boot/grub
	$(Q)cp $(filter %.elf, $^) $@/boot


$(OBJ)monitor/%.o: monitor/%.c $(monitor-headers) | $(OBJ)monitor
	$(call cmd-print,  CC      $@)
	$(Q)$(CC) $(CCFLAGS) -c $< -o $@


$(OBJ)guest/%.o: guest/%.c $(guest-headers) | $(OBJ)guest
	$(call cmd-print,  CC      $@)
	$(Q)$(GUEST_CC) $(GUEST_CCFLAGS) -c $< -o $@

$(OBJ)guest/%.o: guest/%.S $(guest-headers) | $(OBJ)guest
	$(call cmd-print,  AS      $@)
	$(Q)$(GUEST_AS) $(GUEST_ASFLAGS) -c $< -o $@

$(OBJ)guest/trap.S: guest/trap.pl | $(OBJ)guest
	$(call cmd-print,  GEN     $@)
	$(Q)./$< $@

$(OBJ)guest/trap.o: $(OBJ)guest/trap.S $(guest-headers) | $(OBJ)guest
	$(call cmd-print,  AS      $@)
	$(Q)$(GUEST_AS) $(GUEST_ASFLAGS) -c $< -o $@


$(OBJ) $(BIN):
	$(call cmd-print,  MKDIR   $@)
	$(Q)mkdir $@

$(OBJ)guest $(OBJ)monitor: | $(OBJ)
	$(call cmd-print,  MKDIR   $@)
	$(Q)mkdir $@

clean:
	$(call cmd-print,  CLEAN)
	$(Q)rm -rf $(OBJ) $(BIN) 2> /dev/null || true
