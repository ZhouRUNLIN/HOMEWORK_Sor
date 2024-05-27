package rmi;

import utils.enums.ServerState;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ForcedServerShutdown extends Remote {

    void forcedserverShutdown() throws RemoteException;

}
