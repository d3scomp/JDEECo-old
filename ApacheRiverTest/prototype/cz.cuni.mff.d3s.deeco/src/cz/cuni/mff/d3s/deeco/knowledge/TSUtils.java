package cz.cuni.mff.d3s.deeco.knowledge;

import net.jini.space.JavaSpace05;

public class TSUtils {
	
	private static JavaSpace05 space = null;
	
	private static Object spaceLock = new Object();

	public static JavaSpace05 getSpace() {
		synchronized (spaceLock) {
			try {
				if (space == null) {
					Lookup lookup = new Lookup(JavaSpace05.class);
					space = (JavaSpace05) lookup.getService();
				}
				return space;
			} catch (Exception e) {
				System.out.println("ERROR - Space retrieval error: "
						+ e.getMessage());
				return null;
			}
		}
	}

	public static Tuple createTuple(String key, Object value) {
		Tuple result = new Tuple();
		result.key = key;
		result.value = value;
		return result;
	}

	public static Tuple createTemplate(String key) {
		Tuple result = new Tuple();
		result.key = key;
		return result;
	}
}
