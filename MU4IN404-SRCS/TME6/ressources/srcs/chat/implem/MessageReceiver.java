package srcs.chat.implem;

public interface MessageReceiver {
	void newMessage(String pseudo_expediteur, String contenu);
}
