package http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

// TME3 Exercice1 Question1
public class ClientWithBind implements RequestProcessor{
    @Override
    public void process(Socket connexion) {
        try (InputStream is = connexion.getInputStream(); ){
            bind(is, System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // copie sur TD1
    private static void bind(InputStream in, OutputStream out) {
        int lu;
        try {
            while ((lu = in.read()) != -1) {
                out.write(lu);
                out.flush();
            }
        }catch (IOException e){}
    }

}
