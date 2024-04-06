package srcs.webservices.airline.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import srcs.webservices.SRCSWebService;
import srcs.webservices.SRCSWebServiceFactory;
import srcs.webservices.Util;
import srcs.webservices.airline.scheme.Aircraft;
import srcs.webservices.airline.scheme.Airport;
import srcs.webservices.airline.scheme.Flight;
import srcs.webservices.airline.scheme.Passenger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AirlineServiceTest {
	private static final int airsrcs_portuser = 4343;
	private static final int airsrcs_portadmin = 54343;
	private static SRCSWebService airsrcs;
	
	private static final Aircraft cesna = new Aircraft("bonus", "Cesna", 4);
	private static final Airport orly = new Airport("ORY", "Paris Orly", "Paris", "Orly, Ile de France", "France");
	
	private static List<Airport> airports = new LinkedList<>(Util.allairports);
	private static final int min_airport =8;
	
	private static List<Aircraft> aircrafts = new LinkedList<>(Util.aircrafts);
	private static final int min_aircraft =4;
	
	private static final List<Flight> flights = new ArrayList<>();
	
	private static final SimpleDateFormat formater  = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
	
	@BeforeClass
	public static void beforeclass() throws Exception {
		Random random = new Random(42);
		airsrcs=SRCSWebServiceFactory.buildAirline("airsrcs",airsrcs_portuser, airsrcs_portadmin);
		airsrcs.deploy();
		Thread.sleep(100);
		
		int nb = random.nextInt(airports.size() + 1 - min_airport) + min_airport;
		Collections.shuffle(airports, random);
		airports = airports.subList(0, nb);
		
		nb = random.nextInt(aircrafts.size() + 1 - min_aircraft) + min_aircraft;
		Collections.shuffle(aircrafts, random);
		aircrafts = aircrafts.subList(0, nb);
		
		int cpt=0;
		
		flights.add(buildFlight(
				"airsrcs"+(cpt++), 
				airports.get(0), 
				airports.get(1), 
				formater.parse("2021-05-03-17-00"), 
				aircrafts.get(0)));
		
		flights.add(buildFlight(
				"airsrcs"+(cpt++), 
				airports.get(0), 
				airports.get(2), 
				formater.parse("2021-05-06-16-00"), 
				aircrafts.get(1)));
		
		flights.add(buildFlight(
				"airsrcs"+(cpt++), 
				airports.get(0), 
				airports.get(3), 
				formater.parse("2021-05-03-15-52"), 
				aircrafts.get(2)));
		flights.add(buildFlight(
				"airsrcs"+(cpt++), 
				airports.get(0), 
				airports.get(4), 
				formater.parse("2021-05-03-15-30"), 
				aircrafts.get(3)));
		
	}
	

	@Test
	public void testA_Airports_add() throws IOException {
		
		
		final ClientResource clientadmin = new ClientResource("http://localhost:"+airsrcs_portadmin+"/admin/airports");
				
		final List<Airport> airports1 = airports.subList(0, airports.size()/2);
		Representation r = clientadmin.post(new JacksonRepresentation<List<Airport>>(airports1));
		assertEquals(0,r.getSize());
		
		
		final List<Airport> airports2 = airports.subList(airports.size()/2, airports.size());
		r = clientadmin.put(new JacksonRepresentation<List<Airport>>(airports2));
		assertEquals(0,r.getSize());
			
		
		final ClientResource clientuser = new ClientResource("http://localhost:"+airsrcs_portuser+"/airports");
		r = clientuser.get();
		List<Airport> resviauser =  Arrays.asList(new JacksonRepresentation<Airport[]>(r, Airport[].class).getObject());	
		
		final ClientResource clientadmin2 = new ClientResource("http://localhost:"+airsrcs_portadmin+"/airports");
		r = clientadmin2.get();
		List<Airport> resviaadmin2 =  Arrays.asList(new JacksonRepresentation<Airport[]>(r, Airport[].class).getObject());


		final ClientResource clientadmin3 = new ClientResource("http://localhost:"+airsrcs_portadmin+"/admin/airports");
		r = clientadmin3.get();
		List<Airport> resviaadmin3 =  Arrays.asList(new JacksonRepresentation<Airport[]>(r, Airport[].class).getObject());	
	
		assertEquals(airports.size(), resviaadmin2.size());
		assertEquals(airports.size(), resviaadmin3.size());
		assertEquals(airports.size(), resviauser.size());
		for(Airport a : airports) {
			assertTrue(resviaadmin3.contains(a));
			assertTrue(resviaadmin2.contains(a));
			assertTrue(resviauser.contains(a));
		}		
		
		
		
	}
	
	@Test
	public void testB_Airports_errors() throws IOException {
		final ClientResource clientuser = new ClientResource("http://localhost:"+airsrcs_portuser+"/airports");
		final ClientResource clientadmin2 = new ClientResource("http://localhost:"+airsrcs_portadmin+"/airports");

		ResourceException re1 = assertThrows(ResourceException.class, ()->clientuser.post(new JacksonRepresentation<List<Airport>>(Arrays.asList(orly))));
		assertEquals(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED, re1.getResponse().getStatus());
		
		ResourceException re2 = assertThrows(ResourceException.class, ()->clientadmin2.post(new JacksonRepresentation<List<Airport>>(Arrays.asList(orly))));
		assertEquals(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED, re2.getResponse().getStatus());
					
		
		final ClientResource clientuser2 = new ClientResource("http://localhost:"+airsrcs_portuser+"/admin/airports");
	
		ResourceException re3 = assertThrows(ResourceException.class, ()->clientuser2.get());
		assertEquals(Status.CLIENT_ERROR_NOT_FOUND, re3.getResponse().getStatus());		
	}
	
	
	@Test
	public void testC_Aircrafts_add() throws IOException {
		
		final ClientResource clientadmin = new ClientResource("http://localhost:"+airsrcs_portadmin+"/admin/aircrafts");
		List<Aircraft> aircrafts = new LinkedList<>(Util.aircrafts);
		
		
		final List<Aircraft> aircrafts1 = aircrafts.subList(0, aircrafts.size()/2);
		Representation r = clientadmin.post(new JacksonRepresentation<List<Aircraft>>(aircrafts1));
		assertEquals(0,r.getSize());
		
		
		final List<Aircraft> aircrafts2 = aircrafts.subList(aircrafts.size()/2, aircrafts.size());
		r = clientadmin.put(new JacksonRepresentation<List<Aircraft>>(aircrafts2));
		assertEquals(0,r.getSize());
			
		
		final ClientResource clientuser = new ClientResource("http://localhost:"+airsrcs_portuser+"/aircrafts");
		r = clientuser.get();
		List<Aircraft> resviauser =  Arrays.asList(new JacksonRepresentation<Aircraft[]>(r, Aircraft[].class).getObject());	
		
		final ClientResource clientadmin2 = new ClientResource("http://localhost:"+airsrcs_portadmin+"/aircrafts");
		r = clientadmin2.get();
		List<Aircraft> resviaadmin2 =  Arrays.asList(new JacksonRepresentation<Aircraft[]>(r, Aircraft[].class).getObject());	
		
		
		final ClientResource clientadmin3 = new ClientResource("http://localhost:"+airsrcs_portadmin+"/admin/aircrafts");
		r = clientadmin3.get();
		List<Aircraft> resviaadmin3 =  Arrays.asList(new JacksonRepresentation<Aircraft[]>(r, Aircraft[].class).getObject());		
		
		assertEquals(aircrafts.size(), resviaadmin2.size());
		assertEquals(aircrafts.size(), resviaadmin3.size());
		assertEquals(aircrafts.size(), resviauser.size());
		for(Aircraft a : aircrafts) {
			assertTrue(resviaadmin3.contains(a));
			assertTrue(resviaadmin2.contains(a));
			assertTrue(resviauser.contains(a));
		}		
	}
	
	@Test
	public void testD_Aircrafts_errors() throws IOException {
		final ClientResource clientuser = new ClientResource("http://localhost:"+airsrcs_portuser+"/aircrafts");

		final ClientResource clientadmin2 = new ClientResource("http://localhost:"+airsrcs_portadmin+"/aircrafts");

		ResourceException re1 = assertThrows(ResourceException.class, ()->clientuser.post(new JacksonRepresentation<List<Aircraft>>(Arrays.asList(cesna))) );
		assertEquals(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED, re1.getResponse().getStatus());
		
		
		ResourceException re2 = assertThrows(ResourceException.class, ()->clientadmin2.post(new JacksonRepresentation<List<Aircraft>>(Arrays.asList(cesna))));
		assertEquals(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED, re2.getResponse().getStatus());
				
		final ClientResource clientuser2 = new ClientResource("http://localhost:"+airsrcs_portuser+"/admin/aircrafts");
		
		ResourceException re3 = assertThrows(ResourceException.class, ()->clientuser2.get());
		assertEquals(Status.CLIENT_ERROR_NOT_FOUND, re3.getResponse().getStatus());
	}
	
	
	@Test
	public void testE_Flights_add() throws Exception {
		
		ClientResource admin = new ClientResource("http://localhost:"+airsrcs_portadmin+"/admin/flights");
		for(Flight f : flights) {
			admin.post(new JacksonRepresentation<>(f));
		}
		
		
		List<Flight> flights_received =  Arrays.asList(
				new JacksonRepresentation<Flight[]>(
						admin.get(), 
						Flight[].class).getObject()
				);	
		
		assertEquals(flights.size(),flights_received.size());
		for(Flight f : flights) {
			assertTrue(flights_received.contains(f));
		}
		
		
	}
	@Test
	public void testF_Flights_errors() throws Exception {
		
		final ClientResource clientuser0 = new ClientResource("http://localhost:"+airsrcs_portuser+"/admin/flights");
		
		ResourceException re0 = assertThrows(ResourceException.class, ()->clientuser0.get());
		assertEquals(Status.CLIENT_ERROR_NOT_FOUND, re0.getResponse().getStatus());
		
		//test vol deja existant
		ClientResource admin = new ClientResource("http://localhost:"+airsrcs_portadmin+"/admin/flights");
		ResourceException re1 = assertThrows(ResourceException.class, () ->{
			admin.post(new JacksonRepresentation<>(
					new Flight("airsrcs"+1,airports.get(0), airports.get(1),formater.parse("2022-01-07-16-00"),formater.parse("2022-01-07-19-00"), aircrafts.get(0))
					));
		});
		assertEquals(Status.CLIENT_ERROR_PRECONDITION_FAILED,re1.getResponse().getStatus());
		
		
		//test d'un aéroport de départ qui n'existe pas dans la base
		ResourceException re2 = assertThrows(ResourceException.class, () ->{
			admin.post(new JacksonRepresentation<>(
					new Flight("airsrcs"+500,orly, airports.get(1),formater.parse("2022-01-07-16-00"),formater.parse("2022-01-07-19-00"), aircrafts.get(0))
					));
		});
		assertEquals(Status.CLIENT_ERROR_PRECONDITION_FAILED,re2.getResponse().getStatus());

		//test d'un aéroport d'arrivée qui n'existe pas dans la base
		ResourceException re3 = assertThrows(ResourceException.class, () ->{
			admin.post(new JacksonRepresentation<>(
					new Flight("airsrcs"+500,airports.get(1),orly,formater.parse("2022-01-07-16-00"),formater.parse("2022-01-07-19-00"), aircrafts.get(0))
					));
		});
		assertEquals(Status.CLIENT_ERROR_PRECONDITION_FAILED,re3.getResponse().getStatus());
		
		//test d'un avion qui n'existe pas dans la base
		ResourceException re4 = assertThrows(ResourceException.class, () ->{
			admin.post(new JacksonRepresentation<>(
					new Flight("airsrcs"+500,airports.get(1),airports.get(0),formater.parse("2022-01-07-16-00"), formater.parse("2022-01-07-19-00"), cesna)
					));
		});
		assertEquals(Status.CLIENT_ERROR_PRECONDITION_FAILED,re4.getResponse().getStatus());
		
		//test d'un avion qui est en conflit avec un autre vol
		ResourceException re5 = assertThrows(ResourceException.class, () ->{
			admin.post(new JacksonRepresentation<>(
					buildFlight(
							"airsrcs"+359, 
							airports.get(2), 
							airports.get(3), 
							formater.parse("2021-05-03-16-30"), 
							aircrafts.get(2))
					));
		});
		assertEquals(Status.CLIENT_ERROR_PRECONDITION_FAILED,re5.getResponse().getStatus());
		
		//test que la base n'a pas bougée
		assertEquals(flights.size(),Arrays.asList(
				new JacksonRepresentation<Flight[]>(
						admin.get(), 
						Flight[].class).getObject()
				).size());
	}
	
	@Test
	public void testG_Flights_queries() throws Exception {
		
		final String uri = "http://localhost:"+airsrcs_portuser+"/flights";
		ClientResource client1 = new ClientResource(uri+"?to="+airports.get(0).getCodeAITA());
		assertEquals(0,Arrays.asList(
				new JacksonRepresentation<Flight[]>(
						client1.get(), 
						Flight[].class).getObject()
				).size());
		
		
		
		for(Integer i : Arrays.asList(1,2,3,4)) {
			ClientResource clienti = new ClientResource(uri+"?to="+airports.get(i).getCodeAITA());
			assertEquals(1,Arrays.asList(
					new JacksonRepresentation<Flight[]>(
							clienti.get(), 
							Flight[].class).getObject()
					).size());
		}
		
		
		ClientResource client2 = new ClientResource(uri+"?from="+airports.get(0).getCodeAITA());
		assertEquals(4,Arrays.asList(
				new JacksonRepresentation<Flight[]>(
						client2.get(), 
						Flight[].class).getObject()
				).size());
		
		for(Integer i : Arrays.asList(1,2,3,4)) {
			ClientResource clienti = new ClientResource(uri+"?from="+airports.get(i).getCodeAITA());
			assertEquals(0,Arrays.asList(
					new JacksonRepresentation<Flight[]>(
							clienti.get(), 
							Flight[].class).getObject()
					).size());
		}
		
		ClientResource client3 = new ClientResource(uri+"?from="+airports.get(0).getCodeAITA()+"&to="+airports.get(1).getCodeAITA());
		assertEquals(1,Arrays.asList(
				new JacksonRepresentation<Flight[]>(
						client3.get(), 
						Flight[].class).getObject()
				).size());
		
		ClientResource admin = new ClientResource("http://localhost:"+airsrcs_portadmin+"/admin/flights");
		assertEquals(flights.size(),Arrays.asList(
				new JacksonRepresentation<Flight[]>(
						admin.get(), 
						Flight[].class).getObject()
				).size());
	}
	
	
	@Test
	public void testH_Passenger_add() throws Exception{
		int cpt=0;
		for(Flight f : flights) {

			int capacity = f.getAircraft().getPassengerCapacity();
			int nb_pass=capacity;
			for(int i=cpt, j=0; i < cpt + nb_pass ; i++, j++) {
				ClientResource admin = new ClientResource("http://localhost:"+airsrcs_portadmin+"/admin/flight/"+f.getId()+"/passenger?place=P"+j);
				Representation r =admin.post(new JacksonRepresentation<Passenger>(new Passenger("firstname"+i, "lastname"+i)));
				assertTrue(new JacksonRepresentation<Boolean>(r,Boolean.class).getObject());
			}		
			cpt+=nb_pass;
			ClientResource admin2 = new ClientResource("http://localhost:"+airsrcs_portadmin+"/admin/flight/"+f.getId()+"/passenger?place=PlaceToto");
			Representation r =admin2.post(new JacksonRepresentation<Passenger>(new Passenger("Toto", "Titi")));
			assertFalse(new JacksonRepresentation<Boolean>(r,Boolean.class).getObject());
			
		}
		
		cpt=0;
		for(Flight f : flights) {
			ClientResource admin = new ClientResource("http://localhost:"+airsrcs_portadmin+"/admin/flight/"+f.getId()+"/passengers");
			Representation r = admin.get();
			List<Passenger> passengers = Arrays.asList(new JacksonRepresentation<Passenger[]>(r,Passenger[].class).getObject());
			int capacity = f.getAircraft().getPassengerCapacity();
			int nb_pass=capacity;
			assertEquals(capacity,passengers.size());
			for(int i=cpt, j=0; i < cpt + nb_pass ; i++, j++) {
				Passenger p =new Passenger("firstname"+i, "lastname"+i);
				assertTrue(passengers.contains(p));
				
				ClientResource admin2 = new ClientResource("http://localhost:"+airsrcs_portadmin+"/admin/flight/"+f.getId()+"/place?firstname="+p.getFirstName()+"&lastname="+p.getLastName());
				String res = new JacksonRepresentation<String>(admin2.get(), String.class).getObject();
				assertEquals("P"+j, res);
			}		
			cpt+=nb_pass;
			
		}
		
		
		
	}
	
	private static Flight buildFlight(String id, Airport from, Airport to, Date departure, Aircraft aircraft) {
		
		long l_arrival = departure.toInstant().plus(Util.getTime(from.getCodeAITA(), to.getCodeAITA()),	ChronoUnit.SECONDS).toEpochMilli();
		Date arrival = new Date(l_arrival);
		return new Flight(id, from, to, departure, arrival, aircraft);
	}
		
	@AfterClass
	public static void afterclass() throws Exception {
		Thread.sleep(100);
		airsrcs.undeploy();
	}
}
