package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsCompositeDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsCompositeDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_COMPOSITEDIMID = "findByCompositeID";
	private static final String INSERT_INTO_COMPOSITEDIM = "insertCompositeDim";
    private static final String UPDATE_BY_COMPOSITEDIMID = "updateCompositeDim";
    private static final String DELETE_BY_COMPOSITEDIMID = "deleteCompositeDim";
  
	public IrsCompositeDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsCompositeDimData findByCompositeDimId(Long compositeDimId){
		IrsCompositeDimData template = new IrsCompositeDimData();
		template.setCompositeid(compositeDimId);
		return (IrsCompositeDimData) find(FIND_BY_COMPOSITEDIMID,template);    
    }
	
	public IrsCompositeDimData insert(IrsCompositeDimData record)throws SQLException{
		insert(INSERT_INTO_COMPOSITEDIM, record);
		return findByCompositeDimId(record.getCompositeid());
    }	
	
	public void update(IrsCompositeDimData record)throws SQLException{
        update(UPDATE_BY_COMPOSITEDIMID, record);
    }
	
	public void delete(Long compositeDimId)throws SQLException{
		delete(DELETE_BY_COMPOSITEDIMID, compositeDimId);
	}
}