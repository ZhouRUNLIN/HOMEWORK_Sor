package srcs.service.calculatrice;

import srcs.service.MyProtocolException;
import srcs.service.SansEtat;
import srcs.service.Service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

@SansEtat
public class CalculatriceService implements Calculatrice, Service, Serializable {

    @Override
    public void execute(Socket connexion) {
        try(ObjectOutputStream oos = new ObjectOutputStream(connexion.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(connexion.getInputStream())){

            Object ret = ois.readUTF();
            Integer x = ois.readInt();
            Integer y = ois.readInt();

            switch ((String) ret) {
                case "add":
                    oos.writeObject(add(x, y));
                    break;
                case "sous":
                    oos.writeObject(sous(x, y));
                    break;
                case "mult":
                    oos.writeObject(mult(x, y));
                    break;
                case "div":
                    oos.writeObject(div(x, y));
                    break;
                default:
                    oos.writeObject(new MyProtocolException());
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Integer add(Integer x, Integer y) {
        return x + y;
    }

    @Override
    public Integer sous(Integer x, Integer y) {
        return x - y;
    }

    @Override
    public Integer mult(Integer x, Integer y) {
        return x*y;
    }

    @Override
    public ResDiv div(Integer x, Integer y) {
        return new ResDiv(x, y);
    }
}
