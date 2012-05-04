package cz.cuni.mff.d3s.deeco.test;

import cz.cuni.mff.d3s.deeco.knowledge.Knowledge;

public class Path extends Knowledge {
	public Integer currentPosition;
	public String remainingPath;

	public Integer getNextPosition() {
		if (remainingPath.length() > 0) {
			int commaPos = remainingPath.indexOf(",");
			if (commaPos < 0) {
				return Integer.parseInt(remainingPath);
			} else {
				return Integer.parseInt(remainingPath.substring(0, commaPos));
			}
		}
		return -1;
	}
}
