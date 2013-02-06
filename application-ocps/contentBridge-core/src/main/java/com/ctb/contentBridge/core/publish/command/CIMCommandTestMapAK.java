package com.ctb.contentBridge.core.publish.command;

/**
 * @author wmli
 */
public class CIMCommandTestMapAK extends AbstractCIMCommandTestMap {
	private String itemFile;

    CIMCommandTestMapAK(String targetEnvironment, String frameworkCode, String itemFile, String inputFileName, String outputFileName) {
        super(targetEnvironment, frameworkCode, inputFileName, outputFileName);
        
        this.itemFile = itemFile;
    }

    public String getCommandName() {
    	return TESTMAP_ANSWERKEY;
    }
    
    

    public String getItemFile() {
        return itemFile;
    }

    public void setItemFile(String itemFile) {
        this.itemFile = itemFile;
    }

}
