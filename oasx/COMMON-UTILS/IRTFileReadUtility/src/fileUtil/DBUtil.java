package fileUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class DBUtil {

	private static Connection con = null;
	
	public static void insertScoreLookup(List<PVALFileData> fileDataList) {
		
		ResourceBundle rb = ResourceBundle.getBundle("config");
		String levels = rb.getString("file.testLevel");
		
		try {
			PreparedStatement ps = null;
			PreparedStatement ps1 = null;
			con = SqlUtil.openOASDBcon();
			ps = con.prepareStatement(Query.insertScoreLookup);
			ps1 = con.prepareStatement(Query.insertScoreLookupItemSet);
			for (PVALFileData pvalFileData : fileDataList) {
				if(pvalFileData != null && levels.contains(pvalFileData.getLevel().trim())) {
					String scoreLookupId = "TERRAB3" + "_" + pvalFileData.getNormsGroup() 
					+ "_" + pvalFileData.getGrade() + "_" + pvalFileData.getContent() + "_" + pvalFileData.getLevel()
					+ "_" + pvalFileData.getOther();
					ps.setString(1, "SCL");
					ps.setString(2, "OPV");
					ps.setString(3, scoreLookupId);
					ps.setInt(4, 0);
					//ps.setDouble(5, pvalFileData.getCodeValue().getValue());
					ps.setString(6, pvalFileData.getForm());
					ps.setString(7, pvalFileData.getLevel());
					ps.setString(8, pvalFileData.getGrade());
					ps.setString(9, FileUtil.processContentAreaName(pvalFileData.getContent()));
					ps.setString(10, FileUtil.processNongroupName(pvalFileData.getNormsGroup()));
					ps.setString(11, "2011");
					ps.setString(12, "TERRAB3");
					ps.setString(13, FileUtil.getDisplayName(FileUtil.getProductIdFromType(pvalFileData.getOther())));
					System.out.println(scoreLookupId);
					ps.addBatch();
					ps.executeUpdate();
					
					insertScoreLookupItemSet(ps1, scoreLookupId, pvalFileData);
				}
			}
			ps.executeBatch();
			ps1.executeBatch();
			con.commit();
			SqlUtil.close(ps);
			SqlUtil.close(ps1);
		} catch(SQLException e) {
			try {
				con.rollback();
				System.out.println("Data are not saved in SCORE_LOOKUP table.");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	private static void insertScoreLookupItemSet(PreparedStatement ps, String scoreLookupId, 
						PVALFileData pvalFileData) {
		try {
			ps.setString(1, scoreLookupId);
			//ps.setLong(2, FileUtil._OBJECTIVEMAP.get(pvalFileData.getCodeValue().getCode()));
			ps.addBatch();
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void insertItemPvalue(List<PVALFileData> dataList) {
		
		ResourceBundle rb = ResourceBundle.getBundle("config");
		String levels = rb.getString("file.testLevel");
		
		try {
			PreparedStatement ps = null;
			con = SqlUtil.openOASDBcon();
			ps = con.prepareStatement(Query.INSERT_ITEM_P_VALUE);
			for (PVALFileData pvalFileData : dataList) {
				for(CodeValue codeVal : pvalFileData.getDataList()) {
					ps.setString(1, "G");
					ps.setString(2, pvalFileData.getLevel());
					ps.setString(3, pvalFileData.getGrade());
					ps.setString(4, codeVal.getItemId());
					ps.setDouble(5, codeVal.getValue());
					ps.setString(6, pvalFileData.getNormsGroup());
					ps.addBatch();
				}
			}
			ps.executeBatch();
			con.commit();
			SqlUtil.close(ps);
		} catch(SQLException e) {
			try {
				con.rollback();
				System.out.println("Data are not saved in SCORE_LOOKUP table.");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	public static List<String> getAllItemSet(String contentArea) {
		
		con = SqlUtil.openOASDBcon();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> itemSetList = new ArrayList<String>();
		try {
			ps = con.prepareStatement(Query.GET_ALL_ITEM_SET);
			ps.setString(1, contentArea);
			ps.setString(2, "3720");
			rs = ps.executeQuery();
			
			while(rs.next()) {
				itemSetList.add(rs.getString("item_set_id"));
			}
			return itemSetList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map<String, String> getAllItemForItemSet(List<String> itemSetIdList) {
		
		con = SqlUtil.openOASDBcon();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> itemSetMap = new HashMap<String, String>();
		try {
			String itemSetIds = "";
			for(String itemSetId : itemSetIdList) {
				itemSetIds += itemSetId + ",";
			}
			ps = con.prepareStatement(Query.ITEMS_FROM_ITEM_SET.replaceAll("#", itemSetIds.substring(1, itemSetIds.length() - 1)));
			rs = ps.executeQuery();
			
			while(rs.next()) {
				int itemSortOrder = rs.getInt("item_sort_order");
				if(itemSortOrder > 9) {
					itemSetMap.put(String.valueOf(itemSortOrder), rs.getString("item_sort_order"));
				} else {
					itemSetMap.put(String.valueOf("0" + itemSortOrder), rs.getString("item_sort_order"));
				}
			}
			return itemSetMap;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
