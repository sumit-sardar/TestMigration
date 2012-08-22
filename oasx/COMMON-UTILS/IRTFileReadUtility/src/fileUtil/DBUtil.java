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
		
		insertItemPvalue(fileDataList);
		
		ResourceBundle rb = ResourceBundle.getBundle("config");
		String levels = rb.getString("file.testLevel");
		
		try {
			PreparedStatement ps = null;
			PreparedStatement ps1 = null;
			con = SqlUtil.openOASDBcon();
			ps = con.prepareStatement(Query.insertScoreLookup);
			ps1 = con.prepareStatement(Query.insertScoreLookupItemSet);
			for(PVALFileData pvalFileData : fileDataList) {
				if(!(pvalFileData.getContent() == null || pvalFileData.getGrade() == null || pvalFileData.getLevel() == null)) {
					if(levels.contains(pvalFileData.getLevel().trim())) {
						Map<String,Double> sumMap = pvalFileData.getObjectiveItemSumMap();
						Map<String,Integer> countMap = pvalFileData.getObjItemCount();
						Map<String,Integer> objIdMap = pvalFileData.getObjectiveMap();
						for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
							Long objectiveId = Long.valueOf(objIdMap.get(entry.getKey()));
							String objectiveName = entry.getKey();
							Double pVal = sumMap.get(entry.getKey()) / Double.valueOf(countMap.get(entry.getKey()));
							System.out.println(objIdMap.get(entry.getKey()) + " - " + entry.getKey() + " - " + sumMap.get(entry.getKey()) + " - " + countMap.get(entry.getKey()) + " - " + pVal);
							String scoreLookupId = "TERRAB3" + "_" + pvalFileData.getNormsGroup() 
							+ "_" + pvalFileData.getGrade() + "_" + pvalFileData.getContent() + "_" + pvalFileData.getLevel()
							+ "_" + pvalFileData.getOther();
							scoreLookupId = scoreLookupId + "_" + objectiveName.substring(0, 2);
							ps.setString(1, "SCL");
							ps.setString(2, "OPV");
							ps.setString(3, scoreLookupId);
							ps.setInt(4, 0);
							ps.setDouble(5, pVal);
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
							//ps.executeUpdate();
							
							insertScoreLookupItemSet(ps1, scoreLookupId, objectiveId);
						}
						ps.executeBatch();
						ps1.executeBatch();
						con.commit();
						SqlUtil.close(ps);
						SqlUtil.close(ps1);
					}
				}
			}
		} catch(SQLException e) {
			try {
				con.rollback();
				System.out.println("Data are not saved in SCORE_LOOKUP table.");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		try {/*
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
		*/} catch(/*SQL*/Exception e) {/*
			try {
				con.rollback();
				System.out.println("Data are not saved in SCORE_LOOKUP table.");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		*/}
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
	
	private static void insertScoreLookupItemSet(PreparedStatement ps, String scoreLookupId, 
			Long objectiveId) {
		try {
			ps.setString(1, scoreLookupId);
			ps.setLong(2, objectiveId);
			ps.addBatch();
			//ps.executeUpdate();
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
				if(!(pvalFileData.getContent() == null || pvalFileData.getGrade() == null || pvalFileData.getLevel() == null)) {
					if(levels.contains(pvalFileData.getLevel().trim())) {
						for(CodeValue codeVal : pvalFileData.getDataList()) {
							if(codeVal.getItemId() != null) {
								ps.setString(1, pvalFileData.getForm().trim());
								ps.setString(2, pvalFileData.getLevel());
								ps.setString(3, pvalFileData.getGrade());
								ps.setString(4, codeVal.getItemId());
								ps.setDouble(5, codeVal.getValue());
								ps.setString(6, pvalFileData.getNormsGroup());
								ps.addBatch();
							}
						}
					}
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
	
	public static List<String> getAllItemSet(String contentArea, String level, String productId) {
		
		con = SqlUtil.openOASDBcon();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> itemSetList = new ArrayList<String>();
		try {
			ps = con.prepareStatement(Query.GET_ALL_ITEM_SET);
			ps.setString(1, contentArea);
			ps.setString(2, productId);
			ps.setString(3, level);
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
	
	public static Map<String, String> getAllItemForItemSet(List<String> itemSetIdList, PVALFileData pvalFileData) {
		
		con = SqlUtil.openOASDBcon();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> itemSetMap = new HashMap<String, String>();
		Map<String, Integer> objectiveCountMap = new HashMap<String, Integer>();
		Map<String, String> objItemMap = new HashMap<String, String>();
		Map<String, Integer> objMap = new HashMap<String, Integer>();
		String objectiveName = "";
		try {
			String itemSetIds = "";
			for(String itemSetId : itemSetIdList) {
				itemSetIds += itemSetId + ",";
			}
			String itemQuery = Query.ITEMS_FROM_ITEM_SET.replaceAll("#", itemSetIds.substring(0, itemSetIds.length() - 1));
			ps = con.prepareStatement(itemQuery);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				int itemSortOrder = rs.getInt("ITEM_SORT_ORDER");
				objectiveName = rs.getString("OBJECTIVE_NAME");
				objMap.put(objectiveName, rs.getInt("OBJECTIVE_ID"));
				if(objectiveCountMap.containsKey(objectiveName)) {
					int currentCount = objectiveCountMap.get(objectiveName);
					objectiveCountMap.put(objectiveName, ++currentCount);
				} else {
					objectiveCountMap.put(objectiveName, 1);
				}
				String itemId = rs.getString("ITEM_ID");
				objItemMap.put(itemId, objectiveName);
				if(itemSortOrder > 9) {
					itemSetMap.put(String.valueOf(itemSortOrder), itemId);
				} else {
					itemSetMap.put(String.valueOf("0" + itemSortOrder), itemId);
				}
			}
			pvalFileData.setObjItemCount(objectiveCountMap);
			pvalFileData.setItemObjectiveMap(objItemMap);
			pvalFileData.setObjectiveMap(objMap);
			return itemSetMap;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
