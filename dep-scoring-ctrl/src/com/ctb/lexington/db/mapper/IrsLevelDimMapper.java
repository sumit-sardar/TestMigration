package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsLevelDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsLevelDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_LEVELID = "findByLevelDimID";
	private static final String INSERT_INTO_LEVELID = "insertLevelDim";
    private static final String DELETE_BY_LEVELID = "deleteLevelDim";
    private static final String UPDATE_BY_LEVELID = "updateLevelDim";
	
	public IrsLevelDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsLevelDimData findByLevelId(Long levelId){
		IrsLevelDimData template = new IrsLevelDimData();
		template.setLevelid(levelId);
		return (IrsLevelDimData) find(FIND_BY_LEVELID,template);    
    }
	
	public IrsLevelDimData insert(IrsLevelDimData record)throws SQLException{
    	insert(INSERT_INTO_LEVELID, record);
		return findByLevelId(record.getLevelid());
	}	
	
	public void update(IrsLevelDimData record)throws SQLException{
	    update(UPDATE_BY_LEVELID, record);
	}
	
	public void delete(Long levelId)throws SQLException{
	      delete(DELETE_BY_LEVELID, levelId);
	}
}