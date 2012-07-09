package com.ctb.tdc.web.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.ctb.tdc.web.to.ItemResponseData;
import com.ctb.tdc.web.to.ReplicationObject;

public class RosterData extends ReplicationObject {
	String document;
	
	static Logger logger = Logger.getLogger(RosterData.class);
	public static int restartItemCount = 0;
	public static int [] restartItemsArr ={};
	public static int [] restartItemsRawScore ={};
	
	public RosterData() {
		
	}	
	private static ItemResponseData[] sortItemResponseData(ItemResponseData[] startList) {
		HashMap<Integer, ItemResponseData> sortedMap = new HashMap<Integer, ItemResponseData>(startList.length);
		for(int i=0;i<startList.length;i++) {
			ItemResponseData val = startList[i];
			sortedMap.put(new Integer(val.getResponseSeqNum()), val);
		}
		Integer [] keys = sortedMap.keySet().toArray(new Integer[0]);
		Arrays.sort(keys);
		ArrayList<ItemResponseData> finalList = new ArrayList<ItemResponseData>(keys.length);
		for(int i=0;i<keys.length;i++) {
			finalList.add(sortedMap.get(keys[i]));
		}
		return finalList.toArray(new ItemResponseData[0]);
	}
	
	public static void generateRestartData(ItemResponseData[] itemResponseData) throws Exception {
		try {
			String catItemIdPattern = ".TABECAT";
			itemResponseData = sortItemResponseData(itemResponseData);
			
			int maxRSN = 0;
			int totalDur = 0;
			int curEid = 0;
			restartItemCount = itemResponseData.length;
			restartItemsArr =  new int [restartItemCount];
			restartItemsRawScore  = new int [restartItemCount];
			for(int i=0;i<itemResponseData.length;i++) {
				ItemResponseData data = itemResponseData[i];
				String itemIId = data.getItemId();
				boolean isString = false;
				if(itemIId != null && itemIId.indexOf(catItemIdPattern) != -1){
					itemIId = itemIId.substring(0, itemIId.length() - catItemIdPattern.length());
					try {
						Long.parseLong(itemIId);
					} catch (Exception e) {
						isString = true;
					}
					if(!isString)
						restartItemsArr[i] = Integer.parseInt( itemIId );
					    logger.info("restartItemsArr: item " + i + ": " + restartItemsArr[i] );
				}
				restartItemsRawScore[i] = data.getScore();
				
		}
		}catch (Exception e) {
			logger.error("Exception occured : ");
		}
	}
	
}
