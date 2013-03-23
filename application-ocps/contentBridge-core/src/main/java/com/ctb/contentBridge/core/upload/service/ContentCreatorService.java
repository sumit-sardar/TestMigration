package com.ctb.contentBridge.core.upload.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.exception.ExceptionResolver;
import com.ctb.contentBridge.core.publish.report.AbstractReport;
import com.ctb.contentBridge.core.upload.bo.ContentCreatorBO;

public class ContentCreatorService {

	

	public void processExtTstItemSetId(AbstractReport r,
			Configuration configuration, /*Connection conn,*/ String extTstItemSetId)
			throws Exception {
		try {
			ContentCreatorBO mvContentCreatorBO = new ContentCreatorBO();
			mvContentCreatorBO.processExtTstItemSetId(configuration,/* conn,*/
					extTstItemSetId);
		} catch (Exception ex) {
			System.out.println("inside service "+ex.getMessage());
			r.setException(ex);
		}
	}
	
	public void processDeliverableUnitList(AbstractReport r,
			Configuration configuration, ArrayList<String> tdList)
			throws Exception {
		try {
			ContentCreatorBO mvContentCreatorBO = new ContentCreatorBO();
			mvContentCreatorBO.processDeliverableUnitList(configuration,/* conn,*/
					tdList);
		} catch (Exception ex) {
			System.out.println("inside service "+ex.getMessage());
			r.setException(ex);
		}
	}
}