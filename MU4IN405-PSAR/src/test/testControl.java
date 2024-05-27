package test;

import rmi.ClientErrorSet;
import rmi.ServerErrorSet;
import utils.enums.ClientState;
import utils.enums.ServerState;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class testControl {

    private static ServerState serverState;
    private static ClientState clientState;


    private static void controlServer(){

        System.out.println("Please enter a server error:");

        System.out.println("1.timeout");
        System.out.println("2.errorSource");
        System.out.println("3.errorState");
        System.out.println("4.errorNull");
        System.out.println("5.normal");

        Scanner scanner = new Scanner(System.in);
        int indexServerError = scanner.nextInt();

        switch (indexServerError){
            case 1 -> serverState = ServerState.timeout;
            case 2 -> serverState = ServerState.errorSource;
            case 3 -> serverState = ServerState.errorState;
            case 4 -> serverState = ServerState.errorNull;
            case 5 -> serverState = ServerState.normal;
        }

        try {
            Registry registry = LocateRegistry.getRegistry("localhost",1099);
            ServerErrorSet stub = (ServerErrorSet) registry.lookup("ServerErrorService");
            stub.setServerError(serverState);
            System.out.println("Remote method invoked");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            //e.printStackTrace();
        }

    }


    private static void controlClient(){

        System.out.println("Please enter the id of the client you wish to control: ");

        Scanner scanner = new Scanner(System.in);
        String clientID = scanner.nextLine();

        System.out.println("Please enter a server error:");

        System.out.println("1.timeout");
        System.out.println("2.errorSource");
        System.out.println("3.errorState");
        System.out.println("4.errorNull");
        System.out.println("5.normal");

        int indexServerError = scanner.nextInt();

        switch (indexServerError){
            case 1 -> clientState = ClientState.timeout;
            case 2 -> clientState = ClientState.errorSource;
            case 3 -> clientState = ClientState.errorState;
            case 4 -> clientState = ClientState.errorNull;
            case 5 -> clientState = ClientState.normal;
        }

        changeClientState(clientID,clientState);

    }


    public static void main(String[] args) {

        System.out.println("Please select:");

        System.out.println("1.Server");
        System.out.println("2.Client");

        Scanner scanner = new Scanner(System.in);
        int indexClientError = scanner.nextInt();

        switch (indexClientError){
            case 1 -> controlServer();
            case 2 -> controlClient();
        }

    }


    public static void changeClientState(String clientId, ClientState newState) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099); // 连接到新的注册表端口
            String clientName = "ClientControl_" + clientId;
            ClientErrorSet client = (ClientErrorSet) registry.lookup(clientName);
            client.setClientError(newState);
            System.out.println("State changed for " + clientId + " at registry port 1100");
        } catch (Exception e) {
            System.err.println("Controller exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
