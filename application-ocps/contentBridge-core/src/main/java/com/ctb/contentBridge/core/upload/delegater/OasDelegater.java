package com.ctb.contentBridge.core.upload.delegater;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.upload.dao.OasDao;
import com.ctb.contentBridge.core.util.ConnectionUtil;


public class OasDelegater {
	private static OasDelegater instance = null;
	private static Configuration configuration = null;

	private OasDelegater() {
	}

	public static OasDelegater getInstance(Configuration configuration) {
		if (instance == null) {
			instance = new OasDelegater();
		}
		OasDelegater.configuration = configuration;

		return instance;
	}

	public HashMap getItemSetTd(long testCatalogId) throws Exception {
		Connection conn = ConnectionUtil.getOASConnection(configuration);
		HashMap tdmap = OasDao.getItemSetTDForTestCatalog(conn, testCatalogId);

		return tdmap;
	}

	public HashMap getItemSetTd(String extTstItemSetId) throws Exception {
		Connection conn = ConnectionUtil.getOASConnection(configuration);
		HashMap tdmap = OasDao.getItemSetTDForTC(conn, extTstItemSetId);
		return tdmap;
	}
	
	public HashMap getItemSetTd(ArrayList<String> tdList) throws Exception {
		Connection conn = ConnectionUtil.getOASConnection(configuration);
		HashMap tdmap = OasDao.getItemSetTDDtls(conn, tdList);
		return tdmap;
	}

	/*protected void finalize() throws Throwable {
		ConnectionUtil.closeOASConnection();

	}*/

	public void releaseResource() {
		ConnectionUtil.closeOASConnection();
	}

	public void updateItemSet(long itemsetIdTD, String repositoryURI) throws Exception {
		Connection conn = ConnectionUtil.getOASConnection(configuration);
		OasDao.updateItemSet(conn,itemsetIdTD, repositoryURI );
	}

}
