package com.ctb.contentBridge.core.upload.delegater;

import java.sql.Connection;

import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.upload.dao.ADSDao;
import com.ctb.contentBridge.core.util.ConnectionUtil;


public class AdsDelegater {
	private static AdsDelegater instance = null;
	private static Configuration configuration = null;

	private AdsDelegater() {
	}

	public static AdsDelegater getInstance(Configuration configuration) {
		if (instance == null) {
			instance = new AdsDelegater();
			AdsDelegater.configuration = configuration;
		}
		return instance;
	}

	public byte[] getItemByteArray(long itemId) throws Exception {
		Connection conn = ConnectionUtil.getADSConnection(configuration);
		byte[] item = ADSDao.getItemBlob(conn, itemId);

		return item;
	}

	public byte[] getSubtestByteArray(long itemSetId) throws Exception {
		Connection conn = ConnectionUtil.getADSConnection(configuration);
		byte[] item = ADSDao.getSubtestBlob(conn, itemSetId);

		return item;

	}

	/*protected void finalize() throws Throwable {
		ConnectionUtil.closeADSConnection();

	}*/

	public void releaseResource() {
		ConnectionUtil.closeADSConnection();
	}
}
