package http.test;

import http.Serveur;
import http.client_EX1Q1;

public class test_EX1Q1 {
    public static void main(String[] args) {
        client_EX1Q1 c = new client_EX1Q1();
        Serveur s = new Serveur(8080,c);
        s.start();

    }
}
