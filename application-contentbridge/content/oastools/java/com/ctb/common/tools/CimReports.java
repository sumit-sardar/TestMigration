package com.ctb.common.tools;


import java.io.*;
import java.sql.*;

import com.ctb.mapping.*;


public class CimReports {

    private static long USER_ID = 2;

    public static void main(String[] args) throws Exception {
        CommandLine cmdLine = null;

        try {
            cmdLine = new CommandLine(args);
        } catch (SystemException e) {
            System.out.println("Cannot parse command line: " + e.getMessage());
            CommandLine.usage();
        }

        try {
            String commandString = cmdLine.getCommand();

            if (commandString.equals("frameworkreport")) {
                String environment = cmdLine.getParameterValue("env");
                String frameworkName = cmdLine.getParameterValue("framework");
                String productName = cmdLine.getOptionalParameterValue("product",
                        null);
                Connection connection = new DBConfig(new File(environment + ".properties")).openConnection();
                DBConnection dbConnection = new DBConnection(connection);

                DBObjectivesReports report = new DBObjectivesReports(connection, USER_ID);
                String output = report.generateFrameworkReport(frameworkName,
                        productName);

                System.out.println(output);
            } else if (commandString.equals("listframeworks")) {
                String environment = cmdLine.getParameterValue("env");
                Connection connection = new DBConfig(new File(environment + ".properties")).openConnection();
                DBConnection dbConnection = new DBConnection(connection);

                DBObjectivesReports report = new DBObjectivesReports(connection,
                        USER_ID);
                String output = report.listAvailableFrameworks();

                System.out.println(output);
            } else if (commandString.equals("mappeditemsreport")) {
                String env = cmdLine.getParameterValue("env");
                File mappingFile = cmdLine.getFileParameter("mappingFile");
                String framework = cmdLine.getParameterValue("framework");
                String itemsFileName = cmdLine.getOptionalParameterValue("itemsFile",
                        null);
                String detailStr = cmdLine.getOptionalParameterValue("detailedReport",
                        null);
                boolean detailedReport = false;

                if ((detailStr != null)
                        && (detailStr.compareToIgnoreCase("true") == 0)) {
                    detailedReport = true;
                }

                try {
                    Connection connection = new DBConfig(new File(env + ".properties")).openConnection();
                    DBConnection dbConnection = new DBConnection(connection);
                    MappedItemStatusReport report = new MappedItemStatusReport(dbConnection);

                    report.setDetail(detailedReport);
                    ItemMap itemMap = new ItemMap(mappingFile);

                    if (itemsFileName != null) {
                        File itemsFile = new File(itemsFileName);

                        report.run(framework, itemMap,
                                MappedItemStatusReport.getItems(itemsFile));
                    } else {
                        report.run(framework, itemMap);
                    }
                    report.print();
                } catch (IOException e) {
                    System.out.println("Error reading file " + e.getMessage());
                } catch (SystemException sysEx) {
                    System.out.println("System Exception:" + sysEx.getMessage());
                }
            } else {
                System.out.println("Invalid command: " + cmdLine.getCommand());
            }
        } catch (Exception e) {
            System.out.println("An error occured processing the command");
            System.out.println(e.getMessage());
        }
    }

}
