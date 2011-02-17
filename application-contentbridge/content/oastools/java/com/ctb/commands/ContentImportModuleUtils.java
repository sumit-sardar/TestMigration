/*
 * Created on Feb 4, 2004
 *
 */
package com.ctb.commands;

import com.ctb.common.tools.CommandLine;
import com.ctb.common.tools.SystemException;
import com.ctb.reporting.Report;

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
            handler.handleUsageException(e);
        }
        return CommandProcessorFacade.processCommand(command);
    }

    public static CommandLine getCommandLine(
        String[] args,
        InterfaceHandlesUsageException handler) {
        try {
            return new CommandLine(args);
        } catch (Exception e) {
            handler.handleUsageException(e);
            throw new SystemException(e.getMessage(), e); // Unreachable code
        }
    }

    public static String getUsageString(Exception e) {
        String output = "\nERROR:\n 	" + e.getMessage() + "\n";
        output += CommandLine.USAGE;
        output += CIMCommandFactory.getAvailableCIMCommandsInfo();
        return output;
    }
}
