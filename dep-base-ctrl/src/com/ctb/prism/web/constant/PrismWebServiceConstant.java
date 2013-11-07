/**
 * 
 */
package com.ctb.prism.web.constant;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author TCS
 *
 */
public class PrismWebServiceConstant {
	
	public static final int CONNECT_TIMEOUT = 3 * 60 * 1000;
	public static final int REQUEST_TIMEOUT = 3 * 60 * 1000;
	public static final String loggerName = "PrismWebService"; 
	
	public static final int numberOfFailedHitCnt = 5;
	
	
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	public static final String defaultStartDateStr = "01/01/1900";
	
	public static final String SRItemResponseSetType = "SR";
	public static final String CRItemResponseSetType = "CR";
	public static final String GRItemResponseSetType = "GR";
	public static final String GRIDItemResponseSetType = "GRID";
	
	public static final String  GREditedResponseTxt = "GR Edited Response";
	
	public static final  Map<String,String> itemResponseItemCodeMap = new HashMap<String, String>();
	public static final  Map<String,Integer> itemResponseSRScoreValMap = new HashMap<String, Integer>();
	
	public static final String NCContentScoreDetails = "NC";
	public static final String NPContentScoreDetails = "NP";
	public static final String SSContentScoreDetails = "SS";
	public static final String HSEContentScoreDetails = "HSE";
	public static final String PRContentScoreDetails = "PR";
	public static final String NCEContentScoreDetails = "NCE";
	public static final String SSRContentScoreDetails = "SSR";
	
	public static final String NCObjectiveScoreDetails = "NC";
	public static final String NPObjectiveScoreDetails = "NP";
	public static final String SSObjectiveScoreDetails = "SS";
	public static final String MAObjectiveScoreDetails = "MA";
	public static final String MRObjectiveScoreDetails = "MR";
	
	public static final String StudentDemoTestName = "TASC";
	
	public static final  Map<String,Integer> contentDetailsContentCodeMap = new HashMap<String, Integer>();
	
	public static final  Map<String,String> subTestAccomCatNameMap = new HashMap<String, String>();
	
	public static ResourceBundle resourceBundler = null;
	
	static{
		itemResponseItemCodeMap.put("SR", "01");
		itemResponseItemCodeMap.put("CR", "02");
		itemResponseItemCodeMap.put("GR Status", "03");
		itemResponseItemCodeMap.put("GR Edited Response", "04");
		
		itemResponseSRScoreValMap.put("A", 1);
		itemResponseSRScoreValMap.put("B", 2);
		itemResponseSRScoreValMap.put("C", 3);
		itemResponseSRScoreValMap.put("D", 4);
		itemResponseSRScoreValMap.put("E", 5);
		
		contentDetailsContentCodeMap.put("Reading", 1);
		contentDetailsContentCodeMap.put("Writing", 2);
		contentDetailsContentCodeMap.put("ELA", 3);
		contentDetailsContentCodeMap.put("Mathematics", 4);
		contentDetailsContentCodeMap.put("Science", 5);
		contentDetailsContentCodeMap.put("Social Studies", 6);
		contentDetailsContentCodeMap.put("Overall", 7);
		
		subTestAccomCatNameMap.put("Mathematics", "MATH ACCOMMODATION"); 
		subTestAccomCatNameMap.put("Reading", "READING ACCOMMODATION");
		subTestAccomCatNameMap.put("Science", "SCIENCE ACCOMMODATION");
		subTestAccomCatNameMap.put("Social Studies", "SOCIAL STUDIES ACCOMMODATION");
		subTestAccomCatNameMap.put("Writing", "WRITING ACCOMMODATION");
		
		resourceBundler = ResourceBundle.getBundle("PrismWebService");
		
	}
}
