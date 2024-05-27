package machine;

import rmi.ForcedServerShutdown;
import utils.channel.Channel;
import utils.channel.ChannelWithBuffer;
import utils.enums.HeartSource;
import utils.enums.HeartState;
import utils.message.HeartbeatMessage;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MirrorInitiator extends Machine {

    private Channel channel;
    private boolean continueRunning = true;
    private boolean isOverCalled = false; // 避免over方法重复调用
    private String serverHost =  "localhost";
    private int serverport = 8080;
    public MirrorInitiator(String id, int port) throws IOException {
        super(id, port);
        this.channel = new ChannelWithBuffer(new Socket(serverHost, serverport+1));
        startHeartbeat();
    }

    private void startHeartbeat() {
        new Thread(() -> {
            while (continueRunning) {
                try {
                    heartbeatSend();
                    heartbeatRecv();
                    Thread.sleep(5000);// 5s  后期得改成30
                } catch (Exception e) {
                    handleError(e);
                }
            }
        }).start();
    }

    private void handleError(Exception e) {
        if (!isOverCalled) {
            System.out.println("An error occurred: " + e.getMessage());
            try {
                reconnect();
            } catch (IOException | ClassNotFoundException ex) {
            }
            isOverCalled = true; // 标记已调用
        }
    }

    private void heartbeatSend() throws IOException {
        HeartbeatMessage hbm = new HeartbeatMessage(HeartSource.MIRROR, HeartState.HEART);
        channel.send(hbm);
    }

    private void heartbeatRecv() throws IOException, ClassNotFoundException {
        HeartbeatMessage heartbeatMessage = null;

        try {
            heartbeatMessage = (HeartbeatMessage) channel.recvWithTimeout(5000);  //timeout为5秒
        }catch (SocketTimeoutException e){
            handleError(e);
        }

        if (heartbeatMessage.getOperationStatus() != HeartState.HEARTNORMAL || heartbeatMessage.getSource() != HeartSource.SERVER || heartbeatMessage == null) {
            throw new SocketException("No response received from server.");
        }
        //System.out.println(heartbeatMessage.toString());

    }

    public void reconnect() throws IOException, ClassNotFoundException {
        if (!continueRunning) {    // 如果已经处理过，直接返回
            return;
        }

        System.out.println("心跳连接失效/socket连接断开！！！");
        continueRunning = true;
        killServer();

        System.out.println("启动镜像服务器");
        restartServer(8080,"server");

    }

    public static void killServer(){    //关闭服务器
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
            ForcedServerShutdown stub = (ForcedServerShutdown) registry.lookup("RemoteShutdownService");
            stub.forcedserverShutdown();
            System.out.println("Remote method invoked");
        } catch (Exception e) {
            //System.err.println("Client exception: " + e.toString());
            //e.printStackTrace();
        }
    }


    public static void restartServer(int port, String serverName) throws IOException, ClassNotFoundException {
        Server server = new Server(port, serverName);
        server.start();
    }


}
