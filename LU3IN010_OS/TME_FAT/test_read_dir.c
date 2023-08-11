#include <stdlib.h>
#include <stdio.h>
#include "fat.h"

int main (int argc, char ** argv)
{
  
  struct  ent_dir * pt = NULL;
  open_FS();
  
  pt=read_dir(pt);
  do {
    if (pt->del_flag)
      printf ("%s\n",pt->name);
     pt=read_dir(pt);
  } while (pt!= pt_DIR);   
  
  close_FS ();

  return EXIT_SUCCESS;  
}
