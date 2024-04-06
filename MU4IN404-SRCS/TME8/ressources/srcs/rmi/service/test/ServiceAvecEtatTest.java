package srcs.rmi.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.junit.Test;

import srcs.rmi.service.AbstractFunctionService;
import srcs.rmi.service.FunctionService;
import srcs.rmi.service.Host;
import srcs.rmi.service.test.SystemDeployer;
import srcs.rmi.service.test.ServiceAvecEtatTest.Operation.TypeOp;

public class ServiceAvecEtatTest extends SystemDeployer {

	public static class Compteur extends AbstractFunctionService<Operation, Integer>{

		private static final long serialVersionUID = 1L;
		private int cpt=0;
		
		public Compteur(String name) {
			super(name);
		}

		@Override
		protected synchronized Integer perform(Operation param) {
			
			switch (param.getType()) {
			case PLUS:
				cpt+=param.getOperande();
				break;
			case MINUS:
				cpt-=param.getOperande();
				break;
			case TIMES:
				cpt*=param.getOperande();
				break;
			case DIV:
				cpt/=param.getOperande();
				break;
			default:
				break;
			}
			return cpt;
			
		}
		
	}
	
	public static class Operation implements Serializable{
		private static final long serialVersionUID = 1L;
		public static enum TypeOp {PLUS,MINUS,TIMES,DIV};
		private final TypeOp type;
		private final int operande;
		public Operation(TypeOp type, int operande) {
			this.operande=operande;
			this.type=type;
		}
		public TypeOp getType() {
			return type;
		}
		public int getOperande() {
			return operande;
		}		
	}
	
	
	@Test
	public void test() throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry("localhost");
		Host s1 = (Host)registry.lookup("host1");
		Host s2 = (Host)registry.lookup("host2");
		String nameservice = "compteur";
		
		FunctionService<Operation,Integer> cpt1 = s1.deployNewService(nameservice, Compteur.class );
		Operation plus =new Operation(TypeOp.PLUS, 5);
		Operation moins =new Operation(TypeOp.MINUS, 5);
		Operation fois =new Operation(TypeOp.TIMES, 5);
		Operation div =new Operation(TypeOp.DIV, 5);
		Operation get =new Operation(TypeOp.PLUS, 0);
		
		assertTrue(s1.getServices().contains(nameservice));
		assertEquals(Integer.valueOf(5), cpt1.invoke(plus));
		assertEquals(Integer.valueOf(10), cpt1.invoke(plus));
		assertEquals(Integer.valueOf(50), cpt1.invoke(fois));
		assertEquals(Integer.valueOf(45), cpt1.invoke(moins));
		assertEquals(Integer.valueOf(9), cpt1.invoke(div));
		
		
		FunctionService<Operation,Integer> cpt2 = cpt1.migrateTo(s2);
		assertTrue(s2.getServices().contains(nameservice));
		assertEquals(Integer.valueOf(9), cpt1.invoke(get));
		assertEquals(Integer.valueOf(9), cpt2.invoke(get));
		
		
		cpt2.invoke(plus);
		assertEquals(Integer.valueOf(14), cpt1.invoke(get));
		assertEquals(Integer.valueOf(14), cpt2.invoke(get));
		
		s1.undeployService(nameservice);
		cpt2.invoke(plus);
		assertEquals(Integer.valueOf(19), cpt2.invoke(get));
		try {
			cpt1.invoke(get);
			assertTrue(false);
		}catch(NoSuchObjectException e) {
			assertTrue(true);
		}
	}

}





