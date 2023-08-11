#include <stdlib.h>
#include <stdio.h>

#include "fat.h"
int main (int argc, char ** argv)
{
  
  open_FS();
  
  printf ("\nREPETOIRE AVANT :\n");
  list_dir ();
  printf ("FAT AVANT : ");
  list_fat ();
  
  printf ("\nEFFACER %s \n", argv[1]);
  if (delete_file (argv[1]) )
    printf ("%s n'existe pas \n", argv[1]);
  else
    { 
      printf ("\nREPETOIRE APRES :\n");
      list_dir ();
      printf ("FAT APRES : ");
      list_fat ();
    }
  
  close_FS ();

  return EXIT_SUCCESS;  
}
