package com.ctb.commands;

class CIMCommandTestMapInitial extends AbstractCIMCommandTestMap {
    
    CIMCommandTestMapInitial(
        String targetEnvironment,
        String frameworkCode,
        String inputFileName,
        String outputFileName) {
        super(targetEnvironment, frameworkCode, inputFileName, outputFileName);
    }

    public String getCommandName() {
        return TESTMAP_INITIAL;
    }

}
