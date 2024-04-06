package srcs.persistance;

import java.io.*;

public class PersistanceArray {
    static int length = 0;


    public static void saveArrayInt(String f, int[] tab) throws IOException {

        try(FileOutputStream fw = new FileOutputStream(f);
            DataOutputStream dos = new DataOutputStream(fw)){
            for (int t : tab){
                dos.writeInt(t);
                //System.out.println(t);
            }
            length += tab.length;
        }catch (IOException e){
        }

    }

    public static int[] loadArrayInt(String fichier) throws FileNotFoundException {

        int[] table = new int[length];
        try (FileInputStream fr = new FileInputStream(fichier);
            DataInputStream dis = new DataInputStream(fr)) {
            for (int i = 0; i < length; i++) {
                int lu = dis.readInt();
                //System.out.println(lu);
                table[i] = lu;
                //System.out.println(table[i]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return table;
    }

}
