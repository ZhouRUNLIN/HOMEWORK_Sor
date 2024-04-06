package restlet.annuaire.test;

import org.restlet.Component;
import org.restlet.data.Protocol;

import restlet.annuaire.Annuaire;

public class Test_one {
    public static void main(String[] args) throws Exception {
        Component c = new Component();
        c.getServers().add(Protocol.HTTP, 8585);
        c.getDefaultHost().attach("/annuaire", new Annuaire());
        c.start();//démarrage des services
    }
}
