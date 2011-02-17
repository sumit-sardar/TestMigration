/*
 * Created on Nov 3, 2003
 *
 */
package com.ctb.roundtrip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.ctb.common.tools.CommandLine;
import com.ctb.mapping.Mapper;
import com.ctb.mapping.MapperFactory;
import com.ctb.reporting.CommandReport;
import com.ctb.reporting.Formatter;
import com.ctb.reporting.FormatterFactory;
import com.ctb.reporting.Report;

public class ProcessItems {
    private static Logger logger = Logger.getLogger(ProcessItems.class);
    static {
        PropertyConfigurator.configure("conf/log4j.properties");
    }

    public static void main(String[] args) {
        logger.info("Start time: " + new Date());
        CommandLine cmdLine = null;
        cmdLine = getCommandLine(args, cmdLine);

        RoundTripProcessingCommand processingCommand =
            new RoundTripProcessingCommand();

        try {
            String commandString = cmdLine.getCommand();

            if ((commandString.equals("map"))
                || (commandString.equals("validate"))) {

                String sourceEnv = cmdLine.getParameterValue("sourceEnv");
                String targetEnv = cmdLine.getParameterValue("targetEnv");
                String frameworkCode =
                    cmdLine.getParameterValue("frameworkCode");
                String mappingData = cmdLine.getParameterValue("mappingData");

                // mapper used in the mapping process
                Mapper mapper =
                    MapperFactory.newMapper(
                        new File(
                            mappingData + "/" + frameworkCode + "/levels.txt"),
                        new File(
                            mappingData
                                + "/"
                                + frameworkCode
                                + "/objectives.txt"),
                        new File(
                            mappingData
                                + "/"
                                + frameworkCode
                                + "/item_map.txt"),
                        MapperFactory.FILEFORMAT_SHORT);
                String fileName =
                    mappingData + "/" + frameworkCode + "/levels.txt";

                // setup command report
                CommandReport report1 =
                    new CommandReport(commandString, fileName);
                CommandReport.setCurrentReport(report1);
                logger.info("Processing file: " + fileName);

                processingCommand.process(
                    commandString,
                    sourceEnv,
                    targetEnv,
                    frameworkCode,
                    mappingData,
                    mapper);

                Report report = processingCommand.getReport();

                //PrintWriter writer = new PrintWriter(System.out);
                PrintWriter writer =
                    new PrintWriter(
                        new FileOutputStream("RoundTrip_Mapping.txt"));
                Formatter formatter = FormatterFactory.create(report);
                formatter.print(writer, false);
                writer.flush();
                writer.close();

            }

        } catch (Exception e) {
            System.out.println("An error occured processing the command");
            System.out.println(e.toString());
            e.printStackTrace();
        }

        logger.info("Finish time: " + new Date());

    }

    private static CommandLine getCommandLine(
        String[] args,
        CommandLine cmdLine) {
        try {
            cmdLine = new CommandLine(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            CommandLine.usage();
            System.exit(1);
        }
        return cmdLine;
    }
}