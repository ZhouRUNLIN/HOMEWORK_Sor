#include <stdlib.h>
#include <stdio.h>
#include "fat.h"
int main (int argc, char ** argv)
{
  
  open_FS();

  printf ("\n%s: \n", argv[1]);
  if (cat_file (argv[1])) {
    printf ("%s n'existe pas\n", argv[1]);
    list_dir ();
  }

  close_FS ();

  return EXIT_SUCCESS;  
}
