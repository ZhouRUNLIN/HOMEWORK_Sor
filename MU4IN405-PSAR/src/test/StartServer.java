package test;

import machine.Server;
import utils.exception.ServerException;
import utils.processor.ServerProcessor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class StartServer {
    public static void main(String[] args) throws ServerException, ClassNotFoundException, IOException {
        Server server = new Server(8080, "Server");
        server.start();
    }
}