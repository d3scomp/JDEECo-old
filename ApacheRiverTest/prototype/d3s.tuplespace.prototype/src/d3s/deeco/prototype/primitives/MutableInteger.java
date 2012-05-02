package d3s.deeco.prototype.primitives;

import java.io.Serializable;

public class MutableInteger implements Serializable{
	public Integer value;
	
	public MutableInteger(Integer value) {
		this.value = value;
	}
	
	public String toString() {
		return value.toString();
	}
}
