package com.ctb.contentBridge.core.upload.dao;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.util.ClosableHelper;


public class ADSDao {
	private static final String GET_ITEM_BLOB_SQL = "SELECT item_rendition_xml_encr as itemBlob  FROM OB_ITEM_PKG  WHERE ob_item_pkg_id = ?";
	private static final String GET_SUBTEST_BLOB_SQL = "SELECT  asmt_manifest_encr AS subtestBlob FROM OB_ASMT  WHERE ob_asmt_id = ?";

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
		/*byte[] item = getBlobAsArrayOfByte(data);*/

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
			/*rs1.close();*/
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(stmt1, rs1);

		}
		/*byte[] item = getBlobAsArrayOfByte(data);*/
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
