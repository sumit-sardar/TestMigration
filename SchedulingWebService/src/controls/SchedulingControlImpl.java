package controls;

import java.io.*;

import model.*;

import org.apache.beehive.controls.api.bean.*;
import org.apache.beehive.controls.api.events.Client;

@ControlImplementation
public class SchedulingControlImpl implements SchedulingControl, Serializable {
	private static final long serialVersionUID = 1L;

	private Boolean initialized = false;

	@Control
	private SchedulingDB schedulingDB;


 
	public SchedulingData scheduleSession(String testLevel, String firstName, String lastName) {
		return schedulingDB.scheduleSession(testLevel, firstName, lastName);
	}

}
