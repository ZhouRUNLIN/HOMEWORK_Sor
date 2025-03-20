import requests
from bs4 import BeautifulSoup
import random
import csv
import os
import argparse

# Base URL for the book information
baseUrl = 'https://www.gutenberg.org/ebooks/'

# Function to get book data
def get_data(bookID):
    try:
        response = requests.get(baseUrl + str(bookID))
        response.raise_for_status()  # Raise an error for unsuccessful requests
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

        img = soup.find('img', class_='cover-art')
        if img and img.get('src'):
            metadata['img'] = img['src']
        else:
            metadata['img'] = ''

        table = soup.find('table', class_='files')
        if table:
            td = table.find('td', content='text/plain; charset=us-ascii')
            if td:
                metadata['storagePath'] = 'https://www.gutenberg.org' + td.find_next('a').get('href')

        metadata['Downloads'] = metadata.get('Downloads', '').strip(' ')[0]
        metadata['Title'] = metadata.get('Title', '')[:199]
        metadata['Subject'] = metadata.get('Subject', '')[:90]
        return metadata
    except requests.exceptions.RequestException as e:
        print(f"Error fetching data for bookID {bookID}: {e}")
        return None

# Function to download book text
# def download_book_text(bookID, storagePath):
#     try:
#         response = requests.get(storagePath)
#         response.raise_for_status()
#         output_dir = 'resources/books/'
#         os.makedirs(output_dir, exist_ok=True)
#         file_path = os.path.join(output_dir, f"{bookID}.txt")
#         with open(file_path, 'w', encoding='utf-8') as file:
#             file.write(response.text)
#         print(f"Book text for bookID {bookID} saved to {file_path}")
#     except requests.exceptions.RequestException as e:
#         print(f"Error downloading text for bookID {bookID}: {e}")

# Function to write data to a CSV file
def write_to_csv(data, filename):
    with open(filename, 'w', newline='', encoding='utf-8') as csvfile:
        fieldnames = ['refID', 'Title', 'storagePath', 'Author', 'Subject', 'Summary', 'Language', 'Downloads', 'img']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()
        for entry in data:
            row = {field: entry.get(field, '') for field in fieldnames}  # Fill missing fields with empty strings
            writer.writerow(row)

# Main script
if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Fetch random book data from Gutenberg and download text.")
    parser.add_argument('-n', '--number', type=int, required=True, help="Number of random book IDs to fetch")
    args = parser.parse_args()
    

    n = args.number
    min_book_id = 1  # Minimum book ID
    max_book_id = 75000  # Maximum book ID (you can adjust based on your requirement)

    random_book_ids = random.sample(range(min_book_id, max_book_id), n)
    book_data = []

    for bookID in random_book_ids:
        print(f"Fetching data for bookID {bookID}...")
        metadata = get_data(bookID)
        if metadata:
            metadata['refID'] = bookID
            book_data.append(metadata)

            # if 'storagePath' in metadata and metadata['storagePath']:
            #     download_book_text(bookID, metadata['storagePath'])

    if book_data:
        output_file = 'resources/bookInfo/bookInfo.csv'
        os.makedirs(os.path.dirname(output_file), exist_ok=True)
        write_to_csv(book_data, output_file)
        print(f"Data successfully written to {output_file}")
    else:
        print("No data fetched.")
