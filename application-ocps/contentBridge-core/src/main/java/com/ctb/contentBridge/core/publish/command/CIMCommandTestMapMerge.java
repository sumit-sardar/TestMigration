package com.ctb.contentBridge.core.publish.command;

class CIMCommandTestMapMerge extends AbstractCIMCommandTestMap {
    
    CIMCommandTestMapMerge(
        String targetEnvironment,
        String frameworkCode,
        String inputFileName,
        String outputFileName) {
        super(targetEnvironment, frameworkCode, inputFileName, outputFileName);
    }

    public String getCommandName() {
        return TESTMAP_MERGE;
    }
}
