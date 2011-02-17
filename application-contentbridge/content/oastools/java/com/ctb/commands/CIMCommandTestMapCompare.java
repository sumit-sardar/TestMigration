package com.ctb.commands;

/**
 * @author wmli
 */
public class CIMCommandTestMapCompare implements CIMCommand {

    private final String inputFileName;
    private final String outputFileName;

    public CIMCommandTestMapCompare(
        String inputFileName,
        String outputFileName) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }

    public String getCommandName() {
        return TESTMAP_COMPARE;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }
}
