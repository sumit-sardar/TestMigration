package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsRecLevelDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsRecLevelDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_RECLEVELID = "findByRecLevelId";
	private static final String INSERT_INTO_RECLEVELDIM = "insertRecLevelDim";
    private static final String DELETE_BY_RECLEVELID = "deleteRecLevelDim";
    private static final String UPDATE_BY_RECLEVELID = "updateRecLevelDim";
	
	public IrsRecLevelDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsRecLevelDimData findByRecLevelId(Long recLevelId){
		IrsRecLevelDimData template = new IrsRecLevelDimData();
		template.setRecLevelid(recLevelId);
		return (IrsRecLevelDimData) find(FIND_BY_RECLEVELID,template);    
    }
	
	public IrsRecLevelDimData insert(IrsRecLevelDimData record)throws SQLException{
    	insert(INSERT_INTO_RECLEVELDIM, record);
		return findByRecLevelId(record.getRecLevelid());
	}	
	
	public void update(IrsRecLevelDimData record)throws SQLException{
	    update(UPDATE_BY_RECLEVELID, record);
	}
	
	public void delete(Long recLevelId)throws SQLException{
		delete(DELETE_BY_RECLEVELID, recLevelId);
	}
}