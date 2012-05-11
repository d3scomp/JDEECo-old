package cz.cuni.mff.d3s.deeco.knowledge;

public class KnowledgeManagerHelper {
		
	public static boolean isHierarchical(Class structure) {
		return structure.getFields().length > 0;
	}
}
