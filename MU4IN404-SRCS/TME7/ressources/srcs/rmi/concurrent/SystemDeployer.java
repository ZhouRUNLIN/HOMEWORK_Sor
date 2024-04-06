package srcs.rmi.concurrent;

import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
public class SystemDeployer {
    Registry registry = null;

    @Before
    public void launch() throws IOException, InterruptedException {

        registry = LocateRegistry.createRegistry(1099);

        SharedVariable<Integer> svc;
        SharedVariable<Integer> svr;

        svc = new SharedVariableClassical<>(0);
        svr = new SharedVariableReliable<>(0);

        UnicastRemoteObject.exportObject(svc, 0);
        UnicastRemoteObject.exportObject(svr, 0);

        registry.rebind( "variableIntegerClassical" , svc);
        registry.rebind( "variableIntegerReliable" , svr);
    }

    @After
    public void after() throws IOException, NotBoundException {
        registry.unbind("variableIntegerClassical");
        registry.unbind("variableIntegerReliable");

        UnicastRemoteObject.unexportObject(registry, true);
    }
}
