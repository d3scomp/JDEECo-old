package cz.cuni.mff.d3s.deeco.test;

import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsemble;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMapper;
import cz.cuni.mff.d3s.deeco.annotations.DEECoEnsembleMembership;
import cz.cuni.mff.d3s.deeco.knowledge.Knowledge;

@DEECoEnsemble
public class ConvoyEnsemble {

	//must be public, static and extend Knowledge
	public static class MemberInterface extends CoordinatorInterface {
		public String convoyRobot;
	}

	public static class CoordinatorInterface extends Knowledge {
		public Path path;
	}

	@DEECoEnsembleMembership
	public static boolean membership(MemberInterface member,
			CoordinatorInterface coordinator) {
		if (!member.id.equals(coordinator.id)) {
			Path mPath = member.path;
			Path cPath = coordinator.path;
			return mPath.remainingPath.length() > 0
					&& cPath.remainingPath.length() > 0
					&& cPath.currentPosition.equals(
							mPath.getNextPosition());
		}
		return false;
	}

	@DEECoEnsembleMapper
	public static void map(MemberInterface member,
			CoordinatorInterface coordinator) {
		member.convoyRobot = coordinator.path.remainingPath;
	}
}
