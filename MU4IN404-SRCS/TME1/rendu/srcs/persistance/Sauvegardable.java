package srcs.persistance;

import java.io.IOException;
import java.io.OutputStream;

public interface Sauvegardable {
    public void save(OutputStream out) throws IOException;
}
