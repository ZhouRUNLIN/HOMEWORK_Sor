//dans le ficher PlatChaud.java
public class PlatChaud extends Plat implements Estimer{
	private double prix;
	private int val;

	public PlatChaud(String nom, double prix, int val){
		super(nom);
		this.prix = prix;
		this.val = val;
	}

	public String toString(){
		return super.toString() + ", prix : " + prix + ", val : " + val;
	}

	public double getPrix(){
		return prix;
	}

	public int getVal(){
		return val;
	}

	public Plat clone(){
		return new PlatChaud(super.getNom(), prix, val);
	}

	public String eat(){
		return super.getNom() + " ";
	}

	public void commentaire(){
		float val = (float)(Math.random());
		if(val < 0.3)
			System.out.println("J'aime " + super.getNom());
		if(val >= 0.3 && val < 0.6)
			System.out.println("Je pense que " + super.getNom() +" est pas mal.");
		if( val >= 0.6)
			System.out.println("Je n'aime pas " + super.getNom());
	}
}