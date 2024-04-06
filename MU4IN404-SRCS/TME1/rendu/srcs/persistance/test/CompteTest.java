package srcs.persistance.test;

import static org.junit.Assert.assertEquals;
import static srcs.persistance.PersistanceCompte.loadCompte;
import static srcs.persistance.PersistanceCompte.saveCompte;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import srcs.banque.Compte;
public class CompteTest {

	Path fichier;
	
	@Before
	public void setup() throws IOException{
		fichier=Files.createTempFile("compte", "");
	}
	
	@After
	public void cleanup() throws IOException {
		Files.delete(fichier);
	}

	
	@Test
	public void testCompte() throws Exception {
		Compte cpt = new Compte("cpt1");
		cpt.crediter(5);
		cpt.debiter(2);
		assertEquals(3,cpt.getSolde(),0);
		saveCompte(fichier.toFile().getAbsolutePath(), cpt);
		Compte bis = loadCompte(fichier.toFile().getAbsolutePath());
		assertEquals(cpt, bis);
		assertEquals(3,bis.getSolde(),0);
	}
}
