package srcs.service.calculatrice;


import srcs.service.ClientProxy;
import srcs.service.MyProtocolException;

public class CalculatriceProxy extends ClientProxy implements Calculatrice{


    public CalculatriceProxy(String nom, int portCalculatrice) {
        super(nom,portCalculatrice);
    }

    @Override
    public Integer add(Integer a, Integer b) {
        return operationSimple("add", a, b);
    }

    @Override
    public Integer sous(Integer a, Integer b) {
        return operationSimple("sous", a, b);
    }

    @Override
    public Integer mult(Integer a, Integer b) {
        return operationSimple("mult", a, b);
    }

    @Override
    public ResDiv div(Integer a, Integer b) {
        Object[] params = new Object[2];
        params[0] = a;
        params[1] = b;
        try {
            return (ResDiv) super.invokeService("div", params);
        } catch (MyProtocolException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Integer operationSimple(String name, Integer a, Integer b) {
        Object[] params = new Object[2];
        params[0] = a;
        params[1] = b;
        try {
            return (Integer) super.invokeService(name, params);
        } catch (MyProtocolException e) {
            e.printStackTrace();
        }
        return null;
    }
}