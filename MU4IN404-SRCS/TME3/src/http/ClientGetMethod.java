package http;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// TME3 Exercice2 Question1
public class ClientGetMethod implements RequestProcessor{
    @Override
    public void process(Socket connexion) {
        try (BufferedReader b = new BufferedReader(new InputStreamReader(connexion.getInputStream()))){
            // lire la ligne de requete sur le socket
            String requestLine = b.readLine();
            if (requestLine == null)
                return;
            String[] reqs = requestLine.split(" ");
            String method = reqs[0];
            String file = reqs[1];
            String version = reqs[2];

            String statusCode;
            Path filePath = Paths.get(System.getProperty("user.dir")+"/src/http"+file);
            // traitement des err
            if (! method.equals("GET"))
                statusCode = "400 Bad Request";
            else if (! filePath.toFile().exists())
                statusCode = "404 Not Found";
            else {
                statusCode = "200 OK";
            }
            // ecrire la reponse
            String responseLine = version + " " + statusCode + "\n";
            connexion.getOutputStream().write(responseLine.getBytes());
            connexion.getOutputStream().write("Content-Type: text/html\n".getBytes());
            connexion.getOutputStream().write("\n".getBytes());
            System.out.println(responseLine);
            if (statusCode.equals("200 OK"))
                connexion.getOutputStream().write(Files.readAllBytes(filePath));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
