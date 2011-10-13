package com.ctb.contentcreator;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;

import com.ctb.contentcreator.bin.Configuration;
import com.ctb.contentcreator.delegater.OasDelegater;
import com.ctb.contentcreator.processor.MainProcessor;

public class ContentCreatorThread extends Thread {
	private String environment;
	private String extTstItemSetId;

	public ContentCreatorThread(String environment, String extTstItemSetId) {
		this.environment = environment;
		this.extTstItemSetId = extTstItemSetId;

	}

	public void run() {
		Configuration configuration=  new Configuration();
		configuration.load(new File(environment));
		HashMap itemSetTd = null;
		try {
			itemSetTd = getItemSetTd(configuration,extTstItemSetId);
			MainProcessor processor  = new MainProcessor(configuration);
			processor.processor(itemSetTd);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	private HashMap getItemSetTd(Configuration configuration,
			String extTstItemSetId2) throws SQLException {
		HashMap map ;
		OasDelegater delegater = null;
		try{
			delegater =OasDelegater.getInstance(configuration);
			map = delegater.getItemSetTd(extTstItemSetId2);
		} finally {
			if(delegater != null)
				delegater.releaseResource();
		}
		return map;
		
	}

}
