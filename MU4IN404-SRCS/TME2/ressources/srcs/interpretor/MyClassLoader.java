package srcs.interpretor;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyClassLoader extends ObjectInputStream {
    public MyClassLoader(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String className = desc.getName();
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e){
            Path filePath = Paths.get(System.getProperty("user.dir")+"/"+className);
            if (! Files.exists(filePath)) {
                throw new NoSuchFileException(System.getProperty("user.dir")+"/"+className);
            }
            URLClassLoader ucl = URLClassLoader.newInstance(new URL[]{new File(Files.readString(filePath)).toURI().toURL()});
            return ucl.loadClass(className).asSubclass(Command.class);
        }
    }
}

