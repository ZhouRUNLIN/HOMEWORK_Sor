package utils.message;

import java.io.Serializable;

public class ClientMessage implements Message, Serializable {
    private final String command;
    private final String clientId;
    private final String variableId;
    private final int clientPort;

    public ClientMessage(String command, String clientId, String variableId, int clientPort) {  //for dMalloc,dAccessWrite, // dAccessRead,dFree
        this.command = command;
        this.clientId = clientId;
        this.variableId = variableId;
        this.clientPort = clientPort;
    }

    public String getCommand() {
        return command;
    }

    public String getClientId() {
        return clientId;
    }

    public String getVariableId() {
        return variableId;
    }

    public int getClientPort() {
        return  clientPort;
    }

    public String toString() {
        return "Command :" + getCommand() + "\n" +
                "Client id :" + getClientId() + "\n" +
                "Var id :" + getVariableId() + "\n" +
                "Client port :" + getClientId();
    }
}
