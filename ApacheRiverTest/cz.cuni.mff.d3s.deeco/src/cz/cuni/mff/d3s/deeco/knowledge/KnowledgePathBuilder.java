package cz.cuni.mff.d3s.deeco.knowledge;

public class KnowledgePathBuilder {
	public static final String PATH_DELIMITER = ".";

	public static String appendToRoot(String root, String tail) {
		if (root == null || root.equals(""))
			return tail;
		return root + PATH_DELIMITER + tail;
	}
}
