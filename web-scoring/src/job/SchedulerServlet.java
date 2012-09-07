package job;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.SchedulerException;

public class SchedulerServlet extends HttpServlet {

	public static final long serialVersionUID = 1L;
	
	@Override
	public void init() {
		try {
			CronTriggerRunner.start();
			System.out.println("Scheduler started successfully");
		} catch(SchedulerException she) {
			she.printStackTrace();
		}
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		super.doGet(request, response);
	}
	
	@Override
	public void destroy() {
		try {
			CronTriggerRunner.stop();
		} catch(SchedulerException she) {
			she.printStackTrace();
		}
	}
}