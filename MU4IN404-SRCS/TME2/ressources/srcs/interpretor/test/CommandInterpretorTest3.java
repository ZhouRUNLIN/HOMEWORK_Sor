package srcs.interpretor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;

import org.junit.Test;

import srcs.interpretor.Cat;
import srcs.interpretor.Command;
import srcs.interpretor.CommandInterpretor;
import srcs.interpretor.CommandNotFoundException;
import srcs.interpretor.Echo;

public class CommandInterpretorTest3 extends AbstractTest{
	
	
	
	@Test
	public void testTypeCommand() {
		CommandInterpretor i = new CommandInterpretor();
		Class<?extends Command> clsave = i.getClassOf("save");
		assertNotNull(clsave);
		assertTrue(clsave.isMemberClass());	
	}
	
	
	@Test
	public void testSave() throws Exception {
		CommandInterpretor i = new CommandInterpretor();
		File tmp_dir = Files.createTempDirectory("commands").toFile();
		File save_file = Files.createTempFile("save_file", "tmp").toFile();
		produceAndCompileAddCommand(tmp_dir);
		i.perform("deploy add "+tmp_dir.getAbsolutePath()+" srcs.Add", streamout);
		i.perform("save "+save_file.getAbsolutePath(), streamout);

		CommandInterpretor ibis = new CommandInterpretor(save_file.getAbsolutePath());
		assertNotNull(ibis.getClassOf("echo"));
		assertEquals(Echo.class,ibis.getClassOf("echo"));
		assertNotNull(ibis.getClassOf("cat"));
		assertEquals(Cat.class,ibis.getClassOf("cat"));
		Class<?extends Command> cladd = ibis.getClassOf("add");
		assertNotNull(cladd);
		ibis.perform("add 5 4", streamout);

		ibis.perform("undeploy add ", streamout);
		File save_file2 = Files.createTempFile("save_file2", "tmp").toFile();

		ibis.perform("save "+save_file2.getAbsolutePath(), streamout);

		CommandInterpretor iter = new CommandInterpretor(save_file2.getAbsolutePath());
		assertNull(iter.getClassOf("add"));
		assertThrows(CommandNotFoundException.class,()->iter.perform("add 50 40", streamout));

		try(BufferedReader reader = new BufferedReader(new FileReader(file_out))) {
			assertEquals("9",reader.readLine());
			assertNull(reader.readLine());
		}

		deleteRec(tmp_dir);
		Files.delete(save_file.toPath());
		Files.delete(save_file2.toPath());
	}

}
