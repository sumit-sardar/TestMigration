package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsSessionDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsSessionDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_SESSIONID = "findBySessionId";
	private static final String INSERT_INTO_SESSIONDIM = "insertSessionDim";
	private static final String UPDATE_SESSIONDIM = "updateSessionDim";
    private static final String DELETE_BY_SESSIONID = "deleteSessionDim";
	
	public IrsSessionDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsSessionDimData findBySessionId(Long sessionId){
		IrsSessionDimData template = new IrsSessionDimData();
		template.setSessionid(sessionId);
		return (IrsSessionDimData) find(FIND_BY_SESSIONID,template);    
    }
	
	public IrsSessionDimData insert(IrsSessionDimData record)throws SQLException{
    	insert(INSERT_INTO_SESSIONDIM, record);
		return findBySessionId(record.getSessionid());
	}	
	
	public void update(IrsSessionDimData record)throws SQLException{
		update(UPDATE_SESSIONDIM, record);
	}
	
	public void delete(Long sessionId)throws SQLException{
		delete(DELETE_BY_SESSIONID, sessionId);
	}
}