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
	
	public static int numberOfFailedHitCnt = 0;
	public static long retryWaitTime = 0;
	
	
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	public static final String defaultStartDateStr = "01/01/1900";
	
	public static final String SRItemResponseSetType = "SR";
	public static final String CRItemResponseSetType = "CR";
	public static final String GRItemResponseSetType = "GR";
	public static final String GRIDItemResponseSetType = "GRID";
	
	public static final String  GREditedResponseTxt = "GR Edited Response";
	public static final String  GRStatusTxt = "GR Status";
	
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
	public static final String INRCObjectiveScoreDetails = "INRC";
	public static final String CCObjectiveScoreDetails = "CC";
	
	public static final String CustHierarchyDetailsTestName = "TASC";
	
	public static final  Map<String,Integer> contentDetailsContentCodeMap = new HashMap<String, Integer>();
	
	public static final  Map<String,String> subTestAccomCatNameMap = new HashMap<String, String>();
	
	public static ResourceBundle resourceBundler = null;
	
	public static final String NACompositeStatusCode ="NA";
	
	public static final String VAScoringStatus = "VA";
	
	public static final String InvalidContentStatus = "IN";
	
	public static final String InvalidContentStatusCode = "INV";
	
	public static final String OmittedContentStatusCode = "OM";
	
	public static final String SuppressedContentStatusCode = "SUP";
	
	public static final  Map<String,String> contentDetailsStausCodeMap = new HashMap<String, String>();
	
	public static final String fontBackGrClrAttrName = "Acc_Ft_Bk_Cr";
	
	public static final String AddStdInfoSubTestAcc = "ADDITIONAL STUDENT INFORMATION";
	
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
		
		contentDetailsStausCodeMap.put(OmittedContentStatusCode, "3");
		contentDetailsStausCodeMap.put(InvalidContentStatusCode, "5");
		contentDetailsStausCodeMap.put(SuppressedContentStatusCode, "6");
		contentDetailsStausCodeMap.put(NACompositeStatusCode, "7");
		
		resourceBundler = ResourceBundle.getBundle("PrismWebService");
		
		numberOfFailedHitCnt = Integer.valueOf(resourceBundler.getString("retrycount"));
		retryWaitTime = Long.valueOf(resourceBundler.getString("retrywait"));
		
	}
}
