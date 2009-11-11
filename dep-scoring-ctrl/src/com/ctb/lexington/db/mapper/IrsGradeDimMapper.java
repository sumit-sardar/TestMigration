package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsGradeDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsGradeDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_GRADEID = "findByGradeID";
	private static final String INSERT_INTO_GRADEDIM = "insertGradeDim";
	private static final String UPDATE_BY_GRADEID = "updateGradeDim";
    private static final String DELETE_BY_GRADEID = "deleteGradeDim";
    
	public IrsGradeDimMapper(Connection conn){
	    super(conn);
	}
	
	public IrsGradeDimData findByGradeId(Long gradeId){
		IrsGradeDimData template = new IrsGradeDimData();
		template.setGradeid(gradeId);
		return (IrsGradeDimData) find(FIND_BY_GRADEID,template);    
    }
	
	public IrsGradeDimData insert(IrsGradeDimData record)throws SQLException{
		insert(INSERT_INTO_GRADEDIM, record);
		return findByGradeId(record.getGradeid());
    }
	
	public void update(IrsGradeDimData record)throws SQLException{
        update(UPDATE_BY_GRADEID, record);
    }
	
	public void delete(Long gradeId)throws SQLException{
		delete(DELETE_BY_GRADEID, gradeId);
	}
}