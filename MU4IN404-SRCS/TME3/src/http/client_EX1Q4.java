package http;

import java.io.*;
import java.net.Socket;

public class client_EX1Q4 implements RequestProcessor{

    public client_EX1Q4(){

    }
    @Override
    public void process(Socket connexion) throws IOException {
        try {
            BufferedReader b = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            String line;
            while ((line = b.readLine()) != null){
                System.out.println(line);

                if (line.isEmpty()){
                    break;
                }
            }
        }catch (Exception e){}

    }
}
