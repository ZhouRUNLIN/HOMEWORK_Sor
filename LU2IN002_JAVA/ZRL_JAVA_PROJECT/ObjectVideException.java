//dans le ficher ObjectVideException.java
public class ObjectVideException extends Exception {
	public ObjectVideException(String s){
		super(s);
		System.out.println("Error : Cet object est vide !");
	}
}