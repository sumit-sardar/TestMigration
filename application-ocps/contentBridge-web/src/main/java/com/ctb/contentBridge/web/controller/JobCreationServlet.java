package com.ctb.contentBridge.web.controller;

/**
 * @author 543559
 *
 */

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.publish.command.CIMCommandFactory;
import com.ctb.contentBridge.core.publish.command.CommandLine;
import com.ctb.contentBridge.core.publish.command.ContentImportModuleUtils;
import com.ctb.contentBridge.core.publish.command.InterfaceHandlesUsageException;
import com.ctb.contentBridge.core.publish.tools.OCSConfig;
import com.ctb.contentBridge.web.util.JobRecordPush;

/**
 * Servlet implementation class LoginServlet
 */
public class JobCreationServlet extends HttpServlet implements
		InterfaceHandlesUsageException {

	private OCSConfig ocsConfig;
	public static final String MAPPING_LIST = "mapping.list";
	public static final String MAPPING_DIR = "mapping.dir";
	public static final String FILEFORMAT = "fileformat";
	public static final String MAPPINGFILE = "mappingfile";
	public static final String OBJECTIVESFILE = "objectivesfile";
	public static final String FRAMEWORKFILE = "frameworkfile";
	public static final String ITEMFILE = "itemfile";
	public static final String ENV = "env";
	public static final String VALIDATIONMODE = "validationmode";
	public static final String LOCALIMAGEAREA = "localimagearea";
	public static final String IMAGEAREA = "imagearea";
	public static final String _DEFAULT_ENV = "user";
	public static final String OCS_CONFIG_FILE_NAME = "SystemConfig.properties";
	public static final String ENV_PATH_OCPS = "/appl/publishing/cim/ocps_conf/";
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		try {
			this.doPost(request, response);

		}

		catch (Throwable theException) {
			System.out.println(theException);
		}
	}

	protected void performTask(TeamSiteAdapterArguments args) {
		try {
			String sPropFilePath = System.getProperty("PROPERTIES_FILE_PATH");
			Configuration configuration = new Configuration();
			configuration.load(new File(sPropFilePath));

			String[] arguments = {
					args.getCommand(),
					CIMCommandFactory.JOB_ID + "=" + args.getJobID(),
					CIMCommandFactory.ITEMFILE
							+ "="
							+ new File(args.getWorkArea(),
									args.getJobDocument()).getAbsolutePath(),
					CIMCommandFactory.ENV
							+ "="
							+ configuration
									.getRequiredProperty(args.getCommand()
											+ "." + args.getEnvironment()),

					CIMCommandFactory.IMAGEAREA + "=" + new File(args.getWorkArea()).getAbsolutePath(),
					CIMCommandFactory.VALIDATIONMODE + "=AllowDefaults" };

			CommandLine commandLine = ContentImportModuleUtils
					.getCommandLineTeam(arguments, this);

			String imageArea = commandLine.getParameterValue(IMAGEAREA);

			String arg[] = new String[6];

			arg[0] = commandLine.getCommand();
			
			
			arg[1] = commandLine.getOptionalParameterValue(ENV, _DEFAULT_ENV);
			

			arg[2] = commandLine.getOptionalParameterValue(LOCALIMAGEAREA,
					imageArea);
			arg[3] = (String) commandLine.getParameters().get(ITEMFILE);
			arg[4] = commandLine.getParameterValue(IMAGEAREA);
			arg[5] = args.getJobID();

			JobRecordPush jobRecordPush = new JobRecordPush();
			jobRecordPush.processJob(arg);

		} catch (Exception e) {

		} finally {
			final Logger logger = Logger.getLogger("AppendXML");

		}
	}

	public void handleUsageException(Exception e) {
		String usageString = ContentImportModuleUtils.getUsageString(e);
		final Logger logger = Logger.getLogger("AppendXML");
		logger.error(usageString);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		try {

			String sPropFilePath = System.getProperty("PROPERTIES_FILE_PATH");
			Configuration configuration = new Configuration();
			configuration.load(new File(sPropFilePath));

			String arg[] = new String[6];

			arg[0] = request.getParameter("command");
			arg[1] = request.getParameter("environment");
			arg[2] = request.getParameter("jobID");
			arg[3] = request.getParameter("taskID");
			arg[4] = request.getParameter("workArea");
			arg[5] = request.getParameter("jobDocument");

			TeamSiteAdapterArguments arguments = new TeamSiteAdapterArguments(
					arg);
			performTask(arguments);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}