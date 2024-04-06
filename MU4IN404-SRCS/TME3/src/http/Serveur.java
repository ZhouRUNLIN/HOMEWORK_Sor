package http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {

    private final int port;
    private final RequestProcessor processor;

    public Serveur(int port, RequestProcessor processor) {
        this.port = port;
        this.processor = processor;
    }

    public void start() {
        try (ServerSocket ss = new ServerSocket(port)) {
            int i = 0;
            while (true) {
                try (Socket client = ss.accept()) {
                    System.out.println("Debut de requête " + i);
                    processor.process(client);
                }
                System.out.println("Fin de requête " + i);
                System.out.println("**********************\n");
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort(){
        return port;
    }

    public RequestProcessor getProcessor(){
        return processor;
    }
}

