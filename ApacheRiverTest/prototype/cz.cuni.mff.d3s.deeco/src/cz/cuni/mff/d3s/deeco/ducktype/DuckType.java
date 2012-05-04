package cz.cuni.mff.d3s.deeco.ducktype;

import java.lang.reflect.Field;

import cz.cuni.mff.d3s.deeco.knowledge.Knowledge;

public class DuckType {
	public static boolean interfaceMatchesKnowledge(Class interfaceClass,
			Class knowledgeClass) {
		Field cField;
		for (Field field : interfaceClass.getFields()) {
			try {
				cField = knowledgeClass.getField(field.getName());
				if (!(cField.getName().equals(field.getName()) && cField
						.getType().equals(field.getType())))
					return false;
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	public static Knowledge getInterfacedKnowledge(Class interfaceClass,
			Knowledge knowledge) {
		try {
			Knowledge result = null;
			Class knowledgeClass = knowledge.getClass();
			if (Knowledge.class.isAssignableFrom(interfaceClass)) {
				result = (Knowledge) interfaceClass.newInstance();
				for (Field field : interfaceClass.getFields()) {
					field.set(result, knowledgeClass.getField(field.getName()).get(knowledge));
				}
			}
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean castKnowledgeToInterface(Object interfaceInstance,
			Object knowledgeInstance) {
		for (Field field : interfaceInstance.getClass().getFields()) {
			try {
				field.set(interfaceInstance, field.get(knowledgeInstance));
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
}
