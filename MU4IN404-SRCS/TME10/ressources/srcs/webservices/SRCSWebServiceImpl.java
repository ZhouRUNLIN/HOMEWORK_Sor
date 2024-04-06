package srcs.webservices;

import org.restlet.Component;

public class SRCSWebServiceImpl implements SRCSWebService {
    private String name;
    private Component component;

    public SRCSWebServiceImpl(String name, Component component) {
        this.name = name;
        this.component = component;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void deploy() throws Exception {
        component.start();
    }

    @Override
    public void undeploy() throws Exception {
        component.stop();
    }
}
