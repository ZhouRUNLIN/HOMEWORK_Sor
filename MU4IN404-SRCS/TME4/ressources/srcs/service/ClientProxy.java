package srcs.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientProxy {
    private final int port;
    private final String nom;

    public ClientProxy(String nom, int p) {
        this.nom=nom;
        this.port=p;
    }

    public Object invokeService(String name, Object[] params) throws MyProtocolException {
        try(Socket s = new Socket(nom, port);
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream())) {

                oos.writeUTF(name);
                for(Object ob : params)
                    oos.writeObject(ob);

                Object o = ois.readObject();
                if(o instanceof MyProtocolException)
                    throw new MyProtocolException();
                else
                    return o;

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        throw new MyProtocolException();
    }

}
