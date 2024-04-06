package srcs.persistance.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static srcs.persistance.PersistanceSauvegardable.load;
import static srcs.persistance.PersistanceSauvegardable.save;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import srcs.banque.Compte;
import srcs.persistance.Sauvegardable;
public class SauvegardableTest {

	Path fichier;
	
	@Before
	public void setup() throws IOException{
		fichier=Files.createTempFile("save", "");
	}
	
	@After
	public void cleanup() throws IOException {
		Files.delete(fichier);
	}
	
	@Test
	public void testSauvegardable() throws Exception {
		Compte cpt = new Compte("cpt1");
		cpt.crediter(5.0);
		cpt.debiter(2.0);
		assertEquals(3,cpt.getSolde(),0);
		save(fichier.toFile().getAbsolutePath(), cpt);
		Sauvegardable bis = load(fichier.toFile().getAbsolutePath());
		assertTrue(bis instanceof Compte);
		assertEquals(cpt, bis);
		assertEquals(3,((Compte)bis).getSolde(),0);
	}

}
