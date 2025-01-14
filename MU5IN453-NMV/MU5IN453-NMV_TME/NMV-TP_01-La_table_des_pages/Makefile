OBJ := obj/
BIN := bin/

AS      := gcc
ASFLAGS := -Wall -Wextra -O2 -nostdlib -nodefaultlibs
CC      := gcc
CCFLAGS := -Wall -Wextra -O2 -nostdlib -nodefaultlibs -fno-builtin \
           -fno-stack-protector -Wno-implicit-fallthrough -mno-sse -mno-mmx
LD      := ld
LDFLAGS := -z max-page-size=0x1000

MAKEFLAGS += --no-print-directory --no-builtin-rules --no-builtin-variables

V ?= 1

ifneq ($(V),2)
  Q         := @
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
ifeq ($(V),1)
  define cmd-print
    @echo '$(1)'
  endef
endif


kernel-obj := $(patsubst %, $(OBJ)kernel/%.o,   \
  entry idt main memory printk task trap vga    \
)

tasks := adversary hash sieve


all: $(BIN)rackdoll.elf

iso: $(BIN)rackdoll.iso

boot: qemu

qemu: $(BIN)rackdoll.iso
	$(call cmd-print,  BOOT    $<)
	$(Q)qemu-system-x86_64 -smp 1 -m 4G \
            -drive file=$<,format=raw -monitor stdio

bochs: $(BIN)rackdoll.iso
	$(call cmd-print,  BOOT    $<)
	$(Q)bochs -q 'boot:cdrom' \
            'ata0-master: type=cdrom,path=$<,status=inserted'


# VM rules ====================================================================

$(BIN)rackdoll.iso: $(OBJ)iso | $(BIN)
	$(call cmd-print,  MKISO   $@)
	$(Q)$(ISOPREFIX) grub2-mkrescue -d /usr/lib/grub/i386-pc -o $@ \
            --modules=multiboot2 $< $(ISOSUFFIX)

$(OBJ)iso: grub.cfg $(BIN)rackdoll.elf $(patsubst %, $(BIN)%.elf, $(tasks)) \
         | $(OBJ)
	$(call cmd-print,  BUILD   $@)
	$(Q)rm -rf $@ 2> /dev/null || true
	$(Q)mkdir $@
	$(Q)mkdir $@/boot
	$(Q)mkdir $@/boot/grub
	$(Q)cp $< $@/boot/grub
	$(Q)cp $(filter %.elf, $^) $@/boot


# Linkage rules ===============================================================

define cmd-ld
  $(call cmd-print,  LD      $(strip $(1)))
  $(Q)$(LD) -o $(1) -T $(2) $(LDFLAGS) $(3)
endef

$(BIN)rackdoll.elf: kernel.ld $(kernel-obj) | $(BIN)
	$(call cmd-ld, $@, $<, $(filter %.o, $^))



# Directory rules =============================================================

define cmd-mkdir
  $(call cmd-print,  MKDIR   $(strip $(1)))
  $(Q)mkdir $(1)
endef

$(BIN) $(OBJ):
	$(call cmd-mkdir, $@)

$(OBJ)kernel: | $(OBJ)
	$(call cmd-mkdir, $@)

$(OBJ)task: | $(OBJ)
	$(call cmd-mkdir, $@)


# Compilation rules ===========================================================

define cmd-gen
  $(call cmd-print,  GEN     $(strip $(1)))
  $(Q)./$(strip $(2)) $(1)
endef

define cmd-as
  $(call cmd-print,  AS      $(strip $(1)))
  $(Q)$(AS) $(ASFLAGS) -c $(2) -o $(1) -Iinclude
endef

define cmd-cc
  $(call cmd-print,  CC      $(strip $(1)))
  $(Q)$(CC) $(CCFLAGS) -c $(2) -o $(1) -Iinclude
endef

$(OBJ)kernel/trap.S: kernel/trap.pl | $(OBJ)kernel
	$(call cmd-gen, $@, $<)

$(OBJ)kernel/trap.o: $(OBJ)kernel/trap.S $(wildcard include/*.h) | $(OBJ)kernel
	$(call cmd-as, $@, $<)

$(OBJ)kernel/%.o: kernel/%.S $(wildcard include/*.h) | $(OBJ)kernel
	$(call cmd-as, $@, $<)

$(OBJ)kernel/%.o: kernel/%.c $(wildcard include/*.h) | $(OBJ)kernel
	$(call cmd-cc, $@, $<)


# Task rules ==================================================================

$(OBJ)task/%.o: task/%.c include/syscall.h | $(OBJ)task
	$(call cmd-cc, $@, $< -mcmodel=large)

$(BIN)%.elf: task.ld $(OBJ)task/%.o | $(BIN)
	$(call cmd-ld, $@, $<, $(filter %.o, $^))


# Clean rules =================================================================

clean:
	$(call cmd-print,  CLEAN)
	$(Q)rm -rf $(OBJ) $(BIN) 2> /dev/null || true


.SECONDARY:
