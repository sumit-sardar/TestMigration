package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsScoreTypeDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsScoreTypeDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_SCORETYPEID = "findByScoreTypeId";
	private static final String INSERT_INTO_SCORETYPEDIM = "insertScoreTypeDim";
	private static final String UPDATE_SCORETYPEDIM = "updateScoreTypeDim";
    private static final String DELETE_BY_SCORETYPEID = "deleteScoreTypeDim";
	
	public IrsScoreTypeDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsScoreTypeDimData findByScoreTypeId(Long scoreTypeId){
		IrsScoreTypeDimData template = new IrsScoreTypeDimData();
		template.setScoreTypeid(scoreTypeId);
		return (IrsScoreTypeDimData) find(FIND_BY_SCORETYPEID,template);    
    }
	
	public IrsScoreTypeDimData insert(IrsScoreTypeDimData record)throws SQLException{
    	insert(INSERT_INTO_SCORETYPEDIM, record);
		return findByScoreTypeId(record.getScoreTypeid());
	}	
	
	public void update(IrsScoreTypeDimData record)throws SQLException{
		update(UPDATE_SCORETYPEDIM, record);
	}
	
	public void delete(Long scoreTypeId)throws SQLException{
		delete(DELETE_BY_SCORETYPEID, scoreTypeId);
	}
}