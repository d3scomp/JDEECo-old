package cz.cuni.mff.d3s.deeco.scheduling;

import java.lang.annotation.Annotation;

import cz.cuni.mff.d3s.deeco.annotations.DEECoPeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.DEECoTriggeredScheduling;

public class ScheduleHelper {
	public static ProcessSchedule getSchedule(Annotation scheduleAnnotation) {
		if (scheduleAnnotation == null || scheduleAnnotation instanceof DEECoPeriodicScheduling) {
			return new ProcessPeriodicSchedule(((DEECoPeriodicScheduling) scheduleAnnotation).value());
		} else if (scheduleAnnotation instanceof DEECoTriggeredScheduling) {
			return new ProcessTriggeredSchedule();
		} else 
			return null;
	}
}
