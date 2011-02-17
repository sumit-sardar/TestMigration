package com.ctb.util.executable;








/**
 * User: mwshort
 * Date: Mar 21, 2004
 * Time: 9:37:28 AM
 * 
 *
 */
public class ExecutionInfo {

    public int exitValue;
    public StringBuffer stdout = new StringBuffer();
    public StringBuffer stderr = new StringBuffer();

    public void print() {
        System.out.println("process.exitValue() = " + exitValue);
        System.out.println("stdout = " + stdout);
        System.out.println("stderr = " + stderr);
    }

    public String toString() {
        return (
            "process.exitValue() = "
                + exitValue
                + "\n"
                + "stdout = "
                + stdout
                + "\n"
                + "stderr = "
                + stderr);
    }

    public Thread getErrorSink(Process process) {
        return new Thread(new StreamSink("stderr", process.getErrorStream(),stderr));
    }

    public Thread getInputSink(Process process) {
        return new Thread(new StreamSink("stdout", process.getInputStream(),stdout));
    }
    public String getErrors() {
        return stderr.toString();
    }
    public String getOutput() {
        return stdout.toString();
    }

}
