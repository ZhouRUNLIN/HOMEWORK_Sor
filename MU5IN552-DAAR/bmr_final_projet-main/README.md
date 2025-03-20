# DAAR Final Projet

## Overview

This project is a web application built using **Vue 3** for the frontend and **Java (JDK 17) with Spring Boot** for the backend. The backend uses **MySQL** as the database, **redis** as cache and distributed lock, **elasticSearch** as a search engine.

## Tech Stack

### Frontend
- Vue 3
- Vue Router
- Axios (HTTP Requests)
- Vite (Build Tool)

### Backend
- Java 17 (JDK 17)
- Spring Boot
- Redis
- MySQL (Database)
- elasticSearch

## Setup and Installation

### Prerequisites
Make sure you have the following installed:
- **Node.js** v18.20.4
- **Java 17 (JDK 17)**
- **MySQL**
- **Maven**
- **redis**
- **elasticSearch**

### DataCrabing
```
    python3 -m venv myVenv
    source myVenv/bin/activate
    pip3 install requests BeautifulSoup4
    python3 crab.py -n num_book_to_crab
```

### Backend Setup
1. Replace the password for your mysql: (the defaut compte is root).
   You should change the configuration in both *user/src/main/resources/shardingsphere-config.yaml* and *project/src/main/resources/shardingsphere-config.yaml* (line 7 and 8)
   
2. Replace the password for elasticSearch: (the defaut compte is elastic), which is the line 20 and 21 of the *project/src/main/resources/application.yaml*
3. start the backend engine
   *project/src/main/java/org/rhw/bmr/project/BmrProjectApplication.java* and *user/src/main/java/org/rhw/bmr/project/BmrUserApplication.java*
4. Start redis, Mysql, elasticSearch.
5. Import the resources/DB.sql file into your MySQL database.
6. Import your elasticSearch with resources/elastic_index.cfg as index.

### Frontend Setup

1. Navigate to the frontend directory:
   ``` cd frontend ```

2. Install dependencies:
   ``` npm install ```

3. Run the development server: (the defaut port is on 3000)
   ``` npm run dev -- --host ip_of_VLAN ```

