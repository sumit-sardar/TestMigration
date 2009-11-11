package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsRecActivityDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsRecActivityDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_RECACTIVITYID = "findByRecActivityId";
	private static final String INSERT_INTO_RECACTIVITYDIM = "insertRecActivityDim";
	private static final String UPDATE_BY_RECACTIVITYID = "updateRecActivityDim";
	private static final String DELETE_BY_RECACTIVITYID = "deleteRecActivityDim";
    
	public IrsRecActivityDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsRecActivityDimData findByRecActivityId(Long recActivityId){
		IrsRecActivityDimData template = new IrsRecActivityDimData();
		template.setRecActivityid(recActivityId);
		return (IrsRecActivityDimData) find(FIND_BY_RECACTIVITYID,template);    
    }
	
	public IrsRecActivityDimData insert(IrsRecActivityDimData record)throws SQLException{
    	insert(INSERT_INTO_RECACTIVITYDIM, record);
		return findByRecActivityId(record.getRecActivityid());
	}	
	
	public void update(IrsRecActivityDimData record)throws SQLException{
	    update(UPDATE_BY_RECACTIVITYID, record);
	}
	
	public void delete(Long recActivityId)throws SQLException{
		delete(DELETE_BY_RECACTIVITYID, recActivityId);
	}
}