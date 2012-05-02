package d3s.deeco.prototype.knowledge;

import d3s.deeco.prototype.interfaces.KnowledgeInterface;

public class Knowledge implements KnowledgeInterface {
	public Long id;
	public Class typeDefinition;
	
	public Knowledge() {
		this.typeDefinition = this.getClass();
	}

	//------------------ Getters and Setters ----------------------------
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long value) {
		id = value;
	}

	@Override
	public Class getTypeDefinition() {
		return typeDefinition;
	}

	@Override
	public void setTypeDefinition(Class value) {
		typeDefinition = value;
	}
	
	
}
