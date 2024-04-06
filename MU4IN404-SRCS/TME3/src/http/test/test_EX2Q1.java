package http.test;

import http.Serveur;
import http.client_EX2Q1;

public class test_EX2Q1 {

    public static void main(String[] args){

        client_EX2Q1 t = new client_EX2Q1();
        Serveur s = new Serveur(80,t);
        s.start();


    }
}
