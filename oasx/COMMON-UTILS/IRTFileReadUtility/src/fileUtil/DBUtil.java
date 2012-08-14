package fileUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
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
					//for(String grade : FileUtil._GRADES) {
						String scoreLookupId = "TERRAB3" + "_" + pvalFileData.getNormsGroup() 
						+ "_" + pvalFileData.getGrade() + "_" + pvalFileData.getContent()
						+ "_" + pvalFileData.getOther() + "_" + pvalFileData.getCodeValue().getCode()
						+ "_" + pvalFileData.getLevel();
						System.out.println("scoreLookupId ***********" + scoreLookupId);
						ps.setString(1, "SCL");
						ps.setString(2, "OPV");
						ps.setString(3, scoreLookupId);
						ps.setInt(4, 0);
						ps.setDouble(5, pvalFileData.getCodeValue().getValue());
						ps.setString(6, pvalFileData.getForm());
						ps.setString(7, pvalFileData.getLevel());
						ps.setString(8, pvalFileData.getGrade());
						ps.setString(9, FileUtil.processContentAreaName(pvalFileData.getContent()));
						ps.setString(10, FileUtil.processNongroupName(pvalFileData.getNormsGroup()));
						ps.setString(11, "2011");
						ps.setString(12, "TERRAB3");
						ps.setString(13, FileUtil.getDisplayName(FileUtil.getProductIdFromType(pvalFileData.getOther())));
						ps.addBatch();
						//ps.executeUpdate();
						
						insertScoreLookupItemSet(ps1, scoreLookupId, pvalFileData);	
					//}
				}
			}
			ps.executeBatch();
			ps1.executeBatch();
			con.commit();
			SqlUtil.close(ps);
			SqlUtil.close(ps1);
		} catch(Exception e) {
			try {
				con.rollback();
				System.out.println("Data are not saved in SCORE_LOOKUP table.");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	private static void insertScoreLookupItemSet(PreparedStatement ps, String scoreLookupId, 
						PVALFileData pvalFileData) {
		try {
			System.out.println(scoreLookupId + " - " + pvalFileData.getCodeValue().getCode() + " - " + FileUtil._OBJECTIVEMAP.get(pvalFileData.getCodeValue().getCode()) + " - " + FileUtil._OBJECTIVEMAP.get(pvalFileData.getCodeValue().getCode()));
			ps.setString(1, scoreLookupId);
			ps.setLong(2, FileUtil._OBJECTIVEMAP.get(pvalFileData.getCodeValue().getCode() + pvalFileData.getLevel()));
			ps.addBatch();
			//ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
