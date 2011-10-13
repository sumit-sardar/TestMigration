package com.ctb.contentcreator.delegater;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.ctb.contentcreator.bin.Configuration;
import com.ctb.contentcreator.dao.OasDao;
import com.ctb.contentcreator.utils.ConnectionUtil;

public class OasDelegater {
	private static OasDelegater instance = null;
	private static Configuration configuration = null;

	private OasDelegater() {
	}

	public static OasDelegater getInstance(Configuration configuration) {
		if (instance == null) {
			instance = new OasDelegater();
			OasDelegater.configuration = configuration;
		}
		return instance;
	}

	public HashMap getItemSetTd(long testCatalogId) throws SQLException {
		Connection conn = ConnectionUtil.getOASConnection(configuration);
		HashMap tdmap = OasDao.getItemSetTDForTestCatalog(conn, testCatalogId);

		return tdmap;
	}

	public HashMap getItemSetTd(String extTstItemSetId) throws SQLException {
		Connection conn = ConnectionUtil.getOASConnection(configuration);
		HashMap tdmap = OasDao.getItemSetTDForTC(conn, extTstItemSetId);
		return tdmap;
	}

	/*protected void finalize() throws Throwable {
		ConnectionUtil.closeOASConnection();

	}*/

	public void releaseResource() {
		ConnectionUtil.closeOASConnection();
	}

	public void updateItemSet(long itemsetIdTD, String repositoryURI) throws SQLException {
		Connection conn = ConnectionUtil.getOASConnection(configuration);
		OasDao.updateItemSet(conn,itemsetIdTD, repositoryURI );
	}

}
