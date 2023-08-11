#include <stdlib.h>
#include <stdio.h>
#include "fat.h"
int main (int argc, char ** argv)
{
  
  open_FS();
  printf ("\nFAT avant :");
  list_fat ();
  printf ("bloc alloue : %d\n", alloc_bloc ());
   printf ("FAT apres :");
  list_fat ();

  printf ("\n");
  close_FS ();

  return EXIT_SUCCESS;  
}
