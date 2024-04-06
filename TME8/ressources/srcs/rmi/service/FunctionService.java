package srcs.rmi.service;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FunctionService<P, R> extends Remote, Serializable {
    String getName() throws RemoteException;
    R invoke(P arg) throws RemoteException;
    FunctionService<P, R> migrateTo(Host host) throws RemoteException;
}
