MPICXX = mpic++

MPI_CXXFLAGS = -std=c++11 -Wall -O2
TARGET = sparse_mpi
SRC = main.cpp CSRMatrix.cpp 

OBJ = $(SRC:.cpp=.o)
all: $(TARGET)

$(TARGET): $(OBJ)
	$(MPICXX) $(MPI_CXXFLAGS) -o $@ $^

%.o: %.cpp
	$(MPICXX) $(MPI_CXXFLAGS) -c $< -o $@

clean:
	rm -f $(OBJ) $(TARGET)

run:
	mpirun -np 4 ./$(TARGET)
