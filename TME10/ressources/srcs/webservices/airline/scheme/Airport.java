package srcs.webservices.airline.scheme;

import java.util.Objects;

public class Airport {
	private final String codeAITA;
	private final String name;
	private final String city;
	private final String location;
	private final String country;
	
	
	public Airport() {
		this("unknown","unknown","unknown","unknown","unknown");
	}
	
	public Airport(String codeAITA, String name, String city, String location, String country) {
		super();
		this.codeAITA = codeAITA;
		this.name = name;
		this.city = city;
		this.location = location;
		this.country = country;
	}
	public String getCodeAITA() {
		return codeAITA;
	}
	public String getName() {
		return name;
	}
	public String getCity() {
		return city;
	}
	public String getLocation() {
		return location;
	}
	public String getCountry() {
		return country;
	}
	@Override
	public int hashCode() {
		return Objects.hash(codeAITA);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Airport))
			return false;
		Airport other = (Airport) obj;
		return Objects.equals(codeAITA, other.codeAITA);
	}

	@Override
	public String toString() {
		return "Airport [codeAITA=" + codeAITA +
							", name=" + name +
				            ", city=" + city +
				            ", location=" + location +
				            ", country=" + country + "]";
	}
}
