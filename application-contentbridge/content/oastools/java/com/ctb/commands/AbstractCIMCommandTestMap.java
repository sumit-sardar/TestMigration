package com.ctb.commands;

abstract class AbstractCIMCommandTestMap extends AbstractCIMCommand {
    private String frameworkCode;
    private String inputFileName;
    private String outputFileName;

    AbstractCIMCommandTestMap(
        String targetEnvironment,
        String frameworkCode,
        String inputFileName,
        String outputFileName) {
        super(targetEnvironment);

        this.frameworkCode = frameworkCode;
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }

    String getFrameworkCode() {
        return frameworkCode;
    }

    String getInputFileName() {
        return inputFileName;
    }

    String getOutputFileName() {
        return outputFileName;
    }

}
