//HUA Zhichun 28713132
#include<stdio.h>
#include<stdlib.h>

typedef struct tableau{
	int* tab;
	int maxTaille;
	int position;
}Tableau;

void ajouterElement(int a,Tableau *t){
	t->tab[t->position]=a;
	t->position++;
}

Tableau* initTableau(int maxTaille){
	Tableau* t = (Tableau*)malloc(sizeof(Tableau));
	t->position=0;
	t->maxTaille=maxTaille;
	t->tab=(int*)malloc(sizeof(int)*maxTaille);
	return t;
}

void affichageTableau(Tableau* t){
	printf("t->position = %d\n",t->position);
	printf("[ ");	
	for (int i=0;i<(t->position);i++){
		printf("%d ",t->tab[i]);	
	}
	printf("]\n");
}

int main(){
	Tableau* t;
	t = initTableau(100);
	ajouterElement(5,t);
	ajouterElement(18,t);
	ajouterElement(99999,t);
	ajouterElement(-452,t);
	ajouterElement(4587,t);
	affichageTableau(t);
	free(t->tab);	
	free(t);
}

/*Q1.6*/
/*Il censé creer un tableau et ajouter des nombres dans ce tableau et les afficher*/
/*Sur le terminal il affiche les nombres.*/
/*Q1.7*/
/*Le probleme est que le programme ne libère pas le tableau succesivement à la fin.*/
/*Q1.8*/
/*Il y a des tableaux qui sont pas libérés. Les 400 bytes correspondent 100 fois de type int*/
/*Q1.9*/
/*On libére le tableau. On ajoute free(t->tab) avant frre(t)*/
