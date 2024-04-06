package srcs.webservices;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.restlet.Application;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.*;
import srcs.webservices.airline.scheme.Airport;

import java.util.List;

public class AirportsResource extends ServerResource{
    @Get("json")
    public Airport[] request() {
        if (getRequest().getResourceRef().getPath().startsWith("/admin") && getResponse().getServerInfo().getPort() == 4343)
            throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);

        Application app = this.getApplication();
        if (!(app instanceof AirlineApplication))
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL);

        AirlineApplication a = (AirlineApplication) getApplication();

        Airport[] airportsArray = new Airport[a.allairports.size()];
        for (int i = 0; i < a.allairports.size(); i++)
            airportsArray[i] = a.allairports.get(i);
        return airportsArray;
    }

    @Put("json")
    @Post("json")
    public Representation add(Representation r) throws Exception {
        if (getResponse().getServerInfo().getPort() == 4343 || !getRequest().getResourceRef().getPath().startsWith("/admin"))
            throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);

        Application app = this.getApplication();
        if(! (app instanceof AirlineApplication))
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL);

        AirlineApplication a = (AirlineApplication) getApplication();

        ObjectMapper objectMapper = new ObjectMapper();
        List<Airport> airports = objectMapper.readValue(r.getText(), new TypeReference<List<Airport>>() {});
        a.allairports.addAll(airports);
        return new StringRepresentation("Success");
    }
}

