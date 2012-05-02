package d3s.deeco.prototype.test;

import d3s.deeco.prototype.annotations.DEECoEnsemble;
import d3s.deeco.prototype.annotations.DEECoEnsembleMapper;
import d3s.deeco.prototype.annotations.DEECoEnsembleMembership;

@DEECoEnsemble
public class ConvoyEnsemble {

	static interface MemberInterface extends CoordinatorInterface {
		public void setConvoyRobot(Long id);
	}

	static interface CoordinatorInterface {
		public Long getId();

		public Path getPath();
	}

	@DEECoEnsembleMembership
	public static boolean membership(MemberInterface member,
			CoordinatorInterface coordinator) {
		if (!member.getId().equals(coordinator.getId())) {
			Path mPath = member.getPath();
			Path cPath = coordinator.getPath();
			return mPath.remainingPath.length() > 0
					&& cPath.remainingPath.length() > 0
					&& cPath.getCurrentPosition().equals(
							mPath.getNextPosition());
		}
		return false;
	}

	@DEECoEnsembleMapper
	public static void map(MemberInterface member,
			CoordinatorInterface coordinator) {
		member.setConvoyRobot(coordinator.getId());
	}
}
