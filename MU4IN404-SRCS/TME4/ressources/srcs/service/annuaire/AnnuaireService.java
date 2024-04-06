package srcs.service.annuaire;

import srcs.service.EtatGlobal;
import srcs.service.MyProtocolException;
import srcs.service.Service;
import srcs.service.VoidResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

@EtatGlobal
public class AnnuaireService implements Annuaire, Service {
    private Semaphore mutex = new Semaphore(1);
    private Map<String, String> map = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void execute(Socket connexion) {
        try (ObjectOutputStream oos = new ObjectOutputStream(connexion.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(connexion.getInputStream())) {
            String ret = ois.readUTF();
            String name = ois.readUTF();
            switch (ret){
                case "lookup":
                    oos.writeObject(lookup(name));
                    break;
                case "bind":
                    bind(name, ois.readUTF());
                    oos.writeObject(new VoidResponse());
                    break;
                case "unbind":
                    unbind(name);
                    oos.writeObject(new VoidResponse());
                    break;
                default:
                    oos.writeObject(new MyProtocolException());
            }
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public synchronized String lookup(String name) {
        String res = map.get(name);
        return (res == null) ? "" : res;
    }

    @Override
    public synchronized void bind(String name, String value) {
        map.put(name, value);
    }

    @Override
    public synchronized void unbind(String name) {
        map.remove(name);
    }
}
