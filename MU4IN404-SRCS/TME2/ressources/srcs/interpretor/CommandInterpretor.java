package srcs.interpretor;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CommandInterpretor {
    private Map<String, Class<? extends Command>> map = new HashMap<>();

    public CommandInterpretor(){
        map.put("cat", Cat.class);
        map.put("echo", Echo.class);
        map.put("deploy", Deploy.class);
        map.put("undeploy", Undeploy.class);
        map.put("save", Save.class);
    }

    public CommandInterpretor(String fileName) throws IOException, ClassNotFoundException{
        ObjectInputStream ois = new MyClassLoader(new FileInputStream(fileName));
        this.map = (Map<String, Class<? extends Command>>) ois.readObject();
    }

    public Class<? extends Command> getClassOf(String commandName){
        // retourne null si la commande n'exsite pas
        return map.getOrDefault(commandName, null);
    }

    public void perform(String commandLine, PrintStream out) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // parser la ligne de commande par espace
        StringTokenizer tokenizer = new StringTokenizer(commandLine, " ");

        // parcourir tous les elements dans le tokenizer et stocker dans une liste de string
        List<String> wordList = new ArrayList<>();
        while (tokenizer.hasMoreTokens())
            wordList.add(tokenizer.nextToken());

        if (! wordList.isEmpty()) {
            // teste si le premier mot est dans le map de la commande
            String command = wordList.get(0);
            if(getClassOf(command) == null)
                throw new CommandNotFoundException("No such command " + command);

            // testez si la class est interne ou pas
            // cree un object avec la commande correspondant
            if (! getClassOf(command).isMemberClass()){
                getClassOf(command).getDeclaredConstructor(List.class).newInstance(wordList).execute(out);
            } else {
                getClassOf(command)
                        .getDeclaredConstructor(CommandInterpretor.class, List.class)
                        .newInstance(CommandInterpretor.this, wordList)
                        .execute(out);
            }
        }
    }

    public class Deploy implements Command{
        private List<String> args;

        public Deploy(List<String> args) throws MalformedURLException {
            if (args.size() != 4)
                throw new IllegalArgumentException("Usage deploy : <command> <path> <className>");
            this.args = args;
        }

        @Override
        public void execute(PrintStream out) {
            String cmd = args.get(1);
            String path = args.get(2);
            String absoluteName = args.get(3);

            if (map.containsKey(cmd)) throw new IllegalArgumentException(cmd+" already exists");
            try {
                URLClassLoader ucl = URLClassLoader.newInstance(new URL[]{new File(path).toURI().toURL()});
                Class<? extends Command> classe = ucl.loadClass(absoluteName).asSubclass(Command.class);
                map.put(cmd, classe);
                Path filePath = Paths.get(System.getProperty("user.dir")+"/"+absoluteName);
                if (! Files.exists(filePath)) {
                    Files.createFile(filePath);
                }
                Files.write(filePath, path.getBytes(StandardCharsets.UTF_8));
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(path + " not exist");
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(absoluteName + " not exist in " + path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public class Undeploy implements Command{
        private final String commandName;

        public Undeploy(List<String> args){
            if (args == null){
                throw new IllegalArgumentException("Usage undeploy : <command>");
            }
            this.commandName = args.get(1);
        }

        @Override
        public void execute(PrintStream out) {
            if (map.containsKey(commandName)){
                map.remove(commandName);
            }
            else {
                throw new IllegalArgumentException("Undepoly Err : Command not found in map");
            }
        }
    }

    public class Save implements Command{
        private List<String> args;

        public Save(List<String> args){
            if (args.size() != 2)
                throw new IllegalArgumentException("usage save : <file>");
            this.args = args;
        }

        @Override
        public void execute(PrintStream out) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(args.get(1)))){
                oos.writeObject(map);
            } catch (IOException e) {
                e.fillInStackTrace();
            }
        }
    }
}
