/**
 * 
 */
package com.ctb.prism.web.constant;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TCS
 *
 */
public class PrismWebServiceConstant {
	
	
	
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	public static final String defaultStartDateStr = "01/01/1900";
	
	public static final String SRItemResponseSetType = "SR";
	public static final String CRItemResponseSetType = "CR";
	public static final String GRItemResponseSetType = "GR";
	public static final  Map<String,String> itemResponseItemCodeMap = new HashMap<String, String>();
	public static final  Map<String,Integer> itemResponseSRScoreValMap = new HashMap<String, Integer>();
	
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
		
	}
}
