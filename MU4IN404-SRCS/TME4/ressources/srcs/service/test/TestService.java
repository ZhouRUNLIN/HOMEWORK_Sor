package srcs.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import srcs.service.MyProtocolException;
import srcs.service.ServeurMultiThread;
import srcs.service.VoidResponse;
import srcs.service.annuaire.AnnuaireService;
import srcs.service.calculatrice.Calculatrice.ResDiv;
import srcs.service.calculatrice.CalculatriceService;

public class TestService {

	public static int portannuaire=4234;
	public static int portcalculette=14234;
	
	private Thread annuaire;
	private Thread calculette;
	
	@Before
	public void setUp() throws Exception {
		
		annuaire =new Thread( () -> new ServeurMultiThread(portannuaire, AnnuaireService.class).listen());
		calculette =new Thread( () -> new ServeurMultiThread(portcalculette, CalculatriceService.class).listen());

		annuaire.start();
		calculette.start();
		Thread.sleep(200);
		
	}

	@After
	public void clean() {
		annuaire.interrupt();
		calculette.interrupt();
		portannuaire++;
		portcalculette++;
	}
	
	
	@Test
	public void testCalculatriceAdd() throws UnknownHostException, IOException, ClassNotFoundException {
		try(Socket service = new Socket("localhost", portcalculette)){
			try(ObjectOutputStream oos = new ObjectOutputStream(service.getOutputStream())){
				try(ObjectInputStream ois = new ObjectInputStream(service.getInputStream())){
					
					oos.writeUTF("add");
					oos.writeInt(4);
					oos.writeInt(3);
					oos.flush();
					Object ret = ois.readObject();
					assertTrue(ret instanceof Integer);
					int result = (Integer) ret;
					assertEquals(7, result);
					
				}
			}
		} 		
	}
	
	
	@Test
	public void testCalculatriceSous() throws UnknownHostException, IOException, ClassNotFoundException {
		try(Socket service = new Socket("localhost", portcalculette)){
			try(ObjectOutputStream oos = new ObjectOutputStream(service.getOutputStream())){
				try(ObjectInputStream ois = new ObjectInputStream(service.getInputStream())){
					
					oos.writeUTF("sous");
					oos.writeInt(4);
					oos.writeInt(3);
					oos.flush();
					Object ret = ois.readObject();
					assertTrue(ret instanceof Integer);
					int result = (Integer) ret;
					assertEquals(1, result);
					
				}
			}
		} 		
	}
	
	@Test
	public void testCalculatriceMult() throws UnknownHostException, IOException, ClassNotFoundException {
		try(Socket service = new Socket("localhost", portcalculette)){
			try(ObjectOutputStream oos = new ObjectOutputStream(service.getOutputStream())){
				try(ObjectInputStream ois = new ObjectInputStream(service.getInputStream())){
					
					oos.writeUTF("mult");
					oos.writeInt(4);
					oos.writeInt(3);
					oos.flush();
					Object ret = ois.readObject();
					assertTrue(ret instanceof Integer);
					int result = (Integer) ret;
					assertEquals(12, result);
					
				}
			}
		} 		
	}
	
	@Test
	public void testCalculatriceDiv() throws UnknownHostException, IOException, ClassNotFoundException {
		try(Socket service = new Socket("localhost", portcalculette)){
			try(ObjectOutputStream oos = new ObjectOutputStream(service.getOutputStream())){
				try(ObjectInputStream ois = new ObjectInputStream(service.getInputStream())){
					
					oos.writeUTF("div");
					oos.writeInt(4);
					oos.writeInt(3);
					oos.flush();
					Object ret = ois.readObject();
					assertTrue(ret instanceof ResDiv);
					ResDiv result = (ResDiv) ret;
					assertEquals(1, result.getQuotient());
					assertEquals(1, result.getReste());
					
				}
			}
		} 		
	}
	
	@Test
	public void testCalculatriceErreur1() throws UnknownHostException, IOException, ClassNotFoundException {
		try(Socket service = new Socket("localhost", portcalculette)){
			try(ObjectOutputStream oos = new ObjectOutputStream(service.getOutputStream())){
				try(ObjectInputStream ois = new ObjectInputStream(service.getInputStream())){
					
					oos.writeUTF("plus");
					oos.writeInt(4);
					oos.writeInt(3);
					oos.flush();
					Object ret = ois.readObject();
					assertTrue(ret instanceof MyProtocolException);					
				}
			}
		} 		
	}
	
	
	@Test
	public void testAnnuaireSimple() throws UnknownHostException, IOException, ClassNotFoundException {
		String nom = "srcs";
		String value="tme";
		
		try(Socket service = new Socket("localhost", portannuaire)){
			try(ObjectOutputStream oos = new ObjectOutputStream(service.getOutputStream())){
				try(ObjectInputStream ois = new ObjectInputStream(service.getInputStream())){
					
					
					oos.writeUTF("bind");
					oos.writeUTF(nom);
					oos.writeUTF(value);
					oos.flush();
					Object ret = ois.readObject();
					assertTrue(ret instanceof VoidResponse);
										
				}
			}
		} 
		
		try(Socket service = new Socket("localhost", portannuaire)){
			try(ObjectOutputStream oos = new ObjectOutputStream(service.getOutputStream())){
				try(ObjectInputStream ois = new ObjectInputStream(service.getInputStream())){
					oos.writeUTF("lookup");
					oos.writeUTF(nom);
					oos.flush();
					Object ret = ois.readObject();
					assertTrue(ret instanceof String);
					String result = (String) ret;
					assertEquals(value,result);					
				}
			}
		}
		
		try(Socket service = new Socket("localhost", portannuaire)){
			try(ObjectOutputStream oos = new ObjectOutputStream(service.getOutputStream())){
				try(ObjectInputStream ois = new ObjectInputStream(service.getInputStream())){
					oos.writeUTF("lookup");
					oos.writeUTF("nomtest");
					oos.flush();
					Object ret = ois.readObject();
					assertTrue(ret instanceof String);
					String result = (String) ret;
					assertEquals("",result);					
				}
			}
		}
		
		try(Socket service = new Socket("localhost", portannuaire)){
			try(ObjectOutputStream oos = new ObjectOutputStream(service.getOutputStream())){
				try(ObjectInputStream ois = new ObjectInputStream(service.getInputStream())){
					oos.writeUTF("unbind");
					oos.writeUTF(nom);
					oos.flush();
					Object ret = ois.readObject();
					assertTrue(ret instanceof VoidResponse);				
				}
			}
		}
		
		try(Socket service = new Socket("localhost", portannuaire)){
			try(ObjectOutputStream oos = new ObjectOutputStream(service.getOutputStream())){
				try(ObjectInputStream ois = new ObjectInputStream(service.getInputStream())){
					oos.writeUTF("lookup");
					oos.writeUTF(nom);
					oos.flush();
					Object ret = ois.readObject();
					assertTrue(ret instanceof String);
					String result = (String) ret;
					assertEquals("",result);					
				}
			}
		}
	}
	
	AtomicBoolean erreur = new AtomicBoolean(false);
	
	@Test
	public void testAnnuaireRaffale() throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
	
		int nbclients = 100 ;
		List<Thread> clients = new ArrayList<>(nbclients);
		for(int i=0; i< nbclients;i++) {
			final int tmp = i;
			Thread t = new Thread( ()->   {
				try(Socket service = new Socket("localhost", portannuaire)){
					try(ObjectOutputStream oos = new ObjectOutputStream(service.getOutputStream())){
						try(ObjectInputStream ois = new ObjectInputStream(service.getInputStream())){
							oos.writeUTF("bind");
							oos.writeUTF("nom"+tmp);
							oos.writeUTF("value"+tmp);
							oos.flush();
							Object ret = ois.readObject();
							erreur.set(! (ret instanceof VoidResponse));
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
							erreur.set(true);
						}
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
					erreur.set(true);
				} catch (IOException e) {
					e.printStackTrace();
					erreur.set(true);
				} 
			});
			clients.add(t);
			t.start();
		}
		for(Thread t : clients) {
			t.join();
		}
		assertFalse(erreur.get());
		
		for(int i=0; i< nbclients;i++) {
			try(Socket service = new Socket("localhost", portannuaire)){
				try(ObjectOutputStream oos = new ObjectOutputStream(service.getOutputStream())){
					try(ObjectInputStream ois = new ObjectInputStream(service.getInputStream())){
						System.err.println("Test presence nom"+i);
						oos.writeUTF("lookup");
						oos.writeUTF("nom"+i);
						oos.flush();
						Object ret = ois.readObject();
						assertTrue(ret instanceof String);
						String result = (String) ret;
						assertEquals("value"+i,result);	
					} 
				}
			} 
		}
		
		
		
	}
	

}
