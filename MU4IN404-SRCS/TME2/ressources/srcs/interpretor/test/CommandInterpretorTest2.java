package srcs.interpretor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;

import org.junit.Test;

import srcs.interpretor.Command;
import srcs.interpretor.CommandInterpretor;
import srcs.interpretor.CommandNotFoundException;
import srcs.interpretor.Echo;

public class CommandInterpretorTest2 extends AbstractTest{
	
	
	
	@Test
	public void testTypeCommand() {
		CommandInterpretor i = new CommandInterpretor();
		Class<?extends Command> cldeploy = i.getClassOf("deploy");
		assertNotNull(cldeploy);
		assertTrue(cldeploy.isMemberClass());	
		assertEquals(CommandInterpretor.class, cldeploy.getEnclosingClass());
		
		
		Class<?extends Command> clundeploy = i.getClassOf("undeploy");
		assertNotNull(clundeploy);
		assertTrue(clundeploy.isMemberClass());
		assertEquals(CommandInterpretor.class, clundeploy.getEnclosingClass());	
		
		assertSame(clundeploy.getClassLoader(),cldeploy.getClassLoader());
	}
	
	
	@Test
	public void testUnDeployEcho() throws Exception {
		CommandInterpretor i = new CommandInterpretor();
		Class<?extends Command> clecho = i.getClassOf("echo");
		assertNotNull(clecho);
		assertEquals(Echo.class,clecho);
		i.perform("undeploy echo", streamout);
		clecho = i.getClassOf("echo");
		assertNull(clecho);
		assertThrows(CommandNotFoundException.class,()->i.perform("echo bonjour", streamout));			
		streamout.flush();
		assertEquals(0,file_out.length());
	}
	
	@Test
	public void testDeployUndeploy() throws Exception{
		CommandInterpretor i = new CommandInterpretor();
				
		File tmp_dir = Files.createTempDirectory("commands").toFile();
		produceAndCompileAddCommand(tmp_dir);
		assertThrows(IllegalArgumentException.class, ()-> i.perform("deploy add "+tmp_dir.getAbsolutePath()+System.currentTimeMillis()+" srcs.Add", streamout));
		for(int x=0;x<10;x++) {
			Class<?extends Command> cladd = i.getClassOf("add");
			assertNull(cladd);
			i.perform("deploy add "+tmp_dir.getAbsolutePath()+" srcs.Add", streamout);
			assertThrows(IllegalArgumentException.class, ()-> i.perform("deploy add "+tmp_dir.getAbsolutePath()+" srcs.Add", streamout));
			cladd = i.getClassOf("add");
			assertNotNull(cladd);
			assertNotSame(i.getClassOf("deploy").getClassLoader(),cladd.getClassLoader());

			i.perform("add "+x+" "+(x+1), streamout);
			
			i.perform("undeploy add ", streamout);
		}
		
		try(BufferedReader reader = new BufferedReader(new FileReader(file_out))) {
			String line;
			int x=0;
			while((line=reader.readLine())!=null) {
				assertEquals((x+x+1)+"",line);
				x++;
			}
			assertNull(reader.readLine());
		}
		
		deleteRec(tmp_dir);
	}
	
	
	

}
