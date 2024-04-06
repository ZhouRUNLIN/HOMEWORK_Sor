package srcs.webservices.airline.scheme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Flight {

	private final String id;
	private final Airport from;
	private final Airport to;
	private final Date departure;
	private final Date arrival;
	private final Map<Passenger,String> placing;
	private final Aircraft aircraft;
	
	public Flight() {
		this("unknown",null,null,null,null,null);
	}
	
	public Flight(String id, Airport from, Airport to, Date departure, Date arrival, Aircraft aircraft) {
		super();
		this.id = id;
		this.from = from;
		this.to = to;
		this.departure = departure;
		this.arrival = arrival;
		this.placing = new HashMap<>();
		this.aircraft = aircraft;
	}
	public String getId() {
		return id;
	}
	public Airport getFrom() {
		return from;
	}
	public Airport getTo() {
		return to;
	}
	public Date getDeparture() {
		return departure;
	}
	public Date getArrival() {
		return arrival;
	}
	public Set<Passenger> getPassengers() {
		return placing.keySet();
	}
	public Aircraft getAircraft() {
		return aircraft;
	}

	public String getPlace(Passenger p) {
		return placing.get(p);
	}
	
	public void addPassenger(Passenger p, String place) {
		placing.put(p, place);
	}

	public boolean full() {
		return getPassengers().size()>=getAircraft().getPassengerCapacity();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}


	public boolean isInConflict(Flight other) {
		if(other.getDeparture().after(getDeparture()) && other.getDeparture().before(getArrival())) {
			return true;
		}
		if(other.getArrival().after(getDeparture()) && other.getArrival().before(getArrival())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Flight))
			return false;
		Flight other = (Flight) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Flight [from :" + from +
				"\n to :" + to + "]";
	}
}
