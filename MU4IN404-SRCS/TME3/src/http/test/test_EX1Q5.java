package http.test;

import http.Serveur;
import http.client_EX1Q5;

public class test_EX1Q5 {
    public static void main(String[] args) {
        client_EX1Q5 c = new client_EX1Q5("http://www.google.fr/index.html");
        Serveur s = new Serveur(80,c);
        s.start();
    }
}
