package srcs.securite;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.InetAddress;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface Channel {

	void send(byte[] bytesArray) throws IOException;

	byte[] recv() throws IOException;
	
	InetAddress getRemoteHost();
	int getRemotePort();
	
	InetAddress getLocalHost();
	int getLocalPort();

}
