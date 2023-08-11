//dans le ficher Dessert.java
public class Dessert extends PlatFroid implements Estimer{
	private int val;
	private double prix;

	public Dessert(String nom, double prix, int val){
		super(nom);
		this.val = val;
		this.prix = prix;
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
		return new Dessert(super.getNom(), prix, val);
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
