package job;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.ctb.mssretry.MSSTEScoringRetryHelper;
import com.ctb.prismws.PrismWebServiceHelper;
import com.ctb.schedular.Configuration;


public class CronTriggerRunner {
	
	private static SchedulerFactory schedulerFactory = null;
	private static Scheduler scheduler = null;
		
	public static void start() throws SchedulerException {
		// Initiate a Schedule Factory
	    schedulerFactory = new StdSchedulerFactory();
	    // Retrieve a scheduler from schedule factory
	    scheduler = schedulerFactory.getScheduler();
	    
	    // Initiate JobDetail with job name, job group, and executable job class
	    JobDetail jobDetail = newJob(RescoreJob.class)
	    							.withIdentity("jobDetail", "jobDetailGroup").build();
	    // Initiate CronTrigger with its name and group name
	    Configuration configuration = Configuration.getConfiguration();
		CronTrigger trigger =  newTrigger().withIdentity("trigger1", "jobDetailGroup")
	    						.withSchedule(cronSchedule(configuration.getCronExpression()))
	    						.build();
	
	    // schedule a job with JobDetail and Trigger
	    scheduler.scheduleJob(jobDetail, trigger);
	    
	    // Initiate JobDetail with job name, job group, and executable job class
	    JobDetail wsJobDetail = newJob(PrismWSJob.class)
	    							.withIdentity("PrismWebServiceJob", "PrismWebServiceGroup").build();
	    CronTrigger wsTrigger = newTrigger().withIdentity("trigger1", "PrismWebServiceGroup")
				.withSchedule(cronSchedule(new PrismWebServiceHelper().getPrismWSCronExpression()))
				.build();
	    // schedule a job with JobDetail and Trigger
	    scheduler.scheduleJob(wsJobDetail, wsTrigger);
	    
	    /*
	     * Commenting out MSS TE Retry Job of Mosaic Field Test TE Scoring
	     */
	/* // Initiate JobDetail with job name, job group, and executable job class
	    JobDetail mssRetryJobDetail = newJob(FTTERescoreJob.class)
	    							.withIdentity("MSSTEScoringRetryJob", "MSSTEScoringRetryGroup").build();
	    CronTrigger mssRetryTrigger = newTrigger().withIdentity("trigger1", "MSSTEScoringRetryGroup")
				.withSchedule(cronSchedule(new MSSTEScoringRetryHelper().getMSSRetryCronExpression()))
				.build();
	    // schedule a job with JobDetail and Trigger
	    scheduler.scheduleJob(mssRetryJobDetail, mssRetryTrigger);*/
	    
	    // start the scheduler
	    scheduler.start();
	}
	
	public static void stop()  throws SchedulerException {
		if(scheduler != null) 
			scheduler.shutdown();
	}
}
