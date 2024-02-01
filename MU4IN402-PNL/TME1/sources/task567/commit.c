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
  INIT_LIST_HEAD(&c->minor_list);
  INIT_LIST_HEAD(&c->major_list);
  c->parent = c;
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
	list_add(&new->minor_list, &from->minor_list);
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
  c->parent = from->parent;
  c->ops.display = display_minor_commit;
  c->ops.extract = extract_minor;
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
  c->parent = c;
  list_add(&c->major_list, &from->parent->major_list);
  c->ops.display = display_major_commit;
  c->ops.extract = extract_major;
  return c;
  insert_commit(from, c);
}


/**
  * display_commit - display a comit by calling its display function
  *
  * @c: commit to display
  */
void display_commit(struct commit *c)
{
  c->ops.display(c);
}

/**
  * display_minor_commit - display a comit as follows:
  *                    "<id>: <major>.<minor> (stable|unstable) '<comment>'"
  *
  * @c: commit to display
  */
void display_minor_commit(struct commit *c)
{
  printf("%lu: %hu.%lu %s '%s'\n", c->id, c->version.major, c->version.minor,
      is_unstable(&c->version) ? "(unstable)" : "(stable)", c->comment);
}

/**
  * display_major_commit - display a major commit as follows:
  *               "### version <major> : 'Release <major>'"
  *
  * @c: commit to display
  */
void display_major_commit(struct commit *c)
{
  printf("### version %hu : '%s'\n", c->version.major, c->comment);
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
	return container_of(version, struct commit, version);
}


/**
  * del_commit - delete a commit from the history
  *
  * @victim: commit to delete
  */
void del_commit(struct commit *victim)
{
  victim->ops.extract(victim);
}

/**
  * extract_minor - extract a minor commit from the history
  *
  * @victim: commit to extract
  */
void extract_minor(struct commit *victim)
{
  victim->parent = NULL;
  list_del(&victim->minor_list);
  free(victim);
}

/**
  * extract_major - extract all its associated minor commits
  *           in addition to the major commit from the history
  *
  * @victim: commit to extract
  */
void extract_major(struct commit *victim)
{
  struct commit *tmp, *victim_minor;
  list_for_each_entry_safe(victim_minor, tmp, &victim->minor_list, minor_list){
    if(victim_minor->version.major == victim->version.major){
      extract_minor(victim_minor);
    }
  }
  list_del(&victim->major_list);
  extract_minor(victim);
}
