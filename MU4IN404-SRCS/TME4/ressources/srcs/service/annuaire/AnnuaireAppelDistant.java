package srcs.service.annuaire;

import srcs.service.AppelDistant;
import srcs.service.EtatGlobal;

import java.util.HashMap;
import java.util.Map;

@EtatGlobal
public class AnnuaireAppelDistant implements AppelDistant, Annuaire{
    private Map< String,String> map =   new HashMap< String,String>();
    @Override
    public String lookup(String nom) {
        synchronized (map) {
            if (map.containsKey(nom))
                return map.get(nom);
        }
        return "";
    }

    @Override
    public void bind(String nom, String valeur) {
        synchronized (map) {
            if (!map.containsKey(nom))
                map.put(nom, valeur);
        }
    }

    @Override
    public void unbind(String nom) {
        synchronized (map) {
            if (map.containsKey(nom))
                map.remove(nom);
        }
    }


}