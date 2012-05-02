package d3s.deeco.prototype.test;

import d3s.deeco.prototype.knowledge.Knowledge;

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

	public Integer getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(Integer currentPosition) {
		this.currentPosition = currentPosition;
	}

	public String getRemainingPath() {
		return remainingPath;
	}

	public void setRemainingPath(String remainingPath) {
		this.remainingPath = remainingPath;
	}

}
