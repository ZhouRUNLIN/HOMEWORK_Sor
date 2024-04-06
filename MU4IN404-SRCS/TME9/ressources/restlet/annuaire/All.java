package restlet.annuaire;

import org.restlet.Application;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

@XmlRootElement
public class All extends ServerResource {
    @Get("xml|json")
    public Map<String, Etudiant> request() {
        Application app = this.getApplication();

        if (! (app instanceof Annuaire)) {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
        }

        Annuaire a = (Annuaire) getApplication();
        return a.annuaire;
    }
}
