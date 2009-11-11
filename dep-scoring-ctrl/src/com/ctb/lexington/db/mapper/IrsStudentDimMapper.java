package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsStudentDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsStudentDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_STUDENTID = "findByStudentID";
	private static final String INSERT_INTO_STUDENTDIM = "insertStudentDim";
    private static final String DELETE_BY_STUDENTID = "deleteStudentDim";
    private static final String UPDATE_BY_STUDENTID = "updateStudentDim";
  
	public IrsStudentDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsStudentDimData findByStudentId(Long studentId){
		IrsStudentDimData template = new IrsStudentDimData();
		template.setStudentid(studentId);
		return (IrsStudentDimData) find(FIND_BY_STUDENTID,template);    
    }
	
	public IrsStudentDimData insert(IrsStudentDimData record)throws SQLException{
		insert(INSERT_INTO_STUDENTDIM, record);
		return findByStudentId(record.getStudentid());
    }	
	
	public void update(IrsStudentDimData record)throws SQLException{
        update(UPDATE_BY_STUDENTID, record);
    }
	
	public void delete(Long studentId)throws SQLException{
		delete(DELETE_BY_STUDENTID, studentId);
	}
}