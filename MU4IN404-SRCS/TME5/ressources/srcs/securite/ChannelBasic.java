package srcs.securite;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ChannelBasic implements Channel{

    private Socket socket;
    public ChannelBasic(Socket socket){
        this.socket = socket;
    }

    @Override
    public void send(byte[] bytesArray) throws IOException {
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeInt(bytesArray.length);
            dos.write(bytesArray);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public byte[] recv() throws IOException {
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        int size = dis.readInt();
        byte [] bytes = dis.readNBytes(size);
        return bytes;
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
}
