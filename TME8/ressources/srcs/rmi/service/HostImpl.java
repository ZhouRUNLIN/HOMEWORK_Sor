package srcs.rmi.service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HostImpl implements Host{
    private Map<String, FunctionService<?, ?>> map = new HashMap<>();

    @Override
    public <P, R> FunctionService<P, R> deployNewService(String serverName, Class<? extends FunctionService<P, R>> clazz) throws RemoteException {
        if (map.containsKey(serverName))
            throw new RemoteException("Server Existe !");
        try {
            FunctionService<P, R> functionService = clazz.getConstructor(String.class).newInstance(serverName);
            map.put(serverName, functionService);
            UnicastRemoteObject.exportObject(functionService, 0);
            return functionService;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public <P, R> FunctionService<P, R> deployExistingService(FunctionService<P, R> service) throws RemoteException {
        UnicastRemoteObject.exportObject(service, 0);
        map.put(service.getName(), service);
        return service;
    }

    @Override
    public boolean undeployService(String serverName) throws RemoteException {
        if (! map.containsKey(serverName))
            return false;
        UnicastRemoteObject.unexportObject(map.get(serverName), true);
        return map.remove(serverName) != null;
    }

    @Override
    public List<String> getServices() throws RemoteException {
        return new ArrayList<>(map.keySet());
    }
}
