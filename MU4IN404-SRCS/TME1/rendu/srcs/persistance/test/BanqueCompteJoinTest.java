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

public class BanqueCompteJoinTest {

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
	public void test() throws Exception{

		Client cli1 = new Client("client1", new Compte("cpt1"));
		Client cli2 = new Client("client2", cli1.getCompte());

		Banque banque = new Banque();
		banque.addNewClient(cli1);
		banque.addNewClient(cli2);
		banque.getClient("client1").getCompte().crediter(10);
		banque.getClient("client2").getCompte().debiter(2);
		assertEquals(8,banque.getClient("client1").getCompte().getSolde(),0);
		assertTrue(banque.getClient("client2").getCompte().getSolde() == banque.getClient("client1").getCompte().getSolde());
		assertEquals(2,banque.nbClients());
		assertEquals(1,banque.nbComptes());
	

		PersistanceSauvegardable.save(fichier.toFile().getAbsolutePath(), banque);
		Sauvegardable s  = PersistanceSauvegardable.load(fichier.toFile().getAbsolutePath()); 
		assertTrue(s instanceof Banque);
		banque = (Banque) s;
		assertEquals(8,banque.getClient("client1").getCompte().getSolde(),0);
		assertTrue(banque.getClient("client2").getCompte().getSolde() == banque.getClient("client1").getCompte().getSolde());
		assertEquals(2,banque.nbClients());
		assertEquals(1,banque.nbComptes());
		banque.getClient("client1").getCompte().crediter(10);
		assertEquals(18,banque.getClient("client1").getCompte().getSolde(),0);
		//assertTrue(banque.getClient("client2").getCompte().getSolde() == banque.getClient("client1").getCompte().getSolde());

		

	}

}
