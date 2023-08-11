//dans le ficher Table.java
public class Table{
	private final int id;
	private static int cpt = 0;
	private Client[] listeClient;
	private int nbClient = 0;

	public Table(int maxClient){
		listeClient = new Client[maxClient];
		cpt++;
		id = cpt;
	}

	public void ajouter(Client client){
		try{
			if(nbClient >= listeClient.length)
				throw new SansEspaceException("Table" + id);
		}		
		catch(SansEspaceException e){
			System.out.println("Capture de l'exception" + e);
		}
		listeClient[nbClient] = client;
		nbClient++;
	}

	public String toString(){
		String str = "Table " + id + " :\n";
		for(int i = 0; i < nbClient; i++){
			str += listeClient[i] + "\n";
		}
		return str;
	}

	public Client[] getListe(){
		return listeClient;
	}

	public Table clone(){
		Table table = new Table(listeClient.length);
		for(int i = 0; i < nbClient; i++){
			table.ajouter(listeClient[i].clone());
		}
		return table;
	}

	public void payerEnsemble() throws ObjectVideException{
		if(nbClient == 0)
			throw new ObjectVideException("Table" + id);

		double prixTotal = 0;
		for(int i = 0; i < nbClient; i++){
			prixTotal += listeClient[i].prix();
		}
		System.out.println("Table " + id + " doit payer " + prixTotal + " euros");
	}

	public void payerSepare() throws ObjectVideException{
		if(nbClient == 0)
			throw new ObjectVideException("Table" + id);

		for(int i = 0; i < nbClient; i++){
			System.out.println("Client " + listeClient[i].getId() + "doit payer " + listeClient[i].prix() + " euros");
		}
	}

	public String eat() throws ObjectVideException{
		if(nbClient == 0)
			throw new ObjectVideException("Table" + id);
		String str = "";
		for(int i = 0; i < nbClient; i++){
			str += listeClient[i].eat() + "\n";
		}
		return str;
	}

	public void commentaire() throws ObjectVideException{
		if(nbClient == 0)
			throw new ObjectVideException("Table" + id);
		System.out.println("Table" + id + " :");
		for(int i = 0; i < nbClient; i++){
			listeClient[i].commentaire();
		}
	}
}