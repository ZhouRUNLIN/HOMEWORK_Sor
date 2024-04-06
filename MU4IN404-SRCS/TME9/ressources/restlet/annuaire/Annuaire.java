package restlet.annuaire;

import org.restlet.Restlet;
import org.restlet.routing.Router;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.restlet.Application;

public class Annuaire extends Application {
    public Map<String, Etudiant> annuaire = new ConcurrentHashMap<>();

    public Annuaire() {
        annuaire.put("45652", new Etudiant("45652", "Macron", "Emmanuel", "0956874123"));
        annuaire.put("56423", new Etudiant("56423", "Seize", "Louis", "0864287951"));
        annuaire.put("78951", new Etudiant("78951", "Chirac", "Jacques", "0864287951"));
        annuaire.put("89546", new Etudiant("89546", "Sarkozy", "Nicolas", "0978654123"));
    }

    @Override
    public Restlet createInboundRoot() {
        Router res = new Router();
        res.attach("/etudiants", All.class);
        res.attach("/etudiants/{id}", EtudiantResource.class);
        return res;
    }

}
