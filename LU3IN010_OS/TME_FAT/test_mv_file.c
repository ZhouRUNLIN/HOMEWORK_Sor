#include <stdlib.h>
#include <stdio.h>

#include "fat.h"
int main (int argc, char ** argv)
{
  
  open_FS();
  
  printf ("\nREPETOIRE AVANT:\n");
  list_dir ();
  

  if (mv_file (argv[1], argv[2]))
    printf ("%s n'existe pas\n", argv[1]);
  else { 
    printf ("\nREPETOIRE APRES :\n");
    list_dir ();
  }

  close_FS ();

  return EXIT_SUCCESS;  
}
