package com.ctb;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.ctb.utils.ExtractUtils;
import com.ctb.utils.MSSConstantUtils;

/**
 * MainTest is the entry point of this utility.
 * 
 * @author TCS
 *
 */
public class MainTest {

	static Logger logger = Logger.getLogger(MainTest.class.getName());
	/**
	 * Need to pass two arguments
	 * args[0] : Properties file path [e.g. /local/apps/oas/properties/]
	 * args[1] : Properties file name [e.g. conf]
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			if (args.length != 2)
				throw new IllegalArgumentException();
			ExtractUtils.loadExternalProperties(args[0],args[1]);
			PropertyConfigurator.configure(ExtractUtils.get("oas.log4j.file"));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("*** Process Stopped : "+e.getMessage());
			System.exit(1);
		}

		logger.info("*** Process Started ...");
		long startTime = System.currentTimeMillis();
		/**
		 * MSS Request JSON prepare
		 */
		new MSSRequestScore().run();

		long endTime = System.currentTimeMillis();
		logger.info("*** Total time taken : "
				+ MSSConstantUtils.timeTaken((endTime - startTime)) + " !!");

	}

}
