#include "fat.h"
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>

int desc_disc;
short* pt_FAT;
struct ent_dir* pt_DIR;

void open_FS ( )
{ int ret;

  /* ouvir le fichier disque_sym*/
 if ((desc_disc = open (DISC, O_RDWR)) == -1) {
    perror ("open FS");
    exit (1);
 }

 
    /* allouer d'espace pour le dirertoire en memoire */
 if ((pt_DIR = (struct ent_dir *) malloc (SIZE_DIR)) ==  NULL)  {
       perror ("malloc DIR");
       exit (1);
     }
 
 if ( (ret=read (desc_disc,pt_DIR,SIZE_DIR)) != SIZE_DIR){
   printf ("ret =%d\n",ret);
       perror ("read DIR");
       exit (1);
     }
 
 /* allouer espace pour la FAT en mémoire */
     if ((pt_FAT = (short*)malloc (SIZE_FAT)) ==  NULL) {
    perror ("malloc FAT");
    exit (1);
	}

  if (read (desc_disc, pt_FAT, SIZE_FAT) != SIZE_FAT){
    perror ("read FAT");
    exit (1);
  }
	}


void close_FS ( )
{  
  close (desc_disc);
  free (pt_FAT);
  free (pt_DIR);
}

int read_sector (short num_sect, char * buffer) {
  int ret;
  lseek (desc_disc, num_sect*SIZE_SECTOR,SEEK_SET);
  if (((ret =read (desc_disc, buffer, SIZE_SECTOR)) != SIZE_SECTOR) && (ret != EOF) )
    { 
      perror ("read SECTOR");
     return -1;
    }
  else
    return 0;
}

int write_sector (short num_sect, char * buffer) {
  lseek (desc_disc, num_sect*SIZE_SECTOR,SEEK_SET);
  if (write(desc_disc, buffer, SIZE_SECTOR) == -1)     
  { 
      perror ("write SECTOR");
     return -1;
    }
  else
    return 0;
}

int write_DIR_FAT_sectors ( ) {	
   lseek (desc_disc, 0,SEEK_SET);
   if (write(desc_disc, pt_DIR, 2*SIZE_SECTOR) == -1)     
   {  
      perror ("write DIR");
     return -1;
   
   }
   if (write(desc_disc, pt_FAT, 2*SIZE_SECTOR) == -1)     
   {  
      perror ("write FAT");
     return -1;
   
   }
      return 0; 
}
