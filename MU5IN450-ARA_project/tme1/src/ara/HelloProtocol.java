package ara;

import ara.util.HelloMessage;
import ara.util.Message;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.transport.Transport;

import java.util.ArrayList;
import java.util.List;

public class HelloProtocol implements EDProtocol {
    private static final String PAR_TRANSPORT = "transport";
    private static final String PAR_MAXSIZE = "maxsizelist";

    private final int pid_transport;
    private final int maxsizelist;

    private final int my_pid; // 存储协议ID
    private List<Integer> mylist; // 每个节点的专有列表
    private boolean deja_dit_bonjour = false; // 表示是否已经发送过hello消息

    public HelloProtocol(String prefix) {
        String tmp[] = prefix.split("\\.");
        my_pid = Configuration.lookupPid(tmp[tmp.length - 1]);
        pid_transport = Configuration.getPid(prefix + "." + PAR_TRANSPORT);
        maxsizelist = Configuration.getInt(prefix + "." + PAR_MAXSIZE);
        mylist = new ArrayList<>();
    }

    public Object clone() {
        HelloProtocol ap = null;
        try {
            ap = (HelloProtocol) super.clone();
            ap.mylist=new ArrayList<>();
        } catch( CloneNotSupportedException e ) {} // never happens
            return ap;
    }

    public void direBonjour(Node host) {
        Transport tr = (Transport) host.getProtocol(pid_transport);
        for (int i = 0; i < Network.size(); i++) {
            Node dest = Network.get(i);
            Message mess = new HelloMessage(host.getID(), dest.getID(), my_pid, new ArrayList<>(mylist));
            tr.send(host, dest, mess, my_pid);
        }
        deja_dit_bonjour = true;
    }

    private void receiveHelloMessage(Node host, HelloMessage mess) {
        System.out.println("节点 " + host.getID() + " 接收到来自 " + mess.getIdSrc() + " 的Hello消息，内容为：" + mess.getInfo());
        if (!deja_dit_bonjour) {
            direBonjour(host);
        }
    }

    public void initialisation(Node host) {
        int size_list = CommonState.r.nextInt(maxsizelist);
        for (int i = 0; i < size_list; i++) {
            mylist.add(CommonState.r.nextInt(128));
        }
        if (host.getID() == 0) {
            direBonjour(host);
        }
    }

    @Override
    public void processEvent(Node host, int pid, Object event) {
        if (pid != my_pid) throw new IllegalArgumentException("协议ID不一致");
        if (event instanceof HelloMessage) {
            receiveHelloMessage(host, (HelloMessage) event);
        } else {
            throw new IllegalArgumentException("未知事件类型");
        }
    }



}
