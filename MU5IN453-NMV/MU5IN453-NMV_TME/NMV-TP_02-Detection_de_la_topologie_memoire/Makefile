DAT := dat/
BIN := bin/
PDF := pdf/

CC      := gcc
CCFLAGS := -Wall -Wextra -pedantic -O2
LDFLAGS := -lpthread

experiments := cacheline cachesize cacheway siblings
cacheline-param := 8 12 16 32 48 64 80 96 128
cachesize-param := 8 12 16 32 48 64 80 96 128
cacheway-param  := 1 2  4 8 16 32 64
siblings-param  := 1


all:

view:
	evince $^


define EXPERIMENT_PATTERN

  exe: $$(BIN)$(strip $(1))-$(strip $(2))

  $$(BIN)$(strip $(1))-$(strip $(2)): $(1).c $$(wildcard *.h) | $$(BIN)
	$$(CC) $$(CCFLAGS) -DPARAM=$(strip $(2)) $$< -o $$@ $$(LDFLAGS)

  $$(DAT)$(strip $(1)).dat: $$(BIN)$(strip $(1))-$(strip $(2)) | $$(DAT)

endef

$(foreach e, $(experiments), \
  $(foreach p, $($(e)-param), \
    $(eval $(call EXPERIMENT_PATTERN, $(e), $(p)))))


define DATA_PATTERN

  $$(DAT)$(strip $(1)).dat:
	for b in $$^ ; do echo "running $$$$b" >&2 ; ./$$$$b ; done > $$@

  $$(DAT)$(strip $(1)).sort.dat: $$(DAT)$(strip $(1)).dat | $$(DAT)
	sort -g $$< > $$@

  $$(PDF)$(strip $(1)).pdf: $(strip $(1)).sh $$(DAT)$(strip $(1)).sort.dat \
                          | $$(PDF)
	./$$^ $$@

  all: $$(PDF)$(strip $(1)).pdf

  view: $$(PDF)$(strip $(1)).pdf

  $(strip $(1)): $$(PDF)$(strip $(1)).pdf
	evince $$<

endef

$(foreach e, $(experiments), \
  $(eval $(call DATA_PATTERN, $(e))))



$(DAT) $(BIN) $(PDF):
	mkdir $@

clean:
	-rm -rf $(BIN) $(DAT) $(PDF)


.SECONDARY:
.NOTPARALLEL:
