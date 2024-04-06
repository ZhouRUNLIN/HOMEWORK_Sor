package srcs.rmi.concurrent.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import srcs.rmi.concurrent.SharedVariable;
import srcs.rmi.concurrent.SystemDeployer;


public class TestSharedVariableClassical extends SystemDeployer {

	final int timeout=2000;
	final String nameService="variableIntegerClassical";
	
		
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCoherence() throws InterruptedException, AccessException, RemoteException, NotBoundException {
		final int nb_clients=50;
		
		List<Thread> clients = new ArrayList<>();
		final Registry registry = LocateRegistry.getRegistry("localhost");
		for(int i=0; i< nb_clients; i++) {
			clients.add(i, new Thread(()->{
				
				try {
					
				    SharedVariable<Integer> var = (SharedVariable<Integer>)registry.lookup(nameService);
					
					int x= var.obtenir();
					x++;
					var.relacher(x);
					
					
				} catch (RemoteException | NotBoundException e) {
					e.printStackTrace();
				}	
			}));
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
		assertEquals(clients.size(),value );
		var.relacher(value);
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testOrdre() throws InterruptedException, AccessException, RemoteException, NotBoundException {
		final int nb_clients=5;
		List<Thread> clients = new ArrayList<>();
		final Registry registry = LocateRegistry.getRegistry("localhost");
		SharedVariable<Integer> variable = (SharedVariable<Integer>)registry.lookup(nameService);
		int value = variable.obtenir();
		
		
		
		final List<Thread> access = new ArrayList<>(); 
		
		
		for(int i=0; i< nb_clients; i++) {
			clients.add(i, new Thread(()->{
				
				try {
					
				    SharedVariable<Integer> var = (SharedVariable<Integer>)registry.lookup(nameService);
					
					int x= var.obtenir();
					access.add(Thread.currentThread());
					var.relacher(x);
					
					
				} catch (RemoteException | NotBoundException e) {
					e.printStackTrace();
				}	
			}));
		}
		for(Thread t : clients) {
			t.start();
			Thread.sleep(300);
		}
		variable.relacher(value);		
		for(Thread t : clients) {
			t.join(timeout);
			assertFalse(t.isAlive());
		}
			
		assertEquals(clients.size(),access.size());
		for(int i=0;i<clients.size();i++) {
			assertEquals(clients.get(i).getId(), access.get(i).getId());
		}
		
	}
	
	
	

}
