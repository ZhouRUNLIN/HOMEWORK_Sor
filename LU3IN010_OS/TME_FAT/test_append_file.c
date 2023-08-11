#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "fat.h"

int main (int argc, char ** argv)
{
  char data[2*SIZE_SECTOR];
  
   memset (data, '%',SIZE_SECTOR);
   memset (&data[SIZE_SECTOR], '$',SIZE_SECTOR);
  
   open_FS();

    printf ("\nREPETOIRE :\n");
    list_dir ();
    printf ("\nFICHIER AVANT :\n");
    cat_file (argv[1]);
  
    if (! append_file (argv[1],data, 2*SIZE_SECTOR)) {
	printf ("\nREPETOIRE :\n"); 
	list_dir ();
	printf ("\nFICHIER APRES :\n");
	cat_file (argv[1]);
	}
    else
      printf ("erreur append \n");

  close_FS ();

  return EXIT_SUCCESS;  
}
