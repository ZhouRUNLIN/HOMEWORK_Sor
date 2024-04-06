package srcs.securite.app;

import java.util.HashMap;
import java.util.Map;

public class PropertiesStoreImpl implements PropertiesStore {

	private Map<String, String> map = new HashMap<>();
	
	@Override
	public synchronized String put(String key, String value) {
		String old = map.getOrDefault(key, "");
		map.put(key, value);
		return old;
	}

	@Override
	public synchronized String get(String key) {
		return map.getOrDefault(key, "");
	}

	public synchronized void clear() {
		map.clear();
	}
}
