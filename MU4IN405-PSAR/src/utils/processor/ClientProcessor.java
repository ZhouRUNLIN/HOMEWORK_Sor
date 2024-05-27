package utils.processor;

import machine.Client;
import utils.channel.Channel;
import utils.enums.OperationStatus;
import utils.message.*;

import java.io.IOException;

public class ClientProcessor implements Processor{
    private Client client = null;

    public void setCLient(Client client){
        this.client = client;
    }

    @Override
    public void process(Channel channel, String variableId, Message clientMessage) throws IOException, ClassNotFoundException {
        ServerMessage message = (ServerMessage) channel.recv();

        if (message.getMessageType() == MessageType.DAR && message.getOperationStatus() == OperationStatus.SUCCESS) {

            Channel distanceChannel = client.connectToClient(message.getClientHost(), message.getClientPort());

            SendDataMessage sendDataMessage = new SendDataMessage(variableId, client.getHost(), client.getPort());

            System.out.println(sendDataMessage + " read message ！！！！！！！！！！");

            distanceChannel.send(sendDataMessage);


            SendDataMessage replyMessage = (SendDataMessage) distanceChannel.recv();

            client.modifyHeap(sendDataMessage.getVariableId(), replyMessage.getValue());
        }


        if (message.getOperationStatus() == OperationStatus.LOCKED){
            new Thread(()->{

                boolean run = true;

                while (run) {

                    System.out.println("进入locked线程！！！");

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        channel.send(clientMessage);

                        System.out.println("重发完毕");

                        ServerMessage serverMessage = (ServerMessage) channel.recv();

                        if (serverMessage.getOperationStatus() == OperationStatus.SUCCESS && serverMessage.getMessageType() == MessageType.DAR ){

                            run = false;


                            Channel distanceChannel = client.connectToClient(serverMessage.getClientHost(), serverMessage.getClientPort());

                            SendDataMessage sendDataMessage = new SendDataMessage(variableId, client.getHost(), client.getPort());

                            System.out.println(sendDataMessage + " read message ！！！！！！！！！！");

                            distanceChannel.send(sendDataMessage);

                            SendDataMessage replyMessage = (SendDataMessage) distanceChannel.recv();

                            client.modifyHeap(sendDataMessage.getVariableId(), replyMessage.getValue());
                        }

                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

    }


}
