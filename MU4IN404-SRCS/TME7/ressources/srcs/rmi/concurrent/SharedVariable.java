package srcs.rmi.concurrent;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SharedVariable<T extends Serializable> extends Remote {
    public T obtenir() throws RemoteException;

    public void relacher(T x) throws RemoteException;
}
