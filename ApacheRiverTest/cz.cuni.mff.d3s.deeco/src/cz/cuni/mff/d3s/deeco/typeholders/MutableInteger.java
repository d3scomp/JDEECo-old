package cz.cuni.mff.d3s.deeco.typeholders;

import java.io.Serializable;

public class MutableInteger implements Serializable {
	public Integer value;

	public MutableInteger(Integer value) {
		this.value = value;
	}

	public String toString() {
		return value.toString();
	}

	public boolean equals(Object o) {
		return o != null && o instanceof MutableInteger
				&& value.equals((Integer) o);
	}
}
