#ifndef CSRMATRIX_H
#define CSRMATRIX_H

#include <vector>
#include <iostream>
#include <stdexcept>
#include <iomanip>
#include <mpi.h>

class CSRMatrix {
public:
    int rows, cols;                     // 矩阵的行数和列数
    std::vector<int> row_offsets;       // 行偏移数组
    std::vector<int> col_indices;       // 列索引数组
    std::vector<double> values;         // 非零值数组

    CSRMatrix(int r, int c);
    void generateRandom(double density);
    std::vector<double> multiply(const std::vector<double>& x);
    std::vector<double> multiply_mpi(const std::vector<double>& x, int world_rank, int world_size) const;
    void print();
    CSRMatrix getSubMatrix(int start_row, int end_row) const;
};

#endif // CSRMATRIX_H
