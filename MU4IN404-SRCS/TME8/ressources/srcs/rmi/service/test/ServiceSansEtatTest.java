package srcs.rmi.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.junit.Test;

import srcs.rmi.service.AbstractFunctionService;
import srcs.rmi.service.FunctionService;
import srcs.rmi.service.Host;
import srcs.rmi.service.test.SystemDeployer;

public class ServiceSansEtatTest extends SystemDeployer {

    public static class DivisionService extends AbstractFunctionService<Operandes,Double>{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public DivisionService(String name) {
			super(name);
		}

		@Override
		protected Double perform(Operandes param) {
		
			return param.getA()/param.getB();
		}
		
	}
	
	public static class Operandes implements Serializable{

		private static final long serialVersionUID = 1L;
		
		private double a;
		private double b;
		
		public Operandes(double a, double b) {
			this.a=a;
			this.b=b;
		}

		public double getA() {
			return a;
		}

		public double getB() {
			return b;
		}
		
		@Override
		public String toString() {
			return "a="+a+" b="+b;
		}
		
	}
		
	@Test
	public void testServiceSansEtat() throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry("localhost");
		Host s1 = (Host)registry.lookup("host1");
		Host s2 = (Host)registry.lookup("host2");
		String nameservice = "bonjour";
		
		FunctionService<Operandes,Double> div1 = s1.deployNewService(nameservice, DivisionService.class );
		Operandes operandes =new Operandes(5.0, 2.0);
		
		
		assertEquals(2.5, div1.invoke(operandes),0.01); 

		FunctionService<Operandes,Double> div2 = div1.migrateTo(s2);
		assertEquals(2.5, div1.invoke(operandes),0.01); 
		assertEquals(2.5, div2.invoke(operandes),0.01); 
		
		try {
			div1.migrateTo(s2);
			assertTrue(false);
		}catch(RemoteException e) {
			assertTrue(true);
		}
		
		assertFalse(s1.undeployService("inconnu"));
		
		assertTrue(s1.getServices().contains(nameservice));
		assertTrue(s2.getServices().contains(nameservice));
		
		assertEquals(nameservice, div1.getName());
		
		s1.undeployService(nameservice);
		assertFalse(s1.getServices().contains(nameservice));
		assertTrue(s2.getServices().contains(nameservice));
		try {
			div1.getName();
			assertTrue(false);
		}catch(NoSuchObjectException e) {
			assertTrue(true);
		}
		FunctionService<Operandes,Double> div3 = div2.migrateTo(s1);
		assertTrue(s1.getServices().contains(nameservice));
		assertTrue(s2.getServices().contains(nameservice));
		
		assertEquals(nameservice, div3.getName());
		assertEquals(nameservice, div2.getName());
		
		
		
		s1.undeployService(nameservice);
		assertFalse(s1.getServices().contains(nameservice));
		assertTrue(s2.getServices().contains(nameservice));
		try {
			div1.getName();
			assertTrue(false);
		}catch(NoSuchObjectException e) {
			assertTrue(true);
		}
		try {
			div2.invoke(operandes);
			assertTrue(false);
		}catch(ServerException e) {
			assertTrue(true);
		}
		
	}

}
