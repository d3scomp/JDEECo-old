package cz.cuni.mff.d3s.deeco.primitives;

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
