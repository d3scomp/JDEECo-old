package cz.cuni.mff.d3s.deeco.knowledge;

/**
 * Class providing utility functions for dealing with knowledge paths.
 * 
 * @author Michal Kit
 * 
 */
public class KnowledgePathBuilder {
	public static final String PATH_DELIMITER = ".";

	/**
	 * Static function appending a new (tail) part to the existing one (root).
	 * New instance of String object is returned.
	 * 
	 * @param root
	 *            head part of the path
	 * @param tail
	 *            tail path of the path
	 * @return full path
	 */
	public static String appendToRoot(String root, String tail) {
		if (root == null || root.equals(""))
			return tail;
		return root + PATH_DELIMITER + tail;
	}
}
