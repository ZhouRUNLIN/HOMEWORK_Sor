package srcs.securite;

import java.io.IOException;

public class CertificateCorruptedException extends IOException {

	private static final long serialVersionUID = 1L;

	public CertificateCorruptedException() {
		super();
	}

	public CertificateCorruptedException(String message, Throwable cause) {
		super(message, cause);
	}

	public CertificateCorruptedException(String message) {
		super(message);
	}

	public CertificateCorruptedException(Throwable cause) {
		super(cause);
	}
	
}
