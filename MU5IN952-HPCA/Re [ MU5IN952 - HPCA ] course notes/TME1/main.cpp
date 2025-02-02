#include "CSRMatrix.h"
#include "utils.h"

#include <ctime>
#include <chrono>

#define ROWS 6
#define COLS 5
#define DENS 0.1

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int world_rank, world_size;
    MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);

    CSRMatrix matrix(ROWS, COLS);
    std::vector<double> x(COLS), y_normal, y_mpi;

    if (world_rank == 0) {
        matrix.generateRandom(DENS);
        for (int i = 0; i < COLS; ++i) {
            x[i] = (rand() % 100) / 10.0;
        }

        std::cout << "Generated Sparse Matrix:" << std::endl;
        // matrix.print();

        // 使用普通方法计算
        auto start = std::chrono::high_resolution_clock::now();
        y_normal = matrix.multiply(x);
        auto end = std::chrono::high_resolution_clock::now();
        std::chrono::duration<double> elapsed = end - start;
        std::cout << "Normal Method Time: " << elapsed.count() << " seconds" << std::endl;

        // 打印结果
        // std::cout << "Result from Normal Method:" << std::endl;
        // for (double val : y_normal) std::cout << val << " ";
        // std::cout << std::endl;
        
        // std::vector<double> x1 = utils::generateRandomVector(COLS);
        // std::vector<double> x2 = utils::generateRandomVector(COLS);
        // std::cout << "Normal is : " << utils::verify_results(x1, x2, matrix, 5) << std::endl;
    }

    // 使用 MPI 方法计算
    MPI_Barrier(MPI_COMM_WORLD); // 确保所有进程在相同起点
    auto start_mpi = std::chrono::high_resolution_clock::now();
    y_mpi = matrix.multiply_mpi(x, world_rank, world_size);
    auto end_mpi = std::chrono::high_resolution_clock::now();

    if (world_rank == 0) {
        std::chrono::duration<double> elapsed_mpi = end_mpi - start_mpi;
        std::cout << "MPI Method Time: " << elapsed_mpi.count() << " seconds" << std::endl;

        // 打印结果
        // std::cout << "Result from MPI Method:" << std::endl;
        // for (double val : y_mpi) std::cout << val << " ";
        // std::cout << std::endl;

        utils::verify(y_normal, y_mpi);
    }

    MPI_Finalize();
    return 0;
}
