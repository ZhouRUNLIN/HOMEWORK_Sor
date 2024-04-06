package restlet.annuaire.test;

import org.restlet.data.MediaType;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import restlet.annuaire.Etudiant;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws ResourceException, IOException {
        ClientResource client = new ClientResource("http://localhost:8585/annuaire/etudiants/56423");

        client.accept(MediaType.APPLICATION_JSON);
        Representation r = client.get();
        JacksonRepresentation<Etudiant> jr = new JacksonRepresentation<>(r, Etudiant.class);
        Etudiant e = jr.getObject();
        System.out.println(e);

        client.delete();
        try {
            client.accept(MediaType.APPLICATION_JSON);
            jr = new JacksonRepresentation<>(client.get(), Etudiant.class);
            e = jr.getObject();
            System.out.println(e);
        } catch (ResourceException exception) {
            System.out.println("Err !");
            System.out.println("Continue with reload ");
        }

        Etudiant etu = new Etudiant("56423", "Coty", "Rene", "089657484");
        client.post(new JacksonRepresentation<Etudiant>(etu));

        client.accept(MediaType.APPLICATION_JSON);
        jr = new JacksonRepresentation<>(client.get(), Etudiant.class);
        e = jr.getObject();
        System.out.println(e);
    }
}