package srcs.interpretor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;

import org.junit.Test;

import srcs.interpretor.Cat;
import srcs.interpretor.CommandInterpretor;
import srcs.interpretor.CommandNotFoundException;
import srcs.interpretor.Echo;

public class CommandInterpretorTest1 extends AbstractTest{

	
	
	
	@Test
	public void testCommandNotFound() {
		CommandInterpretor i = new CommandInterpretor();
		assertThrows(CommandNotFoundException.class,()->i.perform("toto tutu", streamout));	
		assertThrows(CommandNotFoundException.class,()->i.perform("Echo toto", streamout));	
		assertThrows(CommandNotFoundException.class,()->i.perform("Cat titi", streamout));	
		streamout.flush();
		assertEquals(0,file_out.length());
	}
	
	@Test
	public void testCommandEmpty() throws Exception{
		CommandInterpretor i = new CommandInterpretor();
		i.perform("", streamout);
		streamout.flush();
		assertEquals(0,file_out.length());
	}
	
	
	@Test
	public void testCommandEcho() throws Exception{
		CommandInterpretor i = new CommandInterpretor();
		i.perform("echo bonjour",streamout);
		streamout.flush();
		assertNotEquals(0,file_out.length());
		try(BufferedReader reader = new BufferedReader(new FileReader(file_out))) {
			String line= reader.readLine();
			assertNull(reader.readLine());
			assertEquals("bonjour",line);
		}
	}
	
	
	@Test
	public void testCommandEcho2() throws Exception{
		CommandInterpretor i = new CommandInterpretor();
		i.perform("echo bonjour   tout   le   monde",streamout);
		streamout.flush();
		assertNotEquals(0,file_out.length());
		try(BufferedReader reader = new BufferedReader(new FileReader(file_out))) {
			String line= reader.readLine();
			assertNull(reader.readLine());
			assertEquals("bonjour tout le monde",line);
		}
	}
	
	
	
	
	@Test
	public void testCommandCatError1() throws Exception{
				
		CommandInterpretor i = new CommandInterpretor();
		try {
			i.perform("cat file"+System.currentTimeMillis(),streamout);
			assertFalse(true);
		}catch(InvocationTargetException e) {
			assertNotNull(e.getCause());
			assertTrue(e.getCause() instanceof IllegalArgumentException);
		}catch(Exception e) {
			assertFalse(true);
		}
		streamout.flush();
		assertEquals(0,file_out.length());
	}
	
	@Test
	public void testCommandCatError2() throws Exception{
				
		CommandInterpretor i = new CommandInterpretor();
		File dir_tmp=Files.createTempDirectory("tmp").toFile();
		try {
			i.perform("cat "+dir_tmp.getAbsolutePath(),streamout);
			assertFalse(true);
		}catch(InvocationTargetException e) {
			assertNotNull(e.getCause());
			assertTrue(e.getCause() instanceof IllegalArgumentException);
		}catch(Exception e) {
			assertFalse(true);
		}
		streamout.flush();
		assertEquals(0,file_out.length());
		Files.delete(dir_tmp.toPath());
		
	}
	
	@Test
	public void testCommandCatError3() throws Exception{
				
		CommandInterpretor i = new CommandInterpretor();
		try {
			i.perform("cat ",streamout);
			assertFalse(true);
		}catch(InvocationTargetException e) {
			assertNotNull(e.getCause());
			assertTrue(e.getCause() instanceof IllegalArgumentException);
		}catch(Exception e) {
			assertFalse(true);
		}
		streamout.flush();
		assertEquals(0,file_out.length());		
	}
	
	
	@Test
	public void testCommandCat2() throws Exception{
		File file_tmp = Files.createTempFile("tmp","testcat").toFile();
		try(PrintStream out = new PrintStream(file_tmp)){
			for(int x=0;x<100;x++) {
				out.println(x+" "+(x*x));
			}
		}
		
		CommandInterpretor i = new CommandInterpretor();
		i.perform("cat "+file_tmp.getAbsolutePath(),streamout);
		streamout.flush();
		assertNotEquals(0,file_out.length());
		try(BufferedReader reader = new BufferedReader(new FileReader(file_out))) {
			String line;
			int x=0;
			while((line=reader.readLine())!=null) {
				assertEquals(x+" "+(x*x),line);
				x++;
			}
			assertNull(reader.readLine());
		}
		Files.delete(file_tmp.toPath());
	}
	
	@Test
	public void testTypeCommand() {
		CommandInterpretor i = new CommandInterpretor();
		assertNotNull(i.getClassOf("echo"));
		assertEquals(Echo.class,i.getClassOf("echo"));
		assertNotNull(i.getClassOf("cat"));
		assertEquals(Cat.class,i.getClassOf("cat"));		
	}
	
	
	
	
	

}
