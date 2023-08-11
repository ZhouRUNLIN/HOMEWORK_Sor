//dans le ficher SansEspaceException.java
public class SansEspaceException extends Exception {
	public SansEspaceException(String s){
		super(s);
		System.out.println("Error : Sans espace pour cet object !");
	}
}