/**
 * 
 */
package com.ctb.contentBridge.core.publish.dao;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jdom.Attribute;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.ctb.contentBridge.core.domain.Configuration;
import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.util.AppendXML;
import com.ctb.contentBridge.core.util.ClosableHelper;
import com.ctb.contentBridge.core.util.PublishCommon;
import com.ctb.contentBridge.core.util.XMLParsing;
import com.ctb.contentBridge.core.util.crypto.Crypto;

/**
 * @author
 * 
 */
public class ContentPublishDAO {
	private static final String LINE_SEP = "\n";
	static String strKeyIdArr[];
	static String strEncrKeyArr[];
	static String strHash;
	static String strASmtId;
	static String strKeyRingXml;
	static org.jdom.Document doc;
	static org.jdom.Element key_root;
	static org.jdom.Element keys;
	static org.jdom.Element k_ring;
	static Attribute key_id;
	static Attribute val;
	static DocumentBuilderFactory dbf;
	static DocumentBuilder db;
	static DOMBuilder jdomBuilder;
	static org.jdom.Document JDOMdoc;

	public static String checkAsset(Connection conn, String strAssetId)
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String action = "Insert";
		try {
			StringBuffer sbufQuery = new StringBuffer("SELECT ast.asset_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",ast.asset_blob");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",ast.asset_type");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",ast.source_uri");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",ast.date_row_inserted");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",ast.date_last_modified");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM aa_asset ast");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE ast.asset_id = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strAssetId);
			rs = ps.executeQuery();

			while (rs.next()) {
				action = "Update";
				break;
			}
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return action;
	}

	public static String checkAsmt(Connection conn, String strSubtestId)
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String action = "Insert";
		try {
			StringBuffer sbufQuery = new StringBuffer("SELECT asm.aa_asmt_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM aa_asmt asm");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE asm.aa_asmt_id =  ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strSubtestId);
			rs = ps.executeQuery();

			while (rs.next()) {
				action = "Update";
				break;
			}
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return action;
	}

	public static String checkItem(Connection conn, String strAAItemId)
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String action = "Insert";
		try {
			StringBuffer sbufQuery = new StringBuffer("SELECT itm.aa_item_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM aa_item itm");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE itm.aa_item_id =  ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strAAItemId);
			rs = ps.executeQuery();

			while (rs.next()) {
				action = "Update";
				break;
			}
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return action;
	}

	public static Boolean insertAsset(Connection conn, ArrayList arrList,
			byte buffer[]) throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Boolean hasInserted = Boolean.FALSE;
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"INSERT INTO aa_asset (asset_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",asset_blob");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",asset_type");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",source_uri");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",date_row_inserted)");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("VALUES (?,empty_blob(),?,?,SYSDATE)");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, ((String) arrList.get(0)).trim());
			ps.setString(2, ((String) arrList.get(1)).trim());
			ps.setString(3, ((String) arrList.get(2)).trim());
			ps.execute();
			ClosableHelper.close(ps);

			sbufQuery = new StringBuffer("SELECT ast.asset_blob");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM aa_asset ast");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE ast.asset_id = ?");
			sbufQuery.append(" FOR UPDATE");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, ((String) arrList.get(0)).trim());
			rs = ps.executeQuery();
			System.out.println("before update-->");
			Boolean flag = updateBlobData(conn, rs, buffer);
			System.out.println("flag for update-->"+flag);
			if (flag) {
				/* conn.commit(); */
				hasInserted = Boolean.TRUE;
			} else {
				throw new SystemException("Error in Publish Asset.");
			}
		} catch (SQLException e) {
			System.out.println("Exception for update-->"+e.getMessage());
			
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return hasInserted;
	}

	private static Boolean findForItem(Connection conn, String strItmId)
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer sbufQuery = new StringBuffer("SELECT asi.aa_asmt_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM aa_asmt_item_map asi");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE asi.aa_item_id = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strItmId);
			rs = ps.executeQuery();

			ArrayList arrListNew = new ArrayList();
			for (; rs.next(); arrListNew.add(rs.getString(1)))
				;
			ClosableHelper.close(ps, rs);

			sbufQuery = new StringBuffer("SELECT COUNT(oba.state_id)");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_asmt oba");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",aa_asmt_item_map asi");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE asi.aa_item_id = ?");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("AND asi.aa_asmt_id = oba.aa_asmt_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("AND oba.state_id <> 1");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strItmId);
			rs = ps.executeQuery();
			Integer stateCount = 0;
			while (rs.next()) {
				stateCount = rs.getInt(1);
			}
			if (stateCount > 0) {
				return Boolean.FALSE;
			}
			ClosableHelper.close(ps, rs);

			for (int i = 0; i < arrListNew.size(); i++) {
				String strValue = obAsmtUpdate(conn, (String) arrListNew.get(i));
				if (strValue.equals("false")) {
					return Boolean.FALSE;
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return Boolean.TRUE;
	}

	private static Boolean updateBlobData(Connection conn, ResultSet resultSet,
			byte byteData[]) throws SystemException {
		Boolean flag = Boolean.FALSE;
		try {
			if (resultSet.next()) {
				Blob l_mapBLOB = resultSet.getBlob(1);
				InputStream instream = new ByteArrayInputStream(byteData);
				byte l_buffer[] = new byte[32500];
				int L_NREAD = 0;
				CallableStatement cstmt = conn
						.prepareCall("begin dbms_lob.writeappend( :1, :2, :3 ); end;");
				cstmt.registerOutParameter(1, 2004);
				while ((L_NREAD = instream.read(l_buffer)) != -1) {
					cstmt.setBlob(1, l_mapBLOB);
					cstmt.setInt(2, L_NREAD);
					cstmt.setBytes(3, l_buffer);
					cstmt.executeUpdate();
					l_mapBLOB = cstmt.getBlob(1);
				}
				instream.close();
				cstmt.close();
				l_buffer = (byte[]) null;
				resultSet.close();
				flag = Boolean.TRUE;
			}
		} catch (SQLException e) {
			throw new SystemException(e);
		} catch (IOException e) {
			throw new SystemException(e);
		}
		return flag;
	}

	public static Boolean updateAsset(Connection conn, ArrayList arrList,
			byte buffer[]) throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Boolean hasUpdated = Boolean.FALSE;
		try {
			StringBuffer sbufQuery = new StringBuffer("UPDATE aa_asset");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("SET asset_blob = empty_blob()");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",asset_type = ?");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",source_uri = ?");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",date_last_modified = SYSDATE");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE asset_id = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, ((String) arrList.get(1)).trim());
			ps.setString(2, ((String) arrList.get(2)).trim());
			ps.setString(3, ((String) arrList.get(0)).trim());
			ps.execute();
			ClosableHelper.close(ps);

			sbufQuery = new StringBuffer("SELECT ast.asset_blob");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM aa_asset ast");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE ast.asset_id = ?");
			sbufQuery.append(" FOR UPDATE");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, ((String) arrList.get(0)).trim());
			rs = ps.executeQuery();
			Boolean flag = updateBlobData(conn, rs, buffer);
			
			if (flag) {
					hasUpdated = Boolean.TRUE;
			} else {
				throw new SystemException(
						"Asset cannot be re-published as it is being used and its state is locked.");
			}
			/*if (flag) {
				Boolean flag1 = findForAsset(conn, (String) arrList.get(0));
				if (flag1) {
					hasUpdated = Boolean.TRUE;
				} else {
					throw new SystemException(
							"Asset cannot be re-published as it is being used and its state is locked.");
				}
			} else {
				throw new SystemException("Error in Publish Asset.");
			}*/
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return hasUpdated;
	}

	private static Boolean findForAsset(Connection conn, String strAsset)
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Boolean flag = Boolean.FALSE;
		try {
			StringBuffer sbufQuery = new StringBuffer("SELECT asi.aa_item_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM aa_asset_item_map asi");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE asi.asset_id = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strAsset);
			rs = ps.executeQuery();

			ArrayList arrListNew = new ArrayList();
			if (rs.next()) {
				do
					arrListNew.add(rs.getString(1));
				while (rs.next());
			} else {
				return Boolean.TRUE;
			}
			ClosableHelper.close(ps, rs);

			sbufQuery = new StringBuffer("SELECT COUNT(obi.state_id)");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_item_pkg obi");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",aa_asset_item_map asi");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE asi.asset_id = ?");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("AND asi.aa_item_id = obi.aa_item_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("AND obi.state_id <> 1");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strAsset);
			rs = ps.executeQuery();
			Integer stateCount = 0;
			while (rs.next()) {
				stateCount = rs.getInt(1);
			}
			if (stateCount > 0) {
				return Boolean.FALSE;
			}
			ClosableHelper.close(ps, rs);

			for (int i = 0; i < arrListNew.size(); i++) {
				sbufQuery = new StringBuffer(
						"SELECT itm.item_delivery_xml_blob");
				sbufQuery.append(LINE_SEP);
				sbufQuery.append("FROM aa_item itm");
				sbufQuery.append(LINE_SEP);
				sbufQuery.append("WHERE itm.aa_item_id = ?");

				ps = conn.prepareStatement(sbufQuery.toString());
				ps.setString(1, (String) arrListNew.get(i));
				rs = ps.executeQuery();

				while (rs.next()) {
					Blob appended_xml = rs.getBlob(1);
					int size1 = (int) appended_xml.length();
					byte buffer1[] = new byte[size1];
					buffer1 = appended_xml.getBytes(1L, size1);
					flag = obItemUpdate(conn, new String(buffer1));
					if (!flag) {
						return Boolean.FALSE;
					}
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return flag;
	}

	public static void insertItem(Connection conn, ArrayList arrList)
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"INSERT INTO aa_item (aa_item_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",key_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",item_delivery_xml_blob");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",date_row_inserted)");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("VALUES (?,?,empty_blob(),SYSDATE)");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, ((String) arrList.get(0)).trim());
			ps.setString(2, ((String) arrList.get(1)).trim());
			ps.execute();
			ClosableHelper.close(ps);

			sbufQuery = new StringBuffer("SELECT itm.item_delivery_xml_blob");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM aa_item itm");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE itm.aa_item_id = ?");
			sbufQuery.append(" FOR UPDATE");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, ((String) arrList.get(0)).trim());
			rs = ps.executeQuery();
			String XMLDATA = (String) arrList.get(2);
			byte outData[] = new byte[XMLDATA.length()];
			outData = XMLDATA.getBytes();
			updateBlobData(conn, rs, outData);
			/* conn.commit(); */
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
	}

	public static void insertAssetItemMap(Connection conn, ArrayList arrList)
			throws SystemException {
		PreparedStatement ps = null;
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"INSERT INTO aa_asset_item_map (aa_item_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",asset_id)");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("VALUES (?,?)");
			ps = conn.prepareStatement(sbufQuery.toString());
			for (int i = 2; i < arrList.size(); i++) {
				ps.setString(1, (String) arrList.get(0));
				ps.setString(2, (String) arrList.get(i));
				ps.executeUpdate();
			}
			/* conn.commit(); */
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps);
		}
	}

	public static void insertObItemPkg(Connection conn, String strObItemPkgId,
			String strItemId, String hash, byte outData[])
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"INSERT INTO ob_item_pkg (ob_item_pkg_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",aa_item_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",item_rendition_xml_encr");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",hash");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",date_row_inserted");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",state_id)");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("VALUES (?,?,empty_blob(),?,SYSDATE,?)");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strObItemPkgId);
			ps.setString(2, strItemId);
			ps.setString(3, hash);
			ps.setString(4, "1");
			ps.execute();
			ClosableHelper.close(ps);

			sbufQuery = new StringBuffer("SELECT obi.item_rendition_xml_encr");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_item_pkg obi");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE obi.aa_item_id = ?");
			sbufQuery.append(" FOR UPDATE");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strItemId);
			rs = ps.executeQuery();
			updateBlobData(conn, rs, outData);
			/* conn.commit(); */
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
	}

	public static String getObItemPkgId(Connection conn, String strItemId)
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String strObItemPkgId = "";
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"SELECT obi.ob_item_pkg_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_item_pkg obi");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE obi.aa_item_id =  ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strItemId);
			rs = ps.executeQuery();

			while (rs.next()) {
				strObItemPkgId = rs.getString(1);
			}
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return strObItemPkgId;
	}

	public static String checkForStateId(Connection conn, String strObItemPkgId)
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String status = "LOCKED";
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"SELECT COUNT(oba.state_id)");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_asmt oba");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",ob_asmt_item_map oai");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE oai.ob_item_pkg_id = ?");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("AND oai.ob_asmt_id = oba.ob_asmt_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("AND oba.state_id <> 1");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strObItemPkgId);
			rs = ps.executeQuery();

			while (rs.next()) {
				if (rs.getInt(1) == 0) {
					status = "UNLOCK";
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return status;
	}

	public static void updateItem(Connection conn, ArrayList arrList)
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Boolean hasUpdated = Boolean.FALSE;
		try {
			StringBuffer sbufQuery = new StringBuffer("UPDATE aa_item");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("SET key_id = ?");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",item_delivery_xml_blob = empty_blob()");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",date_last_modified = SYSDATE");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE aa_item_id = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, ((String) arrList.get(1)).trim());
			ps.setString(2, ((String) arrList.get(0)).trim());
			ps.execute();
			ClosableHelper.close(ps);

			sbufQuery = new StringBuffer("SELECT itm.item_delivery_xml_blob");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM aa_item itm");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE itm.aa_item_id = ?");
			sbufQuery.append(" FOR UPDATE");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, ((String) arrList.get(0)).trim());
			rs = ps.executeQuery();
			String XMLDATA = (String) arrList.get(2);
			byte outData[] = new byte[XMLDATA.length()];
			outData = XMLDATA.getBytes();
			updateBlobData(conn, rs, outData);
			/* conn.commit(); */
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
	}

	public static void updateAssetItemMap(Connection conn, ArrayList arrList)
			throws SystemException {
		PreparedStatement ps = null;
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"DELETE FROM aa_asset_item_map");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE aa_item_id = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, ((String) arrList.get(0)).trim());
			ps.executeUpdate();
			insertAssetItemMap(conn, arrList);
			/* conn.commit(); */
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps);
		}
	}
	public static String getHashItemPkg(Connection conn, String strItemId
			) throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String hash=null;
			StringBuffer sbufQuery = new StringBuffer("SELECT obi.hash");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_item_pkg obi");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE obi.aa_item_id = ?");
			
			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strItemId);
			rs = ps.executeQuery();
			while(rs.next()){
				hash=rs.getString(1);
			}
			
			return hash;
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
	}

	public static void updateObItemPkg(Connection conn, String strItemId,
			String hash, byte outData[]) throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer sbufQuery = new StringBuffer("UPDATE ob_item_pkg");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("SET hash = ?");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",date_last_modified = SYSDATE");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",item_rendition_xml_encr = empty_blob()");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE aa_item_id = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, hash);
			ps.setString(2, strItemId);
			ps.executeUpdate();
			ClosableHelper.close(ps);

			sbufQuery = new StringBuffer("SELECT obi.item_rendition_xml_encr");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_item_pkg obi");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE obi.aa_item_id = ?");
			sbufQuery.append(" FOR UPDATE");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strItemId);
			rs = ps.executeQuery();
			updateBlobData(conn, rs, outData);
			/* conn.commit(); */
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
	}

	public static Boolean updateObItemPkgData(Connection conn,
			String strItemId, String hash, byte outData[])
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Boolean flag = Boolean.FALSE;
		try {
			StringBuffer sbufQuery = new StringBuffer("UPDATE ob_item_pkg");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("SET hash = ?");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",date_last_modified = SYSDATE");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",item_rendition_xml_encr = empty_blob()");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE aa_item_id = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, hash);
			ps.setString(2, strItemId);
			ps.executeUpdate();
			ClosableHelper.close(ps);

			sbufQuery = new StringBuffer("SELECT obi.item_rendition_xml_encr");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_item_pkg obi");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE obi.aa_item_id = ?");
			sbufQuery.append(" FOR UPDATE");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strItemId);
			rs = ps.executeQuery();
			Boolean flag1 = updateBlobData(conn, rs, outData);
			if (flag1) {
				flag = findForItem(conn, strItemId);
				/* conn.commit(); */
			}
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return flag;
	}

	public static String getSourcePublishId(Connection conn)
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sourcePublishId = "";
		try {
			StringBuffer sbufQuery = new StringBuffer("SELECT par.value");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ads_param par");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE par.description = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, "Source_Instance_Id");
			rs = ps.executeQuery();

			while (rs.next()) {
				sourcePublishId = rs.getString(1);
			}
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return sourcePublishId;
	}

	public static Integer getMaxObAsmtId(Connection conn)
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer intMaxObAsmtId = 0;
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"SELECT seq_ob_asmt_id.nextval*100 + par.value");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ads_param par");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE par.description = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, "Source_Instance_Id");
			rs = ps.executeQuery();

			while (rs.next()) {
				intMaxObAsmtId = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return intMaxObAsmtId;
	}

	public static String getMaxObItmId(Connection conn) throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String strMaxObAsmtId = null;
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"SELECT seq_ob_item_pkg_id.nextval*100 + par.value");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ads_param par");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE par.description = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, "Source_Instance_Id");
			rs = ps.executeQuery();

			while (rs.next()) {
				strMaxObAsmtId = rs.getString(1);
			}
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return strMaxObAsmtId;
	}

	public static ArrayList getKeyIdEncr(Connection conn,
			String strSecuritykeyId, String sourcePubId) throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList arrKeyIdList = new ArrayList();
		try {
			StringBuffer sbufQuery = new StringBuffer("SELECT kym.key_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",kym.encr_key");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM aa_key_master kym");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE kym.security_classification_id = ?");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("AND kym.source_ads_id = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strSecuritykeyId);
			ps.setString(2, sourcePubId);
			rs = ps.executeQuery();

			if (!rs.next()) {
				ClosableHelper.close(ps);
				sbufQuery = new StringBuffer(
						"SELECT seq_key_id.nextval*100 + par.value");
				sbufQuery.append(LINE_SEP);
				sbufQuery.append("FROM ads_param par");
				sbufQuery.append(LINE_SEP);
				sbufQuery.append("WHERE par.description = ?");

				ps = conn.prepareStatement(sbufQuery.toString());
				ps.setString(1, "Source_Instance_Id");
				rs = ps.executeQuery();

				if (rs.next()) {
					arrKeyIdList.add(Integer.toString(Integer.parseInt(rs
							.getString(1))));
				} else {
					arrKeyIdList.add("error");
					return arrKeyIdList;
				}
				arrKeyIdList.add(PublishCommon.randomPW());
				arrKeyIdList.add(strSecuritykeyId);
				arrKeyIdList.add(sourcePubId);

				ClosableHelper.close(ps, rs);
				sbufQuery = new StringBuffer(
						"INSERT INTO aa_key_master (key_id");
				sbufQuery.append(LINE_SEP);
				sbufQuery.append(",encr_key");
				sbufQuery.append(LINE_SEP);
				sbufQuery.append(",security_classification_id");
				sbufQuery.append(LINE_SEP);
				sbufQuery.append(",source_ads_id)");
				sbufQuery.append(LINE_SEP);
				sbufQuery.append("VALUES (?,?,?,?)");

				ps = conn.prepareStatement(sbufQuery.toString());
				ps.setString(1, (String) arrKeyIdList.get(0));
				ps.setString(2, (String) arrKeyIdList.get(1));
				ps.setString(3, (String) arrKeyIdList.get(2));
				ps.setString(4, (String) arrKeyIdList.get(3));
				ps.executeUpdate();
			} else {
				arrKeyIdList.add(rs.getString(1));
				arrKeyIdList.add(rs.getString(2));
			}
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return arrKeyIdList;
	}

	public static String createAppendedXML(Connection conn,
			ArrayList assetIdList, String strObItemId, AppendXML appendXML)
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String strAppendedXML = null;
		Integer count = 0;
		try {
			StringBuffer sbufQuery = new StringBuffer("SELECT ast.asset_blob");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",ast.asset_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",ast.asset_type");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM aa_asset ast");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE ast.asset_id IN (");
			for (int i = 2; i < assetIdList.size(); i++) {
				sbufQuery.append("'").append(assetIdList.get(i)).append("'");
				if (i != assetIdList.size() - 1) {
					sbufQuery.append(",");
				} else {
					sbufQuery.append(")");
				}
				count++;
			}

			if (count > 0) {
				ps = conn.prepareStatement(sbufQuery.toString());
				rs = ps.executeQuery();
				strAppendedXML = appendXML.parseWithDOM(rs, assetIdList, strObItemId);
			} else {
				strAppendedXML = appendXML
						.parseWithDOM(null, null, strObItemId);
			}

		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		insertDecryptedItemXml(conn,strAppendedXML , (String)assetIdList.get(0));
		return strAppendedXML;
	}

	public static String createAppendedXMLWithPakage(Connection conn,
			ArrayList assetIdList, String strObItemId, AppendXML appendXML)
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String strAppendedXML = null;
		Integer count = 0;
		try {
			StringBuffer sbufQuery = new StringBuffer("SELECT ast.asset_blob");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",ast.asset_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",ast.asset_type");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM aa_asset ast");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE ast.asset_id = ?");

			if (assetIdList.size() > 0)
				count++;

			if (count > 0) {
				ps = conn.prepareStatement(sbufQuery.toString());
				ps.setString(1, (String) assetIdList.get(0));
				rs = ps.executeQuery();
				strAppendedXML = appendXML.parseWithDOM(rs, assetIdList, strObItemId);
			} else {
				strAppendedXML = appendXML
						.parseWithDOM(null, null, strObItemId);
			}

		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		insertDecryptedItemXml(conn,strAppendedXML , (String)assetIdList.get(0));
		return strAppendedXML;
	}
   public static  String getDecryptedItemXml(Connection conn, String itemId) throws SystemException{
	   
	   PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Blob decData=null;
			
            //String decXml=null;
			StringBuffer sbufQuery = new StringBuffer(
					"SELECT itd.item_rendition_xml");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM aa_item_decrypted itd");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE itd.aa_item_id = ?");
			
			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, itemId);
			rs = ps.executeQuery();
			while(rs.next()){
				decData=rs.getBlob(1);
			}
			ClosableHelper.close(ps);
			int len = (int)decData.length();
			byte [] data = decData.getBytes(1, len);
			//decData.free();
			String decXml = new String(data);

			return decXml;
			
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
   }
	public static void insertDecryptedItemXml(Connection conn, String itemXml,
			String itemId) throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"DELETE FROM aa_item_decrypted");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE aa_item_id = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, itemId);
			ps.executeUpdate();
			ClosableHelper.close(ps);

			sbufQuery = new StringBuffer(
					"INSERT INTO aa_item_decrypted (aa_item_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",item_rendition_xml");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",created_date_time)");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("VALUES (?,empty_blob(),SYSDATE)");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, itemId);
			ps.executeUpdate();
			ClosableHelper.close(ps);

			sbufQuery = new StringBuffer("SELECT itd.item_rendition_xml");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM aa_item_decrypted itd");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE itd.aa_item_id = ?");
			sbufQuery.append(" FOR UPDATE");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, itemId);
			rs = ps.executeQuery();

			byte xmlDATA[] = new byte[itemXml.length()];
			xmlDATA = itemXml.getBytes();
			updateBlobData(conn, rs, xmlDATA);

			/* conn.commit(); */
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
	}

	public static void insertAssessment(Connection conn, String strAsmtId,
			String strAsmtXML) throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"INSERT INTO aa_asmt (aa_asmt_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",asmt_xml_blob");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",date_row_inserted)");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("VALUES (?,empty_blob(),SYSDATE)");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strAsmtId);
			ps.executeUpdate();
			ClosableHelper.close(ps);

			sbufQuery = new StringBuffer("SELECT asm.asmt_xml_blob");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM aa_asmt asm");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE asm.aa_asmt_id = ?");
			sbufQuery.append(" FOR UPDATE");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strAsmtId);
			rs = ps.executeQuery();

			byte xmlDATA[] = new byte[strAsmtXML.length()];
			xmlDATA = strAsmtXML.getBytes();
			updateBlobData(conn, rs, xmlDATA);

			/* conn.commit(); */
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
	}

	public static void updateAssessment(Connection conn, String strAsmtId,
			String strAsmtXML) throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer sbufQuery = new StringBuffer("UPDATE aa_asmt");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("SET asmt_xml_blob = empty_blob()");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",date_last_modified = SYSDATE");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE aa_asmt_id = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strAsmtId);
			ps.executeUpdate();
			ClosableHelper.close(ps);

			sbufQuery = new StringBuffer("SELECT asm.asmt_xml_blob");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM aa_asmt asm");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE asm.aa_asmt_id = ?");
			sbufQuery.append(" FOR UPDATE");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strAsmtId);
			rs = ps.executeQuery();

			byte xmlDATA[] = new byte[strAsmtXML.length()];
			xmlDATA = strAsmtXML.getBytes();
			updateBlobData(conn, rs, xmlDATA);

			/* conn.commit(); */
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
	}

	public static void updateObAsmtData(Connection conn, String strAsmtId,
			String strAsmtEncrkey, String strAsmtHash,
			byte bAsmtkeyRingXmlEncrArr[], String strAsmtManifestXml,
			byte bAsmtManifestEncrArr[]) throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer sbufQuery = new StringBuffer("UPDATE ob_asmt");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("SET asmt_encr_key = ?");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",asmt_hash = ?)");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",asmt_key_ring_xml_encr = empty_blob()");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",asmt_manifest_encr = empty_blob()");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",asmt_manifest_xml_blob = empty_blob()");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",date_last_modified = SYSDATE)");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE aa_asmt_id = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strAsmtEncrkey);
			ps.setString(2, strAsmtHash);
			ps.setString(3, strAsmtId);
			ps.executeUpdate();
			ClosableHelper.close(ps);

			sbufQuery = new StringBuffer("SELECT oba.asmt_manifest_xml_blob");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_asmt oba");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE oba.aa_asmt_id = ?");
			sbufQuery.append(" FOR UPDATE");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strAsmtId);
			rs = ps.executeQuery();

			byte xmlDATA[] = new byte[strAsmtManifestXml.length()];
			xmlDATA = strAsmtManifestXml.getBytes();
			updateBlobData(conn, rs, xmlDATA);
			ClosableHelper.close(ps, rs);

			sbufQuery = new StringBuffer("SELECT oba.asmt_key_ring_xml_encr");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_asmt oba");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE oba.aa_asmt_id = ?");
			sbufQuery.append(" FOR UPDATE");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strAsmtId);
			rs = ps.executeQuery();

			updateBlobData(conn, rs, bAsmtkeyRingXmlEncrArr);
			ClosableHelper.close(ps, rs);

			sbufQuery = new StringBuffer("SELECT oba.asmt_manifest_encr");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_asmt oba");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE oba.aa_asmt_id = ?");
			sbufQuery.append(" FOR UPDATE");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strAsmtId);
			rs = ps.executeQuery();

			updateBlobData(conn, rs, bAsmtManifestEncrArr);

			/* conn.commit(); */
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
	}

	public static void updateObAsmt(Connection conn, Integer iObAsmtId,
			String strAsmtId, String strAsmtEncrkey, String strAsmtHash,
			byte bAsmtkeyRingXmlEncrArr[], String strAsmtManifestXml,
			byte bAsmtManifestEncrArr[]) throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer sbufQuery = new StringBuffer("UPDATE ob_asmt");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("SET asmt_encr_key = ?");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",asmt_hash = ?");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",asmt_key_ring_xml_encr = empty_blob()");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",asmt_manifest_encr = empty_blob()");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",asmt_manifest_xml_blob = empty_blob()");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",date_last_modified = SYSDATE");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE aa_asmt_id = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strAsmtEncrkey);
			ps.setString(2, strAsmtHash);
			ps.setString(3, strAsmtId);
			ps.executeUpdate();
			ClosableHelper.close(ps);

			sbufQuery = new StringBuffer("SELECT oba.asmt_manifest_xml_blob");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_asmt oba");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE oba.aa_asmt_id = ?");
			sbufQuery.append(" FOR UPDATE");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strAsmtId);
			rs = ps.executeQuery();

			byte xmlDATA[] = new byte[strAsmtManifestXml.length()];
			xmlDATA = strAsmtManifestXml.getBytes();
			updateBlobData(conn, rs, xmlDATA);
			ClosableHelper.close(ps, rs);

			sbufQuery = new StringBuffer("SELECT oba.asmt_key_ring_xml_encr");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_asmt oba");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE oba.ob_asmt_id = ?");
			sbufQuery.append(" FOR UPDATE");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setInt(1, iObAsmtId);
			rs = ps.executeQuery();

			updateBlobData(conn, rs, bAsmtkeyRingXmlEncrArr);
			ClosableHelper.close(ps, rs);

			sbufQuery = new StringBuffer("SELECT oba.asmt_manifest_encr");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_asmt oba");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE oba.ob_asmt_id = ?");
			sbufQuery.append(" FOR UPDATE");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setInt(1, iObAsmtId);
			rs = ps.executeQuery();

			updateBlobData(conn, rs, bAsmtManifestEncrArr);

			/* conn.commit(); */
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
	}

	public static void insertObAsmt(Connection conn, Integer iObAsmtId,
			String strAsmtId, String strAsmtEncrkey, String strAsmtHash,
			byte bAsmtkeyRingXmlEncrArr[], String strAsmtManifestXml,
			byte bAsmtManifestEncrArr[]) throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"INSERT INTO ob_asmt (ob_asmt_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",aa_asmt_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",asmt_encr_key");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",asmt_hash");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",asmt_key_ring_xml_encr");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",asmt_manifest_xml");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",asmt_manifest_encr");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",asmt_manifest_xml_blob");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",row_inserted_date");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",state_id)");
			sbufQuery.append(LINE_SEP);
			sbufQuery
					.append("VALUES (?,?,?,?,empty_blob(),null,empty_blob(),empty_blob(),SYSDATE,?)");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setInt(1, iObAsmtId);
			ps.setString(2, strAsmtId);
			ps.setString(3, strAsmtEncrkey);
			ps.setString(4, strAsmtHash);
			ps.setString(5, "1");
			ps.executeUpdate();
			ClosableHelper.close(ps);

			sbufQuery = new StringBuffer("SELECT oba.asmt_manifest_xml_blob");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_asmt oba");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE oba.aa_asmt_id = ?");
			sbufQuery.append(" FOR UPDATE");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strAsmtId);
			rs = ps.executeQuery();

			byte xmlDATA[] = new byte[strAsmtManifestXml.length()];
			xmlDATA = strAsmtManifestXml.getBytes();
			updateBlobData(conn, rs, xmlDATA);
			ClosableHelper.close(ps, rs);

			sbufQuery = new StringBuffer("SELECT oba.asmt_key_ring_xml_encr");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_asmt oba");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE oba.ob_asmt_id = ?");
			sbufQuery.append(" FOR UPDATE nowait");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setInt(1, iObAsmtId);
			rs = ps.executeQuery();

			updateBlobData(conn, rs, bAsmtkeyRingXmlEncrArr);
			ClosableHelper.close(ps, rs);

			sbufQuery = new StringBuffer("SELECT oba.asmt_manifest_encr");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_asmt oba");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE oba.ob_asmt_id = ?");
			sbufQuery.append(" FOR UPDATE");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setInt(1, iObAsmtId);
			rs = ps.executeQuery();

			updateBlobData(conn, rs, bAsmtManifestEncrArr);

			/* conn.commit(); */
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
	}

	public static void insertAsmtItemMap(Connection conn, String strAsmtId,
			ArrayList itemList) throws SystemException {
		PreparedStatement ps = null;
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"INSERT INTO aa_asmt_item_map (aa_asmt_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",aa_item_id)");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("VALUES (?,?)");
			ps = conn.prepareStatement(sbufQuery.toString());
			for (int i = 0; i < itemList.size(); i++) {
				ps.setString(1, strAsmtId);
				ps.setString(2, (String) itemList.get(i));
				ps.executeUpdate();
			}
			/* conn.commit(); */
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps);
		}
	}

	public static void updateAsmtItemMap(Connection conn, String strAsmtId,
			ArrayList itemList) throws SystemException {
		PreparedStatement ps = null;
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"DELETE FROM aa_asmt_item_map");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE aa_asmt_id = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strAsmtId);
			ps.executeUpdate();
			insertAsmtItemMap(conn, strAsmtId, itemList);
			/* conn.commit(); */
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps);
		}
	}

	public static void insertObAsmtItemMap(Connection conn,
			Integer intObAsmtItm, ArrayList itemList) throws SystemException {
		PreparedStatement ps = null;
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"INSERT INTO ob_asmt_item_map (ob_asmt_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",ob_item_pkg_id)");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("VALUES (?,?)");
			ps = conn.prepareStatement(sbufQuery.toString());
			for (int i = 0; i < itemList.size(); i++) {
				ps.setInt(1, intObAsmtItm);
				ps.setString(2, (String) itemList.get(i));
				ps.executeUpdate();
			}
			/* conn.commit(); */
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps);
		}
	}

	public static void updateObAsmtItemMap(Connection conn,
			Integer intObAsmtItm, ArrayList itemList) throws SystemException {
		PreparedStatement ps = null;
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"DELETE FROM ob_asmt_item_map");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE ob_asmt_id = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setInt(1, intObAsmtItm);
			ps.executeUpdate();
			insertObAsmtItemMap(conn, intObAsmtItm, itemList);
			/* conn.commit(); */
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps);
		}
	}

	public static ResultSet selectManifestXMLData(Connection conn,
			ArrayList itemIdList) throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer sbufQuery = new StringBuffer("SELECT obi.aa_item_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",obi.ob_item_pkg_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",obi.hash");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",itm.key_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_item_pkg obi");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",aa_item itm");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE itm.aa_item_id IN (");
			for (int i = 0; i < itemIdList.size(); i++) {
				sbufQuery.append("'").append(itemIdList.get(i)).append("'");
				if (i != itemIdList.size() - 1) {
					sbufQuery.append(",");
				} else {
					sbufQuery.append(")");
				}
			}
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("AND obi.aa_item_id = itm.aa_item_id");

			ps = conn.prepareStatement(sbufQuery.toString());
			rs = ps.executeQuery();
		} catch (SQLException e) {
			ClosableHelper.close(ps, rs);
			throw new SystemException(e);
		}
		return rs;
	}

	public static ResultSet selectKeyRingXMLData(Connection conn,
			String keyId[]) throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer sbufQuery = new StringBuffer("SELECT par.value");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ads_param par");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE par.description = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, "Source_Instance_Id");
			rs = ps.executeQuery();

			String sourcePublishId = null;
			while (rs.next()) {
				sourcePublishId = rs.getString(1);
			}
			ClosableHelper.close(ps, rs);

			sbufQuery = new StringBuffer("SELECT kym.key_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append(",kym.encr_key");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM aa_key_master kym");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE kym.key_id IN (");
			for (int i = 0; i < keyId.length; i++) {
				sbufQuery.append("'").append(keyId[i]).append("'");
				if (i != keyId.length - 1) {
					sbufQuery.append(",");
				} else {
					sbufQuery.append(")");
				}
			}
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("AND kym.source_ads_id = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, sourcePublishId);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			ClosableHelper.close(ps, rs);
			throw new SystemException(e);
		}
		return rs;
	}

	public static Integer getObAsmtId(Connection conn, String strAsmtId)
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer intObAsmtId = 0;
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"SELECT oba.ob_asmt_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_asmt oba");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE oba.aa_asmt_id = ?");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strAsmtId);
			rs = ps.executeQuery();

			while (rs.next()) {
				intObAsmtId = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return intObAsmtId;
	}

	public static Integer checkStateId(Connection conn, Integer obId,
			String flag) throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer intStateId = 0;
		try {
			StringBuffer sbufQuery = new StringBuffer("SELECT ");
			if ("Subtest".equals(flag)) {
				sbufQuery.append("nvl(oba.state_id,'1')");
				sbufQuery.append(LINE_SEP);
				sbufQuery.append("FROM ob_asmt oba");
				sbufQuery.append(LINE_SEP);
				sbufQuery.append("WHERE oba.ob_asmt_id = ?");
			} else if ("Item".equals(flag)) {
				sbufQuery.append("nvl(obi.state_id,'1')");
				sbufQuery.append(LINE_SEP);
				sbufQuery.append("FROM ob_item_pkg obi");
				sbufQuery.append(LINE_SEP);
				sbufQuery.append("WHERE obi.ob_item_pkg_id = ?");
			}

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setInt(1, obId);
			rs = ps.executeQuery();

			while (rs.next()) {
				intStateId = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return intStateId;
	}

	public static ArrayList getObItemId(Connection conn, ArrayList itemIdList)
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList obItmIdList = new ArrayList();
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"SELECT obi.ob_item_pkg_id");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM ob_item_pkg obi");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE obi.aa_item_id IN (");
			for (int i = 0; i < itemIdList.size(); i++) {
				sbufQuery.append("'").append(itemIdList.get(i)).append("'");
				if (i != itemIdList.size() - 1) {
					sbufQuery.append(",");
				} else {
					sbufQuery.append(")");
				}
			}

			ps = conn.prepareStatement(sbufQuery.toString());
			rs = ps.executeQuery();

			while (rs.next()) {
				obItmIdList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return obItmIdList;
	}

	public static String getAsmtManifest(Connection conn, ArrayList aa_item_id,
			ArrayList aa_item_id1, String xmlFile) throws SystemException {
		String strXmlManifest = null;
		try {
			dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			db = dbf.newDocumentBuilder();
			int j = 0;
			ArrayList iObItemIdArr = new ArrayList();
			ArrayList strAAHash = new ArrayList();
			ArrayList strAAKey = new ArrayList();
			Map iObItemIdMap = new HashMap<String, String>();
			Map strAAHashMap = new HashMap<String, String>();
			Map strAAKeyMap = new HashMap<String, String>();
			ResultSet oraset = selectManifestXMLData(conn, aa_item_id1);
			InputStream is_new = new ByteArrayInputStream(xmlFile.getBytes());
			Document w3cDoc_new = db.parse(is_new);
			Element domRootElement_new = w3cDoc_new.getDocumentElement();
			NodeList sectionElements_new = domRootElement_new
					.getElementsByTagName("itemref");
			if(aa_item_id1 != null && !aa_item_id1.isEmpty()){
				System.out.println("ORIGINAL Item Order:::");
				for(int z =0 ; z< aa_item_id1.size(); z++){
					System.out.println(aa_item_id1.get(z));
				}
			}
			
			while (oraset.next()) {
				iObItemIdMap.put(oraset.getString(1), oraset.getString(2));
				strAAHashMap.put(oraset.getString(1), oraset.getString(3));
				strAAKeyMap.put(oraset.getString(1), oraset.getString(4));
					
			}
			if(aa_item_id1 != null && !aa_item_id1.isEmpty()){
				
				for(int z =0 ; z < aa_item_id1.size(); z++){
					iObItemIdArr.add(iObItemIdMap.get(String.valueOf(aa_item_id1.get(z))));
					strAAHash.add(strAAHashMap.get(String.valueOf(aa_item_id1.get(z))));
					strAAKey.add(strAAKeyMap.get(String.valueOf(aa_item_id1.get(z))));
				}
			}
			if(iObItemIdArr != null && !iObItemIdArr.isEmpty()){
				System.out.println("MODIFIED Item Order:::");
				for(int z =0 ; z< iObItemIdArr.size(); z++){
					System.out.println(iObItemIdArr.get(z));
				}
			}
			/*while (oraset.next()) {
				if (aa_item_id1.contains(oraset.getString(1))) {
					iObItemIdArr.add(oraset.getString(2));
					strAAHash.add(oraset.getString(3));
					strAAKey.add(oraset.getString(4));
					System.out.println(oraset.getString(1));
				}
			}*/
			
			/*for (int i = 0; i < aa_item_id1.size(); i++) {
				oraset.first();
				do
					if (oraset.getString(1).equals((String) aa_item_id1.get(i))) {
						iObItemIdArr.add(oraset.getString(2));
						strAAHash.add(oraset.getString(3));
						strAAKey.add(oraset.getString(4));
					}
				while (oraset.next());
			}*/
			 

			Document doc = db.newDocument();
			org.w3c.dom.Node root = doc.createElement("ob_assessment");
			doc.appendChild(root);
			Element rootElement = doc.getDocumentElement();
			if (XMLParsing.FLAG != -1) {
				rootElement.setAttribute("starting_question_number",
						Integer.toString(XMLParsing.FLAG));
				XMLParsing.FLAG = -1;
			}
			Element assessment_title = doc.createElement("assessment_title");
			rootElement.appendChild(assessment_title);
			Element ob_element_list = doc.createElement("ob_element_list");
			for (int x = 0; x < iObItemIdArr.size(); x++) {
				Element f = doc.createElement("f");
				f.setAttribute("id", (String) iObItemIdArr.get(x));
				f.setAttribute("h", (String) strAAHash.get(x));
				f.setAttribute("k", (String) strAAKey.get(x));
				for (int i = 0; i < sectionElements_new.getLength(); i++) {
					Element l_name = (Element) sectionElements_new.item(i);
					if (l_name.getAttribute("linkrefid").equalsIgnoreCase(
							(String) aa_item_id1.get(x))) {
						if (l_name.getAttributes().getNamedItem("type") != null)
							f.setAttribute("type", l_name.getAttribute("type"));
						String allow_revisit = l_name
								.getAttribute("allow_revisit");
						String allow_revisit_on_restart = l_name
								.getAttribute("allow_revisit_on_restart");
						f.setAttribute(
								"allow_revisit",
								allow_revisit != null
										&& allow_revisit.trim().length() >= 1 ? allow_revisit
										: "true");
						f.setAttribute(
								"allow_revisit_on_restart",
								allow_revisit_on_restart != null
										&& allow_revisit_on_restart.trim()
												.length() >= 1 ? allow_revisit_on_restart
										: "true");
					}
				}

				ob_element_list.appendChild(f);
			}

			rootElement.appendChild(ob_element_list);
			iObItemIdArr = new ArrayList();
			ResultSet oraset1 = selectManifestXMLData(conn, aa_item_id);
			while (oraset1.next()) {
				if (aa_item_id.contains(oraset1.getString(1))) {
					iObItemIdArr.add(oraset1.getString(2));
				}
			}
			/*
			 * for (int i = 0; i < aa_item_id.size(); i++) { oraset1.first(); do
			 * if (oraset1.getString(1).equals((String) aa_item_id.get(i)))
			 * iObItemIdArr.add(oraset1.getString(2)); while (oraset1.next()); }
			 */

			InputStream is = new ByteArrayInputStream(xmlFile.getBytes());
			Document w3cDoc = db.parse(is);
			Element domRootElement = w3cDoc.getDocumentElement();
			NodeList sectionElements = domRootElement
					.getElementsByTagName("section");
			if (sectionElements.getLength() > 1 || XMLParsing.FLAG1 > 0) {
				rootElement.appendChild((Element) doc.importNode(
						sectionElements.item(0), true));
				jdomBuilder = new DOMBuilder();
				JDOMdoc = jdomBuilder.build(doc);
				org.jdom.Element rElement = JDOMdoc.getRootElement();
				List rList = rElement.getChildren();
				Iterator iDescend = rList.iterator();
				List attrList = null;
				Iterator attrIterator = null;
				Attribute c1_1Attr = null;
				do {
					org.jdom.Element c1Element = (org.jdom.Element) iDescend
							.next();
					if (c1Element.getName().equals("section")) {
						c1Element.setName("ob_element_select_order");
						List iattr = c1Element.getAttributes();
						List r1List = c1Element.getChildren();
						Iterator i1Descend = r1List.iterator();
						if (iattr.size() > 0) {
							String ai = c1Element
									.getAttributeValue("associate_items");
							c1Element.removeAttribute("ident");
							c1Element.removeAttribute("associate_items");
							org.jdom.Element c1_1Element = (org.jdom.Element) i1Descend
									.next();
							org.jdom.Element c2Element = new org.jdom.Element(
									"g");
							c1Element.addContent(c2Element);
							c2Element.setAttribute("ai", ai);
							if (c1_1Element.getName().equals(
									"selection_ordering")) {
								List r4List = c1_1Element.getChildren();
								Iterator i4Descend = r4List.iterator();
								do {
									org.jdom.Element c1_2_1Element = (org.jdom.Element) i4Descend
											.next();
									if (c1_2_1Element.getName().equals("order"))
										c2Element.setAttribute("ot", "Random");
									if (c1_2_1Element.getName().equals(
											"selection")) {
										List r5List = c1_2_1Element
												.getChildren();
										Iterator i5Descend = r5List.iterator();
										do {
											org.jdom.Element c1_3_1Element = (org.jdom.Element) i5Descend
													.next();
											if (c1_3_1Element.getName().equals(
													"selection_number"))
												c2Element.setAttribute("sn",
														c1_3_1Element
																.getValue());
										} while (i5Descend.hasNext());
									}
								} while (i4Descend.hasNext());
							}
							do {
								org.jdom.Element e = new org.jdom.Element("e");
								c2Element.addContent(e);
								Attribute id = new Attribute("id",
										(String) iObItemIdArr.get(j++));
								e.setAttribute(id);
							} while (j < iObItemIdArr.size());
						} else {
							do {
								org.jdom.Element c1_1Element = (org.jdom.Element) i1Descend
										.next();
								if (c1_1Element.getName().equals("itemref")
										&& j < iObItemIdArr.size()) {
									c1_1Element.setName("e");
									attrList = c1_1Element.getAttributes();
									attrIterator = attrList.iterator();
									do {
										c1_1Attr = (Attribute) attrIterator
												.next();
										if (c1_1Attr.getName().equals(
												"linkrefid")) {
											c1_1Attr.setName("id");
											c1_1Attr.setValue((String) iObItemIdArr
													.get(j++));
										}
									} while (attrIterator.hasNext());
								} else if (c1_1Element.getName().equals(
										"section")) {
									c1_1Element.setName("g");
									c1_1Element.removeAttribute("ident");
									attrList = c1_1Element.getAttributes();
									attrIterator = attrList.iterator();
									do {
										c1_1Attr = (Attribute) attrIterator
												.next();
										if (c1_1Attr.getName().equals(
												"associate_items"))
											c1_1Attr.setName("ai");
									} while (attrIterator.hasNext());
									List r3List = c1_1Element.getChildren();
									Iterator i3Descend = r3List.iterator();
									do {
										org.jdom.Element c1_1_1Element = (org.jdom.Element) i3Descend
												.next();
										if (c1_1_1Element.getName().equals(
												"selection_ordering")) {
											List r4List = c1_1_1Element
													.getChildren();
											Iterator i4Descend = r4List
													.iterator();
											do {
												org.jdom.Element c1_2_1Element = (org.jdom.Element) i4Descend
														.next();
												if (c1_2_1Element.getName()
														.equals("order"))
													c1_1Element.setAttribute(
															"ot", "Random");
												if (c1_2_1Element.getName()
														.equals("selection")) {
													List r5List = c1_2_1Element
															.getChildren();
													Iterator i5Descend = r5List
															.iterator();
													do {
														org.jdom.Element c1_3_1Element = (org.jdom.Element) i5Descend
																.next();
														if (c1_3_1Element
																.getName()
																.equals("selection_number"))
															c1_1Element
																	.setAttribute(
																			"sn",
																			c1_3_1Element
																					.getValue());
													} while (i5Descend
															.hasNext());
												}
											} while (i4Descend.hasNext());
										}
										if (c1_1_1Element.getName().equals(
												"itemref")
												&& j < iObItemIdArr.size()) {
											c1_1_1Element.setName("e");
											attrList = c1_1_1Element
													.getAttributes();
											attrIterator = attrList.iterator();
											do {
												c1_1Attr = (Attribute) attrIterator
														.next();
												if (c1_1Attr.getName().equals(
														"linkrefid")) {
													c1_1Attr.setName("id");
													c1_1Attr.setValue((String) iObItemIdArr
															.get(j++));
												}
											} while (attrIterator.hasNext());
										}
									} while (i3Descend.hasNext());
									c1_1Element
											.removeChild("selection_ordering");
								}
							} while (i1Descend.hasNext());
						}
						c1Element.removeChild("selection_ordering");
						for (int i = j; i > 0; i--)
							c1Element.removeChild("itemref");

					}
				} while (iDescend.hasNext());
			} else {
				jdomBuilder = new DOMBuilder();
				JDOMdoc = jdomBuilder.build(doc);
			}
			XMLOutputter xop = new XMLOutputter();
			strXmlManifest = xop.outputString(JDOMdoc);
			XMLParsing.FLAG1 = 0;
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
		System.out.println("STRING MANIFEST XML:   " + strXmlManifest);
		return strXmlManifest;
	}

	public static Integer getKey(Connection conn, ArrayList itemIdList)
			throws SystemException {
		Integer intCount = 0;
		ResultSet oraset = null;
		String strAAKeyArr[] = new String[itemIdList.size()];
		try {
			int INC = 0;
			oraset = selectManifestXMLData(conn, itemIdList);
			while (oraset.next()) {
				if (itemIdList.contains(oraset.getString(1))) {
					strAAKeyArr[INC] = oraset.getString(4);
					INC++;
				}
			}
			/*
			 * for (int i = 0; i < itemIdList.size(); i++) { oraset.first(); do
			 * if (oraset.getString(1).equals((String) itemIdList.get(i))) {
			 * strAAKeyArr[INC] = oraset.getString(4); INC++; } while
			 * (oraset.next()); }
			 */

			strKeyIdArr = new String[strAAKeyArr.length];
			strEncrKeyArr = new String[strAAKeyArr.length];
			ResultSet resultset = selectKeyRingXMLData(conn, strAAKeyArr);
			for (intCount = 0; resultset.next(); intCount++) {
				strKeyIdArr[intCount] = resultset.getString(1);
				strEncrKeyArr[intCount] = resultset.getString(2);
			}

		} catch (Exception exception) {
			throw new SystemException(exception);
		} finally {
			ClosableHelper.close(oraset);
		}
		return intCount;
	}

	public static String getAsmtKeyRing(Connection conn, ArrayList aa_item_id)
			throws SystemException {
		try {
			int count = getKey(conn, aa_item_id);
			key_root = new org.jdom.Element("asmt_key_ring");
			doc = new org.jdom.Document(key_root);
			keys = new org.jdom.Element("keys");
			for (int x = 0; x < count; x++) {
				key_id = new Attribute("id", (new StringBuffer(
						String.valueOf(strKeyIdArr[x]))).toString());
				val = new Attribute("val", (new StringBuffer(
						String.valueOf(strEncrKeyArr[x]))).toString());
				k_ring = new org.jdom.Element("k");
				k_ring.setAttribute(key_id);
				k_ring.setAttribute(val);
				keys.addContent(k_ring);
			}

			key_root.addContent(keys);
			try {
				XMLOutputter outputter = new XMLOutputter();
				strKeyRingXml = outputter.outputString(doc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception exception) {
			throw new SystemException(exception);
		}
		return strKeyRingXml;
	}

	public static byte[] encryptedXML(String strRequestXML, String strKey)
			throws SystemException {
		Crypto crypto = new Crypto();
		byte bEncrManifest[] = new byte[strRequestXML.length()];
		bEncrManifest = crypto.encrypt(strKey, strRequestXML, "UTF-8", true,
				false);
		strHash = crypto.generateHash(bEncrManifest);
		return bEncrManifest;
	}

	public static String randomGeneration(int range) {
		String strRandom = null;
		int rand_num = (int) (Math.random() * (double) range);
		strRandom = String.valueOf(rand_num);
		return strRandom;
	}

	public static Boolean obItemUpdate(Connection conn, String xmlFile)
			throws SQLException {
		Boolean flag = false;
		try {
			AppendXML appendXML = null;
			ArrayList arrList = new ArrayList();
			XMLParsing xmlParsing = new XMLParsing();
			Crypto crypto = new Crypto();
			xmlParsing.set_Attribute_values(xmlFile);
			arrList = xmlParsing.get_Attribute_Values();
			arrList = PublishCommon.removeDuplicate(arrList, "item");
			String xsltPath = System.getProperty("ITM_XSLT_FILE_PATH")
					+ "Item_Pkg.xslt";
			String strCfgFileURL = PublishCommon.getXSLTXSDContent(new File(
					xsltPath));
			xmlFile = PublishCommon.transforms(xmlFile, strCfgFileURL);
			String sourcePubId = getSourcePublishId(conn);
			appendXML = new AppendXML(xmlFile);
			ArrayList arrlist1 = new ArrayList();
			arrlist1 = getKeyIdEncr(conn, (String) arrList.get(1), sourcePubId);
			String Ob_Item_pkg_Id = getObItemPkgId(conn,
					(String) arrList.get(0));
			String strAppendedXML = createAppendedXML(conn, arrList,
					Ob_Item_pkg_Id, appendXML);
			byte outData[] = new byte[strAppendedXML.length()];
			outData = crypto.encrypt((String) arrlist1.get(1), strAppendedXML,
					"UTF-8", true, false);
			flag = updateObItemPkgData(conn, crypto.generateHash(outData),
					(String) arrList.get(0), outData);
			XMLParsing.arrList = new ArrayList();
		} catch (Exception e) {
			XMLParsing.arrList = new ArrayList();
			throw new SystemException(e);
		}
		return flag;
	}

	public static String obAsmtUpdate(Connection conn, String strAsmtId)
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"SELECT asm.asmt_xml_blob");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM aa_asmt asm");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE asm.aa_asmt_id = ?");
			sbufQuery.append(" FOR UPDATE");

			ps = conn.prepareStatement(sbufQuery.toString());
			ps.setString(1, strAsmtId);
			rs = ps.executeQuery();

			String xmlFile = "";
			XMLParsing xmlParsing = new XMLParsing();
			if (rs.next()) {
				Blob APPENDED_XML = rs.getBlob(1);
				int size1 = (int) APPENDED_XML.length();
				byte buffer1[] = new byte[size1];
				buffer1 = APPENDED_XML.getBytes(1L, size1);
				xmlFile = new String(buffer1);
			}
			xmlParsing.set_Attribute_values(xmlFile);
			ArrayList aa_item_id1 = xmlParsing.get_Attribute_Values();
			ArrayList aa_item_id = new ArrayList();
			for (int i = 1; i < aa_item_id1.size(); i++)
				aa_item_id.add(aa_item_id1.get(i));

			aa_item_id1 = new ArrayList(aa_item_id);
			aa_item_id = PublishCommon.removeDuplicate(aa_item_id, "Subtest");
			String random = randomGeneration(2000);
			String key_ring = getAsmtKeyRing(conn, aa_item_id1);
			byte b_xml[] = encryptedXML(key_ring, random);
			Crypto cc = new Crypto();
			String new_xml = getAsmtManifest(conn, aa_item_id1, aa_item_id,
					xmlFile);
			byte b[] = encryptedXML(new_xml, random);
			String hash = cc.generateHash(b);
			updateObAsmtData(conn, strAsmtId, random, hash, b_xml, new_xml, b);
			XMLParsing.arrList = new ArrayList();
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return "true";
	}

	public static String assetCheck(Connection conn, ArrayList assetIdList)
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (assetIdList.size() > 2) {
				StringBuffer sbufQuery = new StringBuffer(
						"SELECT COUNT(ast.asset_id)");
				sbufQuery.append(LINE_SEP);
				sbufQuery.append(",sum(rownum)");
				sbufQuery.append(LINE_SEP);
				sbufQuery.append("FROM aa_asset ast");
				sbufQuery.append(LINE_SEP);
				sbufQuery.append("WHERE ast.asset_id IN (");
				for (int i = 2; i < assetIdList.size(); i++) {
					sbufQuery.append("'").append(assetIdList.get(i))
							.append("'");
					if (i != assetIdList.size() - 1) {
						sbufQuery.append(",");
					} else {
						sbufQuery.append(")");
					}
				}

				ps = conn.prepareStatement(sbufQuery.toString());
				rs = ps.executeQuery();

				Integer assetCount = 0;
				while (rs.next()) {
					assetCount = rs.getInt(1);
				}
				if (assetCount == assetIdList.size() - 2) {
					return "Found";
				} else {
					throw new SystemException("Asset Not Found");
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return "Found";
	}

	public static String itemCheck(Connection conn, ArrayList itemIdList)
			throws SystemException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer sbufQuery = new StringBuffer(
					"SELECT COUNT(itm.aa_item_id)");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("FROM aa_item itm");
			sbufQuery.append(LINE_SEP);
			sbufQuery.append("WHERE itm.aa_item_id IN (");
			for (int i = 0; i < itemIdList.size(); i++) {
				sbufQuery.append("'").append(itemIdList.get(i)).append("'");
				if (i != itemIdList.size() - 1) {
					sbufQuery.append(",");
				} else {
					sbufQuery.append(")");
				}
			}

			ps = conn.prepareStatement(sbufQuery.toString());
			rs = ps.executeQuery();

			Integer itmCount = 0;
			while (rs.next()) {
				itmCount = rs.getInt(1);
			}
			if (itmCount == itemIdList.size()) {
				return "Found";
			}

		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(ps, rs);
		}
		return "Item Not Found";
	}

	public static void doTdContentSize(Connection conn, String extTstItemSetId,Configuration config)
			throws SystemException {
		CallableStatement cs = null;
		try {
			
			String env=config.getEnv();
			// cs = conn.prepareCall( "{call do_oas_td_content_size (?)}");
			cs = conn.prepareCall( "{call update_oas_from_ads(?,?)}");
			cs.setString(1, extTstItemSetId);
			cs.setString(2, env);
			cs.execute();
			
			/*conn.commit();*/
		} catch (SQLException e) {
			throw new SystemException(e);
		} finally {
			ClosableHelper.close(cs);
		}
	}
}