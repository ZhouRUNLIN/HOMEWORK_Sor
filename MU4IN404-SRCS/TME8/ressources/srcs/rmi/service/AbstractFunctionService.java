package srcs.rmi.service;

import java.io.*;
import java.rmi.RemoteException;
import java.util.concurrent.AbstractExecutorService;

public abstract class AbstractFunctionService<P,R> implements FunctionService<P,R>{
    private String serverName;
    private FunctionService<P, R> deployedService = null;

    public AbstractFunctionService(String serverName){
        this.serverName = serverName;
    }
    @Override
    public String getName() throws RemoteException {
        if (deployedService != null)
            return deployedService.getName();
        return this.serverName;
    }

    @Override
    public R invoke(P arg) throws RemoteException {
        if (deployedService != null)
            return deployedService.invoke(arg);
        return perform(arg);
    }

    @Override
    public FunctionService<P, R> migrateTo(Host host) throws RemoteException {
        if (host.getServices().contains(serverName))
            throw new RemoteException("Server existe !");
        try {
            AbstractFunctionService copy = (AbstractFunctionService) deepCopy(this);
            deployedService = host.deployExistingService(copy);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return deployedService;
    }

    public static <T extends Serializable> T deepCopy(T object) {
        T copy = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            copy = (T) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return copy;
    }

    protected abstract R perform(P param);

}
