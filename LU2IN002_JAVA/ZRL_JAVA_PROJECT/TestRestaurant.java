public class TestRestaurant{
	public static void main(String[]angs){
		//la creation de menu 
		Menu menu = Menu.getInstance();

		Plat cafe = new Boissons("cafe", 2.5, 15);
		Plat coca = new Boissons("coca", 4, 20);
		Plat salade = new Entree("salade", 8, 35);
		Plat croquette = new Entree("croquette_merguez", 7.5, 25);
		Plat coteBoeuf = new PlatChaud("cote_boeuf", 20, 70);
		Plat burger  = new PlatChaud("burger", 18, 80);
		Plat pizza   = new PlatChaud("pizza", 25, 75);
		Plat[] listePlat = new Plat[7];
		listePlat[0] = cafe;
		listePlat[1] = coca;
		listePlat[2] = salade;
		listePlat[3] = croquette;
		listePlat[4] = burger;
		listePlat[5] = coteBoeuf;
		listePlat[6] = pizza;

		menu.remplirMenu(listePlat);
		System.out.println(menu);

		Restaurant res = new Restaurant(10);

		Table table1 = new Table(8);
		Client client1 = new Client("client");

		try{
			client1.ajouter(cafe);
			client1.ajouter(coca);
			client1.ajouter(salade);
			client1.ajouter(coteBoeuf);
			client1.ajouter(burger);
			
			client1.supprimer(cafe);

			System.out.print("les suggestions pour client 1 : ");
			client1.suggestion();
			System.out.print("\n");
			System.out.println(client1.eat());
			System.out.println("\nles commentaires de : ");
			client1.commentaire();
			
			Client client2 = client1.clone();
			Client client3 = client1.clone();
			Client client4 = client1.clone();
			Client client5 = client1.clone();
			Client client6 = client1.clone();
	
			table1.ajouter(client1);
			table1.ajouter(client2);
			table1.ajouter(client3);
			table1.ajouter(client4);
			table1.ajouter(client5);
			table1.ajouter(client6);

			System.out.print("\n");
			System.out.println(table1.eat());
			table1.payerEnsemble();
			System.out.print("\nles commentaires de ");
			table1.commentaire();
			
			Table table2 = table1.clone();
			System.out.println("\nPour Table 2 : ");
			table2.payerSepare();

			System.out.print("\n");
			res.ajouter(table1);
			res.ajouter(table2);
			System.out.println(res);
		}
		catch(WrongObjectException e1){
			System.out.println("test1 : fin de la methode " + e1);
		}
		catch(SansEspaceException e2){
			System.out.println("test2 : fin de la methode " + e2);
		}
		catch(ObjectVideException e3){
			System.out.println("test3 : fin de la methode " + e3);
		}
		finally{
			System.out.println("Fin des tests du main !");
		}
	}
}