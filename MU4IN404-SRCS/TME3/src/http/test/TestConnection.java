package http.test;

import http.ClientStopWhenEmpty;
import http.ServeurIndiqueHost;
import http.RequestProcessor;

// TME3 Exercice1 Question5-7
public class TestConnection {
    public static void main(String[] args) {
        // Question 5
        // connect avec  http://www.google.fr/index.html
        RequestProcessor client = new ClientStopWhenEmpty();
        ServeurIndiqueHost serverIndex = new ServeurIndiqueHost("www.google.fr", 80, "/index.html", client);
        serverIndex.start("GET", "HTTP/1.1");

        // Question 6
        // remplacer "index.html" avec "toto.html"
        ServeurIndiqueHost serverToto = new ServeurIndiqueHost("www.google.fr", 80, "/toto.html", client);
        serverToto.start("GET", "HTTP/1.1");

        // Question 6
        // remplacer "index.html" avec "toto.html"
        ServeurIndiqueHost serverWrongHTTPVerison = new ServeurIndiqueHost("www.google.fr", 80, "/index.html", client);
        serverWrongHTTPVerison.start("GET", "HTTP/10.1");
    }
}
