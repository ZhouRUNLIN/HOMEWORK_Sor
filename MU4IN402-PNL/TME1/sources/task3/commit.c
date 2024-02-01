#include <stdlib.h>
#include <stdio.h>

#include "commit.h"

static int next_id;

/**
  * new_commit - allocate and initialise a commit
  *
  * @major: major version
  * @minor: minor version
  * @comment: pointer to a comment
  *
  * @return: a pointer to a newly allocated and initialised commit
  */
struct commit *new_commit(unsigned short major, unsigned long minor,
			  char *comment)
{
	struct commit *c = malloc(sizeof(struct commit));
  c->id = next_id++;
  c->version.major = major;
  c->version.minor = minor;
  c->comment = comment;
  c->next = NULL;
  c->prev = NULL;
  return c;
}

/**
  * insert_commit - insert a commit after another one without modifying them
  *
  * @from: commit that will be the predecessor of the new one
  * @new: commit to be inserted after from
  *
  * @return: a pointer to the newly inserted commit
  */
static struct commit *insert_commit(struct commit *from, struct commit *new)
{
	new->next = from->next;
  new->prev = from;
  from->next->prev = new;
  from->next = new;
  return new;
}

/**
  * add_minor_commit - create and insert a new minor commit
  *
  * @from: predecessor of the new minor commit
  * @comment: comment for the new commit
  *
  * @return: pointer to the new minor commit
  */
struct commit *add_minor_commit(struct commit *from, char *comment)
{
  struct commit *c = new_commit(from->version.major, from->version.minor + 1, comment);
  insert_commit(from, c);
  return c;
}

/**
  * add_major_commit - create and insert a new major commit
  *
  * @from: predecessor of the new major commit
  * @comment: comment for the new commit
  *
  * @return: pointer to the new major commit
  */
struct commit *add_major_commit(struct commit *from, char *comment)
{
  struct commit *c = new_commit(from->version.major + 1, 0, comment);
  insert_commit(from, c);
  return c;
}

/**
  * display_commit - display a comit as follows:
  *                    "<id>: <major>.<minor> (stable|unstable) '<comment>'"
  *
  * @c: commit to display
  */
void display_commit(struct commit *c)
{
  printf("%lu: %hu.%lu %s '%s'\n", c->id, c->version.major, c->version.minor,
      is_unstable(&c->version) ? "(unstable)" : "(stable)", c->comment);
}

/**
  * commitOf - returns the commit containing the version passed as a parameter
  *
  * @version: pointer to a version
  *
  * @return: the commit containing the version in parameter
  *
  * Note:      this function must still work if the order and number of members
  *            in struct commit changes!
  */
struct commit *commitOf(struct version *version)
{
	return (struct commit *)
			((void *)version - (void *)&((struct commit *)0)->version);
}
