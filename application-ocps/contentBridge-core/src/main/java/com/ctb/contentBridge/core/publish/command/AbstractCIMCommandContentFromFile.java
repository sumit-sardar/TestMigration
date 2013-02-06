package com.ctb.contentBridge.core.publish.command;

import java.io.File;

abstract class AbstractCIMCommandContentFromFile extends AbstractCIMCommand {

	protected File inputFile;
	
    AbstractCIMCommandContentFromFile(String targetEnvironment, File inputFile) {
        super(targetEnvironment);
        this.inputFile = inputFile;
    }

    public File getInputFile() {
        return inputFile;
    }

}
