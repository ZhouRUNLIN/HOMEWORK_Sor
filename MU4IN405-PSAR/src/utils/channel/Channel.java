package utils.channel;

import utils.message.ClientMessage;
import utils.message.Message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface Channel {
    // S2C通信方法
    void send(Message message) throws IOException;  // 服务器到客户端发送消息
    Object recv() throws IOException, ClassNotFoundException;  // 服务器到客户端接收消息


    public void ownQueueOffer(Message message);
    public void ownQueuePOP();

    // 接收消息，可以指定超时时间
    Object recvWithTimeout(int timeout) throws IOException, ClassNotFoundException;

    // 获取远程主机地址
    InetAddress getRemoteHost();

    // 获取远程端口号
    int getRemotePort();

    // 获取本地主机地址
    InetAddress getLocalHost();

    // 获取本地端口号
    int getLocalPort();

    // 获取Socket连接
    Socket getSocket();

    // 关闭连接
    void close() throws IOException;
}
