package srcs.webservices;

import static srcs.webservices.airline.scheme.Aircraft.buildAircraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import srcs.webservices.airline.scheme.Aircraft;
import srcs.webservices.airline.scheme.Airport;

public class Util {

	
	public final static List<Airport> allairports;	
	public final static List<String> cities;
	public final static List<Aircraft> aircrafts;
	
	public final static int port_admin=4343;
	public final static int port_user=4242;
	
	private final static Map<String,Map<String,Long>> time_flight = new HashMap<>();
	
	private static void putTime(String a, String b, long time) {
		time_flight.putIfAbsent(a, new HashMap<>());
		time_flight.putIfAbsent(b, new HashMap<>());
		time_flight.get(a).put(b, time);
		time_flight.get(b).put(a, time);
	}
	
	public static long getTime(String a, String b) {
		return time_flight.get(a).get(b);
	}
	
	static {
		allairports=new ArrayList<>();
		allairports.add(new Airport("ATL", "Atlanta H.-Jackson", "Atlanta", "Georgia", "United States"));
		allairports.add(new Airport("PEK", "Beijing Capital", "Beijing", "Chaoyang-Shunyi,Beijing", "China"));
		
		putTime("ATL", "PEK", 14*3600+12*60);
		
		allairports.add(new Airport("LAX", "Los Angeles", "Los Angeles", "California", "United States"));
		
		putTime("ATL", "LAX", 4*3600+7*60);
		putTime("PEK", "LAX", 12*3600+25*60);
		
		allairports.add(new Airport("DXB", "Dubai", "Dubai", "Garhoud,Dubai", "United Arab Emirates"));	
		
		putTime("ATL", "DXB", 14*3600+58*60);
		putTime("PEK", "DXB", 7*3600+22*60);
		putTime("LAX", "DXB", 16*3600+23*60);
		
		allairports.add(new Airport("HND", "Tokyo Haneda", "Tokyo", "Ota,Tokyo", "Japan"));
		
		putTime("ATL", "HND", 13*3600+35*60);
		putTime("PEK", "HND", 2*3600+53*60);
		putTime("LAX", "HND", 10*3600+56*60);
		putTime("DXB", "HND", 9*3600+52*60);
		
		allairports.add(new Airport("ORD", "O'Hare", "Chicago", "Illinois", "United States"));	
		
		
		putTime("ATL", "ORD", 1*3600+47*60);
		putTime("PEK", "ORD", 13*3600+4*60);
		putTime("LAX", "ORD", 3*3600+44*60);
		putTime("DXB", "ORD", 14*3600+18*60);
		putTime("HND", "ORD", 12*3600+31*60);
		
		
		allairports.add(new Airport("LHR", "Heathrow", "London", "Hillingdon,London", "United Kingdom"));
		
		putTime("ATL", "LHR", 8*3600+29*60);
		putTime("PEK", "LHR", 10*3600+7*60);
		putTime("LAX", "LHR", 10*3600+51*60);
		putTime("DXB", "LHR", 6*3600+56*60);
		putTime("HND", "LHR", 11*3600+49*60);
		putTime("ORD", "LHR", 7*3600+59*60);
		
		allairports.add(new Airport("PVG", "Shanghai Pudong", "Shanghai", "Pudong, Shanghai", "China"));
		
		putTime("ATL", "PVG", 15*3600+5*60);
		putTime("PEK", "PVG", 1*3600+40*60);
		putTime("LAX", "PVG", 12*3600+52*60);
		putTime("DXB", "PVG", 8*3600+4*60);
		putTime("HND", "PVG", 2*3600+28*60);
		putTime("ORD", "PVG", 13*3600+28*60);
		putTime("LHR", "PVG", 11*3600+23*60);
		
		
		allairports.add(new Airport("CDG", "Charles de Gaulle", "Paris", "Roissy-en-France,Ile-de-France", "France"));
		
		putTime("ATL", "CDG", 8*3600+48*60);
		putTime("PEK", "CDG", 10*3600+13*60);
		putTime("LAX", "CDG", 11*3600+15*60);
		putTime("DXB", "CDG", 6*3600+39*60);
		putTime("HND", "CDG", 12*3600+00*60);
		putTime("ORD", "CDG", 8*3600+21*60);
		putTime("LHR", "CDG", 0*3600+54*60);
		putTime("PVG", "CDG", 11*3600+28*60);
		
		
		allairports.add(new Airport("DFW", "Dallas/Fort Worth", "Dallas", "Dallas-Fort Worth,Texas", "United States"));
		
		putTime("ATL", "DFW", 1*3600+47*60);
		putTime("PEK", "DFW", 13*3600+50*60);
		putTime("LAX", "DFW", 2*3600+47*60);
		putTime("DXB", "DFW", 15*3600+50*60);
		putTime("HND", "DFW", 12*3600+49*60);
		putTime("ORD", "DFW", 1*3600+56*60);
		putTime("LHR", "DFW", 9*3600+31*60);
		putTime("PVG", "DFW", 14*3600+32*60);
		putTime("CDG", "DFW", 9*3600+52*60);
		
		
		allairports.add(new Airport("CAN", "Guangzhou Baiyun", "Guangzhou", "Baiyun-Huadu,Guangzhou", "China"));
		
		putTime("ATL", "CAN", 16*3600+25*60);
		putTime("PEK", "CAN", 2*3600+38*60);
		putTime("LAX", "CAN", 14*3600+18*60);
		putTime("DXB", "CAN", 7*3600+22*60);
		putTime("HND", "CAN", 3*3600+51*60);
		putTime("ORD", "CAN", 15*3600+17*60);
		putTime("LHR", "CAN", 11*3600+44*60);
		putTime("PVG", "CAN", 1*3600+50*60);
		putTime("CDG", "CAN", 11*3600+44*60);
		putTime("DFW", "CAN", 15*3600+57*60);
		
		
		allairports.add(new Airport("AMS", "Amsterdam", "Amsterdam", "Haarlemmermeer,North Holland", "Netherlands"));
		
		putTime("ATL", "AMS", 8*3600+50*60);
		putTime("PEK", "AMS", 9*3600+45*60);
		putTime("LAX", "AMS", 11*3600+5*60);
		putTime("DXB", "AMS", 6*3600+33*60);
		putTime("HND", "AMS", 11*3600+30*60);
		putTime("ORD", "AMS", 8*3600+18*60);
		putTime("LHR", "AMS", 00*3600+55*60);
		putTime("PVG", "AMS", 11*3600+00*60);
		putTime("CDG", "AMS", 1*3600+02*60);
		putTime("DFW", "AMS", 9*3600+50*60);
		putTime("CAN", "AMS", 11*3600+19*60);
		
		allairports.add(new Airport("YYZ", "Toronto Pearson", "Toronto", "Mississauga,Ontario", "Canada"));
		
		putTime("ATL", "YYZ", 1*3600+48*60);
		putTime("PEK", "YYZ", 13*3600+3*60);
		putTime("LAX", "YYZ", 4*3600+34*60);
		putTime("DXB", "YYZ", 13*3600+37*60);
		putTime("HND", "YYZ", 12*3600+46*60);
		putTime("ORD", "YYZ", 1*3600+26*60);
		putTime("LHR", "YYZ", 7*3600+13*60);
		putTime("PVG", "YYZ", 14*3600+02*60);
		putTime("CDG", "YYZ", 7*3600+34*60);
		putTime("DFW", "YYZ", 2*3600+42*60);
		putTime("CAN", "YYZ", 15*3600+18*60);
		putTime("AMS", "YYZ", 7*3600+33*60);
		
		
		allairports.add(new Airport("KUL", "Kuala Lumpur", "Kuala Lumpur", "Sepang,Selangor", "Malaysia"));
		
		putTime("ATL", "KUL", 19*3600+19*60);
		putTime("PEK", "KUL", 5*3600+34*60);
		putTime("LAX", "KUL", 17*3600+16*60);
		putTime("DXB", "KUL", 6*3600+59*60);
		putTime("HND", "KUL", 6*3600+44*60);
		putTime("ORD", "KUL", 18*3600+12*60);
		putTime("LHR", "KUL", 12*3600+58*60);
		putTime("PVG", "KUL", 4*3600+51*60);
		putTime("CDG", "KUL", 12*3600+49*60);
		putTime("DFW", "KUL", 18*3600+58*60);
		putTime("CAN", "KUL", 3*3600+25*60);
		putTime("AMS", "KUL", 12*3600+33*60);
		putTime("YYZ", "KUL", 18*3600+4*60);
		
		allairports.add(new Airport("SYD", "Sydney Kingsford-Smith", "Sydney", "Mascot,New South Wales", "Australia"));
		
		putTime("ATL", "SYD", 18*3600+12*60);
		putTime("PEK", "SYD", 11*3600+2*60);
		putTime("LAX", "SYD", 14*3600+47*60);
		putTime("DXB", "SYD", 14*3600+44*60);
		putTime("HND", "SYD", 9*3600+41*60);
		putTime("ORD", "SYD", 18*3600+7*60);
		putTime("LHR", "SYD", 20*3600+38*60);
		putTime("PVG", "SYD", 9*3600+46*60);
		putTime("CDG", "SYD", 20*3600+36*60);
		putTime("DFW", "SYD", 16*3600+52*60);
		putTime("CAN", "SYD", 9*3600+19*60);
		putTime("AMS", "SYD", 20*3600+13*60);
		putTime("YYZ", "SYD", 18*3600+57*60);
		putTime("KUL", "SYD", 8*3600+16*60);
		
		
		
//		allairports.add(new Airport("ICN", "Seoul Incheon", "Seoul", "Incheon,Seoul", "South Korea"));
//		allairports.add(new Airport("FRA", "Frankfurt", "Frankfurt", "Frankfurt,Hesse", "Germany"));
//		allairports.add(new Airport("DEN", "Denver", "Denver", "Colorado", "United States"));
//		allairports.add(new Airport("DEL", "Indira Gandhi", "Delhi", "Delhi", "India"));
//		allairports.add(new Airport("SIN", "Singapore Changi", "Singapore", "Changi,East Region", "Singapore"));
//		allairports.add(new Airport("BKK", "Suvarnabhumi", "Bang Phli", "Samut Prakan", "Thailand"));
//		allairports.add(new Airport("JFK", "John F. Kennedy", "New York", "Queens,New York", "United States"));		
//		allairports.add(new Airport("MAD", "Madrid Barajas", "Madrid", "Barajas,Madrid", "Spain"));
//		allairports.add(new Airport("SFO", "San Francisco", "San Francisco", "San Mateo County,California", "United States"));
//		allairports.add(new Airport("BCN", "Barcelona–El Prat", "Barcelona", "Barcelona", "Spain"));
//		allairports.add(new Airport("IST", "Istanbul", "Istanbul", "Arnavutköy,Istanbul", "Turkey"));
//		allairports.add(new Airport("SEA", "Seattle–Tacoma", "Seattle", "Washington", "United States"));
//		allairports.add(new Airport("LAS", "McCarran", "Las Vegas", "Nevada", "United States"));
//		allairports.add(new Airport("MCO", "Orlando", "Orlando", "Florida", "United States"));		
		
//		allairports.add(new Airport("MEX", "Benito Juárez", "Mexico city", "Venustiano Carranza", "Mexico"));
//		allairports.add(new Airport("SVO", "Sheremetyevo", "Moscow", "Khimki,Oblast", "Russia"));
//		allairports.add(new Airport("TPE", "Taiwan Taoyuan", "Taipei", "Dayuan,Taoyuan", "Taiwan"));
//		allairports.add(new Airport("MUC", "Munich", "Munich", "Freising,Bavaria", "Germany"));
//		allairports.add(new Airport("MNL", "Ninoy Aquino", "Manila", "Pasay/Parañaque,Metro Manila", "Philippines"));
		
		
		cities=allairports.stream().map(a->a.getCity()).collect(Collectors.toList());
		
		
	

		
		//on divise les capacités par 10 par rapport à la réalité pour réduire la latence des tests
		aircrafts=new ArrayList<>();
		aircrafts.add(buildAircraft("Airbus-A300", 34));
		aircrafts.add(buildAircraft( "Airbus-A300", 34));
		aircrafts.add(buildAircraft( "Airbus-A300", 34));
		aircrafts.add(buildAircraft("Airbus-A300", 34));
		aircrafts.add(buildAircraft( "Airbus-A350-1000", 40));
		aircrafts.add(buildAircraft( "Airbus-A350-1000", 40));
		aircrafts.add(buildAircraft("Airbus-A350-1000", 40));
		aircrafts.add(buildAircraft("Airbus-A350-1000", 40));
		aircrafts.add(buildAircraft("Airbus-A350-1000", 40));
		aircrafts.add(buildAircraft("Airbus-A350-1000", 40));
		aircrafts.add(buildAircraft("Airbus-A350-1000", 40));
		aircrafts.add(buildAircraft("Airbus-A350-1000", 40));
		aircrafts.add(buildAircraft("Airbus-A380", 61));
		aircrafts.add(buildAircraft("Airbus-A380", 61));
		aircrafts.add(buildAircraft("Airbus-A380", 61));
		aircrafts.add(buildAircraft("Airbus-A380", 61));
		aircrafts.add(buildAircraft("Airbus-A380", 61));
		aircrafts.add(buildAircraft("Airbus-A380", 61));
		aircrafts.add(buildAircraft("Boeing 747", 52));
		aircrafts.add(buildAircraft("Boeing 747", 52));
		aircrafts.add(buildAircraft("Boeing 747", 52));
		aircrafts.add(buildAircraft("Boeing 747", 52));
		aircrafts.add(buildAircraft("Boeing 747", 52));
		aircrafts.add(buildAircraft("Boeing 747", 52));
		aircrafts.add(buildAircraft("Boeing 757", 20));
		aircrafts.add(buildAircraft("Boeing 757", 20));
		aircrafts.add(buildAircraft("Boeing 757", 20));
		aircrafts.add(buildAircraft("Boeing 757", 20));
		aircrafts.add(buildAircraft("Boeing 757", 20));
		aircrafts.add(buildAircraft("Boeing 757", 20));
		aircrafts.add(buildAircraft("Boeing 757", 20));
		aircrafts.add(buildAircraft("Boeing 757", 20));		
		aircrafts.add(buildAircraft("Boeing 787", 25));
		aircrafts.add(buildAircraft("Boeing 787", 25));
		aircrafts.add(buildAircraft("Boeing 787", 25));
		aircrafts.add(buildAircraft("Boeing 787", 25));
		aircrafts.add(buildAircraft("Boeing 787", 25));
		aircrafts.add(buildAircraft("Boeing 787", 25));
	}
	
}
