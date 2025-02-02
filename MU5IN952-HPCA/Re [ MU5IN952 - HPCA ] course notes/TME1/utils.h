#ifndef UTILS_H 
#define UTILS_H

#include "CSRMatrix.h"

#include <iomanip>

#define PRECISION 1e-6

namespace utils {
    double calculateNorm(const std::vector<double>& v1, const std::vector<double>& v2) {
        double norm = 0.0;
        for (size_t i = 0; i < v1.size(); ++i) {
            norm += (v1[i] - v2[i]) * (v1[i] - v2[i]);
        }
        return std::sqrt(norm);
    }

    void restoreMatrix(const std::vector<double>& values, const std::vector<int>& col_indices, const std::vector<int>& row_offsets, 
                    int rows, int cols) {
        std::vector< std::vector<double> > matrix(rows, std::vector<double>(cols, 0));

        for (int i = 0; i < rows; ++i) {
            for (int j = row_offsets[i]; j < row_offsets[i + 1]; ++j) {
                int col = col_indices[j];
                matrix[i][col] = values[j];
            }
        }

        std::cout << "Restored Matrix:" << std::endl;
        for (const auto& row : matrix) {
            for (double val : row) {
                std::cout << std::setw(5) << val << " ";
            }
            std::cout << std::endl;
        }
    }   

    std::vector<double> generateRandomVector(int size){
        std::vector<double> vector(size);
        for (int i = 0; i < size; ++i) {
            vector[i] = int((rand() % 100) / 10.0 + 1);
        }
        return vector;
    }

    bool verify_results(std::vector<double> x1, std::vector<double> x2, CSRMatrix matrix, int alpha) {
        std::vector<double> Ax1 = matrix.multiply(x1);
        std::vector<double> Ax2 = matrix.multiply(x2);

        // 计算 A(x1 + alpha * x2)
        std::vector<double> x_combined(matrix.cols);
        for (int i = 0; i < matrix.cols; ++i) {
            x_combined[i] = x1[i] + alpha * x2[i];
        }
        std::vector<double> A_combined = matrix.multiply(x_combined);

        // 计算 A * x1 + alpha * A * x2
        std::vector<double> result_combined(matrix.rows);
        for (int i = 0; i < matrix.rows; ++i) {
            result_combined[i] = Ax1[i] + alpha * Ax2[i];
        }

        // 比较误差 (A * x1 + alpha * A * x2) - A * (x1 + alpha * x2)
        double error = utils::calculateNorm(result_combined, A_combined);
        std::cout << "\nError between (A * x1 + alpha * A * x2) and A * (x1 + alpha * x2): " << error << std::endl;
        
        return error <= PRECISION;
    }

    void verify(const std::vector<double>& y1, const std::vector<double>& y2) {
        double norm = calculateNorm(y1, y2);
        if (norm < PRECISION) {
            std::cout << "Verification passed! Norm difference: " << norm << std::endl;
        } else {
            std::cout << "Verification failed. Norm difference: " << norm << std::endl;
        }
    }
} 

#endif // UTILS_H
