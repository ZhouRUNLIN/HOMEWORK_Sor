CC = gcc
CFLAGS = -Wall -Wextra -std=c99

# tagert fils 
TEST_TARGET = test # un petit exemple du prog
COMPARE = compare  # produit les donnees pour presenter la resultat

# sources
SRC_DIR = src
OBJ_DIR = obj
BIN_DIR = bin
TEST_TARGET = test
COMPARE = compare

# define les fichers source et les fichers obj
SRC_TEST = $(wildcard $(SRC_DIR)/*.c) $(OBJ_DIR)/test.o
OBJ_TESTS = $(patsubst $(SRC_DIR)/%.c,$(OBJ_DIR)/%.o,$(SRC_TEST))

SRC_COMAPRE = $(wildcard $(SRC_DIR)/*.c) $(OBJ_DIR)/comparison.o
OBJ_COMPARE = $(patsubst $(SRC_DIR)/%.c,$(OBJ_DIR)/%.o,$(SRC_COMAPRE))

# linkage 
$(TEST_TARGET): $(OBJ_TESTS)
	$(CC) $(CFLAGS) -o $(BIN_DIR)/$(TEST_TARGET) $(OBJ_TESTS)

$(COMPARE): $(OBJ_COMPARE)
	$(CC) $(CFLAGS) -o $(BIN_DIR)/$(COMPARE) $(OBJ_COMPARE)

# compile les fichers source
$(OBJ_DIR)/%.o: $(SRC_DIR)/%.c
	$(CC) $(CFLAGS) -c $^ -o $@

$(OBJ_DIR)/%.o : %.c
	$(CC) $(CFLAGS) -c $^ -o $@

clean:
	rm -f $(OBJ_DIR)/*.o bin/test bin/compare

# verifier la fuite de mémoire
leak: 
	leaks -atExit -- ./$(BIN_DIR)/TEST_TARGET
