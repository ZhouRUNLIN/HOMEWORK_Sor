package srcs.interpretor.test;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;

public abstract class AbstractTest {

	protected File file_out;
	protected PrintStream streamout;
	
	@Before
	public void setup() throws IOException {
		file_out=Files.createTempFile("testInterpretor", "").toFile();
		streamout=new PrintStream(file_out);
	}

	@After
	public void cleanup() throws IOException {
		streamout.close();
		Files.delete(file_out.toPath());
	}
	
	public static void deleteRec(File file) {
		if(file.isDirectory()) {
			for(File subfile:file.listFiles()) {
				deleteRec(subfile);
			}
		}
		System.err.println("Deleting "+file.getAbsolutePath());
		file.delete();
	}
	
	public static void produceAndCompileAddCommand(File dir) throws Exception {
		
		File srcs_dir = new File(dir, "srcs");
		if(!srcs_dir.mkdir()) throw new IllegalStateException("Impossible de creer un le dossier"+srcs_dir.getAbsolutePath());
		File java_file = new File(srcs_dir, "Add.java");
		try(PrintStream ps = new PrintStream(java_file)) {
			ps.println("package srcs;");
			ps.println("import java.util.List;");
			ps.println("import java.io.PrintStream;");
			ps.println("import srcs.interpretor.Command;");
			ps.println("");
			ps.println("public class Add implements Command {");
			ps.println(" private final int a;");
			ps.println(" private final int b;");
			ps.println("");
			ps.println(" public Add(List<String> args) {");
			ps.println("  if(args.size() < 3) {");
			ps.println("   throw new IllegalArgumentException(\"usage add : <operande1> <operande2>\");");
			ps.println("  }");
			ps.println("  this.a=Integer.parseInt(args.get(1));");
			ps.println("  this.b=Integer.parseInt(args.get(2));");
			ps.println(" }");
			ps.println("");
			ps.println(" @Override");
			ps.println(" public void execute(PrintStream out) {");
			ps.println("  out.println(a+b);");
			ps.println(" }");
			ps.println("}");
		}
		System.out.println();
		ProcessBuilder pb = new ProcessBuilder("javac","-cp",System.getProperty("java.class.path"),java_file.getAbsolutePath());
		pb.inheritIO();
		Process proc = pb.start();
		int ret = proc.waitFor();
		if(ret!=0) throw new IllegalStateException("Impossible de compiler "+java_file.getAbsolutePath());
	}
}
