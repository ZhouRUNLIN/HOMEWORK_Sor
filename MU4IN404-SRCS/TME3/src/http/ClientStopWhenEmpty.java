package http;

import java.io.*;
import java.net.Socket;

// TME3 Exercice1 Question4
public class ClientStopWhenEmpty implements RequestProcessor{
    @Override
    public void process(Socket connexion){
        try {
            BufferedReader b = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            String line;
            while ((line = b.readLine()) != null){
                System.out.println(line);
                if (line.isEmpty())
                    break;
            }
        }catch (Exception e){}
    }
}
