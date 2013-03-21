package com.ctb.contentBridge.core.audit.service;

import java.util.Collection;

import com.ctb.contentBridge.core.audit.bo.ContentBridgeBO;
import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.domain.JobBean;
import com.ctb.contentBridge.core.exception.ExceptionResolver;
import com.ctb.contentBridge.core.upload.bo.ContentCreatorBO;

public class ContentBridgeService {

	public Collection<JobBean> getPublishOrderJob(Configuration configuration)
			throws Exception {
		try {
			ContentBridgeBO mvContentBridgeBO = new ContentBridgeBO();
			return mvContentBridgeBO.getPublishOrderJob(configuration);
		} catch (Exception ex) {
			throw ExceptionResolver.resolve(ex);
		}
	}

	public static void updateJobStatus(Configuration configuration, Long jobPk,
			String status, String errMsg) throws Exception {
		try {
			ContentBridgeBO mvContentBridgeBO = new ContentBridgeBO();
			mvContentBridgeBO.updateJobStatus(configuration, jobPk, status, errMsg);
		} catch (Exception ex) {
			throw ExceptionResolver.resolve(ex);
		}
	}
	public static void getJobIdEnv(Configuration configuration, Long jobPk,String[] JonEnv) throws Exception {
		try {
			ContentBridgeBO mvContentBridgeBO = new ContentBridgeBO();
			ContentBridgeBO.getJobIdEnv(configuration, jobPk,JonEnv);
		} catch (Exception ex) {
			throw ExceptionResolver.resolve(ex);
		}
	}
}
