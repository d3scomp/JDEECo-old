package cz.cuni.mff.d3s.deeco.demo;

import cz.cuni.mff.d3s.deeco.annotations.DEECoCoordinatorIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsemble;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMapper;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMembership;
import cz.cuni.mff.d3s.deeco.annotations.DEECoMemberIn;
import cz.cuni.mff.d3s.deeco.annotations.DEECoMemberOut;
import cz.cuni.mff.d3s.deeco.knowledge.RootKnowledge;

@DEECoEnsemble
public class ConvoyEnsemble {

	// must be public, static and extend RootKnowledge
	public static class ConvoyOutInterface {
		public String convoyRobot;
	}

	private class EnsemblePath {
		public Long currentPosition;
		public String remainingPath;
	}

	@DEECoEnsembleMembership
	public static boolean membership(@DEECoMemberIn("id") String mId,
			@DEECoMemberIn("path.remainingPath") String mRemainingPath,
			@DEECoCoordinatorIn("id") String cId,
			@DEECoCoordinatorIn("path") EnsemblePath cPath) {
		if (!mId.equals(cId)) {
			return mRemainingPath.length() > 0
					&& cPath.remainingPath.length() > 0
					&& cPath.currentPosition
							.equals(getNextPosition(mRemainingPath));
		}
		return false;
	}

	@DEECoEnsembleMapper
	public static void map(@DEECoMemberOut("convoyRobot") ConvoyOutInterface mOutCR,
			@DEECoCoordinatorIn("path.remainingPath") String cRemainingPath) {
		mOutCR.convoyRobot = cRemainingPath;
	}

	public static Integer getNextPosition(String remainingPath) {
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
