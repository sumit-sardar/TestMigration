package com.ctb.common.tools;

import java.io.*;
import java.util.*;

import org.apache.commons.lang.StringUtils;

/**
 * Parses the command line:
 * CCS <command> arg1=value1 arg2=value2
 */
public class CommandLine {
    public static String USAGE =
        "USAGE:\n 	<scriptname> command parm1=value1 parm2=value2...";

    Map parameters = new HashMap();
    String command;

    public CommandLine(String[] args) {
        if (args.length < 1 || args[0].indexOf('=') >= 0) {
            throw new SystemException("Cannot parse command line. No command specified.");
        }
        command = args[0].toLowerCase();
        // parse through parameters
        for (int i = 1; i < args.length; i++) {
            int pos = args[i].indexOf('=');

            if (pos > 0) {
                String parameterName = args[i].substring(0, pos).toLowerCase();
                String value = args[i].substring(pos + 1);

                parameters.put(parameterName, value);
            } else {
                throw new SystemException("Invalid argument: " + args[i]);
            }
        }
    }

    public String getCommand() {
        return command;
    }

    public String getParameterValue(String parameterName) {
        if (parameters.containsKey(parameterName.toLowerCase())) {
            return (String) parameters.get(parameterName.toLowerCase());
        }

        throw new SystemException(
            "Required parameter '" + parameterName + "' not specified");
    }

    public String getOptionalParameterValue(
        String parameterName,
        String defaultValue) {
        if (parameters.containsKey(parameterName.toLowerCase())) {
            return (String) parameters.get(parameterName.toLowerCase());
        }

        return defaultValue;
    }

    public String[] getOptionalParameterList(
        String parameterName,
        String[] defaultValue) {
        if (parameters.containsKey(parameterName.toLowerCase())) {
            return StringUtils.split(
                (String) parameters.get(parameterName.toLowerCase()),
                ",");
        }
        return defaultValue;
    }

    public File getFileParameter(String parameterName) {
        String fileName = getParameterValue(parameterName);
        File file = new File(fileName);

        if (!file.exists()) {
            throw new SystemException("File " + file.getPath() + " not found.");
        }
        return file;
    }

    public File getOptionalFileParameter(
        String parameterName,
        File defaultValue) {
        String fileName = getOptionalParameterValue(parameterName, null);
        File file;

        if (fileName == null) {
            file = defaultValue;
        } else {
            file = new File(fileName);
        }
        if (!file.exists()) {
            throw new SystemException("File " + file.getName() + " not found.");
        }
        return file;
    }

    public File getOptionalFileParameter(String parameterName) {
        String fileName = getOptionalParameterValue(parameterName, null);
        File file;

        if (fileName == null) {
            file = null;
        } else {
            file = new File(fileName);
        }

        if (file != null && !file.exists()) {
            throw new SystemException("File " + file.getName() + " not found.");
        }

        return file;
    }

    public File getOptionalOutputFileParameter(
        String parameterName,
        File defaultValue) {
        String fileName = getOptionalParameterValue(parameterName, null);
        File file;

        if (fileName == null) {
            file = defaultValue;
        } else {
            file = new File(fileName);
        }

        return file;
    }

    public File getPathParameter(String parameterName) {
        String fileName = getParameterValue(parameterName);
        File file = new File(fileName);

        if (!file.isDirectory()) {
            throw new SystemException(
                parameterName + " path '" + file.getName() + "' not found.");
        }
        return file;
    }

    public File getOptionalPathParameter(
        String parameterName,
        File defaultValue) {
        String fileName = getOptionalParameterValue(parameterName, null);

        if (fileName == null) {
            return defaultValue;
        }

        File file = new File(fileName);

        if (!file.isDirectory()) {
            throw new SystemException(
                parameterName + " path '" + file.getName() + "' not found.");
        }
        return file;
    }

    public static void usage(PrintStream s) {
        s.println(USAGE);
    }

    public static void usage() {
        usage(System.err);
    }

}
