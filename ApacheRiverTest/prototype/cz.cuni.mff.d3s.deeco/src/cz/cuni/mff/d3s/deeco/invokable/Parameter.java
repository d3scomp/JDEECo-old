package cz.cuni.mff.d3s.deeco.invokable;


/**
 * Class used to represent a method parameter.
 * 
 * @author Michal Kit
 *
 */
public class Parameter {
	
	public String name;
	public Class type;
	public int index;
	
	public Parameter(String name, Class type, int index) {
		this.name = name;
		this.type = type;
		this.index = index;
	}
}
