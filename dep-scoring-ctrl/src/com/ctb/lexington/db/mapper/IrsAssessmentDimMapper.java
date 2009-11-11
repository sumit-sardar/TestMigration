package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsAssessmentDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsAssessmentDimMapper extends AbstractDBMapper{
	
     private static final String FIND_BY_ASSESSMENTID = "findByAssessmentDimId";
     private static final String INSERT_BY_ASSESSMENTID = "insertAssessmentDim";
     private static final String DELETE_BY_ASSESSMENTID = "deleteAssessmentDim";
     private static final String UPDATE_BY_ASSESSMENTID = "updateAssessmentDim";
     
    public IrsAssessmentDimMapper(Connection conn){
    	super(conn);
	}
    
	public IrsAssessmentDimData findByAssessmentId(Long assessmentId){
		IrsAssessmentDimData template = new IrsAssessmentDimData();
		template.setAssessmentid(assessmentId);
		return (IrsAssessmentDimData) find(FIND_BY_ASSESSMENTID,template);    
 	}
	
	public IrsAssessmentDimData insert(IrsAssessmentDimData record) throws SQLException {
	    insert(INSERT_BY_ASSESSMENTID, record);
		return findByAssessmentId(record.getAssessmentid());
	}
	
	public void update(IrsAssessmentDimData record) throws SQLException {
		update(UPDATE_BY_ASSESSMENTID, record);
	}
	
	public void deleteAssessmentDim(Long assessmentId)throws SQLException{
	    delete(DELETE_BY_ASSESSMENTID, assessmentId);
	}
}