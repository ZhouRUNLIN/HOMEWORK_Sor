package srcs.webservices;

import org.restlet.Component;
import org.restlet.data.Protocol;
import srcs.webservices.airline.scheme.Airport;

import java.util.ArrayList;
import java.util.List;

public class SRCSWebServiceFactory {
    public List<Airport> allairports = new ArrayList<>();
    public static SRCSWebService buildAirline(String name, int portUser, int portAdmin) {
        Component component = new Component();

        component.getServers().add(Protocol.HTTP, portUser);
        component.getServers().add(Protocol.HTTP, portAdmin);

        component.getDefaultHost().attach("", new AirlineApplication(portUser, portAdmin));

        return new SRCSWebServiceImpl(name, component);
    }
}
