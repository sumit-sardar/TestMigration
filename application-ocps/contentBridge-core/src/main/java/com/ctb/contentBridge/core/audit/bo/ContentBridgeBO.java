package com.ctb.contentBridge.core.audit.bo;

import java.io.File;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.ctb.contentBridge.core.audit.delegater.ContentBridgeDelegater;
import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.domain.JobBean;
import com.ctb.contentBridge.core.upload.delegater.OasDelegater;
import com.ctb.contentBridge.core.upload.processor.MainProcessor;

public class ContentBridgeBO {

	public Collection<JobBean> getPublishOrderJob(Configuration configuration)
			throws Exception {
		Collection<JobBean> rJobList;
		ContentBridgeDelegater delegater = null;
		try {
			delegater = ContentBridgeDelegater.getInstance(configuration);
			rJobList = delegater.getPublishOrderJob();
		} finally {
			if (delegater != null)
				delegater.releaseResource();
		}
		return rJobList;
	}
	
	public static void updateJobStatus(Configuration configuration, Long jobPk,
			String status, String errMsg) throws Exception {
		ContentBridgeDelegater delegater = null;
		try {
			delegater = ContentBridgeDelegater.getInstance(configuration);
			delegater.updateJobStatus(jobPk, status, errMsg);
		} finally {
			if (delegater != null)
				delegater.releaseResource();
		}
	}
	public static void getJobIdEnv(Configuration configuration, Long jobPk,String[] jobEnv) throws Exception {
		ContentBridgeDelegater delegater = null;
		try {
			delegater = ContentBridgeDelegater.getInstance(configuration);
			ContentBridgeDelegater.getJobIdEnv(jobPk,jobEnv);
		} finally {
			if (delegater != null)
				delegater.releaseResource();
		}
	}
	
}
