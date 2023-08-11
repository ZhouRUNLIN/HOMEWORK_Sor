
/* nombre d'entree dans le repertoire */
#define NB_DIR 16

/* taille d'un  bloc (secteur) */
#define SIZE_SECTOR 128

/* nombre de secteur occupé par la FAT */
#define NB_SECTOR_FAT 2

/* nombre d'entree de FAT par bloc */
#define NB_FAT_ENT_PER_SECTOR 64

/* nombre total d'entree de la FAT */
#define NB_ENT_FAT  NB_SECTOR_FAT*NB_FAT_ENT_PER_SECTOR

/* taille en octet de la FAT */
#define SIZE_FAT NB_FAT_ENT_PER_SECTOR*NB_SECTOR_FAT*sizeof(short)

/* taille en octet du repertoire */
#define SIZE_DIR  NB_DIR*sizeof(struct ent_dir)

/*FIN FICHIER : utilisé dans la FAT */ 
#define FIN_FICHIER -1

#define DISC "disque_image"


/* pointeur debut de la FAT */
extern short* pt_FAT; 

/* pointeur debut du repertoire*/
extern struct ent_dir* pt_DIR;


/* entree d'un repertoire */
struct ent_dir{
  char del_flag; /* 0: entree libre ; 1 : entree occupe */
  char name[9]; /* nom du fichier : 8 caracteres + 0 */
  short first_bloc; /* premier bloc du fichier */
  short last_bloc; /* dernier bloc du fichier */
  short size; /* taille */
};

void open_FS ( ); 
void close_FS ( );
int read_sector (short, char*);
int write_sector (short, char* ); 
int write_DIR_FAT_sectors ( );
void list_fat ();
int file_found (char* );
void list_dir ( );
int cat_file (char*);
int mv_file (char*, char*);
int delete_file (char*);
int create_file (char*);
short alloc_bloc (); 
int append_file (char*, char *, short);
struct ent_dir * read_dir (struct ent_dir*);
