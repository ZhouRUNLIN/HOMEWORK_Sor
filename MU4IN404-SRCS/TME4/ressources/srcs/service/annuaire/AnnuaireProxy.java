package srcs.service.annuaire;

import srcs.service.ClientProxy;
import srcs.service.MyProtocolException;

import java.util.HashMap;
import java.util.Map;

public class AnnuaireProxy extends ClientProxy implements Annuaire {
    private Map< String,String> map =   new HashMap< String,String>();
    public AnnuaireProxy(String nom, int port) {
        super(nom,port);
    }

    public String lookup(String nom) {
        Object[] params = new Object[1];
        params[0] = nom;
        try {
            return (String) super.invokeService("lookup", params);
        } catch (MyProtocolException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void bind(String nom, String valeur) {
        Object[] params = new Object[2];
        params[0] = nom;
        params[1] = valeur;
        try {
            super.invokeService("bind", params);
        } catch (MyProtocolException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void unbind(String nom) {
        Object[] params = new Object[1];
        params[0] = nom;
        try {
            super.invokeService("unbind", params);
        } catch (MyProtocolException e) {
            e.printStackTrace();
        }
    }

}

