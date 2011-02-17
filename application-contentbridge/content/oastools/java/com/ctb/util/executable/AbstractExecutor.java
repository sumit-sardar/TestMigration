package com.ctb.util.executable;


import com.ctb.common.tools.SystemException;
import com.ctb.util.executable.ExecutionInfo;

import java.util.StringTokenizer;
import java.io.*;

/**
 * User: mwshort
 * Date: Mar 21, 2004
 * Time: 9:36:01 AM
 * 
 *
 */
public class AbstractExecutor {


    public void exec(String commandLine) {
        StringTokenizer tokenizer = new StringTokenizer(commandLine);
        String[] args = new String[tokenizer.countTokens()];

        for (int i = 0;tokenizer.hasMoreTokens();i++) {
            args[i] = tokenizer.nextToken();
        }
        exec(args);
    }

    public void exec(String[] args) {
        ExecutionInfo info = new ExecutionInfo();
        Runtime rt = Runtime.getRuntime();
        Process process = null;
        try {
            process = rt.exec(args);
            info.getErrorSink(process).start();
            info.getInputSink(process).start();
            process.waitFor();
        } catch (IOException e) {
            throw new SystemException(e.getMessage(),e);
        } catch (InterruptedException e) {
            throw new SystemException(e.getMessage(),e);
        }
        info.exitValue = process.exitValue();
        checkForErrors(info);
    }

    protected void checkForErrors(ExecutionInfo info) {
        if (info.exitValue != 0) {
            throw new SystemException(info.toString());
        }
    }





}
