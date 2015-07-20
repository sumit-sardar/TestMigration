package job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ctb.mssretry.MSSTEScoringRetryHelper;


public class FTTERescoreJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("MSS TE Scoring Retry Job triggered.");
		MSSTEScoringRetryHelper hlpr = new MSSTEScoringRetryHelper();
		hlpr.retryMSSTEScoringProgress();
	}

}
