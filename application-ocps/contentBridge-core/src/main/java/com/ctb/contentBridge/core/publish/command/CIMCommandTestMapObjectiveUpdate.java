package com.ctb.contentBridge.core.publish.command;

class CIMCommandTestMapObjectiveUpdate extends AbstractCIMCommandTestMap {
	
	CIMCommandTestMapObjectiveUpdate(
		String targetEnvironment,
		String frameworkCode,
		String inputFileName,
		String outputFileName) {
		super(targetEnvironment, frameworkCode, inputFileName, outputFileName);
	}

	public String getCommandName() {
		return TESTMAP_OBJECTIVE;
	}
}
