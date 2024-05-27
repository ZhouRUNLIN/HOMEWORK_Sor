package utils.message;

import utils.enums.HeartSource;
import utils.enums.HeartState;
import utils.tools.Pair;

import java.io.Serializable;
import java.net.InetAddress;

public class HeartbeatMessage implements Message, Serializable {

    private HeartSource source;
    private HeartState operationStatus;
    private Pair pair;
    public HeartbeatMessage(HeartSource source, HeartState op) {
        this.source = source;
        this.operationStatus = op;
    }

    public HeartbeatMessage(HeartSource source, HeartState operationStatus, InetAddress host, int port) {
        this.source = source;
        this.operationStatus = operationStatus;
        this.pair = new Pair(host,port);
    }

    public Pair getPair() {
        return pair;
    }

    public HeartSource getSource() {
        return source;
    }

    public HeartState getOperationStatus() {
        return operationStatus;
    }


    @Override
    public String toString() {
        return "HeartbeatMessage{" +
                "source=" + source +
                ", operationStatus=" + operationStatus +
                ", pair=" + pair +
                '}';
    }
}
