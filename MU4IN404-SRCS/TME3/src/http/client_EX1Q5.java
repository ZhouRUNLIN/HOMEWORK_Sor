package http;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class client_EX1Q5{

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
        String path = "/index.html";

        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("GET " + path + " HTTP/1.1"); //发送请求
            out.println("Host: " + host);//发送请求头
            out.println("Connection: close"); //设定请求头结束后断开连接
            out.println();  //请求头结束

            process(socket);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }


}
