#include <stdlib.h>
#include <stdio.h>

#include "history.h"

/**
  * new_history - allocate, initialise and return a history
  *
  * @name: name of the history
  *
  * @return: a new history
  */
struct history *new_history(char *name)
{
	struct history *h = malloc(sizeof(struct history));
  h->name = name;
  h->commit_list = new_commit(0, 0, "DO NOT PRINT ME!!!");
  h->commit_count = 0;
  h->commit_list->next = h->commit_list;
  h->commit_list->prev = h->commit_list;
  return h;
}

/**
  * last_commit - returns the last commit in the history
  *
  * @h: pointer to a history
  *
  * @return: the last commit in h
  */
struct commit *last_commit(struct history *h)
{
	return h->commit_list->prev;
}

/**
  * display_history - display the history, i.e., all the commits in the history
  *
  * @h: pointer to the history to display
  */
void display_history(struct history *h)
{
  struct commit *first = h->commit_list;
  struct commit *current = first->next;
  printf("History of %s\n", h->name);
  while(current != first){
    display_commit(current);
    current = current->next;
  }
  printf("\n");

}

/**
  * infos - display the commit matching the major and minor numbers in history
  *         if it exists, "Not here!!!" otherwise
  *
  * @h: history to search
  * @major: major version of the commit to display
  * @minor: minor version of the commit to display
  */
void infos(struct history *h, int major, unsigned long minor)
{
  struct commit *first = h->commit_list;
  struct commit *current = first->next;
  while(current != first){
    if(!cmp_version(&current->version, major, minor)){
      current = current->next;
    }
    else{
      display_commit(current);
      return;
    }
  }
  printf("Not here!!!\n");
}


void del_commit(struct commit *victim)
{
  victim->prev->next = victim->next;
  victim->next->prev = victim->prev;
  //free(victim->comment);
  free(victim);
}