package srcs.rmi.service;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Host extends Remote {
    <P, R>FunctionService<P, R> deployNewService(String serverName, Class<? extends FunctionService<P, R>> clazz) throws RemoteException;
    <P, R>FunctionService<P, R> deployExistingService(FunctionService<P, R> service) throws RemoteException;
    boolean undeployService(String serverName) throws RemoteException;
    List<String> getServices() throws RemoteException;
}
