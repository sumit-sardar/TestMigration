package com.ctb.tms.rdb.oracle;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import noNamespace.AdssvcResponseDocument;
import noNamespace.AdssvcResponseDocument.AdssvcResponse;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.DownloadItem;
import noNamespace.AdssvcResponseDocument.AdssvcResponse.GetSubtest;
import noNamespace.ErrorDocument;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;

import com.ctb.tms.bean.delivery.ItemData;
import com.ctb.tms.bean.delivery.SubtestData;
import com.ctb.tms.rdb.ADSRDBSource;

public class ADSOracleSource implements ADSRDBSource {
	private static final String GET_SUBTEST_SQL = "SELECT ob_asmt_id AS subtestId,  \tasmt_hash as hash FROM OB_ASMT  WHERE ob_asmt_id = ?";
	private static final String GET_SUBTEST_BLOB_SQL = "SELECT  asmt_manifest_encr AS subtestBlob FROM OB_ASMT  WHERE ob_asmt_id = ?";
	private static final String GET_ITEM_SQL = "SELECT ob_item_pkg_id AS itemId, hash FROM OB_ITEM_PKG  WHERE ob_item_pkg_id = ?";
	private static final String GET_ITEM_BLOB_SQL = "SELECT item_rendition_xml_encr as itemBlob  FROM OB_ITEM_PKG  WHERE ob_item_pkg_id = ?";
	
	static Logger logger = Logger.getLogger(ADSOracleSource.class);
	
	public Connection getADSConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return OracleSetup.getADSConnection();
	}
	
	public String getSubtest(Connection conn, int subtestId, String hash) {
		XmlOptions xmlOptions = new XmlOptions(); 
        xmlOptions = xmlOptions.setUnsynchronized();
		AdssvcResponseDocument responseDoc = AdssvcResponseDocument.Factory.newInstance(xmlOptions);
		AdssvcResponse adssvcResponse = responseDoc.addNewAdssvcResponse();
		GetSubtest getSubtest = adssvcResponse.addNewGetSubtest();
		getSubtest.setSubtestid(""+subtestId);
		try {
			SubtestData subtestData = getSubtest(conn, subtestId);
			if (subtestData == null)
				throw new Exception("Subtest with subtest id '"+subtestId+"' not found in ADS");
			if (!subtestData.getHash().equals(hash))
				throw new Exception("Incorrect hash for subtest id '"+subtestId+"' in ADS");
			boolean moreData = true;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream is = getSubtestBlob(conn, subtestId).getBinaryStream();
			while(moreData) {
				byte [] buffer = new byte[128];
				int read = is.read(buffer);
				moreData = read > 0;
				if(moreData) {
					baos.write(buffer, 0, read);
				}
			}
			subtestData.setSubtest(baos.toByteArray());
			getSubtest.setContent(subtestData.getSubtest());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ErrorDocument.Error error = getSubtest.addNewError();
			error.setErrorElement(String.valueOf(subtestId));
			error.setErrorDetail(e.getMessage());
		}
		return responseDoc.xmlText();
	}
	
	public String getItem(Connection conn, int itemId, String hash) {
		XmlOptions xmlOptions = new XmlOptions(); 
        xmlOptions = xmlOptions.setUnsynchronized();
		AdssvcResponseDocument responseDoc = AdssvcResponseDocument.Factory.newInstance(xmlOptions);
		AdssvcResponse adssvcResponse = responseDoc.addNewAdssvcResponse();
		DownloadItem downloadItem = adssvcResponse.addNewDownloadItem();
		downloadItem.setItemid(""+itemId);

		try {

			ItemData itemData = getItem(conn, itemId);
			if (itemData == null)
				throw new Exception("Subtest with item id '"+itemId+"' not found in ADS");
			if (!itemData.getHash().equals(hash))
				throw new Exception("Incorrect hash for item id '"+itemId+"' in ADS");
			boolean moreData = true;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream is = getItemBlob(conn, itemId).getBinaryStream();
			while(moreData) {
				byte [] buffer = new byte[128];
				int read = is.read(buffer);
				moreData = read > 0;
				if(moreData) {
					baos.write(buffer, 0, read);
				}
			}
			itemData.setItem(baos.toByteArray());

			downloadItem.setContent(itemData.getItem());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ErrorDocument.Error error = downloadItem.addNewError();
			error.setErrorElement(String.valueOf(itemId));
			error.setErrorDetail(e.getMessage());
		}
		return responseDoc.xmlText();
	}
	
	private static SubtestData getSubtest(Connection con, int subtestId) {
		SubtestData data = null;
    	PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(GET_SUBTEST_SQL);
			stmt1.setInt(1, subtestId);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				data = new SubtestData();
				data.setSubtestId(rs1.getInt("subtestId"));
				data.setHash(rs1.getString("hash"));
			}
			rs1.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt1 != null) stmt1.close();
			} catch (Exception e) {
				// do nothing
			}
		}
		return data;
	}
	
	private static Blob getSubtestBlob(Connection con, int subtestId) {
		Blob data = null;
		PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(GET_SUBTEST_BLOB_SQL);
			stmt1.setInt(1, subtestId);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				data = rs1.getBlob("subtestBlob");
			}
			rs1.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt1 != null) stmt1.close();
			} catch (Exception e) {
				// do nothing
			}
		}
		return data;
	}
	
	private static ItemData getItem(Connection con, int itemId) {
		ItemData data = null;
    	PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(GET_ITEM_SQL);
			stmt1.setInt(1, itemId);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				data = new ItemData();
				data.setItemId(rs1.getInt("itemId"));
				data.setHash(rs1.getString("hash"));
			}
			rs1.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt1 != null) stmt1.close();
			} catch (Exception e) {
				// do nothing
			}
		}
		return data;
	}
	
	private static Blob getItemBlob(Connection con, int itemId) {
		Blob data = null;
		PreparedStatement stmt1 = null;
    	try {
			stmt1 = con.prepareStatement(GET_ITEM_BLOB_SQL);
			stmt1.setInt(1, itemId);
			ResultSet rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				data = rs1.getBlob("itemBlob");
			}
			rs1.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt1 != null) stmt1.close();
			} catch (Exception e) {
				// do nothing
			}
		}
		return data;
	}

	public void shutdown() {
		// do nothing
	}
}
