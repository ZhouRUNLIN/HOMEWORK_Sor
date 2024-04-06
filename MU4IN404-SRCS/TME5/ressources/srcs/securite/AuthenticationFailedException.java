package srcs.securite;

import java.io.IOException;

public class AuthenticationFailedException extends IOException {


	private static final long serialVersionUID = 1L;

	public AuthenticationFailedException() {
		super();
	}

	public AuthenticationFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticationFailedException(String message) {
		super(message);
	}

	public AuthenticationFailedException(Throwable cause) {
		super(cause);
	}

	

}
