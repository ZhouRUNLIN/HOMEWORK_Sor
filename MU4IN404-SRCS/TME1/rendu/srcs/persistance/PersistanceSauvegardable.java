package srcs.persistance;

import srcs.banque.Compte;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

public class PersistanceSauvegardable {
    public static void save(String fichier, Sauvegardable s) throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(fichier));
        dos.writeUTF(s.getClass().getCanonicalName());
        s.save(dos);
        dos.close();
    }

    public static Sauvegardable load(String fichier) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        DataInputStream dis = new DataInputStream(new FileInputStream(fichier));
        String nameClass = dis.readUTF();
        Sauvegardable s = Class.forName(nameClass).asSubclass(Sauvegardable.class).getConstructor(InputStream.class).newInstance(dis);
        dis.close();
        return s;
    }
}
