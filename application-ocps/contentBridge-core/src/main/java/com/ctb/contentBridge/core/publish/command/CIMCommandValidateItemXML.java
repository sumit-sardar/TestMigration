package com.ctb.contentBridge.core.publish.command;






/**
 * Created by IntelliJ IDEA.
 * User: JEMarley
 * Date: May 17, 2004
 */
public class CIMCommandValidateItemXML extends AbstractCIMCommand {
    private String logFile;

    public CIMCommandValidateItemXML(String env, String logFile) {
        super(env + ".properties");
        this.logFile = logFile;
    }

    public String getCommandName() {
        return CIMCommand.VALIDATE_ITEM_XML;
    }

    public String getLogFile() {
        return logFile;
    }
}
