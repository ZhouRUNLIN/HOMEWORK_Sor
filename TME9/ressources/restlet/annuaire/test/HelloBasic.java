package restlet.annuaire.test;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;

public class HelloBasic {
    public static void main(String[] args) throws Exception {
        Server s = new Server(Protocol.HTTP, 8585, new Restlet() {
            @Override
            public void handle(Request request, Response response) {
                response.setEntity("Hello !!\n", MediaType.TEXT_PLAIN);
            }
        });
        s.start();
    }
}