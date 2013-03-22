/*
 * Created on Feb 4, 2004
 *
 */
package com.ctb.contentBridge.core.publish.command;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.report.Report;


public class ContentImportModuleUtils {

    public static Report run(
        CommandLine commandLine,
        InterfaceHandlesUsageException handler)
        throws Exception {
        CIMCommand command = null;
        try {
            command =
                CIMCommandFactory.createCIMCommandFromCommandLine(commandLine);
        } catch (Exception e) {
        	e.printStackTrace();
            handler.handleUsageException(e);
        }
        return CommandProcessorFacade.processCommand(command);
    }

    public static CommandLine getCommandLine(
        String[] args,
        InterfaceHandlesUsageException handler) {
        try {
            return new CommandLine();
        } catch (Exception e) {
        	e.printStackTrace();
            handler.handleUsageException(e);
            throw new SystemException(e.getMessage()); // Unreachable code
        }
    }
    public static CommandLine getCommandLineTeam(
            String[] args,
            InterfaceHandlesUsageException handler) {
            try {
                return new CommandLine(args);
            } catch (Exception e) {
            	e.printStackTrace();
                handler.handleUsageException(e);
                throw new SystemException(e.getMessage()); // Unreachable code
            }
        }

    public static String getUsageString(Exception e) {
        String output = "\nERROR:\n 	" + e.getMessage() + "\n";
        output += CommandLine.USAGE;
        output += CIMCommandFactory.getAvailableCIMCommandsInfo();
        return output;
    }
}
