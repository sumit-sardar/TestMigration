package com.ctb.mapping;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.commons.lang.StringUtils;

import com.ctb.common.tools.CommandLine;
import com.ctb.common.tools.DBConfig;
import com.ctb.common.tools.SystemException;
import com.ctb.hibernate.HibernateSession;
import com.ctb.hibernate.HibernateUtils;

/** 
 * manage execution of framework imports and comparisons, including
 * connection management and reporting of results 
 * @author djrice
 */
public class ObjectivesFileProcessorMain {

    public static final String FRAMEWORK_IMPORT_COMMAND = "import";
    public static final String FRAMEWORK_COMPARE_COMMAND = "compare";

    public static void main(String[] args) {
        ObjectivesCommandLine cmdLine = null;
        ObjectivesFileProcessorMain main = new ObjectivesFileProcessorMain();
        try {
            cmdLine = new ObjectivesCommandLine(args);
        } catch (Exception e) {
            System.err.println("unable to parse command line");
            e.printStackTrace();
            System.err.println("\n\n\n----");
            main.showHelp();
        }
        try {
            main.exec(cmdLine);
        } catch (HibernateException e1) {
            System.err.println("unable to execute command");
            e1.printStackTrace();
        }
    }

    public void exec(ObjectivesCommandLine cmdLine) throws HibernateException {
        Connection conn = null;
        Session session = null;
        try {
            conn = cmdLine.dbConfig.openConnection();
            session = HibernateUtils.getSession(conn);
            if (cmdLine.cmd.equals(FRAMEWORK_IMPORT_COMMAND)) {
                try {
                    executeFrameworkImport(cmdLine, session);
                    conn.commit();
                } catch (Exception e) {
                    System.out.println(
                        "Unexpected error importing framwork.  Transaction will rollback.");
                    e.printStackTrace();
                    rollback(conn);
                }
            } else {
                executeFrameworkCompare(cmdLine, session);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException("unexpected error", e);
        } finally {
            if (session != null) {
                HibernateSession.currentSession().flush();
                HibernateSession.currentSession().close();
            }
            close(conn);
        }
    }

    private void executeFrameworkCompare(
        ObjectivesCommandLine cmdLine,
        Session session)
        throws IOException {
        ObjectiveComparison comparison =
            new ObjectiveComparison(
                session,
                cmdLine.objectivesFileName,
                cmdLine.frameworkInfo,
                cmdLine.builder);

        List activities = null;
        if (cmdLine.rootObjective == null) {
            activities = comparison.run(cmdLine.verbose, cmdLine.fixnames);
        } else {
            activities =
                comparison.run(
                    cmdLine.verbose,
                    cmdLine.fixnames,
                    cmdLine.rootObjective);
        }

        reportObjectiveComparison(activities, cmdLine);
    }

    private void reportObjectiveComparison(
        List activities,
        ObjectivesCommandLine cmdLine)
        throws IOException {
        String header =
            "===="
                + "\nobjectives file:  "
                + new File(cmdLine.objectivesFileName).getAbsolutePath()
                + "\ndb properties:  "
                + cmdLine.env
                + "\nroot objective:  "
                + (cmdLine.rootObjective == null
                    ? cmdLine.frameworkInfo.getFrameworkCode()
                    : cmdLine.rootObjective)
                + "\nverbose="
                + cmdLine.verbose
                + "\n====\n";

        PrintWriter logger = null;
        FileWriter fileWriter = null;

        if (cmdLine.console) {
            logger = new PrintWriter(System.out);
        } else {
            DateFormat timeFmt = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            String filename =
                "objective_rpt_"
                    + cmdLine.frameworkInfo.getFrameworkCode()
                    + "_"
                    + cmdLine.env
                    + "_"
                    + timeFmt.format(new Date())
                    + ".txt";

            fileWriter = new FileWriter(filename);
            logger = new PrintWriter(fileWriter);
        }
        new ObjectiveComparisonActivityReporter(header, activities, logger)
            .run();
        if (fileWriter != null) {
            fileWriter.flush();
            fileWriter.close();
        }
    }

    private void executeFrameworkImport(
        ObjectivesCommandLine cmdLine,
        Session session) {
        ObjectivesImporter importer =
            new ObjectivesImporter(
                session,
                cmdLine.objectivesFileName,
                cmdLine.frameworkInfo,
                cmdLine.builder);

        if (cmdLine.rootObjective == null) {
            importer.importFramework();
        } else {
            importer.importSubtree(cmdLine.rootObjective);
        }

    }

    private void close(Connection conn) {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException sqlEx) {
            System.out.println("Warning:  unexpected error closing connection");
            sqlEx.printStackTrace();
        }
    }

    private void rollback(Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException sqlEx) {
            System.out.println(
                "Warning:  unexpected error rolling back connection");
            sqlEx.printStackTrace();
        }
    }

    private void showHelp() {
        String text =
            "usage: \n"
                + "fimp <import/compare> env=<env> FrameworkFile=<filename> [FileFormat=long|short]"
                + "ObjectivesFile=<filename> [RootObjective=true] [fixnames=true] [verbose=true] [console=true]\n"
                + "where\n"
                + "\tenv             env name (properties file)\n"
                + "\tFrameworkFile   contains framework definition\n"
                + "\tFileformat      (optional) format of the objectives file, permitted values= long, short\n"
                + "\tObjectivesFile  file that contains the objectives\n"
                + "\tRootObjective   (optional) the root of the import operation\n";

        System.out.println(text);
    }

    static class ObjectivesCommandLine {
        String cmd;
        FrameworkInfo frameworkInfo;
        String objectivesFileName;
        String rootObjective;
        String env;
        DBConfig dbConfig;
        ObjectiveBuilder builder;
        boolean verbose;
        boolean fixnames;
        boolean console;

        public ObjectivesCommandLine(String[] args) throws IOException {
            CommandLine cmdLine = new CommandLine(args);

            cmd = cmdLine.getCommand();
            if (!cmd.equals(FRAMEWORK_IMPORT_COMMAND)
                && !cmd.equals(FRAMEWORK_COMPARE_COMMAND)) {
                throw new SystemException("unrecognized command:  " + cmd);
            }
            String fileFormat = null;

            frameworkInfo =
                new FrameworkInfo(cmdLine.getFileParameter("frameworkfile"));
            objectivesFileName = cmdLine.getParameterValue("objectivesfile");
            rootObjective =
                cmdLine.getOptionalParameterValue("rootobjective", null);
            String envParam = cmdLine.getParameterValue("env");
            File envFile = new File(envParam + ".properties");
            env = StringUtils.chomp(envFile.getName(), ".properties");
            dbConfig = new DBConfig(envFile);
            fileFormat =
                cmdLine.getOptionalParameterValue(
                    "fileformat",
                    MapperFactory.FILEFORMAT_SHORT);
            verbose =
                new Boolean(
                    cmdLine.getOptionalParameterValue("verbose", "false"))
                    .booleanValue();
            fixnames =
                new Boolean(
                    cmdLine.getOptionalParameterValue("fixnames", "false"))
                    .booleanValue();
            console =
                new Boolean(
                    cmdLine.getOptionalParameterValue("console", "false"))
                    .booleanValue();

            if (fileFormat.equals("long")) {
                builder = new LongFormatBuilder();
            } else {
                builder = new ShortFormatBuilder();
            }
        }

    }
}
