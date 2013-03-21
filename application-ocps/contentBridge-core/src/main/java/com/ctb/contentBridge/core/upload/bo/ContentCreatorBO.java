package com.ctb.contentBridge.core.upload.bo;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.upload.dao.OasDao;
import com.ctb.contentBridge.core.upload.delegater.OasDelegater;
import com.ctb.contentBridge.core.upload.processor.MainProcessor;

public class ContentCreatorBO {

	/* catalog=13297 env=C:\mappingdata\DEV */
	/* catalog=13396 env=C:\mappingdata\DEV */

	/**
	 * @param args
	 * @throws Exception
	 */
	/*
	 * public static void main(String[] args) throws Exception { if (args.length
	 * < 2 ) { usage (); System.exit (0); } Configuration configuration= new
	 * Configuration(); configuration.load(new File(args[1]+".properties"));
	 * HashMap itemSetTd = null; itemSetTd =
	 * getItemSetTd(configuration,args[0]); MainProcessor processor = new
	 * MainProcessor(configuration); processor.processor(itemSetTd);
	 * 
	 * }
	 */

	public void processCatalog(String catalogId, Configuration configuration)
			throws Exception {
		/*
		 * Configuration configuration= new Configuration();
		 * configuration.load(new File(configFilePath));
		 */
		HashMap itemSetTd = null;
		itemSetTd = getItemSetTd(configuration, catalogId);
		MainProcessor processor = new MainProcessor(configuration);
		System.out.println("inside processCatalog");
		processor.processor(itemSetTd);
	}

	public void processExtTstItemSetId(Configuration configuration,
			Connection conn, String extTstItemSetId) throws Exception {
		HashMap itemSetTd = null;
		itemSetTd = getItemSetTd(conn, extTstItemSetId);
		MainProcessor processor = new MainProcessor(configuration);
		processor.processor(itemSetTd);
	}
	
	public void processExtTstItemSetId(Configuration configuration,
			String extTstItemSetId) throws Exception {
		System.out.println("inside processExtTstItemSetId");
		HashMap itemSetTd = null;		
		itemSetTd = getItemSetTd(configuration, extTstItemSetId);
		MainProcessor processor = new MainProcessor(configuration);
		processor.processor(itemSetTd);
	}

	private static HashMap getItemSetTd(Configuration configuration,
			String extTstItemSetId) throws NumberFormatException, SQLException,
			Exception {
		HashMap map;
		OasDelegater delegater = null;
		try {
			System.out.println("contentcreationBO itemset"+extTstItemSetId);
			delegater = OasDelegater.getInstance(configuration);
			map = delegater.getItemSetTd(extTstItemSetId);
			System.out.println("contentcreationBO "+map.size());
		} finally {
			if (delegater != null)				
				delegater.releaseResource();
		}
		return map;

	}

	private static HashMap getItemSetTd(Connection conn, String extTstItemSetId)
			throws SystemException {
		HashMap map;
		try {
			map = OasDao.getItemSetTDForTC(conn, extTstItemSetId);
		} catch (Exception e) {
			System.out.println("getItemSetTd "+e.getMessage());
			throw new SystemException(e);
		}
		return map;

	}

	private static void usage() {

		System.out.println("\nUsage:  <argument1> <argument2>");
		System.out.println("   <arguments>");
		System.out
				.println("   argument1: 123  - Catalog id i23 for test whose items should be compressed.");
		System.out
				.println("   argument2: path   - Valid path for environment information.");
	}

}
