//dans le ficher Client.java
import java.util.ArrayList;

public class Client{
	private String nom;
	private final int id;
	private static int cpt = 0;
	private ArrayList<Plat> listePlat;
	private int nbPlat = 0;

	public Client(String nom){
		this.nom = nom;
		cpt++;
		id = cpt;
		listePlat = new ArrayList<Plat>();
	}

	public void ajouter(Plat plat){
		listePlat.add(plat);
		nbPlat++;
	}

	public void supprimer(Plat plat) throws WrongObjectException, ObjectVideException{
		if(nbPlat == 0)
			throw new ObjectVideException(nom+id);

		for(int i = 0; i < nbPlat; i++){
			if(listePlat.get(i).getNom() == plat.getNom()){
				listePlat.remove(i);
				nbPlat--;
				break;
			}
			if(i + 1 == nbPlat)
				throw new WrongObjectException(nom+id);
		}
	}

	public void modifier(Plat plat1, Plat plat2) throws WrongObjectException, ObjectVideException{
		if(nbPlat == 0)
			throw new ObjectVideException(nom+id);

		for(int i = 0; i < nbPlat; i++){
			if(listePlat.get(i).getNom() == plat1.getNom()){
				listePlat.set(i, plat2);
				break;
			}
			if(i+1 == nbPlat)
				throw new WrongObjectException(nom+id);
		}
	}

	public String toString(){
		String str = nom + " | id : " + id;
		if(listePlat.size() == 0)		return str;
		for(int i = 0; i < listePlat.size() - 1; i++){
			str += listePlat.get(i) + ", ";
		}
		str += listePlat.get(listePlat.size() - 1);
		return str;
	}

	public double prix() throws ObjectVideException{
		if(nbPlat == 0)
			throw new ObjectVideException(nom+id);

		double prixTotal = 0;
		for(int i = 0; i < nbPlat; i++){
			prixTotal += listePlat.get(i).getPrix();
		}
		return prixTotal;
	}

	public int getId(){
		return id;
	}
	
	public void suggestion() throws ObjectVideException{
		if(nbPlat == 0)
			throw new ObjectVideException(nom+id);

		int val = 0;
		for(int i = 0; i < nbPlat; i++){
			val += listePlat.get(i).getVal();
		}
		if(val > 150)		System.out.println("On pense que tu doit enlever certain plat");
		else if(val < 80) 	System.out.println("On pense que tu doit choisir plus");
		else 				System.out.println("votre choix est adapte");
	}

	public Client clone(){
		Client client = new Client(nom);
		for(int i = 0; i < nbPlat; i++){
			client.ajouter(this.listePlat.get(i).clone());
		}
		return client;
	}

	public String eat() throws ObjectVideException{
		if(nbPlat == 0)
			throw new ObjectVideException(nom+id);

		String str = nom + id + " manger: ";
		for(int i = 0; i < nbPlat; i++){
			str += listePlat.get(i).eat();
		}
		str += "!";
		return str;
	}

	public void commentaire() throws ObjectVideException{
		if(nbPlat == 0)
			throw new ObjectVideException(nom+id);

		System.out.println(nom + id + " :");
		for(int i = 0; i < nbPlat; i++){
			listePlat.get(i).commentaire();
		}
	}
}