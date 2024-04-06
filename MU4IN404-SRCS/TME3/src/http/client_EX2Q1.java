package http;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class client_EX2Q1 implements RequestProcessor{

    @Override
    public void process(Socket connexion) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            OutputStream output = connexion.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String requestLine = reader.readLine(); //http请求的第一行：指令 路径 协议

            while (reader.readLine().length() > 0) {  //耗尽剩下的http请求
            }

            if (requestLine == null) {
                return;
            }

            String[] request = requestLine.split(" ");


            if (!request[0].equals("GET")) {    //[0] 是 commande
                writer.println("HTTP/1.1 400 Bad Request\n");
                writer.println();
                return;
            }

            File file = new File(System.getProperty("user.dir"), request[1]); //[1] 是 path
            if (!file.exists() || file.isDirectory()) {
                writer.println("HTTP/1.1 404 Not Found\n");
                writer.println();
                return;
            }

            writer.println("HTTP/1.1 200 OK\r\n");  //这是个http响应
            writer.println(); //加个空格，把响应头和内容分开

            Files.copy(file.toPath(), output);  //发送文件
            output.flush(); 

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
