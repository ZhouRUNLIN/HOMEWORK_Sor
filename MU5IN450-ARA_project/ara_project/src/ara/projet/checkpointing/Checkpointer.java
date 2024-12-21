package ara.projet.checkpointing;

import peersim.core.Node;

public interface Checkpointer {

	
	/*revenir à un point de reprise cohérent, on remettra les noeuds fautif à l'état OK pour simuler leur redémarage*/
	void recover(Node host);

	/*créer un point de reprise*/
	void createCheckpoint(Node host);

}
