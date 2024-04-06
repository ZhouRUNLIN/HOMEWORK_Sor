package srcs.rmi.service.test;

import org.junit.After;
import org.junit.Before;
import srcs.rmi.service.Host;
import srcs.rmi.service.HostImpl;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class SystemDeployer {
    Registry registry;

    @Before
    public void launch() throws IOException {
        registry = LocateRegistry.createRegistry(1099);

        Host h1 = new HostImpl();
        Host h2 = new HostImpl();

        UnicastRemoteObject.exportObject(h1, 0);
        UnicastRemoteObject.exportObject(h2, 0);
        registry.rebind("host1", h1);
        registry.rebind("host2", h2);
    }

    @After
    public void after() throws IOException, NotBoundException {
        registry.unbind("host1");
        registry.unbind("host2");

        UnicastRemoteObject.unexportObject(registry, true);
    }
}
