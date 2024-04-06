package srcs.grpc.util;

import java.lang.reflect.Method;

public final class BuilderUtil {

	private BuilderUtil() {	}
	
	public static <T> T disableStat(T builder) {
		try {
			String names_method[] = new String[] {"setStatsEnabled",
					"setTracingEnabled",
					"setStatsRecordStartedRpcs",
					"setStatsRecordFinishedRpcs",
					"setStatsRecordRealTimeMetrics"};
			for(String name_method : names_method) {
				try {
					Method m = builder.getClass().getDeclaredMethod(name_method, boolean.class);
					m.setAccessible(true);
					m.invoke(builder, false);
				}catch(NoSuchMethodException nsme) {}
				
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return builder;
	}

}
