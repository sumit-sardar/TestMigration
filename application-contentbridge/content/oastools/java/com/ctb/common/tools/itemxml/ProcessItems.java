package com.ctb.common.tools.itemxml;


import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.ctb.common.tools.CommandLine;
import com.ctb.common.tools.DBConfig;
import com.ctb.util.FileListUtils;
import com.ctb.util.ListUtils;


/**
 * the 'main' class for processing item XML
 */
public class ProcessItems {

    private static String ENVIRONMENT_DIR = "/export/cim";

    public static void main(String[] args) {
        CommandLine cmdLine = null;

        try {
            cmdLine = new CommandLine(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            CommandLine.usage();
            System.exit(1);
        }

        try {
            String commandString = cmdLine.getCommand();

            if (commandString.equals("builditemset")) {
                String environment = cmdLine.getParameterValue("env");
                String jobId = cmdLine.getParameterValue("jobId");
                String criteria = "item.item_id "
                        + ListUtils.listToInClause(FileListUtils.fileToList(jobId
                                + ".txt"));
                JobBuilder builder = new JobBuilder(jobId);
                DBConfig config = new DBConfig(new File(environment
                        + ".properties"));
                ItemsProcessor procMgr = new ItemsProcessor(builder, criteria,
                        null, false, config);

                procMgr.process();
            } else if (commandString.equals("archiveitems")) {
                String environment = cmdLine.getParameterValue("env");
                String criteria = cmdLine.getOptionalParameterValue("criteria",
                        "");
                String outDir = cmdLine.getParameterValue("outDir");
                ItemArchiver archiver = new ItemArchiver(outDir);
                DBConfig config = new DBConfig(new File(environment
                        + ".properties"));
                ItemsProcessor procMgr = new ItemsProcessor(archiver, criteria,
                        null, false, config);

                procMgr.process();
            } else if (commandString.equals("mungeitems")) {
                String environment = cmdLine.getParameterValue("env");
                String criteria = cmdLine.getOptionalParameterValue("criteria",
                        "");
                String outDir = cmdLine.getParameterValue("outDir");
                File mungeDefFile = cmdLine.getFileParameter("mungeDefFile");
                String commitToDb = cmdLine.getParameterValue("commitToDb");
                ItemMunger substProc = new ItemMunger(outDir);
                Properties substitutions = new Properties();

                substitutions.load(new FileInputStream(mungeDefFile));
                substProc.setSubstitutions(substitutions);
                DBConfig config = new DBConfig(new File(environment
                        + ".properties"));
                ItemsProcessor procMgr = new ItemsProcessor(substProc, criteria,
                        outDir, new Boolean(commitToDb).booleanValue(), config);

                procMgr.process();
            } else if (commandString.equals("oasjdomtest")) {
                String environment = cmdLine.getParameterValue("env");
                String criteria = cmdLine.getOptionalParameterValue("criteria",
                        "");
                String outDir = cmdLine.getParameterValue("outDir");
                OASJdomParseReport reporter = new OASJdomParseReport(outDir);
                DBConfig config = new DBConfig(new File(environment
                        + ".properties"));
                ItemsProcessor procMgr = new ItemsProcessor(reporter, criteria,
                        null, false, config);

                procMgr.process();

            } else {
                System.out.println("Invalid command: " + cmdLine.getCommand());
            }
        } catch (Exception e) {
            System.out.println("An error occured processing the command");
            System.out.println(e.getMessage());
        }
    }
}
