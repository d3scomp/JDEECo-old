package cz.cuni.mff.d3s.deeco.knowledge;


public class Knowledge {
	public String id;
	public Class typeDefinition;
	
	public Knowledge() {
		this.typeDefinition = this.getClass();
	}
}
