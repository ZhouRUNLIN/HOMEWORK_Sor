package srcs.webservices;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.restlet.Application;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.*;
import srcs.webservices.airline.scheme.Aircraft;

import java.util.List;

public class AircraftsResource extends ServerResource {
    @Get("json")
    public Aircraft[] request() {
        if (getRequest().getResourceRef().getPath().startsWith("/admin") && getResponse().getServerInfo().getPort() == 4343)
            throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);

        Application app = this.getApplication();
        if (!(app instanceof AirlineApplication)) {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
        }
        AirlineApplication a = (AirlineApplication) getApplication();

        Aircraft[] aircraftsArray = new Aircraft[a.allaircrafts.size()];
        for (int i = 0; i < a.allaircrafts.size(); i++) {
            aircraftsArray[i] = a.allaircrafts.get(i);
        }
        return aircraftsArray;
    }

    @Put("json")
    @Post("json")
    public Representation add(Representation r) throws Exception {
        if (getResponse().getServerInfo().getPort() == 4343 || !getRequest().getResourceRef().getPath().startsWith("/admin"))
            throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);

        Application app = this.getApplication();
        if(! (app instanceof AirlineApplication)) {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
        }

        AirlineApplication a = (AirlineApplication) getApplication();

        ObjectMapper objectMapper = new ObjectMapper();
        List<Aircraft> aircrafts = objectMapper.readValue(r.getText(), new TypeReference<List<Aircraft>>() {});
        a.allaircrafts.addAll(aircrafts);

        return new StringRepresentation("Success");
    }
}
