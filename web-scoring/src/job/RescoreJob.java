package job;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ctb.schedular.Configuration;
import com.ctb.schedular.ScheduleRosterBO;


/**
 * @author TCS Kolkata Offshore 
 * @version 05/09/2012
 */
public class RescoreJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		System.out.println("Scheduler For Rescore Fired@" + new Date());
		try {
			Configuration configuration = Configuration.getConfiguration();
			ScheduleRosterBO bo = new ScheduleRosterBO();
			bo.scheduleRoster(configuration);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
