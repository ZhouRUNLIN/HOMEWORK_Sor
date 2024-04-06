package srcs.interpretor;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.nio.file.Paths;

public class Cat implements Command{
    private final String filePath;

    public Cat(List<String> args){
        // tester les arguments passe dans la liste
        if (args == null){
            throw new IllegalArgumentException("Cat Err : Arguments not exist");
        }
        if (args.size() != 2) {
            throw new IllegalArgumentException("Cat Err : Wrong arguments");
        }
        // enregister le chemin du ficher
        filePath = args.get(1);
        // en utilisent la methode Files.isRegularFile() pour tester si le ficher est un ficher regulier
        if (! Files.isRegularFile(Paths.get(filePath))) {
            throw new IllegalArgumentException("Cat Err : Not reguler file");
        }
    }

    @Override
    public void execute(PrintStream out) {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // emet ligne par ligne vers la flux de sortie
            while ((line = bufferedReader.readLine()) != null) {
                out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
