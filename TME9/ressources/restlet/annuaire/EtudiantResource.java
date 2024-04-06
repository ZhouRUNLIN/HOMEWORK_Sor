package restlet.annuaire;

import org.restlet.Application;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.*;

import java.io.IOException;

public class EtudiantResource extends ServerResource {
    @Get("xml|json")
    public Etudiant request() {
        Application app = this.getApplication();
        if(! (app instanceof Annuaire)) {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
        }
        Annuaire a = (Annuaire) getApplication();
        Object id = getRequest().getAttributes().get("id");
        if(!a.annuaire.containsKey(id)) {
            throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
        }
        return a.annuaire.get(id);
    }

    @Post("json")
    public void ajouter(Representation r) throws IOException {
        JacksonRepresentation<Etudiant> jr = new JacksonRepresentation<>(r, Etudiant.class);
        Etudiant e = jr.getObject();
        Application app = this.getApplication();
        if(! (app instanceof Annuaire)) {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
        }
        Annuaire a = (Annuaire) getApplication();
        Object id = getRequest().getAttributes().get("id");
        if(!id.equals(e.getId())) {
            throw new ResourceException(Status.CLIENT_ERROR_CONFLICT);
        }
        a.annuaire.put(id.toString(), e);
    }

    @Delete
    public void supprimer() {
        Application app = this.getApplication();
        if(! (app instanceof Annuaire)) {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
        }

        Annuaire a = (Annuaire) getApplication();
        Object id = getRequest().getAttributes().get("id");
        if(! a.annuaire.containsKey(id)) {
            throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
        }
        a.annuaire.remove(id);
    }
}
