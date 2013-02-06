package com.ctb.contentBridge.core.publish.command;

import com.ctb.contentBridge.core.publish.report.Report;


public class CommandProcessorFacade {

    public static Report processCommand(CIMCommand cIMCommand) throws Exception {
        return CIMCommandProcessor.processCommand(cIMCommand);
    }
}
