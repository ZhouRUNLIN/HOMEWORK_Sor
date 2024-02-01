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
  INIT_LIST_HEAD(&h->commit_list->list);
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
	return list_last_entry(&h->commit_list->list, struct commit, list);
}

/**
  * display_history - display the history, i.e., all the commits in the history
  *
  * @h: pointer to the history to display
  */
void display_history(struct history *h)
{
  struct commit *first = list_first_entry(&h->commit_list->list, struct commit, list);
  printf("History of %s\n", h->name);
  list_for_each_entry(first, &h->commit_list->list, list){
    display_commit(first);
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
  struct commit *first = list_first_entry(&h->commit_list->list, struct commit, list);
  list_for_each_entry(first, &h->commit_list->list, list){
    if(first->version.major == major && first->version.minor == minor){
      display_commit(first);
      return;
    }
  }
  printf("Not here!!!\n");
}

/**
  * del_commit - delete a commit from the history
  *
  * @victim: commit to delete
  */
void del_commit(struct commit *victim)
{
  list_del(&victim->list);
  free(victim);
}
