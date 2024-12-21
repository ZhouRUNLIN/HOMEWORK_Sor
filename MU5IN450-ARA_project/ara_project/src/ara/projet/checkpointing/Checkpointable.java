package ara.projet.checkpointing;

public interface Checkpointable {
	
	/**
	 * Obtenir l'état courant du noeud
	 */
	NodeState getCurrentState();
	
	
	/**
	 * restaurer un état
	 */
	void restoreState(NodeState restored_state);
	
	
	/**
	 * suspendre l'exécution du noeud (debut du recovery)
	 */
	void suspend();
	
	/**
	 * reprendre l'execution du noeud (fin du recovery)
	 */
	void resume();
}
