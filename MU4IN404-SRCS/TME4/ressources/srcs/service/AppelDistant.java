package srcs.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

public interface AppelDistant extends Service {
    @Override
    default public void execute(Socket connexion) {
        try(ObjectOutputStream oos = new ObjectOutputStream(connexion.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(connexion.getInputStream())){
            String name = ois.readUTF();

            int i,len;
            for(Method m : this.getClass().getMethods()) {
                if (m.getName().equals(name)) {
                    len = m.getParameterCount();
                    Object[] objs = new Object[len];

                    for(i = 0; i < len; i++)
                        objs[i] = ois.readObject();
                    oos.writeObject(m.invoke(this, objs));
                    break;
                }
            }
        } catch (ReflectiveOperationException| IOException e) {
            e.printStackTrace();
        }
    }

}
