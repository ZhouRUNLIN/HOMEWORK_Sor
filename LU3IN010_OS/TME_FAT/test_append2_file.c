#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "fat.h"

int main (int argc, char ** argv)
{
  
   open_FS();

    printf ("\nREPETOIRE :\n");
    list_dir ();
  
     printf ("\nFICHIER AVANT :\n");
    cat_file (argv[1]);
  
    if (! append_file (argv[1],argv[2], strlen (argv[2]))) {
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
