package com.ctb.contentBridge.core.publish.executable;



import java.util.StringTokenizer;
import java.io.*;

import com.ctb.contentBridge.core.exception.BusinessException;

/**
 * User: mwshort
 * Date: Mar 21, 2004
 * Time: 9:36:01 AM
 * 
 *
 */
public class AbstractExecutor {


    public void exec(String commandLine) throws BusinessException {
        StringTokenizer tokenizer = new StringTokenizer(commandLine);
        String[] args = new String[tokenizer.countTokens()];

        for (int i = 0;tokenizer.hasMoreTokens();i++) {
            args[i] = tokenizer.nextToken();
        }
        exec(args);
    }

    public void exec(String[] args) throws BusinessException {
        ExecutionInfo info = new ExecutionInfo();
        Runtime rt = Runtime.getRuntime();
        Process process = null;
        try {
            process = rt.exec(args);
            info.getErrorSink(process).start();
            info.getInputSink(process).start();
            process.waitFor();
        } catch (IOException e) {
            throw new BusinessException(e.getMessage());
        } catch (InterruptedException e) {
            throw new BusinessException(e.getMessage());
        }
        info.exitValue = process.exitValue();
        checkForErrors(info);
    }

    protected void checkForErrors(ExecutionInfo info) throws BusinessException {
        if (info.exitValue != 0) {
            throw new BusinessException(info.toString());
        }
    }





}
