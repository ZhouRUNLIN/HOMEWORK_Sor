package rmi;

import utils.enums.ClientState;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientErrorSet extends Remote {
    void setClientError(ClientState clientState) throws RemoteException;

}
