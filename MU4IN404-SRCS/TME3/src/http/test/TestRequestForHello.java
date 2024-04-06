package http.test;

import http.*;

// TME3 Exercice2 Question2
public class TestRequestForHello {
    public static void main(String[] args){
        String currPath = System.getProperty("user.dir") + "/src/http";
        System.out.println(currPath);
        RequestProcessor client = new ClientGetMethod();
        Serveur server = new Serveur(8080, client);
        server.start();
    }
}
