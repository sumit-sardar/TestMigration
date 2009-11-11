package com.ctb.lexington.domain.score.collector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ctb.lexington.db.ConnectionFactory;
import com.ctb.lexington.db.data.ScoreSummaryData;
import com.ctb.lexington.exception.DataException;
import com.ctb.lexington.util.CTBConstants;


public class StudentScoreCollector {
    
    private Connection osrConnection=null;
    
    public StudentScoreCollector(Connection osrConnection) {
        this.osrConnection = osrConnection;
    }
    
    public ScoreSummaryData collectStudentScoreData(int oasRosterId) throws DataException, SQLException {
        // get Score information from OSR for the give roster id
    	// 05/06, ncohen: there are multiple score summary rows for a given roster,
    	// reflecting sub-scores against different objectives. These must be
    	// combined to get scores for the whole test. 
        String sql = "select sss.student_test_history_id, sss.ats_archive, " +
					 "sum(sss.num_incorrect) as num_incorrect, " +
					 "sum(sss.num_unattempted) as num_unattempted, " + 
					 "sum(sss.points_possible) as points_possible, " +
					 "sum(sss.points_obtained) as points_obtained, " +
					 "round((sum(sss.points_obtained)/sum(sss.points_possible))*100) as percent_obtained, " +
					 "sum(sss.points_attempted) as points_attempted, " + 
					 "sum(sss.num_of_items) as num_of_items " +
					 "from student_score_summary sss, student_test_history sth " +
					 "where sss.student_test_history_id = sth.student_test_history_id " +
					 "and sth.test_roster_id = ? " +
					 "group by sss.student_test_history_id, sss.ats_archive";       
        
        PreparedStatement ps=null;
        ScoreSummaryData data=null;
        try{
            ps = osrConnection.prepareStatement(sql);
            ps.setInt(1, oasRosterId);
            ResultSet rs = ps.executeQuery();
            data = new ScoreSummaryData();
            if(rs.next()){
//            	// 05/06, ncohen: this value is objective-specific, removed
                //data.setReportItemSetId(rs.getInt("REPORT_ITEM_SET_ID"));
                data.setAtsArchived(rs.getString("ATS_ARCHIVE"));
//              // 05/06, ncohen: this value is objective-specific, removed
                //data.setMastered(rs.getString("MASTERED"));
                data.setNumIncorrect(rs.getBigDecimal("NUM_INCORRECT"));
                data.setNumOfItems(rs.getInt("NUM_OF_ITEMS"));
                data.setNumUnAttempted(rs.getBigDecimal("NUM_UNATTEMPTED"));
                data.setPercentObtained(rs.getBigDecimal("PERCENT_OBTAINED"));
                data.setPointsAttempted(rs.getBigDecimal("POINTS_ATTEMPTED"));
                data.setPointsObtained(rs.getBigDecimal("POINTS_OBTAINED"));
                data.setPointsPossible(rs.getBigDecimal("POINTS_POSSIBLE"));
//              //05/06, ncohen: this value is objective-specific, removed
                //data.setReportItemSetName(rs.getString("REPORT_ITEM_SET_NAME"));
                data.setStudentTestHistoryId(rs.getInt("STUDENT_TEST_HISTORY_ID"));
                ps.close();
            }
            else{
                throw new DataException("Score Summary record for roster: " +oasRosterId +" does not exist in OSR");
            }
            
            String scaleScoreSql = "select sss.score_value from student_test_history sth, " +
            "student_subtest_scores sss, score_type st where test_roster_id=? " +
            "and sth.student_test_history_id=sss.student_test_history_id and " +
            "sss.score_type_code=st.score_type_code and st.score_type_code='SCL'";

            //now get the scale score value
            ps = osrConnection.prepareStatement(scaleScoreSql);
            ps.setInt(1, oasRosterId);
            rs = ps.executeQuery();
            if(rs.next()){
                data.setScaleScore(rs.getBigDecimal(1));
            }else{
                data.setScaleScore(CTBConstants.SCALESCORENOTPRESENT);
            }
            
        }finally{
            ConnectionFactory.getInstance().release(ps);
        }
        return data;
    }
}
