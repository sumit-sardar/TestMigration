/**
 * 
 */
package com.ctb.contentBridge.web.util;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.ctb.contentBridge.core.audit.service.ContentBridgeService;
import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.domain.JobBean;
import com.ctb.contentBridge.core.publish.command.CommandLine;
import com.ctb.contentBridge.core.publish.command.ContentImportModule;
import com.ctb.contentBridge.core.publish.command.EmailGateway;
import com.ctb.contentBridge.core.publish.report.CommandReport;
import com.ctb.contentBridge.core.publish.report.CommandReportFormatter;
import com.ctb.contentBridge.core.publish.report.Report;
import com.ctb.contentBridge.core.publish.tools.OCSConfig;

/**
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public class PublishOrderPollingAction implements Runnable {
	 private  EmailGateway emailGateway;
	 private  OCSConfig ocsConfig;
	 protected static Logger logger = Logger
				.getLogger(PublishOrderPollingAction.class);
	@Override
	public void run() {
		System.out.println("PollingThread starting.");
				 
		
        PropertyConfigurator.configure(PublishOrderPollingAction.class.getClassLoader().getResource(
                "log4j.properties"));

        
      logger.info("Start");
		String sPropFilePath = System.getProperty("PROPERTIES_FILE_PATH");
		System.out.println("PropFilePath:	" + sPropFilePath);
		
         File configFile=new File(sPropFilePath);
		Configuration configuration = new Configuration();
		Report report = null;
		CommandLine commandLine = null;
		String subject_test=null;
		try {
			do {
				/*
				 * Thread.sleep(3000);
				 * System.out.println("In MyThread, count is " +
				 * System.currentTimeMillis());
				 */
				configuration.load(new File(sPropFilePath));
				 ocsConfig = new OCSConfig(configFile);
					emailGateway = new EmailGateway(ocsConfig);
					 

				ContentBridgeService vContentBridgeService = new ContentBridgeService();
				ArrayList<JobBean> rJobList = (ArrayList<JobBean>) vContentBridgeService
						.getPublishOrderJob(configuration);
				if (rJobList != null && !rJobList.isEmpty()) {
					
					for (JobBean vJobBean : rJobList) {
						System.out.println("Processing job starts:	"+ vJobBean.getJobPk());
												commandLine = new CommandLine();
						commandLine.setCommand(vJobBean.getCommand());
						commandLine.setParameters(vJobBean.getParameters());
						
						ContentImportModule vContentImportModule = new ContentImportModule();
						 report = vContentImportModule.begin(commandLine);

						String jobStatus = "Error";
						String errMsg = "";
						String subject = "";
						String subjectBig = "OCS Job ";
						 String msgText = "";
						if (report != null) {
							if (report.isSuccess()) {
								jobStatus = "Success";
								msgText = report.toString();
							} else {
								
								jobStatus = "Error";
								errMsg = report.toString();
								System.out.println("Error in Report\n" + errMsg);
										/*CommandReportFormatter
										.getErrorMessage((CommandReport) report);*/
								msgText = errMsg;
							}
						} else {
							jobStatus = "Error";
							errMsg = "Error has occurred in publish process.";
							msgText = errMsg;
						}
						String jobEnv[]=new String[2];
						ContentBridgeService.getJobIdEnv(configuration, vJobBean.getJobPk(),jobEnv);
						/*msgText=report.toString();*/

						subject+= subjectBig+jobEnv[0]
					                + " ["
					                + "conf/"+jobEnv[1]
					                + "]";
			            subject += (report.isSuccess())
			                    ? EmailGateway.SUCCESS_SUBJECT
			                    : EmailGateway.FAILURE_SUBJECT;
			            
			            if(errMsg != null && errMsg.length() >= 4000) {
			            	errMsg = errMsg.substring(0, 3900);
			            }
						vContentBridgeService.updateJobStatus(configuration,
								vJobBean.getJobPk(), jobStatus, errMsg);
						System.out.println("Processing job ends:	"+ vJobBean.getJobPk());
						logger.info("Message for Mail : "+msgText);
						emailGateway.sendEmail(subject,msgText);
						
					}
				}
				System.out.println("Waiting for next set of jobs.");
				Long sleepDuration = new Long(3000);
				String sleepTime = configuration.getSleepTime();
				if(sleepTime != null) {
					sleepDuration = new Long(sleepTime);
				}
				Thread.sleep(sleepDuration);
			} while (true);
		} catch (InterruptedException exc) {
			logger.info(exc);
			System.out.println("MyThread interrupted." + exc);
		} catch (Exception exc) {
			logger.info(exc);
			System.out.println("MyThread interrupted." + exc);
		}
		finally{
			 /*if ( report != null )
	            {
					boolean longReportFormat = getReportFormat(commandLine);
		            PrintWriter writer = new PrintWriter(System.out);
		            Formatter formatter =
		                FormatterFactory.create(report, longReportFormat);
		            formatter.print(writer, false);
		            if(formatter instanceof CommandReportFormatter)
		                ((CommandReportFormatter) formatter).print(true);
		            writer.flush();
	            }*/
			
		}
		System.out.println("MyThread terminating.");
	}

	public static void main(String[] args) {
		String sPropFilePath = System.getProperty("PROPERTIES_FILE_PATH");
		// PropertyConfigurator.configure(PublishOrderPollingAction.class.getClassLoader().getResource(
	      //          "log4j.properties"));

		Configuration configuration = new Configuration();
		try {
			configuration.load(new File(sPropFilePath));
			ContentBridgeService vContentBridgeService = new ContentBridgeService();
			ArrayList<JobBean> rJobList = (ArrayList<JobBean>) vContentBridgeService
					.getPublishOrderJob(configuration);
			if (rJobList != null && !rJobList.isEmpty()) {
				CommandLine commandLine = null;
				for (JobBean vJobBean : rJobList) {
					commandLine = new CommandLine();
					commandLine.setCommand(vJobBean.getCommand());
					commandLine.setParameters(vJobBean.getParameters());

					ContentImportModule vContentImportModule = new ContentImportModule();
					Report report = vContentImportModule.begin(commandLine);

					String jobStatus = "Error";
					String errMsg = "";
					if (report != null) {
						if (report.isSuccess()) {
							jobStatus = "Success";
						} else {
							jobStatus = "Error";
							String errorMsg = CommandReportFormatter
									.getErrorMessage((CommandReport) report);
						}
					} else {
						jobStatus = "Error";
						String errorMsg = "Error has occurred in publish process.";
					}
					vContentBridgeService.updateJobStatus(configuration,
							vJobBean.getJobPk(), jobStatus, errMsg);
				}
				System.out.println("End");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 protected static boolean getReportFormat(CommandLine commandLine) {
	        boolean longReportFormat = true;
	        final String longFormat = "longformat";
	        final String reportFormat = "reportformat";
	        final String shortFormat = "shortformat";
	        if (longFormat
	            .equals(
	                commandLine.getOptionalParameterValue(
	                    reportFormat,
	                    shortFormat)))
	            return longReportFormat;
	        return !longReportFormat;
	    }
}