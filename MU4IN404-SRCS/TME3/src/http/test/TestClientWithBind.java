package http.test;

import http.Serveur;
import http.ClientWithBind;

public class TestClientWithBind {
    public static void main(String[] args) {
        ClientWithBind c = new ClientWithBind();
        Serveur s = new Serveur(8080,c);
        s.start();
    }
}
