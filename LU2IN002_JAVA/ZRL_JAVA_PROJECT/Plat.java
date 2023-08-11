//dans le ficher Plat.java
public abstract class Plat{
	private String nom;

	public Plat(String nom){
		this.nom = nom;
	}

	public String getNom(){
		return nom;
	}

	public String toString(){
		return " | nom : " + nom;
	}
	public abstract double getPrix();
	
	public abstract int getVal();

	public abstract Plat clone();

	public abstract String eat();

	public abstract void commentaire();
}