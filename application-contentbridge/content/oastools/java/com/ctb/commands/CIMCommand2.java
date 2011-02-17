/*
 * Created on Dec 11, 2003
 *
 */
package com.ctb.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ctb.common.tools.CommandLine;

/* ********************************************************************* */

public interface CIMCommand2 {

    /* Paramaters */
    public static final String FWK_CODE = "frameworkCode";
    public static final String ENV = "env";
    
    /* Default values for optional parameters */
    public static final String _DEFAULT_ENV = "user";

    /* Command Names */
    public static final String SAMPLE_CMD = "sample";

    String[] getOptionalParams();
    String[] getRequiredParams();
    String getDefaultValue(String optionalParameterName);

}

/* ********************************************************************* */

class CIMCommandFactory2 {

    public CIMCommand2 getCIMCommand(CommandLine cmdLine) {
        String cmd = cmdLine.getCommand();
        AbstractCIMCommand2 command = null;
        if (cmd.equals(CIMCommand2.SAMPLE_CMD)) {
            command = new CIMCommandSample();
            setParameterValues(cmdLine, command);
        }
        return command;
    }

    private void setParameterValues(
        CommandLine cmdLine,
        AbstractCIMCommand2 cimCommand2) {
        Map paramValues = new HashMap();
        String[] optionalParams = cimCommand2.getOptionalParams();
        String[] requiredParams = cimCommand2.getRequiredParams();
        for (int i = 0; i < requiredParams.length; i++) {
            paramValues.put(
                requiredParams[i],
                cmdLine.getParameterValue(requiredParams[i]));
        }
        for (int i = 0; i < optionalParams.length; i++) {
            paramValues.put(
                optionalParams[i],
                cmdLine.getOptionalParameterValue(
                    optionalParams[i],
                    cimCommand2.getDefaultValue(optionalParams[i])));
        }
        cimCommand2.setParameterValues(paramValues);
    }

}

/* ********************************************************************* */

abstract class AbstractCIMCommand2 implements CIMCommand2 {
    private final List reqParams = new ArrayList();
    private HashMap optionalParams = new HashMap();
    private Map paramValues;

    public abstract String getCommandName();

    public String[] getRequiredParams() {
        return (String[]) reqParams.toArray(new String[0]);
    }

    public String[] getOptionalParams() {
        return (String[]) optionalParams.keySet().toArray(new String[0]);
    }

    void setParameterValues(Map paramValues) {
        this.paramValues = paramValues;
    }

    protected String get(String paramName) {
        return (String) this.paramValues.get(paramName);
    }

    public String getDefaultValue(String optionalParamName) {
        return (String) optionalParams.get(optionalParamName);
    }

    protected void addOptionalParam(String paramName, String defaultValue) {
        this.optionalParams.put(paramName, defaultValue);
    }

    protected void addRequiredParam(String paramName) {
        this.reqParams.add(paramName);
    }

}

/* ********************************************************************* */

class CIMCommandSample extends AbstractCIMCommand2 {

    public CIMCommandSample() {
        addOptionalParam(CIMCommand2.ENV, CIMCommand2._DEFAULT_ENV);
        addRequiredParam(CIMCommand2.FWK_CODE);
    }

    public String getCommandName() {
        return CIMCommand2.SAMPLE_CMD;
    }

    public String getEnv() {
        return get(CIMCommand2.ENV);
    }

    public String getFrameworkCode() {
        return get(CIMCommand2.FWK_CODE);
    }
}

/* ********************************************************************* */
