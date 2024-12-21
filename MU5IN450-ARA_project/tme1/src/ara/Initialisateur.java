package ara;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

public class Initialisateur implements Control {

    private static final String PAR_PROTO_HELLO = "hellopid";
    private final int hellopid;

    public Initialisateur(String prefix) {
        hellopid = Configuration.getPid(prefix + "." + PAR_PROTO_HELLO);
    }

    @Override
    public boolean execute() {
        for (int i = 0; i < Network.size(); i++) {
            Node node = Network.get(i);
            HelloProtocol hello = (HelloProtocol) node.getProtocol(hellopid);
            hello.initialisation(node);
        }
        return false;
    }


}
