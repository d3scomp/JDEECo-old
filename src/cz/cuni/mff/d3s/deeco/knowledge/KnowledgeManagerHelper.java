package cz.cuni.mff.d3s.deeco.knowledge;

/**
 * Class used to provide utility function for checking class structure.
 * 
 * @author Michal Kit
 * 
 */
public class KnowledgeManagerHelper {

	/**
	 * Function checks whether the given class is a hierarchical structure, i.e.
	 * it contains public fields.
	 * 
	 * @param structure
	 *            class that needs to be checked
	 * @return true or false regarding the class structure
	 */
	public static boolean isHierarchical(Class structure) {
		return structure.getFields().length > 0;
	}
}
