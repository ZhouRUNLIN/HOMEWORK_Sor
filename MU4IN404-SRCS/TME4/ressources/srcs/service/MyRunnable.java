package srcs.service;

import java.net.Socket;
// TME4 Exercice1 Question3
public class MyRunnable implements Runnable {
    private final Service service;
    private final Socket connexion;

    public MyRunnable(Service service, Socket connexion){
        this.service = service;
        this.connexion = connexion;
    }

    @Override
    public void run() {
        service.execute(connexion);
    }
}
