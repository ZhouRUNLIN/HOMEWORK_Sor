package ara.projet.ee1;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import ara.projet.mutex.NaimiTrehelAlgo;

public class Initialisateur implements Control {
    private static final String PAR_PROTOCOL = "ini_protocol"; // 对应配置文件中的参数名称
    private final int protocolID;

    public Initialisateur(String prefix) {
        // 从配置中读取协议 ID
        protocolID = Configuration.lookupPid(Configuration.getString(PAR_PROTOCOL));
    }

    @Override
    public boolean execute() {
        System.out.println("Initializing network...");

        // 遍历网络中的每个节点，初始化其协议状态
        for (int i = 0; i < Network.size(); i++) {
            Node node = Network.get(i);
            NaimiTrehelAlgo algo = (NaimiTrehelAlgo) node.getProtocol(protocolID);

            // 输出每个节点的初始化状态
            System.out.println("Node " + node.getID() + " initialized.");
        }

        System.out.println("Initialization completed.");
        return false; // 返回 false 表示模拟继续
    }
}
