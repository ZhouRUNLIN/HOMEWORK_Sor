package rmi;

import utils.enums.ServerState;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerErrorSet extends Remote {
    void setServerError(ServerState serverState) throws RemoteException;

}
