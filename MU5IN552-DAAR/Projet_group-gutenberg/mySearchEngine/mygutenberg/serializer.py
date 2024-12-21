from rest_framework.serializers import ModelSerializer
from mygutenberg.models import ProduitAllBooks, ProduitEnglishBooks, ProduitFrenchBooks


class ProduitAllBooksSerializer(ModelSerializer):
    class Meta:
        model = ProduitAllBooks
        fields = ('id', 'bookID')

class ProduitEnglishBooksSerializer(ModelSerializer):
    class Meta:
        model = ProduitEnglishBooks
        fields = ('id', 'bookID', 'Language')

class ProduitFrenchBooksSerializer(ModelSerializer):
    class Meta:
        model = ProduitFrenchBooks
        fields = ('id', 'bookID', 'Language')
