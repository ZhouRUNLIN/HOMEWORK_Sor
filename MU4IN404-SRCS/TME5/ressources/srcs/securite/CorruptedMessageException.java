package srcs.securite;

import java.io.IOException;

public class CorruptedMessageException extends IOException {

	private static final long serialVersionUID = 1L;

	public CorruptedMessageException() {
		super();
	}

	public CorruptedMessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public CorruptedMessageException(String message) {
		super(message);
	}

	public CorruptedMessageException(Throwable cause) {
		super(cause);
	}

	

}
