package srcs.webservices.airline.scheme;

import java.util.Objects;

public class Passenger {

	private final String firstName;
	private final String lastName;
	
	public Passenger() {
		this("unknown","unknown");
	}
	
	public Passenger(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}
	

	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}


	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Passenger))
			return false;
		Passenger other = (Passenger) obj;
		return Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName);
	}

	@Override
	public String toString() {
		return "Passenger: [ firstName= " + firstName + ", lastName= " + lastName + "]";
	}
}
