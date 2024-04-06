package srcs.webservices.airline.scheme;

import java.util.Objects;

public class Aircraft {

	private static int cpt=0;
	public static Aircraft buildAircraft(String model, int passengerCapacity) {
		return new Aircraft("aircraft"+(cpt++), model, passengerCapacity);
	}
		
	private final String registration;
	private final String model;
	private final int passengerCapacity;
	
	public Aircraft() {
		this("unknown","unknown",0);
	}
	
	public Aircraft(String registration, String model, int passengerCapacity) {
		super();
		this.registration = registration;
		this.model = model;
		this.passengerCapacity = passengerCapacity;
	}
	public String getRegistration() {
		return registration;
	}
	public String getModel() {
		return model;
	}
	public int getPassengerCapacity() {
		return passengerCapacity;
	}

	@Override
	public int hashCode() {
		return Objects.hash(registration);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Aircraft))
			return false;
		Aircraft other = (Aircraft) obj;
		return Objects.equals(registration, other.registration);
	}	
}
