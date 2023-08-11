//dans le ficher Restaurant.java
public class Restaurant{
	private Table[] listeTable;
	private int nbTable = 0;

	public Restaurant(int maxTable){
		listeTable = new Table[maxTable];
	}

	public void ajouter(Table table) throws SansEspaceException{
		if(nbTable < listeTable.length){
			listeTable[nbTable] = table;
			nbTable++;
		}
		else 		
			throw new SansEspaceException("Restaurant ");
	}

	public String toString(){
		String str = "Dans ce restaurant, il y a " + nbTable + " tables\n";
		for(int i = 0; i < nbTable; i++){
			str += listeTable[i] + "\n";
		}
		return str;
	}
}