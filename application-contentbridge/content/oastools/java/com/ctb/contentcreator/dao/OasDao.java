package com.ctb.contentcreator.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.ctb.contentcreator.bin.Item;
import com.ctb.contentcreator.bin.ItemSet;
import com.ctb.contentcreator.utils.ColsableHelper;

public class OasDao {

	private static String getItemSetTDForTestCatalog = "SELECT ISETTD.ITEM_SET_ID OAS_ID, ISETTD.ADS_OB_ASMT_ID ADS_ID, ISETTD.ASMT_HASH HASH_KEY , ISETTD.ASMT_ENCRYPTION_KEY KEY  FROM TEST_CATALOG CATALOG, ITEM_SET_ANCESTOR ANCESTOR, ITEM_SET ISETTC, ITEM_SET ISETTD    WHERE  CATALOG.TEST_CATALOG_ID = ?    AND CATALOG.ITEM_SET_ID = ISETTC.ITEM_SET_ID   AND ISETTC.ACTIVATION_STATUS = 'AC'   AND ANCESTOR.ANCESTOR_ITEM_SET_ID = CATALOG.ITEM_SET_ID   AND ANCESTOR.ITEM_SET_TYPE = 'TD'   AND ISETTD.ITEM_SET_ID = ANCESTOR.ITEM_SET_ID   AND ISETTD.ACTIVATION_STATUS = 'AC'";
	private static String getItemSetTDForTC = "SELECT ISETTD.ITEM_SET_ID OAS_ID, ISETTD.ADS_OB_ASMT_ID ADS_ID, ISETTD.ASMT_HASH HASH_KEY , ISETTD.ASMT_ENCRYPTION_KEY KEY  FROM ITEM_SET_ANCESTOR ANCESTOR, ITEM_SET ISETTC, ITEM_SET ISETTD WHERE ISETTC.EXT_TST_ITEM_SET_ID = ?  AND ISETTC.ACTIVATION_STATUS = 'AC'  AND ISETTC.ITEM_SET_TYPE = 'TC'  AND ANCESTOR.ANCESTOR_ITEM_SET_ID = ISETTC.ITEM_SET_ID  AND ANCESTOR.ITEM_SET_TYPE = 'TD'  AND ISETTD.ITEM_SET_ID = ANCESTOR.ITEM_SET_ID  AND ISETTD.ACTIVATION_STATUS = 'AC'";
	private static String getItemForTD = "SELECT DISTINCT ITEM.ITEM_ID OAS_ID, ITEM.ADS_ITEM_ID ADS_ID FROM ITEM_SET_ITEM ISI, ITEM WHERE ITEM_SET_ID = ? AND ISI.ITEM_ID= ITEM.ITEM_ID AND ITEM.ACTIVATION_STATUS = 'AC'";
	private static String updateItemSet = " UPDATE ITEM_SET SET CONTENT_REPOSITORY_URI = ? WHERE ITEM_SET_ID = ? ";

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
				populteChildren(conn, td);
				itemSetMap.put(new Long(td.getOasid()), td);

			}

		} finally {
			ColsableHelper.close(ps, rs);
		}

		return itemSetMap;

	}

	public static HashMap getItemSetTDForTC(Connection conn,
			String extTstItemSetId) throws SQLException {
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

		} finally {
			ColsableHelper.close(ps, rs);
		}

		return itemSetMap;

	}

	private static void populteChildren(Connection conn, ItemSet td)
			throws SQLException {
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
				td.addChildren(item);

			}

		} finally {
			ColsableHelper.close(ps, rs);
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
			ColsableHelper.close(ps);
		}

	}

}
