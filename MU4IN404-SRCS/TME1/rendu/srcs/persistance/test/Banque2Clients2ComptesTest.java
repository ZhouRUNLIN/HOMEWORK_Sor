package srcs.persistance.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import srcs.banque.Banque;
import srcs.banque.Client;
import srcs.banque.Compte;
import srcs.persistance.PersistanceSauvegardable;
import srcs.persistance.Sauvegardable;

public class Banque2Clients2ComptesTest {

	Path fichier;
	
	@Before
	public void setup() throws IOException{
		fichier=Files.createTempFile("banque", "");
	}
	
	@After
	public void cleanup() throws IOException {
		Files.delete(fichier);
	}
	
	@Test
	public void test() throws Exception {
				
		Client cli1 = new Client("client1", new Compte("cpt1"));
		Client cli2 = new Client("client2", new Compte("cpt2"));
				
		Banque banque = new Banque();
		banque.addNewClient(cli1);
		banque.addNewClient(cli2);
		banque.getClient("client1").getCompte().crediter(10);
		banque.getClient("client2").getCompte().debiter(2);
		assertEquals(10,banque.getClient("client1").getCompte().getSolde(),0);
		assertEquals(-2,banque.getClient("client2").getCompte().getSolde(),0);
		assertEquals(2,banque.nbClients());
		assertEquals(2,banque.nbComptes());
			
		
		
		PersistanceSauvegardable.save(fichier.toFile().getAbsolutePath(), banque);
		Sauvegardable s  = PersistanceSauvegardable.load(fichier.toFile().getAbsolutePath()); 
		assertTrue(s instanceof Banque);
		banque = (Banque) s;
		assertEquals(10,banque.getClient("client1").getCompte().getSolde(),0);
		assertEquals(-2,banque.getClient("client2").getCompte().getSolde(),0);
		assertEquals(2,banque.nbClients());
		assertEquals(2,banque.nbComptes());
		banque.getClient("client1").getCompte().crediter(5);
		banque.getClient("client2").getCompte().debiter(2);
		assertEquals(15,banque.getClient("client1").getCompte().getSolde(),0);
		assertEquals(-4,banque.getClient("client2").getCompte().getSolde(),0);

		
				
	}
	
	
	

}
