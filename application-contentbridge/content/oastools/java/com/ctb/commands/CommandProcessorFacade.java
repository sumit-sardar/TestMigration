package com.ctb.commands;

import com.ctb.reporting.Report;

public class CommandProcessorFacade {

    public static Report processCommand(CIMCommand cIMCommand) throws Exception {
        return CIMCommandProcessor.processCommand(cIMCommand);
    }
}
