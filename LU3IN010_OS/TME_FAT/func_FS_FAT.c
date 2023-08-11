#include "fat.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>

#define MAX_NAME_LENGTH 256
/*
  reponse pour question 1:
  Taille maximale d'un fichier :
	Un fichier de taille maximale prend toutes les entrées de la table FAT sauf la dernière qui est -1 : NB_ENT_FAT - 1 = 127 entrée au total. 
	Chaque entrée représente SIZE_SECTOR = 128 octets, donc 127*128 = 16 256 octets.
*/


int file_found (char * file ) {
  int i;
  struct ent_dir * pt = pt_DIR;

  for (i=0; i< NB_DIR; i++) {
    if ((pt->del_flag) && (!strcmp (pt->name, file))) 
      return 0;
    pt++;
  }
  /* finchier n'existe pas */
  return 1;
}


void list_fat () {
  int i;
  short *pt = pt_FAT;
  for (i=0; i < NB_ENT_FAT; i++) {
    if (*pt)
      printf ("%d ",i);
    pt++;
  }
  printf ("\n");
}


void list_dir ( ) {
  unsigned int cpt = 0;
  struct ent_dir * ptr = NULL;

  for (ptr = pt_DIR; ptr < pt_DIR + NB_DIR; ptr++) {
    if (ptr->del_flag) {
      short currentBlock = ptr->first_bloc;

      printf("-\e[0;32m%s\e[0m %4i \e[0;90m-", ptr->name, ptr->size);

      while (currentBlock != FIN_FICHIER) {
        printf(" %i", currentBlock);
        currentBlock = *(pt_FAT + currentBlock);
      }
      printf("\e[0m\n");

      cpt++;
    }
  }
  printf("total %d\n", cpt);
}

int cat_file (char* file) {
  struct ent_dir * ptr = NULL;

  for (ptr = pt_DIR; ptr < pt_DIR + NB_DIR; ptr++) {
    if ((ptr->del_flag) && !strcmp (ptr->name, file)) {
      short currentBlock = ptr->first_bloc;
      short size_left = ptr->size;
      while (currentBlock != FIN_FICHIER && size_left > 0) {
        char line[size_left > 128 ? 128 : size_left];
        size_left -= 128;
        
        if (read_sector(currentBlock, line) == -1)
          return -1;

        printf("%s", line);
        currentBlock = *(pt_FAT + currentBlock);
      } 
      printf("\n");
      return 0;
    }
  }
  return -1;
}

int mv_file (char*file1, char *file2) {
  struct ent_dir * ptr = NULL;

  for (ptr = pt_DIR; ptr < pt_DIR + NB_DIR; ptr++) {
    if ((ptr->del_flag) && !strcmp (ptr->name, file1)) {
      strcpy(ptr->name, file2);
      return write_DIR_FAT_sectors();
    }
  }

  return -1;
}

int delete_file (char* file) {
  struct ent_dir * ptr = NULL;
  for (ptr = pt_DIR; ptr < pt_DIR + NB_DIR; ptr++) {
    if ((ptr->del_flag) && !strcmp (ptr->name, file)) {
      short currentBlock = ptr->first_bloc;

      while (currentBlock != FIN_FICHIER) {
        short temp = currentBlock;
        currentBlock = *(pt_FAT + currentBlock);
        *(pt_FAT + temp) = 0;
      }
      ptr->del_flag = 0;
      return write_DIR_FAT_sectors();
    }
  }
  return -1;
}

int create_file (char *file) {
  struct ent_dir * ptr = NULL;
  if (!file_found(file))
    return -1;

  for (ptr = pt_DIR; ptr < pt_DIR + NB_DIR; ptr++) {
    if (ptr->del_flag == 0) {
      ptr->del_flag = 1;
      strcpy(ptr->name, file);
      ptr->size = 0;
      ptr->first_bloc = ptr->last_bloc = FIN_FICHIER;
      return write_DIR_FAT_sectors();
    }
  }

  return -1;
}


short alloc_bloc () { 
  short *ptr = NULL;

  for (ptr = pt_FAT; ptr < pt_FAT + NB_ENT_FAT; ptr++) {
    if (*ptr == 0) {
      *ptr = FIN_FICHIER;
      return ptr - pt_FAT;
    }
  }
  return -1;
}

int append_file (char*file, char *buffer, short size) {
  struct ent_dir * ptr = NULL;

  for (ptr = pt_DIR; ptr < pt_DIR + NB_DIR; ptr++) {
    if ((ptr->del_flag) && !strcmp (ptr->name, file)) {
      
      if (ptr->first_bloc == FIN_FICHIER) {
        ptr->first_bloc = ptr->last_bloc = alloc_bloc();
      }

      short nb_blocs = size / SIZE_SECTOR;
      short currentBlock = -1;
      int i;

      for (i = 0; i < nb_blocs; i++) {
        currentBlock = ptr->last_bloc;
        ptr->last_bloc = alloc_bloc();
        *(pt_FAT + currentBlock) = ptr->last_bloc;

        ptr->size += SIZE_SECTOR;
        write_sector(currentBlock, buffer + i*SIZE_SECTOR);
      }
      return write_DIR_FAT_sectors();
    }
  }
  return -1;
}

struct ent_dir*  read_dir (struct ent_dir *pt_ent ) {
  return NULL;
}