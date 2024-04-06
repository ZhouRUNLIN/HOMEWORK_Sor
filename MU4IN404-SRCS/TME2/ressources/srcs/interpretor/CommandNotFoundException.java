package srcs.interpretor;

@SuppressWarnings("serial")
public class CommandNotFoundException extends RuntimeException {

	public CommandNotFoundException() {
		super();
	}

	public CommandNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CommandNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommandNotFoundException(String message) {
		super(message);
	}

	public CommandNotFoundException(Throwable cause) {
		super(cause);
	}

}
