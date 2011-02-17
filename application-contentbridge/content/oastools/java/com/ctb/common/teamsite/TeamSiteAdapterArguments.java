/*
 * Created on Feb 4, 2004
 *
 */
package com.ctb.common.teamsite;

import java.util.Arrays;
import java.util.List;

import com.ctb.common.tools.SystemException;

public class TeamSiteAdapterArguments {

    private final String command;
    private final String environment;
    private final String jobID;
    private final String taskID;
    private final String workArea;
    private final String jobDocument;

    public TeamSiteAdapterArguments(String[] args) {
        List argList = Arrays.asList(args);
        //validate args
        //number of args should be 5 sor 6 for first pass, 6/7 for SOFA vs ItemSet
        //Order should be String, Job Number, Task ID, File Path, rest of file path
        if (!AdapterInputValidator.isValidArgList(argList))
            throw new SystemException("Argments for TeamSiteAdapter not valid");

        command = args[0];
        environment = args[1];
        jobID = args[2];
        taskID = args[2];
        workArea = args[4];
        jobDocument = args[5];
    }
    public String getEnvironment() {
        return environment;
    }

    public String getJobDocument() {
        return jobDocument;
    }

    public String getJobID() {
        return jobID;
    }

    public String getTaskID() {
        return taskID;
    }

    public String getWorkArea() {
        return workArea;
    }

    public String getCommand() {
        return command;
    }

}
