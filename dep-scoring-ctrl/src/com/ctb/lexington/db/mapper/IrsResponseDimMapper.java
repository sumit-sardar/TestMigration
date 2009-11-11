package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsResponseDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsResponseDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_RESPONSEID = "findByResponseID";
	private static final String INSERT_INTO_RESPONSEDIM = "insertResponseDim";
    private static final String UPDATE_RESPONSEDIM = "updateResponseDim";
    private static final String DELETE_BY_RESPONSEID = "deleteResponseDim";
	
	public IrsResponseDimMapper(Connection conn){
	        super(conn);
	}
	
	public IrsResponseDimData findByResponseId(Long responseId){
		IrsResponseDimData template = new IrsResponseDimData();
		template.setResponseid(responseId);
		return (IrsResponseDimData) find(FIND_BY_RESPONSEID,template);    
    }
	
	public IrsResponseDimData insert(IrsResponseDimData record)throws SQLException{
    	insert(INSERT_INTO_RESPONSEDIM, record);
		return findByResponseId(record.getResponseid());
	}	
	
	public void update(IrsResponseDimData record)throws SQLException{
	    update(UPDATE_RESPONSEDIM, record);
	}
	
	public void delete(Long responseId)throws SQLException{
	      delete(DELETE_BY_RESPONSEID, responseId);
	}
}