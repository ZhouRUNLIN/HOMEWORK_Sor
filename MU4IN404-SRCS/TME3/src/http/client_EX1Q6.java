package http;

import java.io.*;
import java.net.Socket;

public class client_EX1Q6 {
    public static void process(Socket connexion) throws IOException {

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

    public static void main(String[] args) {
        String host = "www.google.fr";
        int port = 80;
        String path = "/toto.html";

        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("GET " + path + " HTTP/1.1");
            out.println("Host: " + host);

            out.println("Connection: close");
            out.println();

            process(socket);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

}
