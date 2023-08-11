
int* alloue_tableau(int n);

void desalloue_tableau(int *T);

void remplir_tableau(int *T, int V, int n);

void afficher_tableau(int*T, int n);

int algo1(int *T,int n);

int algo2(int *T, int n);

//exo2.4
int** alloue_matrice(int n);

void desalloue_matrice(int **mat, int n);

void remplir_matrice(int **mat, int V, int n);

void afficher_matrice(int **mat, int n);

//exo2.5
int mat_algo1(int **mat, int n);

int mat_algo2(int **mat, int n, int V);

//exo2.6
int** calculer_matrice_algo1(int **mat1, int **mat2, int n);

int** calculer_matrice_algo2(int **mat1, int **mat2, int n);