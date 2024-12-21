package ara.projet.mutex;

public class InternalEvent {

	public static enum TypeEvent{release_cs,request_cs}
	
	private final TypeEvent type;
	private final int date;//permet de détecter des éventuels evénements obsolètes à ne pas traiter
	
	public InternalEvent(TypeEvent type, int date) {
		this.type=type;
		this.date=date;
	}

	public TypeEvent getType() {
		return type;
	}

	public int getDate() {
		return date;
	}
	
	
	
}
