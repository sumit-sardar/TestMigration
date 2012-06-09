package controls;

import model.*;

import org.apache.beehive.controls.api.bean.*;
import org.apache.beehive.controls.api.events.EventSet;

@ControlInterface
public interface SchedulingControl {
	public SchedulingData scheduleSession(String testLevel, String firstName, String lastName);

	
}
