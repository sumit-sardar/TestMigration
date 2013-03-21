package com.ctb.contentBridge.core.audit.delegater;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.ctb.contentBridge.core.audit.dao.JobDAO;
import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.domain.JobBean;
import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.upload.dao.OasDao;
import com.ctb.contentBridge.core.util.ConnectionUtil;

public class ContentBridgeDelegater {
	private static ContentBridgeDelegater instance = null;
	private static Configuration configuration = null;

	private ContentBridgeDelegater() {
	}

	public static ContentBridgeDelegater getInstance(Configuration configuration) {
		if (instance == null) {
			instance = new ContentBridgeDelegater();
			ContentBridgeDelegater.configuration = configuration;
		}
		return instance;
	}

	public static Collection<JobBean> getPublishOrderJob() throws Exception {
		Connection conn = ConnectionUtil.getContBrgConnection(configuration);
		Collection<JobBean> rJobList = JobDAO.getPublishOrderJob(conn);

		return rJobList;
	}

	public static void updateJobStatus(Long jobPk,
			String status, String errMsg) throws Exception {
		Connection conn = ConnectionUtil.getContBrgConnection(configuration);
		JobDAO.updateJobStatus(conn, jobPk, status, errMsg);
	}
	public static void getJobIdEnv(Long jobPk,String[] jobEnv) throws Exception {
		Connection conn = ConnectionUtil.getContBrgConnection(configuration);
		JobDAO.getJobIdEnv(conn, jobPk,jobEnv);
	}

	public void releaseResource() {
		ConnectionUtil.closeContBrgConnection();
	}
}
