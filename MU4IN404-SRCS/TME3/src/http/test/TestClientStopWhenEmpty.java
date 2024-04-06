package http.test;

import http.Serveur;
import http.ClientStopWhenEmpty;

public class TestClientStopWhenEmpty {
    public static void main(String[] args) {
        ClientStopWhenEmpty c = new ClientStopWhenEmpty();
        Serveur s = new Serveur(8080,c);
        s.start();
    }
}
