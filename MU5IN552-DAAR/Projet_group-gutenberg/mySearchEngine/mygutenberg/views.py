import requests
from rest_framework.views import APIView
from rest_framework.response import Response
from mygutenberg.config import baseUrl
from mygutenberg.models import ProduitAllBooks
from mygutenberg.serializer import ProduitAllBooksSerializer
from bs4 import BeautifulSoup

def get_data(bookID):
    response = requests.get(baseUrl + str(bookID))
    html_content = response.text 
    soup = BeautifulSoup(html_content, 'html.parser')

    table = soup.find('table', class_='bibrec')
    metadata = {}

    if table:
        rows = table.find_all('tr')
        for row in rows:
            key = row.find('th')
            value = row.find('td')
            if key and value:
                metadata[key.text.strip()] = value.text.strip()
    
    return metadata

class RedirectionBookList(APIView):
    def get(self, request, format=None):
        res = []
        for prod in ProduitAllBooks.objects.all():
            serializer = ProduitAllBooksSerializer(prod)
            metadata = get_data((serializer.data['bookID']))
            if metadata != None:
                res.append(metadata)

        return Response(res)

class RedirectionBookDetail(APIView):
    def get(self, request, pk, format=None):
        return Response(get_data(pk))

class RedirectionEnglishBookList(APIView):
    def get(self, request, format=None):
        res = []
        for prod in ProduitAllBooks.objects.all():
            serializer = ProduitAllBooksSerializer(prod)
            metadata = get_data((serializer.data['bookID']))
            if metadata['Language'] == 'English':
                res.append(metadata)

        return Response(res)

class RedirectionFrenchBookList(APIView):
    def get(self, request, format=None):
        res = []
        for prod in ProduitAllBooks.objects.all():
            serializer = ProduitAllBooksSerializer(prod)
            metadata = get_data((serializer.data['bookID']))
            if metadata['Language'] == 'French':
                res.append(metadata)

        return Response(res)

