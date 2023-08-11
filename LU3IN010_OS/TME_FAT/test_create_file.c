#include <stdlib.h>
#include <stdio.h>

#include "fat.h"
int main (int argc, char ** argv)
{
  
  open_FS();
  
  printf ("\nREPETOIRE AVANT :\n");
  list_dir ();
   
  printf ("cree fichier %s\n", argv[1]);
  if (create_file (argv[1]))
    printf ("%s existe deja \n", argv[1]);
  else
    {
      printf ("\nREPETOIRE APRES :\n");
      list_dir ();
    }
  
  close_FS ();

  return EXIT_SUCCESS;  
}
