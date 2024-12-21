from django.core.management.base import BaseCommand, CommandError
from mygutenberg.models import ProduitAllBooks
from mygutenberg.serializer import ProduitAllBooksSerializer
from mygutenberg.config import baseUrl
from bs4 import BeautifulSoup
import requests
import time

class Command(BaseCommand):
    help = 'Refresh the list of products which are on sale.'

    def handle(self, *args, **options):
        self.stdout.write('['+time.ctime()+'] Refreshing data...')
        start_index = 1
        should_stop = False
        ProduitAllBooks.objects.all().delete()

        while not should_stop and start_index <= 25:
            response = requests.get(baseUrl+'search/?start_index=' + str(start_index))
            if response.status_code != 200:
                should_stop = True
            else:
                html_content = response.text 
                soup = BeautifulSoup(html_content, 'html.parser')

                book_items = soup.find_all('li', class_='booklink')
                for book_item in book_items:
                    link_tag = book_item.find('a', class_='link')
                    link = link_tag['href'] if link_tag else None
     
                    serializer = ProduitAllBooksSerializer(data = {'bookID':link[8:]})
                    if serializer.is_valid():
                        serializer.save()
                self.stdout.write('['+time.ctime()+'] Data added bettween ' + str(start_index) + ' and ' + str(start_index + 24))
                start_index += 25
        self.stdout.write('['+time.ctime()+'] Data refresh terminated.')
