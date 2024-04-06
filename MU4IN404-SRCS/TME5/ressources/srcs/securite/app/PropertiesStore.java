package srcs.securite.app;

import java.io.IOException;

public interface PropertiesStore {

	String put(String key, String value) throws IOException;
	String get(String key)throws IOException;

}
