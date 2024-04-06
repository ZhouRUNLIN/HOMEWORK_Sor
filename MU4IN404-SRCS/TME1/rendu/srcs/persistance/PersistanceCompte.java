package srcs.persistance;

import srcs.banque.Compte;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PersistanceCompte {
    public static void saveCompte(String f, Compte e) throws IOException {
        FileOutputStream fos = new FileOutputStream(f);
        e.save(fos);
    }

    public static Compte loadCompte(String  f)throws IOException{
        FileInputStream fis = new FileInputStream(f);
        return new Compte(fis);
    }
}
