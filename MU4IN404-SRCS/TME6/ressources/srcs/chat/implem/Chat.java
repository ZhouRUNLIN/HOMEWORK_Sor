package srcs.chat.implem;

import java.util.List;

public interface Chat {
	public boolean subscribe(String pseudo, String host, int port);
	public int send(String pseudo, String message);
	public List<String> listChatter();
	public void unsubscribe(String pseudo);
}
