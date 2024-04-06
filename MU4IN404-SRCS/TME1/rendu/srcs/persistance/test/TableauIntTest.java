package srcs.persistance.test;

import static org.junit.Assert.assertArrayEquals;
import static srcs.persistance.PersistanceArray.loadArrayInt;
import static srcs.persistance.PersistanceArray.saveArrayInt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
public class TableauIntTest {
	
	Path fichier;
	
	@Before
	public void setup() throws IOException{
		fichier=Files.createTempFile("tab", "");
	}
	
	@After
	public void cleanup() throws IOException {
		Files.delete(fichier);
	}
	
	@Test
	public void testTableauInt() throws IOException {
		int[] test = new int[] {0,4,5,6000,-2,-27543};
		saveArrayInt(fichier.toFile().getAbsolutePath(), test);
		int[] tab = loadArrayInt(fichier.toFile().getAbsolutePath());
		assertArrayEquals(test, tab);			
		
	}
}
