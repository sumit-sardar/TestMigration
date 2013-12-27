/**
 * 
 */
package job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ctb.prismws.PrismWebServiceHelper;

/**
 * @author TCS
 *
 */
public class PrismWSJob implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("Prism Web Service Job triggered.");
		PrismWebServiceHelper hlpr = new PrismWebServiceHelper();
		hlpr.retryWSProgress();
		
	}

}
