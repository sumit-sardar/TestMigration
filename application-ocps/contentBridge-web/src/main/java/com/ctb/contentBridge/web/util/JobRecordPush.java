package com.ctb.contentBridge.web.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.ctb.contentBridge.core.audit.dao.JobDAO;

public class JobRecordPush {
	static Map<String, String> parameters = new LinkedHashMap<String, String>();
	static ArrayList<String> paramName=new ArrayList<String>();
    String command;
    
    public void processJob(String[] args) throws Exception{
    	 	JobDAO.jobRecordProcess(args);
    }	
}
