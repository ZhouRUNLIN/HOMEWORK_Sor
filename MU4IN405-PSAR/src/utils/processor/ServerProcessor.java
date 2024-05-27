package utils.processor;

import machine.Server;
import utils.channel.ChannelWithBuffer;
import utils.enums.HeartSource;
import utils.enums.HeartState;
import utils.enums.OperationStatus;
import utils.enums.ServerState;
import utils.tools.Pair;
import utils.message.*;
import utils.channel.Channel;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static utils.enums.OperationStatus.*;

public class ServerProcessor implements Processor {
    private Server server = null;
    private Pair pair;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private volatile long lastHeartbeatTime = System.currentTimeMillis();
    private static ServerState serverState = ServerState.normal;


    private void checkHeartbeat(HeartbeatMessage heartbeatMessage) {
        if (System.currentTimeMillis() - lastHeartbeatTime > 3000  || heartbeatMessage.getSource() != HeartSource.CLIENT || heartbeatMessage.getOperationStatus() != HeartState.HEART || heartbeatMessage == null) { //3s
            //System.out.println("未收到客户端心跳");


            InetAddress host = (InetAddress) pair.first();
            int port = (int)pair.second();


            System.out.println("客户端(" + host + "   " + port + ")" + "发生错误，已断开连接！");
            System.out.println("清理该客户端痕迹");

//            LinkedList<String> keysToRemove = new LinkedList<>();

            // 遍历HashMap
            for (String key : server.getHeap().keySet()) {
                LinkedList<Pair> pairs = server.getHeap().get(key);
                boolean pairFound = false;
                Iterator<Pair> iterator = pairs.iterator();

                while (iterator.hasNext()) {
                    Pair currentPair = iterator.next();
                    if (currentPair.equals(pair)) {
                        iterator.remove();  // 移除这个Pair
                        pairFound = true;
                        System.out.println("Removed Pair " + currentPair + " from list under key '" + key + "'");
                    }
                }

//                // 如果找到了Pair，并且列表为空，则标记键为删除
//                if (pairFound && pairs.isEmpty()) {
//                    keysToRemove.add(key);
//                    System.out.println("List under key '" + key + "' is now empty and will be removed from the heap.");
//                }
            }

//            // 删除所有标记的键
//            for (String key : keysToRemove) {
//                server.getHeap().remove(key);
//                System.out.println("Removed key '" + key + "' from the heap.");
//            }
            scheduler.shutdownNow();
       }

    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void process(Channel channel, String id,Message message) throws IOException, ClassNotFoundException, InterruptedException {

        //System.out.println("waiting for message on port : " + channel.getRemotePort());   //这句记得取消注释

        Message messageRecy = (Message) channel.recv(); // 注意~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        if (messageRecy instanceof HeartbeatMessage) {  //收到的是心跳信息
            switch (((HeartbeatMessage) messageRecy).getSource()){
                case MIRROR -> handlingHBMirror(channel);
                case CLIENT ->  handlingHBClient((HeartbeatMessage) messageRecy);
            }
        } else if (messageRecy instanceof ClientMessage){  //收到的是客户端信息
            handlingClientMessage((ClientMessage) messageRecy, channel);
        }
    }

    private void handlingHBMirror(Channel channel) throws IOException, InterruptedException {
        //System.out.println("收到来自镜像的心跳信息");


        switch (serverState){

            case timeout -> {
                Thread.sleep(100000); //100s
                channel.send(null);
            }
            case errorSource -> {
                Random random = new Random(3);
                int index = random.nextInt();

                switch (index){
                    case 0 -> channel.send(new HeartbeatMessage(HeartSource.CLIENT, HeartState.HEARTNORMAL));
                    case 1 -> channel.send(new HeartbeatMessage(HeartSource.MIRROR,HeartState.HEARTNORMAL));
                    case 2 -> channel.send(new HeartbeatMessage(null,HeartState.HEARTNORMAL));
                }
            }
            case errorState -> {
                Random random = new Random(2);
                int index = random.nextInt();

                switch (index){
                    case 0 -> channel.send(new HeartbeatMessage(HeartSource.SERVER,HeartState.HEART));
                    case 1 -> channel.send(new HeartbeatMessage(HeartSource.SERVER,null));
                }

            }
            case errorNull -> {
                channel.send(null);
            }
            case normal -> {
                channel.send(new HeartbeatMessage(HeartSource.SERVER,HeartState.HEARTNORMAL));

            }
        }


    }

    private void handlingHBClient(HeartbeatMessage heartbeatMessage) {
        scheduler.scheduleWithFixedDelay(() -> checkHeartbeat(heartbeatMessage), 0, 1, TimeUnit.SECONDS);

        lastHeartbeatTime = System.currentTimeMillis();

        pair = heartbeatMessage.getPair();
    }

    private void handlingClientMessage(ClientMessage clientMessage,Channel channel) throws IOException {
        ServerMessage message;
        String clientId = clientMessage.getClientId();
        String variableId = clientMessage.getVariableId();

        int clientPort = clientMessage.getClientPort();
        System.out.println("message recv from client : " + clientId);
        System.out.println("client port : " + clientPort);
        System.out.println("variableId: " + variableId);

        System.out.println("处理客户端请求");
        switch (clientMessage.getCommand()) {
            case "dMalloc" -> message = handleDMalloc(variableId);
            case "dAccessWrite" -> message = handleDAccessWrite(variableId, channel.getRemoteHost(), clientPort);
            case "dAccessRead" -> message = handleDAccessRead(variableId);
            case "dRelease" -> message = handleDRelease(variableId);
            case "dFree" -> message = handleDFree(variableId);
            default -> message = new ServerMessage(MessageType.EXP, OperationStatus.COMMAND_ERROR);
        }
        System.out.println("heap: " + server.getHeap());

        if (message.getOperationStatus() == LOCKED){
            ChannelWithBuffer.addMessageToLockedMap(clientMessage);
        }

        channel.ownQueuePOP();

        channel.send(message);
    }


    public static void setNormalorNot(ServerState serverState1){
        serverState = serverState1;
    }



    //返回成功信息//如果数据信息已经存在或添加数据信息失败，发送错误信息
    private ServerMessage handleDMalloc(String variableId) {
        System.out.println("收到初始化数据信息");
        //检查服务器中是否有这个数据
        if (server.variableExistsHeap(variableId)) {
            return new ServerMessage(MessageType.DMA, OperationStatus.DATA_EXISTS);
        } else {
            //尝试在服务器堆中添加数据信息
            server.modifyHeapDMalloc(variableId);
            return new ServerMessage(MessageType.DMA, SUCCESS);
        }
    }

    //检查是否有这个数据，如果存在并且未上锁，(如果还没有此客户的信息)尝试在服务器堆中添加数据信息。等待dRelease信息，接收到修改完毕消息后，设置成拥有最新消息客户(将数据放到双向链表头部代表此客户拥有最新数据信息)
    private ServerMessage handleDAccessWrite(String variableId, InetAddress host, int port) {
        System.out.println("收到写入请求");
        if (!server.variableExistsHeap(variableId)) {
            return new ServerMessage(MessageType.DAW, OperationStatus.DATA_NOT_EXISTS);
        }

        switch (server.modifyHeapDAccessWrite(variableId, host, port)) {
            case SUCCESS -> {
                return new ServerMessage(MessageType.DAW, SUCCESS); //没锁
            }
            case LOCKED -> {
                return new ServerMessage(MessageType.DAW, LOCKED); //锁了
            }
            default -> {
                return new ServerMessage(MessageType.DRE, OperationStatus.ERROR);
            }
        }
    }

    //检查是否有这个数据，如果存在并且未上锁，如果此客户不是最新消息客户，回信最新客户的地址用来联系。等待dRelease信息，接收到读取完毕消息后，设置成拥有最新消息客户(放到双向链表头部代表此客户拥有最新数据信息,如果出现问题无所谓)
    private ServerMessage handleDAccessRead(String variableId) {
        System.out.println("收到客户端阅读请求");
        if (!server.variableExistsHeap(variableId)) {
            return new ServerMessage(MessageType.DAR, OperationStatus.DATA_NOT_EXISTS);
        }

        Object obj = server.modifyHeapDAccessRead(variableId).first();
        //System.out.println(obj.toString() + "   obj");
        switch ((OperationStatus) obj) {
            case SUCCESS -> {
                Pair p = (Pair) server.modifyHeapDAccessRead(variableId).second();

               // System.out.println(p.first().toString() + "  p1");
               // System.out.println(p.second().toString() + "  p2");

                ServerMessage s = new ServerMessage(MessageType.DAR, SUCCESS,(InetAddress) p.first(), (Integer) p.second());

                //System.out.println(s.toString() + "  s");

                return s;
            }
            case LOCKED -> {
                return new ServerMessage(MessageType.DAR, LOCKED);
            }
            default -> {
                return new ServerMessage(MessageType.DAR, OperationStatus.ERROR);
            }
        }

    }


    //在这里收到Drelease是错误的，直接报错
    private ServerMessage handleDRelease(String variableId) throws IOException {
        System.out.println("数据使用完毕");

        if (!server.variableExistsHeap(variableId)) {
            return new ServerMessage(MessageType.DAR, OperationStatus.DATA_NOT_EXISTS);
        }

        switch (server.modifyHeapDRelease(variableId)) {
            case SUCCESS -> {

                ChannelWithBuffer.removeMessagesByVariableId(variableId);

                return new ServerMessage(MessageType.DRE, SUCCESS);
            }
            case ERROR -> {
                return new ServerMessage(MessageType.DRE, OperationStatus.ERROR);
            }
        }

        return null;
    }

    //检查服务器是否有这个数据，如果存在，通知所有存在数据信息的客户删除数据，收到回信后删除这个数据信息
    private ServerMessage handleDFree(String variableId) throws IOException {
        System.out.println("收到客户端删除数据请求");

        if (!server.variableExistsHeap(variableId)) {
            return new ServerMessage(MessageType.DFR, OperationStatus.DATA_NOT_EXISTS);
        }

        switch (server.modifyHeapDFree(variableId)) {
            case SUCCESS -> {
                return new ServerMessage(MessageType.DFR, SUCCESS);
            }
            case LOCKED -> {
                return new ServerMessage(MessageType.DFR, LOCKED);
            }
        }

        return new ServerMessage(MessageType.DRE, OperationStatus.ERROR);
    }


}


