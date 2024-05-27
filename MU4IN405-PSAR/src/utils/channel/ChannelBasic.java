package utils.channel;

import utils.message.Message;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class ChannelBasic implements Channel {
    private final Socket socket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public ChannelBasic(Socket socket){
        this.socket = socket;
    }

    @Override
    public void send(Message message) throws IOException{
        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(message);
        oos.flush();
    }


    @Override
    public Object recv() throws IOException, ClassNotFoundException  {
        ois = new ObjectInputStream(socket.getInputStream());
        return ois.readObject();
    }



    @Override
    public void ownQueueOffer(Message message) {

    }

    @Override
    public void ownQueuePOP() {

    }

    @Override
    public Object recvWithTimeout(int timeout) throws IOException, ClassNotFoundException {  //这是个带超时的recv
        //超时之后会抛出SocketTimeoutException
        if (timeout > 0) {
            socket.setSoTimeout(timeout);
        }
        try {
            if (ois == null) {
                ois = new ObjectInputStream(socket.getInputStream());
            }
            return ois.readObject();
        } catch (SocketTimeoutException e) {
            throw new IOException("接收超时: " + e.getMessage(), e);
        }
    }

    @Override
    public InetAddress getRemoteHost() {
        return socket.getInetAddress();
    }

    @Override
    public int getRemotePort() {
        return socket.getPort();
    }

    @Override
    public InetAddress getLocalHost() {
        return socket.getLocalAddress();
    }

    @Override
    public int getLocalPort() {
        return socket.getLocalPort();
    }

    @Override
    public Socket getSocket() {
        return socket;
    }

    @Override
    public void close() throws IOException {
        try {
            if (ois != null) {
                ois.close(); // 关闭输入流
            }
        } finally {
            try {
                if (oos != null) {
                    oos.close(); // 关闭输出流
                }
            } finally {
                if (socket != null) {
                    socket.close(); // 关闭socket连接
                }
            }
        }
        System.out.println("Channel closed successfully.");
    }
}