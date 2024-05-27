package machine;

import annotations.CommandMethod;
import rmi.ClientErrorSet;
import utils.channel.ChannelWithBuffer;
import utils.enums.ClientState;
import utils.enums.HeartSource;
import utils.enums.HeartState;
import utils.message.*;
import utils.channel.Channel;
import utils.processor.ClientProcessor;
import utils.tools.CountdownTimer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Client extends Machine implements ClientErrorSet {
    private HashMap<String, Object> localHeap = new HashMap<>();
    private ClientProcessor processor = new ClientProcessor();
    private Channel channel;
    private Channel channelHeart;
    private final int serverPort = 8080; // 服务器端口
    private final String serverHost = "localhost"; // 服务器地址
    private ClientState clientState = ClientState.normal;

    int localPort = -1;
    int localPortHeart = -1;

    public Client(int port, String clientId) throws IOException {
        super(clientId, port);
        this.channel = createChannel(true);  //true代表正常消息的隧道
        this.channelHeart = createChannel(false); //false代表心跳消息的隧道
        processor.setCLient(this);
        listenForClientMessages();
        heartBeat();
        registerRmiClient();
    }

    private void heartBeat() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {

            InetAddress localHost = channelHeart.getLocalHost();
            int localPort = super.getPort();

            HeartbeatMessage heartbeatMessage = null;

            switch (clientState){
                case timeout -> {
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    heartbeatMessage = null;
                }
                case errorSource -> {
                    Random random = new Random(3);
                    int index = random.nextInt();

                    switch (index){
                        case 0 -> heartbeatMessage = new HeartbeatMessage(HeartSource.CLIENT, HeartState.HEART,localHost,localPort);
                        case 1 -> heartbeatMessage = new HeartbeatMessage(HeartSource.MIRROR, HeartState.HEART,localHost,localPort);
                        case 2 -> heartbeatMessage = new HeartbeatMessage(null, HeartState.HEART,localHost,localPort);
                    }
                }
                case errorState -> {
                    Random random = new Random(2);
                    int index = random.nextInt();

                    switch (index){
                        case 0 -> heartbeatMessage = new HeartbeatMessage(HeartSource.CLIENT, HeartState.HEARTNORMAL,localHost,localPort);
                        case 1 -> heartbeatMessage = new HeartbeatMessage(HeartSource.MIRROR, null,localHost,localPort);
                    }
                }
                case errorNull -> {
                    heartbeatMessage = null;
                }
                case normal -> {
                    heartbeatMessage = new HeartbeatMessage(HeartSource.CLIENT, HeartState.HEART,localHost,localPort);
                }
            }


            try {
                channelHeart.send(heartbeatMessage);

            } catch (IOException e) {

                reconnectToServer();
                try {
                    channelHeart.send(heartbeatMessage);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

//        }, 20, 20, TimeUnit.SECONDS);
        }, 3, 3, TimeUnit.SECONDS);
//          }, 100, 100, TimeUnit.MILLISECONDS);

    }




    private Channel createChannel(boolean generalMessageOrNo) throws IOException {
        Socket socket = new Socket();
        socket.setReuseAddress(true);
        int targetPort = generalMessageOrNo ? serverPort : serverPort + 1;  // 根据布尔值选择端口
        int port = generalMessageOrNo ? this.localPort : this.localPortHeart;

        if (port == -1) {
            socket.connect(new InetSocketAddress(serverHost, targetPort));
            if (generalMessageOrNo){
                this.localPort = socket.getLocalPort();
            }else {
                this.localPortHeart = socket.getLocalPort();
            }
        } else {
            socket.bind(new InetSocketAddress((InetAddress)null, port));
            socket.connect(new InetSocketAddress(serverHost, targetPort));
        }
        return new ChannelWithBuffer(socket);
    }

    private void reconnectToServer() {
        try {
                System.out.println("检测到连接中断，尝试重连");
                channel.close();
                channelHeart.close();
                CountdownTimer timer = new CountdownTimer(2);  // 创建一个2秒的倒计时
                timer.start();
                this.channel = createChannel(true); // 重新建立连接
                this.channelHeart = createChannel(false);
                System.out.println("重连成功！");
        } catch (IOException e) {
                System.out.println("无法连接到服务器: " + e.getMessage());
        }
    }

    public boolean heapHaveData(String variableId){
        return localHeap.containsKey(variableId);
    }

    public HashMap<String, Object> getLocalHeap(){
        return localHeap;
    }

    public List<String> getAllStringsFromLocalHeap() {
        List<String> stringsList = new ArrayList<>();

        for (Map.Entry<String, Object> entry : localHeap.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String) {
                stringsList.add((String) value);
            }
        }

        return stringsList;
    }

    public void setObject(String variableId, Object o){
        localHeap.put(variableId, o);
    }

    public boolean modifyHeap(String key, Object value) {
        localHeap.put(key, value);
        return true;
    }

    public void request(String methodType, String args) throws InvocationTargetException, IllegalAccessException {
        for (Method method: getClass().getDeclaredMethods()){
            if (method.getName().equals(methodType) && method.isAnnotationPresent(CommandMethod.class)){
                method.invoke(this, args);
                break;
            }
        }
    }

    public Channel connectToClient(InetAddress host, int port) throws IOException {
        return new ChannelWithBuffer(new Socket(host, port));
    }

    public void listenForClientMessages() {
        new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {

                    System.out.println("lfcm已启动");

                    Channel localChannel = new ChannelWithBuffer(super.getServerSocket().accept());

                    System.out.println("read开始~");

                    SendDataMessage recv = (SendDataMessage) localChannel.recv();

                    System.out.println("lfcm收到的消息：" + recv.toString());

                    Object result = localHeap.get(recv.getVariableId());

                    localChannel.send(new SendDataMessage(recv.getVariableId(), result));
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }


    //向服务器发送消息，查看该数据是否存在，如果收到不存在消息，在自己的堆里加入这个数据
    @CommandMethod
    private void dMalloc(String id) throws IOException, SecurityException, IllegalArgumentException, ClassNotFoundException {
        ClientMessage message = new ClientMessage("dMalloc", getId(), id, super.getPort());
        sendMessage(message,id);
    }

    //向服务器发送写入请求，(如果存在这个数据并且数据未上锁)收到确认消息，返回自己堆中该数据的地址位置，如果收到报错信息，返回null
    @CommandMethod
    private int dAccessWrite(String id) throws IOException, ClassNotFoundException{
        ClientMessage message = new ClientMessage("dAccessWrite", getId(), id, super.getPort());
        sendMessage(message,id);
        return 1;
    }

    //向服务器发送读取请求，(如果存在这个数据并且数据未上锁)收到确认消息，根据返回的信息判断是否直接读取自己的数据，或向另一个客户端传输读取请求，如果读取出错，向服务器发送错误消息，读取成功修改自己的堆返回地址
    @CommandMethod
    private int dAccessRead(String id) throws  IOException, ClassNotFoundException{
        ClientMessage message = new ClientMessage("dAccessRead", getId(), id, super.getPort());
        sendMessage(message,id);
        return 1;
    }

    //回复修改确认消息，(将数据设置为不可修改?)
    @CommandMethod
    private void dRelease(String variableId) throws IOException, ClassNotFoundException{
        ClientMessage message = new ClientMessage("dRelease", getId(), variableId, super.getPort());
        sendMessage(message,variableId);
    }

    //发出删除信号消息，等待回信
    @CommandMethod
    private void dFree(String id) throws IOException, ClassNotFoundException{
        ClientMessage message = new ClientMessage("dFree",getId(), id, super.getPort());
        sendMessage(message,id);
    }

    private void sendMessage(Message message, String id) throws IOException, ClassNotFoundException {

        try {
            channel.send(message);
        }catch (IOException e){
            reconnectToServer();
            channel.send(message);
        }
        processor.process(channel, id, message);

    }

    private void setClientState(ClientState clientState1){

        clientState = clientState1;

    }
    @Override
    public void setClientError(ClientState clientState) throws RemoteException {
        setClientState(clientState);
    }

    private void registerRmiClient() throws RemoteException {
        try {
            String name = "ClientControl_" + getId(); // 唯一标识符
            ClientErrorSet stub = (ClientErrorSet) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.getRegistry("localhost", 1099); // 获取默认注册表或创建新的
            registry.rebind(name, stub); // 绑定或重新绑定到注册表
            System.out.println("Client bound in registry as " + name);
        } catch (RemoteException e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

}