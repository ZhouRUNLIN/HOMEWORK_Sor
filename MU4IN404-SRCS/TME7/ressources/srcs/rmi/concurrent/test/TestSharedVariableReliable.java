package srcs.rmi.concurrent.test;

import static org.junit.Assert.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import srcs.rmi.concurrent.SharedVariable;
import srcs.rmi.concurrent.SystemDeployer;

public class TestSharedVariableReliable extends SystemDeployer {

	int timeout = 3000;
	final String nameService="variableIntegerReliable";
	@SuppressWarnings("unchecked")
	@Test
	public void test() throws RemoteException, InterruptedException, NotBoundException {
		final int nb_clients=50;
		List<Thread> clients = new ArrayList<>();
		final List<Thread> fautifs = new ArrayList<>();
		final Registry registry = LocateRegistry.getRegistry("localhost");
		
		for(int i=0; i< nb_clients; i++) {
			clients.add(i, new Thread(()->{
				
				
				try {
					boolean fautif=fautifs.contains(Thread.currentThread());
				    SharedVariable<Integer> var = (SharedVariable<Integer>)registry.lookup(nameService);
					
					int x= var.obtenir();
					x++;
					if(!fautif)
						var.relacher(x);
					
					
				} catch (RemoteException | NotBoundException e) {
					e.printStackTrace();
				}	
			}));
			if(i==nb_clients/2) {
				fautifs.add(clients.get(i));
			}
		}
		for(Thread t : clients) {
			t.start();
		}
		for(Thread t : clients) {
			t.join(timeout);
			assertFalse(t.isAlive());
		}
		SharedVariable<Integer> var = (SharedVariable<Integer>)registry.lookup(nameService);
		int value = var.obtenir();
		assertEquals(clients.size()-1,value );
		var.relacher(value);
	}

}
