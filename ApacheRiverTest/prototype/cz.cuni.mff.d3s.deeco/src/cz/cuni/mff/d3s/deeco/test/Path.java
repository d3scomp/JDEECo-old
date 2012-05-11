package cz.cuni.mff.d3s.deeco.test;


public class Path {
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
