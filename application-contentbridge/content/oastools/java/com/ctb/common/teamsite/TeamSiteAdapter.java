package com.ctb.common.teamsite;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;

import com.ctb.commands.CIMCommandFactory;
import com.ctb.commands.ContentImportModuleUtils;
import com.ctb.commands.EmailGateway;
import com.ctb.commands.InterfaceHandlesUsageException;
import com.ctb.common.tools.CommandLine;
import com.ctb.common.tools.OCSConfig;
import com.ctb.common.tools.SystemException;
import com.ctb.reporting.Report;

/**
 * User: mwshort
 * Date: Feb 3, 2004
 * Time: 10:34:09 AM
 *
 */
public class TeamSiteAdapter implements InterfaceHandlesUsageException {

    protected static Logger logger = Logger.getLogger(TeamSiteAdapter.class);

    public static final String DEFAULT_OCS_HOME = "/export/cim/ccs/";
    public static final String OCS_CONFIG_FILE_NAME = "OCS.config";
    public static File OCS_CONFIG_FILE =
        new File("/export/cim/" + OCS_CONFIG_FILE_NAME);

    public static final String ROOT_JOB_PATH = "/appl/oas1/";
    private static final String LOG4J_CONFIG_FILE_PATH =
        "conf/log4j.properties";
    public static final String LOG4J_PATTERN = "%d{ISO8601} %5p [%t] %m%n";
    public static final String LOG_FILE_NAME = "ctb_deploy.trace";

    private final OCSConfig ocsConfig;
    private final EmailGateway emailGateway;

    public TeamSiteAdapter(File configFile) {
        ocsConfig = new OCSConfig(configFile);
        emailGateway = new EmailGateway(ocsConfig);
    }
    public TeamSiteAdapter(OCSConfig ocsConfig,EmailGateway gateway) {
        this.ocsConfig = ocsConfig;
        this.emailGateway = gateway;
    }

    public static void main(String[] args) throws Exception {
        String ocsHome = null;
        File configFile = null;
        String log4JConfigFilename = null;

        if (new File(DEFAULT_OCS_HOME).exists()) {
            ocsHome = DEFAULT_OCS_HOME;
            configFile = OCS_CONFIG_FILE;
        } else {
            ocsHome = ".";
            configFile = new File(OCS_CONFIG_FILE_NAME);
        }

        PropertyConfigurator.configure(LOG4J_CONFIG_FILE_PATH);

        if (configFile.exists()) {
            TeamSiteAdapter tsa = new TeamSiteAdapter(configFile);
            tsa.process(args);
        } else
            throw new SystemException(
                OCS_CONFIG_FILE_NAME + " file not found in " + ocsHome);

    }

    protected void process(String[] args) {
        String subject = "OCS Job ";
        String msgText = "";
        try {
            TeamSiteAdapterArguments arguments =
                new TeamSiteAdapterArguments(args);
            setLoggingToDir(createJobDirectory(arguments.getJobID()));
            performTask(arguments);
        } catch (Exception e) {
            subject += EmailGateway.FAILURE_SUBJECT;
            msgText = "Error while processing Command:\n" + e.toString() + "\n";
            logger.info(msgText);
            emailGateway.sendEmail(subject, msgText);
        }

    }

    protected void performTask(TeamSiteAdapterArguments args) {
        String[] arguments =
            {
                args.getCommand(),
                CIMCommandFactory.JOB_ID + "=" + args.getJobID(),
                CIMCommandFactory.ITEMFILE
                    + "="
                    + new File(args.getWorkArea(), args.getJobDocument())
                        .getAbsolutePath(),
                CIMCommandFactory.ENV
                    + "="
                    + ocsConfig.getOptionalProperty(
                        args.getCommand() + "." + args.getEnvironment(),
                        args.getEnvironment()),
                CIMCommandFactory.IMAGEAREA + "=" + args.getWorkArea(),
                CIMCommandFactory.VALIDATIONMODE + "=AllowDefaults" };

        CommandLine commandLine =
            ContentImportModuleUtils.getCommandLine(arguments, this);

        String subject = "OCS Job ";
        String msgText = "";

        try {
            subject += commandLine.getParameterValue(CIMCommandFactory.JOB_ID)
                + " ["
                + commandLine.getParameterValue(CIMCommandFactory.ENV)
                + "]";
            Report report = ContentImportModuleUtils.run(commandLine, this);
            subject += (report.isSuccess())
                ? EmailGateway.SUCCESS_SUBJECT
                : EmailGateway.FAILURE_SUBJECT;
            msgText = report.toString();
        } catch (Exception e) {
            subject += EmailGateway.FAILURE_SUBJECT;
            msgText = "Error while processing Command:\n" + e.toString() + "\n";
        } finally {
            logger.info(msgText);
            emailGateway.sendEmail(subject, msgText);
        }
    }

    public void handleUsageException(Exception e) {
        String usageString = ContentImportModuleUtils.getUsageString(e);
        logger.error(usageString);
        emailGateway.sendEmail(EmailGateway.USAGE_ERROR_SUBJECT, usageString);
    }

    private File createJobDirectory(String dirName) {
        File jobDir = new File(ROOT_JOB_PATH + dirName + "/");
        if (!jobDir.exists())
            jobDir.mkdirs();
        return jobDir;
    }

    private void setLoggingToDir(File jobDirectory) {
        String logFileName =
            new File(jobDirectory, LOG_FILE_NAME).getAbsolutePath();
        Logger rootLogger = Logger.getRootLogger();
        Appender fa;
        try {
            fa =
                new FileAppender(new PatternLayout(LOG4J_PATTERN), logFileName);
        } catch (IOException e) {
            throw new SystemException(e.getMessage(), e);
        }
        rootLogger.addAppender(fa);
    }

}
