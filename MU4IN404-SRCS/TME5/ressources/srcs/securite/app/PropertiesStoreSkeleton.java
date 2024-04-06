package srcs.securite.app;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

import srcs.securite.Channel;

public class PropertiesStoreSkeleton {

	private final Channel comm;
	private final PropertiesStore store;


	public PropertiesStoreSkeleton(Channel comm, PropertiesStore store) {
		super();
		this.comm = comm;
		this.store = store;
	}


	public void loop() throws IOException {
		try {
			while(!Thread.currentThread().isInterrupted()) {
				byte[] rcv =  comm.recv();
				if(rcv == null) break;
				try(DataInputStream dis = new DataInputStream(new ByteArrayInputStream(rcv))){
					String m = dis.readUTF();
					switch(m) {
					case "put":{
						String key = dis.readUTF();
						String value = dis.readUTF();
						String res = store.put(key, value);
						comm.send(res.getBytes());
					}
					break;
					case "get":{
						String key = dis.readUTF();
						String res = store.get(key);
						comm.send(res.getBytes());
						
					}
					break;
					default:
						throw new IllegalArgumentException("commande recue : "+m);
					}

				}

			}
		}catch(EOFException e) {}
		catch(IOException e) {
			e.printStackTrace();
			throw e;
		}
        }



}