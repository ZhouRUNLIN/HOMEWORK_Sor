package srcs.webservices;

import org.restlet.*;
import org.restlet.routing.Router;
import srcs.webservices.airline.scheme.Aircraft;
import srcs.webservices.airline.scheme.Airport;
import srcs.webservices.airline.scheme.Flight;

import java.util.ArrayList;
import java.util.List;

public class AirlineApplication extends Application {
    public List<Airport> allairports = new ArrayList<>();
    public List<Aircraft> allaircrafts = new ArrayList<>();
    public List<Flight> allflights = new ArrayList<>();

    private final int portUser;
    private final int portAdmin;

    public AirlineApplication(int portUser, int portAdmin) {
        this.portUser = portUser;
        this.portAdmin = portAdmin;
    }

    public int getPortUser() {
        return portUser;
    }

    public int getPortAdmin() {
        return portAdmin;
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());

        router.attach("/admin/airports", AirportsResource.class);
        router.attach("/admin/aircrafts", AircraftsResource.class);
        router.attach("/admin/flights", FlightsResource.class);
        router.attach("/admin/flight/{idVol}/passenger", FlightsResource.FlightsPassengerResource.class);
        router.attach("/admin/flight/{idVol}/passengers", FlightsResource.FlightsPassengersResource.class);
        router.attach("/admin/flight/{idVol}/place", FlightsResource.FlightsPlaceResource.class);

        router.attach("/airports", AirportsResource.class);
        router.attach("/aircrafts", AircraftsResource.class);
        router.attach("/flights", FlightsResource.class);

        return router;
    }
}
