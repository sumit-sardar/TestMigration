package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsContentAreaDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsContentAreaDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_CONTENTAREAID = "findByContentAreaID";
	private static final String INSERT_INTO_CONTENTAREADIM = "insertContentAreaDim";
    private static final String UPDATE_BY_CONTENTAREAID = "updateContentAreaDim";
    private static final String DELETE_BY_CONTENTAREAID = "deleteContentAreaDim";
  
	public IrsContentAreaDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsContentAreaDimData findByContentAreaId(Long contentAreaId){
		IrsContentAreaDimData template = new IrsContentAreaDimData();
		template.setContentAreaid(contentAreaId);
		return (IrsContentAreaDimData) find(FIND_BY_CONTENTAREAID,template);    
    }
	
	public IrsContentAreaDimData insert(IrsContentAreaDimData record)throws SQLException{
		insert(INSERT_INTO_CONTENTAREADIM, record);
		return findByContentAreaId(record.getContentAreaid());
    }	
	
	public void update(IrsContentAreaDimData record)throws SQLException{
		update(UPDATE_BY_CONTENTAREAID, record);
    }
	
	public void delete(Long contentAreaId)throws SQLException{
		delete(DELETE_BY_CONTENTAREAID, contentAreaId);
	}
}