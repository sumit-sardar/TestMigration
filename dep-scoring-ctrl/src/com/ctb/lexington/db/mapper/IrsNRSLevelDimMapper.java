package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsNRSLevelDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsNRSLevelDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_NRSLEVELID = "findByNRSLevelId";
	private static final String INSERT_INTO_NRSLEVELDIM = "insertNRSLevelDim";
	private static final String UPDATE_BY_NRSLEVELID = "updateNRSLevelDim";
    private static final String DELETE_BY_NRSLEVELID = "deleteNRSLevelDim";
    
	public IrsNRSLevelDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsNRSLevelDimData findByNRSLevelId(Long nrsLevelId){
		IrsNRSLevelDimData template = new IrsNRSLevelDimData();
		template.setNrsLevelid(nrsLevelId);
		return (IrsNRSLevelDimData) find(FIND_BY_NRSLEVELID,template);    
    }
	
	public IrsNRSLevelDimData insert(IrsNRSLevelDimData record)throws SQLException{
	    insert(INSERT_INTO_NRSLEVELDIM, record);
		return findByNRSLevelId(record.getNrsLevelid());
    }	
	
	public void update(IrsNRSLevelDimData record)throws SQLException{
        update(UPDATE_BY_NRSLEVELID, record);
    }
	
	public void delete(Long nrsLevelId)throws SQLException{
		delete(DELETE_BY_NRSLEVELID, nrsLevelId);
	}
}