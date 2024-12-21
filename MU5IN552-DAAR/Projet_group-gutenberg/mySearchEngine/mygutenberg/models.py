from django.db import models

# Create your models here.
class ProduitAllBooks(models.Model):
    created = models.DateTimeField(auto_now_add=True)
    bookID = models.IntegerField(default='-1')

    class Meta:
        ordering = ('bookID',)

class ProduitEnglishBooks(models.Model):
    created = models.DateTimeField(auto_now_add=True)
    bookID = models.IntegerField(default='-1')

    class Meta:
        ordering = ('bookID',)

class ProduitFrenchBooks(models.Model):
    created = models.DateTimeField(auto_now_add=True)
    bookID = models.IntegerField(default='-1')

    class Meta:
        ordering = ('bookID',)
