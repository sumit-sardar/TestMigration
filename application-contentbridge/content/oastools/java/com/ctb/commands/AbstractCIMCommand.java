package com.ctb.commands;


abstract class AbstractCIMCommand implements CIMCommand{

    private String targetEnvironment;

	AbstractCIMCommand(String targetEnvironment) {
		this.targetEnvironment = targetEnvironment;
	}

	public String getTargetEnvironment() {
		return targetEnvironment;
	}
	
	public boolean isTargetProduction()
	{
	    return ( targetEnvironment != null && targetEnvironment.endsWith( "PRODUCTION.properties"));
	}
	
	public void useCQA_ENV()
	{
	    targetEnvironment = "conf/CQA.properties";
	//    targetEnvironment = "user.properties";
	}

}
