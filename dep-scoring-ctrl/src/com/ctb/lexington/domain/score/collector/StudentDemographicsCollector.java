package com.ctb.lexington.domain.score.collector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ctb.lexington.db.ConnectionFactory;
import com.ctb.lexington.db.data.StudentDemographicData;
import com.ctb.lexington.util.SQLUtil;

/**
 * @author rama_rao
 *
 */

public class StudentDemographicsCollector{
    private final Connection conn;
    private String gender;
    
    public StudentDemographicsCollector(Connection conn){
        this.conn = conn;
    }

    public StudentDemographicData collectDemographicData(Long oasRosterId) throws SQLException{
    	
    	final StudentDemographicData data = new StudentDemographicData();
        PreparedStatement ps = null;
        ResultSet rs = null;
        final String researchSql ="select distinct student.gender, decode(sign(cd.CUSTOMER_DEMOGRAPHIC_ID - sdd.CUSTOMER_DEMOGRAPHIC_ID), 0, cd.label_code, null) as label_code, sdd.value_name " +
            "from customer_demographic cd, student_demographic_data sdd, student, test_roster " +
            "where student.student_id=test_roster.student_id " +
		 	"and cd.customer_id (+) = test_roster.customer_id " +
            "and sdd.student_id (+) = test_roster.student_id " +
            "and test_roster.test_roster_id = ? " +
			"order by label_code"; 
      
        final String accommodationSql = "select sa.screen_magnifier, sa.screen_reader, sa.CALCULATOR, sa.TEST_PAUSE, " +
        	"sa.UNTIMED_TEST, sa.QUESTION_BACKGROUND_COLOR, sa.QUESTION_FONT_COLOR, sa.QUESTION_FONT_SIZE, sa.MUSIC_FILE_ID, sa.MASKING_RULER, sa.MAGNIFYING_GLASS " +
        	"from student_accommodation sa, test_roster tr " +
        	"where sa.STUDENT_ID = tr.STUDENT_ID and tr.TEST_ROSTER_ID =?";

        Map researchMap = new HashMap(); // takes null values?
        try{
	        ps = conn.prepareStatement(researchSql);
	        ps.setLong(1, oasRosterId.longValue());
	        rs = ps.executeQuery();
	        while (rs.next()) {
                if(SQLUtil.getString(rs, "label_code") != null) {
                	if(researchMap.containsKey(SQLUtil.getString(rs, "label_code"))){
                		ArrayList<String> list =  (ArrayList<String>)researchMap.get(SQLUtil.getString(rs, "label_code"));
                		list.add(SQLUtil.getString(rs, "value_name"));
                		researchMap.put(SQLUtil.getString(rs, "label_code"), list);
	                }else {
	                	ArrayList<String> Value = new ArrayList<String>();
	                	Value.add(SQLUtil.getString(rs, "value_name"));
	                	researchMap.put(SQLUtil.getString(rs, "label_code"), Value);
	                    //researchMap.put(SQLUtil.getString(rs, "label_code"), SQLUtil.getString(rs, "value_name"));
	                }
                }
                gender = SQLUtil.getString(rs,"gender");
	        }
	        data.setResearchData(researchMap);
            data.setGender(gender);
        }finally{
            SQLUtil.close(rs);
            ConnectionFactory.getInstance().release(ps);
        }
        
        try{
        	ps = conn.prepareStatement(accommodationSql);
        	ps.setLong(1, oasRosterId.longValue());
        	rs = ps.executeQuery();
        	while(rs.next()){
        		data.setCalculator(SQLUtil.getString(rs, "CALCULATOR"));
        		data.setScreenMagnifier(SQLUtil.getString(rs, "SCREEN_MAGNIFIER"));
        		data.setScreenReader(SQLUtil.getString(rs, "SCREEN_READER"));
        		data.setTestPause(SQLUtil.getString(rs, "TEST_PAUSE"));
        		data.setUntimedTest(SQLUtil.getString(rs, "UNTIMED_TEST"));
        		data.setQuestionBGColor(SQLUtil.getString(rs, "QUESTION_BACKGROUND_COLOR"));
        		data.setQuestionFontColor(SQLUtil.getString(rs, "QUESTION_FONT_COLOR"));
        		data.setQuestionFontSize(SQLUtil.getString(rs, "QUESTION_FONT_SIZE"));
        		data.setMusicFileId(SQLUtil.getString(rs, "MUSIC_FILE_ID")); // Added for Laslink
        		data.setMaskingRuler(SQLUtil.getString(rs, "MASKING_RULER")); // Added for Laslink
        		data.setMagnifyingGlass(SQLUtil.getString(rs, "MAGNIFYING_GLASS")); // Added for Laslink
        		}
        }finally {
            SQLUtil.close(rs);
            ConnectionFactory.getInstance().release(ps);
        }
        return data;
    }
    
    
}