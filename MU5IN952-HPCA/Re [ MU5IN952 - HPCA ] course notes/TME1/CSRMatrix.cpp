#include "CSRMatrix.h"
#include <cstdlib>
#include <ctime>

CSRMatrix::CSRMatrix(int r, int c) : rows(r), cols(c) {
    row_offsets.resize(rows + 1, 0);
}

void CSRMatrix::generateRandom(double density) {
    srand(static_cast<unsigned>(time(0))); 
    row_offsets[0] = 0;

    for (int i = 0; i < rows; ++i) {
        for (int j = 0; j < cols; ++j) {
            if ((rand() / (double)RAND_MAX) < density) {
                values.push_back(int((rand() % 100) / 10.0 + 1)); 
                col_indices.push_back(j);
            }
        }
        row_offsets[i + 1] = values.size(); 
    }
}

std::vector<double> CSRMatrix::multiply(const std::vector<double>& x) {
    if (x.size() != cols) {
        throw std::invalid_argument("Vector size does not match matrix columns");
    }

    std::vector<double> result(rows, 0.0);

    for (int i = 0; i < rows; ++i) {
        for (int j = row_offsets[i]; j < row_offsets[i + 1]; ++j) {
            result[i] += values[j] * x[col_indices[j]];
        }
    }

    return result;
}

std::vector<double> CSRMatrix::multiply_mpi(const std::vector<double>& x, int world_rank, int world_size) const {
    if (x.size() != cols) {
        throw std::invalid_argument("Vector size does not match matrix columns");
    }

    // 确定每个进程分配的行数
    int local_rows = rows / world_size;
    int start_row = world_rank * local_rows;
    int end_row = (world_rank == world_size - 1) ? rows : start_row + local_rows;

    // 提取当前进程的子矩阵
    CSRMatrix localMatrix = this->getSubMatrix(start_row, end_row);

    // 广播向量 x 给所有进程
    std::vector<double> x_global = x;
    MPI_Bcast(x_global.data(), cols, MPI_DOUBLE, 0, MPI_COMM_WORLD);
    std::vector<double> y_local = localMatrix.multiply(x_global);

    std::vector<double> y_global(rows, 0.0);
    MPI_Gather(y_local.data(), local_rows, MPI_DOUBLE, y_global.data(), local_rows, MPI_DOUBLE, 0, MPI_COMM_WORLD);

    return y_global;
}

void CSRMatrix::print() {
    std::cout << "CSR Matrix Representation:" << std::endl;
    std::cout << "Values: ";
    for (double v : values) std::cout << std::fixed << std::setprecision(1) << v << " ";
    std::cout << "\nColumn Indices: ";
    for (int idx : col_indices) std::cout << idx << " ";
    std::cout << "\nRow Offsets: ";
    for (int offset : row_offsets) std::cout << offset << " ";
    std::cout << std::endl;
}

CSRMatrix CSRMatrix::getSubMatrix(int start_row, int end_row) const {
    if (start_row < 0 || end_row > rows || start_row >= end_row) {
        throw std::out_of_range("Invalid row range for submatrix");
    }

    // 子矩阵的行数
    int sub_rows = end_row - start_row;
    CSRMatrix subMatrix(sub_rows, cols);

    // 复制子矩阵数据
    subMatrix.row_offsets[0] = 0;
    for (int i = start_row; i < end_row; ++i) {
        int row_start = row_offsets[i];
        int row_end = row_offsets[i + 1];
        for (int j = row_start; j < row_end; ++j) {
            subMatrix.values.push_back(values[j]);
            subMatrix.col_indices.push_back(col_indices[j]);
        }
        subMatrix.row_offsets[i - start_row + 1] = subMatrix.values.size();
    }

    return subMatrix;
}