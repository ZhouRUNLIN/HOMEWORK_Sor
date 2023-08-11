#include <stdlib.h>
#include "fat.h"
int main (int argc, char ** argv)
{
  
  open_FS();
  list_dir ();
  close_FS ();

  return EXIT_SUCCESS;  
}
