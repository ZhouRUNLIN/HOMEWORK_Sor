package machine;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Machine {
    private final String id;
    private ServerSocket serverSocket;
    private final int port;
    private final InetAddress host = InetAddress.getLocalHost();
    private int maxSize = 0;

    public Machine(String id, int port) throws IOException {
        this.id = id;
        this.port = port;
        serverSocket = new ServerSocket(port);
    }

    public String getId() {
        return id;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getHost() {
        return host;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

}
