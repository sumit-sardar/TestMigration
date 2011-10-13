package com.ctb.contentcreator;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;

import com.ctb.contentcreator.bin.Configuration;
import com.ctb.contentcreator.delegater.OasDelegater;
import com.ctb.contentcreator.processor.MainProcessor;

public class ContentCreatorMain {

	
	/*catalog=13297 env=C:\mappingdata\DEV*/
	/*catalog=13396 env=C:\mappingdata\DEV*/
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 2 ) {
			usage ();
			System.exit (0);
		}
		Configuration configuration=  new Configuration();
		configuration.load(new File(args[1]+".properties"));
		HashMap itemSetTd = null;
		itemSetTd = getItemSetTd(configuration,args[0]);
		MainProcessor processor  = new MainProcessor(configuration);
		processor.processor(itemSetTd);

	}
	
	private static HashMap getItemSetTd(Configuration configuration, String testCataLog) throws NumberFormatException, SQLException {
		HashMap map ;
		OasDelegater delegater = null;
		try{
			delegater =OasDelegater.getInstance(configuration);
			map = delegater.getItemSetTd(Long.valueOf(testCataLog).longValue());
		} finally {
			if(delegater != null)
				delegater.releaseResource();
		}
		return map;
		
	}

	private static void usage () {
		
	    System.out.println ("\nUsage:  <argument1> <argument2>");
	    System.out.println ("   <arguments>");
	    System.out.println ("   argument1: 123  - Catalog id i23 for test whose items should be compressed.");
	    System.out.println ("   argument2: path   - Valid path for environment information.");
	}

}
