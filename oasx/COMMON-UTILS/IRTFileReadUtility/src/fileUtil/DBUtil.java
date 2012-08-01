package fileUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DBUtil {

	private static Connection con = null;
	
	public static void insertScoreLookup(List<PVALFileData> fileDataList) {
		
		try {
			PreparedStatement ps = null;
			PreparedStatement ps1 = null;
			con = SqlUtil.openOASDBcon();
			ps = con.prepareStatement(Query.insertScoreLookup);
			ps1 = con.prepareStatement(Query.insertScoreLookupItemSet);
			for (PVALFileData pvalFileData : fileDataList) {
				if(pvalFileData != null) {
					String scoreLookupId = "TERRAB3" + "_" + pvalFileData.getNonGroup() 
					+ "_" + pvalFileData.getGrade() + "_" + pvalFileData.getContent();
					ps.setString(1, "SCL");
					ps.setString(2, "OPV");
					ps.setString(3, scoreLookupId);
					ps.setInt(4, 0);
					ps.setDouble(5, pvalFileData.getCodeValue().getValue());
					ps.setString(6, pvalFileData.getForm());
					ps.setString(7, pvalFileData.getLevel());
					ps.setString(8, pvalFileData.getGrade());
					ps.setString(9, FileUtil.processContentAreaName(pvalFileData.getContent()));
					ps.setString(10, FileUtil.processNongroupName(pvalFileData.getNonGroup()));
					ps.setString(11, "2011");
					ps.setString(12, "TERRAB3");
					ps.setString(13, FileUtil.getDisplayName(FileUtil.getProductIdFromType(pvalFileData.getOther())));
					ps.addBatch();
					
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
			ps = con.prepareStatement(Query.insertScoreLookupItemSet);
			ps.setString(1, scoreLookupId);
			ps.setLong(2, FileUtil._OBJECTIVEMAP.get(pvalFileData.getCodeValue().getCode()));
			ps.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
