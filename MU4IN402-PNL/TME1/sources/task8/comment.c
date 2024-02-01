#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "comment.h"

struct comment *new_comment(int title_size, char *title, int author_size,
			    char *author, int text_size, char *text)
{
	struct comment *c = malloc(sizeof(struct comment));

	if(title_size < strlen(title)||title_size<0)
		title_size = strlen(title);
	struct title *t = malloc(sizeof(struct title) + title_size + 1);
	t->size = title_size;
	memcpy(t->title, title, title_size + 1);

	if(author_size < strlen(author)||author_size<0)
		author_size = strlen(author);
	struct author *a = malloc(sizeof(struct author) + author_size + 1);
	a->size = author_size;
	memcpy(a->author, author, author_size+1);

	
	if(text_size < strlen(text)||text_size<0)
		text_size = strlen(text);
	struct text *te = malloc(sizeof(struct text) + text_size + 1);
	te->size = text_size;
	memcpy(te->text, text, text_size+1);

	c->title = t;
	c->author = a;
	c->text = te;

	return c;
}

void display_comment(struct comment *c)
{
	printf("%s from %s \"%s\"\n", c->title->title, c->author->author, c->text->text);
}

void free_comment(struct comment *c)
{
	if (!c)
		return;
	
	free(c->title);		
	free(c->author);	
	free(c->text);	
		
	free(c);
}
