package srcs.securite.app;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import srcs.securite.Channel;

public class PropertiesStoreProxy implements PropertiesStore {
	
	private final Channel channel;
	
	public PropertiesStoreProxy( Channel communicator) {
		this.channel=communicator;
	}
	
	@Override
	public String put(String key, String value) throws IOException {
		
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
			try(DataOutputStream dos = new DataOutputStream(baos)){
				dos.writeUTF("put");
				dos.writeUTF(key);
				dos.writeUTF(value);
			}
			channel.send(baos.toByteArray());
			byte[] rcv = channel.recv();
			return new String(rcv);
			

        }
    }

	@Override
	public String get(String key) throws IOException {
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
			try(DataOutputStream dos = new DataOutputStream(baos)){
				dos.writeUTF("get");
				dos.writeUTF(key);
			}
			channel.send(baos.toByteArray());
			byte[] rcv = channel.recv();
			return new String(rcv);

        }
    }
}
