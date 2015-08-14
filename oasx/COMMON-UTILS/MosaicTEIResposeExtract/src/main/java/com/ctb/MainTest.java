package com.ctb;

import com.ctb.utils.ExtractUtils;

/**
 * MainTest is the entry point of this utility.
 * 
 * @author TCS
 *
 */
public class MainTest {

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
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("*** MainTest : Process Started ...");
		long startTime = System.currentTimeMillis();
		/**
		 * MSS Request JSON prepare
		 */
		new MSSRequestScore().run();

		long endTime = System.currentTimeMillis();
		System.out.println("*** MainTest : Total time taken : "
				+ ((endTime - startTime) / 1000) + " Sec !!");

	}

}
