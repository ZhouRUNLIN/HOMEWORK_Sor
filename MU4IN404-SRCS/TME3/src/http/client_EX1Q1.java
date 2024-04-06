package http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class client_EX1Q1 implements RequestProcessor{

    int port;
    Socket socket;

    public client_EX1Q1(){

    }

    @Override
    public void process(Socket connexion) throws IOException {
            InputStream is = connexion.getInputStream();
            bind(is,System.out);
    }

    public static void bind(InputStream in, OutputStream out) throws IOException {
        int lu = 0;
        try {
            while ((lu = in.read()) != -1) {
                out.write(lu);
                out.flush();
            }
        }catch (IOException e){}
    }

}
