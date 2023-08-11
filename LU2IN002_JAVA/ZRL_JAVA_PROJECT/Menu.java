//dans le ficher Menu.java
import java.util.ArrayList;

public class Menu{
	private ArrayList<Plat> menuType;

	private static Menu menu = new Menu();

	private Menu(){
		menuType = new ArrayList<Plat>();
		// ce qui cree une menu vide
	}

	//cette methode ajouter les plats dans la menu
	public void remplirMenu(Plat[] listePlat){
		for(int i = 0; i < listePlat.length; i++){
			menuType.add(listePlat[i]);
		}
	}

	public static Menu getInstance(){
		return menu;
	}

	public String toString(){
		String str = "Menu :\n";
		for(int i = 0; i < menuType.size(); i++){
			str += menuType.get(i) + ",\n";
		}
		return str;
	}
}