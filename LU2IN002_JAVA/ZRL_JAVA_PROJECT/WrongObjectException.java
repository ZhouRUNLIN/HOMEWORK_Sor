//dans le ficher WrongObjectException.java
public class WrongObjectException extends Exception {
	public WrongObjectException(String s){
		super(s);
		System.out.println("Error : Cet object n'exist pas !");
	}
}