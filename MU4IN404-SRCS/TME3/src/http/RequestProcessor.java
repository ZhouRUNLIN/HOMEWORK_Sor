package http;
import java.net.Socket;

public interface RequestProcessor {
    public void process(Socket connexion);
}
