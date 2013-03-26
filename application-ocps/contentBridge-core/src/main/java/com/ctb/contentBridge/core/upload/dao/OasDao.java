package com.ctb.contentBridge.core.upload.dao;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.ctb.contentBridge.core.domain.Item;
import com.ctb.contentBridge.core.domain.ItemSet;
import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.util.ClosableHelper;


public class OasDao {

	private static String getItemSetTDForTestCatalog = "SELECT ISETTD.ITEM_SET_ID OAS_ID, ISETTD.ADS_OB_ASMT_ID ADS_ID, ISETTD.ASMT_HASH HASH_KEY , ISETTD.ASMT_ENCRYPTION_KEY KEY  FROM TEST_CATALOG CATALOG, ITEM_SET_ANCESTOR ANCESTOR, ITEM_SET ISETTC, ITEM_SET ISETTD    WHERE  CATALOG.TEST_CATALOG_ID = ?    AND CATALOG.ITEM_SET_ID = ISETTC.ITEM_SET_ID   AND ISETTC.ACTIVATION_STATUS = 'AC'   AND ANCESTOR.ANCESTOR_ITEM_SET_ID = CATALOG.ITEM_SET_ID   AND ANCESTOR.ITEM_SET_TYPE = 'TD'   AND ISETTD.ITEM_SET_ID = ANCESTOR.ITEM_SET_ID   AND ISETTD.ACTIVATION_STATUS = 'AC'";
	private static String getItemSetTDForTC = "SELECT ISETTD.ITEM_SET_ID OAS_ID, ISETTD.ADS_OB_ASMT_ID ADS_ID, ISETTD.ASMT_HASH HASH_KEY , ISETTD.ASMT_ENCRYPTION_KEY KEY  FROM ITEM_SET_ANCESTOR ANCESTOR, ITEM_SET ISETTC, ITEM_SET ISETTD WHERE ISETTC.EXT_TST_ITEM_SET_ID = ?  AND ISETTC.ACTIVATION_STATUS = 'AC'  AND ISETTC.ITEM_SET_TYPE = 'TC'  AND ANCESTOR.ANCESTOR_ITEM_SET_ID = ISETTC.ITEM_SET_ID  AND ANCESTOR.ITEM_SET_TYPE = 'TD'  AND ISETTD.ITEM_SET_ID = ANCESTOR.ITEM_SET_ID  AND ISETTD.ACTIVATION_STATUS = 'AC'";
	private static String getItemSetTDDtls = "SELECT ISETTD.ITEM_SET_ID OAS_ID, ISETTD.ADS_OB_ASMT_ID ADS_ID, ISETTD.ASMT_HASH HASH_KEY , ISETTD.ASMT_ENCRYPTION_KEY KEY  FROM ITEM_SET ISETTD WHERE ISETTD.EXT_TST_ITEM_SET_ID = ?";
	private static String getItemForTD = "SELECT DISTINCT ITEM.ITEM_ID OAS_ID, ITEM.ADS_ITEM_ID ADS_ID FROM ITEM_SET_ITEM ISI, ITEM WHERE ITEM_SET_ID = ? AND ISI.ITEM_ID= ITEM.ITEM_ID AND ITEM.ACTIVATION_STATUS = 'AC'";
	private static String updateItemSet = " UPDATE ITEM_SET SET CONTENT_REPOSITORY_URI = ? WHERE ITEM_SET_ID = ? ";
	private static final String GET_ITEM_BLOB_SQL = "SELECT item_rendition_xml_encr as itemBlob  FROM pub_ob_item_pkg  WHERE pub_ob_item_pkg_id = ?";
	private static final String GET_SUBTEST_BLOB_SQL = "SELECT  asmt_manifest_encr AS subtestBlob FROM pub_ob_asmt  WHERE pub_ob_asmt_id = ?";
	
	public static HashMap getItemSetTDForTestCatalog(Connection conn,
			long testCatalogId) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		HashMap itemSetMap = new HashMap();
		try {
			ps = conn.prepareStatement(getItemSetTDForTestCatalog);
			ps.setLong(1, testCatalogId);
			rs = ps.executeQuery();
			while (rs.next()) {
				ItemSet td = new ItemSet();
				td.setOasid(rs.getLong("OAS_ID"));
				td.setAdsid(rs.getLong("ADS_ID"));
				td.setHash(rs.getString("HASH_KEY"));
				td.setKey(rs.getString("KEY"));
				System.out.println("OAS_ID"+"\t"+"ADS_ID"+"\t"+"HASH_KEY\t"+"KEY");
				System.out.println(""+td.getOasid()+"\t"+td.getAdsid()+"\t"+td.getHash()+"\t"+td.getKey());
				populteChildren(conn, td);
				itemSetMap.put(new Long(td.getOasid()), td);

			}

		} finally {
			ClosableHelper.close(ps, rs);
		}

		return itemSetMap;

	}

	public static HashMap getItemSetTDForTC(Connection conn,
			String extTstItemSetId) throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		HashMap itemSetMap = new HashMap();
		try {
			ps = conn.prepareStatement(getItemSetTDForTC);
			ps.setString(1, extTstItemSetId);
			rs = ps.executeQuery();
			while (rs.next()) {
				ItemSet td = new ItemSet();
				td.setOasid(rs.getLong("OAS_ID"));
				td.setAdsid(rs.getLong("ADS_ID"));
				td.setHash(rs.getString("HASH_KEY"));
				td.setKey(rs.getString("KEY"));
				populteChildren(conn, td);
				itemSetMap.put(new Long(td.getOasid()), td);

			}
			System.out.println(itemSetMap.size());

		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}

		return itemSetMap;

	}
	
	public static HashMap getItemSetTDDtls(Connection conn,
			ArrayList<String> tdList) throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		HashMap itemSetMap = new HashMap();
		try {
			ps = conn.prepareStatement(getItemSetTDDtls);

			if (tdList != null && !tdList.isEmpty()) {
				for (String extTstItemSetId : tdList) {
					ps.setString(1, extTstItemSetId);
					rs = ps.executeQuery();
					while (rs.next()) {
						ItemSet td = new ItemSet();
						td.setExtTstItemSetId(extTstItemSetId);
						
						td.setOasid(rs.getLong("OAS_ID"));
						td.setAdsid(rs.getLong("ADS_ID"));
						td.setHash(rs.getString("HASH_KEY"));
						td.setKey(rs.getString("KEY"));
						populteChildren(conn, td);
						itemSetMap.put(new Long(td.getOasid()), td);
					}
				}
			}
			System.out.println(itemSetMap.size());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return itemSetMap;
	}

	private static void populteChildren(Connection conn, ItemSet td)
			throws SQLException {
		System.out.println("Inside populteChildren().....");
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(getItemForTD);
			ps.setLong(1, td.getOasid());
			rs = ps.executeQuery();
			while (rs.next()) {
				Item item = new Item();
				item.setOasitemid(rs.getString("OAS_ID"));
				item.setAdsid(rs.getLong("ADS_ID"));
				System.out.println("OAS_ID >>"+item.getOasitemid()+"\t ADS_ID >>"+item.getAdsid());
				td.addChildren(item);

			}

		} finally {
			ClosableHelper.close(ps, rs);
		}

	}

	public static void updateItemSet(Connection conn, long itemsetIdTD,
			String repositoryURI) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(updateItemSet);
			ps.setString(1, repositoryURI);
			ps.setLong(2, itemsetIdTD);
			ps.executeUpdate();
		} finally {
			ClosableHelper.close(ps);
		}

	}
	
	public  static byte[] getItemBlob(final Connection con,
			final long itemId) throws Exception {
		Blob data = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		byte[] item = null;
		try {
			ps = con.prepareStatement(GET_ITEM_BLOB_SQL);
			ps.setLong(1, itemId);
			rs = ps.executeQuery();
			if (rs.next()) {
				data = rs.getBlob("itemBlob");
			}			
			
			if(data != null) {
				item = getBlobAsArrayOfByte(data);
			}
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}

		return item;
	}

	public  static byte[] getSubtestBlob(Connection con, long subtestId)
			throws Exception {
		Blob data = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		byte[] item = null;
		try {
			stmt1 = con.prepareStatement(GET_SUBTEST_BLOB_SQL);
			stmt1.setLong(1, subtestId);
			rs1 = stmt1.executeQuery();
			if (rs1.next()) {
				data = rs1.getBlob("subtestBlob");
			}
			
			if(data != null) {
				item = getBlobAsArrayOfByte(data);
			}
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(stmt1, rs1);
		}
		return item;
	}

	private static byte[] getBlobAsArrayOfByte(Blob data) throws Exception {

		boolean moreData = true;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;
		try {
			is = data.getBinaryStream();
			while (moreData) {
				byte[] buffer = new byte[128];
				int read = is.read(buffer);
				moreData = read > 0;
				if (moreData) {
					baos.write(buffer, 0, read);
				}
			}
		} finally {
			ClosableHelper.close(is);
		}

		return baos.toByteArray();
	}
}
