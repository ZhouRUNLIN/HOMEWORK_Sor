#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <time.h>
#include <math.h>
#include <unistd.h>

typedef int integer;
typedef double scalar;

struct matrix {
    integer n;
    integer m;
    integer nnz;
    integer *ia;
    integer *ja;
    scalar *val;
};

integer MatMult_seq(struct matrix *A, const scalar *x, scalar *y) {
    for (integer i = 0; i < A->n; ++i) {
        y[i] = 0;
        for (integer j = A->ia[i]; j < A->ia[i + 1]; ++j)
            y[i] += A->val[j] * x[A->ja[j]];
    }
    return 0;
}

integer MatMult_MPI(struct matrix *A, const scalar *x, scalar *y, MPI_Comm communicator) {
    scalar *global_input = calloc(A->m, sizeof(scalar));
    int rank;
    MPI_Comm_rank(communicator, &rank);
    integer first_row = rank * A->n;
    for (integer i = 0; i < A->n; ++i)
        global_input[i + first_row] = x[i];
    MPI_Allreduce(MPI_IN_PLACE, global_input, A->m, MPI_DOUBLE, MPI_SUM, communicator);
    MatMult_seq(A, global_input, y);
    free(global_input);
    return 0;
}

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);
    int size, rank;
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    integer n; // (local) number of rows
    integer N; // (global) number of rows
    integer m; // number of columns
    double density; // number of nonzero
    integer accumulate = 0, mean = 10;
    assert(argc >= 4);
    N = atoi(argv[1]);
    assert((N % size) == 0);
    n = N / size;
    m = atoi(argv[2]);
    if (size > 1)
        assert(m == N);
    density = atof(argv[3]);
    if (rank == 0)
        printf("We need to generate on each process a matrix of dimension %d x %d, with approximately %d nonzeros\n", n, m, (integer)(n * m * density));
    struct matrix *ptr;
    ptr = malloc(sizeof(*ptr));
    ptr->n = n;
    ptr->m = m;
    ptr->nnz = n * m * density;
    ptr->ia = calloc(n + 1, sizeof(integer));
    ptr->ja = malloc(ptr->nnz * sizeof(integer));
    ptr->val = malloc(ptr->nnz * sizeof(scalar));
    srand(time(NULL));
    for (integer i = 0; i < n; ++i) {
        integer nz_for_this_row = density * n;// m / 10 + rand() % (m / 100);
        ptr->ia[i + 1] = ptr->ia[i];
        for (integer j = 0; j < nz_for_this_row && accumulate < ptr->nnz; ++j) {
            integer col = rand() % m;
            integer insert = 1;
            for (integer k = ptr->ia[i]; k < ptr->ia[i + 1]; ++k) {
                if (ptr->ja[k] == col)
                    insert = 0;
            }
            if (insert) {
                ptr->ia[i + 1]++;
                ptr->ja[accumulate] = col;
                ptr->val[accumulate] = rand() / 1000000.0;
                accumulate++;
                if (accumulate >= ptr->nnz)
                    break;
            }
        }
    }
    integer first_row = rank * n;
    MPI_Barrier(MPI_COMM_WORLD);
    sleep(rank * 0.5);
    printf("Rows local to process #%d\n", rank);
#if 0
    for (integer i = 0; i < ptr->n; ++i) {
        for (integer j = ptr->ia[i]; j < ptr->ia[i + 1]; ++j)
            printf("(%d, %d) = %f\n", first_row + i, ptr->ja[j], ptr->val[j]);
    }
#else
    printf("%d -> %d\n", first_row, first_row + ptr->n);
#endif
    MPI_Barrier(MPI_COMM_WORLD);
    ptr->nnz = ptr->ia[n];
    // assert(accumulate == ptr->nnz);
    // assert(ptr->ia[n] == ptr->nnz);
    scalar *input_vec1 = malloc((ptr->m / size) * sizeof(scalar));
    scalar *input_vec2 = malloc((ptr->m / size) * sizeof(scalar));
    scalar *input_vec3 = malloc((ptr->m / size) * sizeof(scalar));
    scalar *output_vec1 = malloc(ptr->n * sizeof(scalar));
    scalar *output_vec2 = malloc(ptr->n * sizeof(scalar));
    scalar *output_vec3 = malloc(ptr->n * sizeof(scalar));
    for (integer i = 0; i < ptr->m / size; ++i) {
        input_vec1[i] = rand() / 100000.0;
        input_vec2[i] = rand() / 100000.0;
        input_vec3[i] = input_vec1[i] + input_vec2[i];
    }
    if (size == 1) {
        MatMult_seq(ptr, input_vec1, output_vec1);
        MatMult_seq(ptr, input_vec2, output_vec2);
        MatMult_seq(ptr, input_vec3, output_vec3);
        for (integer i = 0; i < ptr->n; ++i)
            output_vec2[i] += output_vec1[i];
        // check that output_vec2 == output_vec3 by computing the l_2 norm of the difference
        scalar norm[2] = {0.0, 0.0};
        for (integer i = 0; i < ptr->n; ++i) {
            norm[0] += (output_vec2[i] - output_vec3[i]) * (output_vec2[i] - output_vec3[i]);
            norm[1] += output_vec2[i] * output_vec2[i];
        }
        norm[0] = sqrt(norm[0]);
        norm[1] = sqrt(norm[1]);
        printf("In sequential: norm of the difference / norm of the input vector = %f / %f = %f\n", norm[0], norm[1], norm[0] / norm[1]);
    }
    else {
        MatMult_MPI(ptr, input_vec1, output_vec1, MPI_COMM_WORLD);
        MatMult_MPI(ptr, input_vec2, output_vec2, MPI_COMM_WORLD);
        MatMult_MPI(ptr, input_vec3, output_vec3, MPI_COMM_WORLD);
        for (integer i = 0; i < ptr->n; ++i)
            output_vec2[i] += output_vec1[i];
        // check that output_vec2 == output_vec3 by computing the l_2 norm of the difference
        scalar norm[2] = {0.0, 0.0};
        for (integer i = 0; i < ptr->n; ++i) {
            norm[0] += (output_vec2[i] - output_vec3[i]) * (output_vec2[i] - output_vec3[i]);
            norm[1] += output_vec2[i] * output_vec2[i];
        }
        MPI_Reduce(MPI_IN_PLACE, norm, 2, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);
        if (rank == 0) {
            norm[0] = sqrt(norm[0]);
            norm[1] = sqrt(norm[1]);
            printf("In parallel: norm of the difference / norm of the input vector = %f / %f = %f\n", norm[0], norm[1], norm[0] / norm[1]);
        }
    }
    free(input_vec1);
    free(output_vec1);
    free(input_vec2);
    free(output_vec2);
    free(input_vec3);
    free(output_vec3);
    free(ptr->ia);
    free(ptr->ja);
    free(ptr->val);
    free(ptr);
    MPI_Finalize();
    return 0;
}
