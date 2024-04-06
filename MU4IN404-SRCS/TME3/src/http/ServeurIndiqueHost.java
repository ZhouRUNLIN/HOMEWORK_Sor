package http;

import java.io.*;
import java.net.Socket;
import java.util.StringJoiner;

// TME3 Exercice1 Question5
public class ServeurIndiqueHost extends Serveur{
    private final String host;
    private final String sitePath;

    public ServeurIndiqueHost(String host, int port, String sitePath, RequestProcessor processor) {
        super(port, processor);
        this.host = host;
        this.sitePath = sitePath;
    }

    public void start(String methode, String HTTPVersion){
        try (Socket socket = new Socket(host, super.getPort());
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            StringJoiner stringJoiner = new StringJoiner(" ");
            stringJoiner.add(methode).add(sitePath).add(HTTPVersion);
            out.println(stringJoiner.toString());   // envoyer le requete
            out.println("Host: " + host);           // envoyer l'en-tete
            out.println("Connection: close");       // fin du connection
            out.println();
            super.getProcessor().process(socket);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
