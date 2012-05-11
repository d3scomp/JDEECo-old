package cz.cuni.mff.d3s.deeco.annotations;

/**
 * IValuedAnnotation interface is used to simplify annotation parsing. It is
 * used to retrieve the value attribute, which is very common among the
 * annotations.
 * 
 * @author Michal Kit
 * 
 */
public interface IValuedAnnotation {
	public String value();
}
