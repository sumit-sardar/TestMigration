package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsSecObjDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsSecObjDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_SECOBJID = "findBySecObjID";
	private static final String INSERT_INTO_SECOBJDIM = "insertSecObjDim";
    private static final String UPDATE_BY_SECOBJID = "updateSecObjDim";
    private static final String DELETE_BY_SECOBJID = "deleteSecObjDim";
  
	public IrsSecObjDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsSecObjDimData findBySecObjId(Long secObjId){
		IrsSecObjDimData template = new IrsSecObjDimData();
		template.setSecObjid(secObjId);
		return (IrsSecObjDimData) find(FIND_BY_SECOBJID,template);    
    }
	
	public IrsSecObjDimData insert(IrsSecObjDimData record)throws SQLException{
		insert(INSERT_INTO_SECOBJDIM, record);
		return findBySecObjId(record.getSecObjid());
    }	
	
	public void update(IrsSecObjDimData record)throws SQLException{
        update(UPDATE_BY_SECOBJID, record);
    }
	
	public void delete(Long secObjId)throws SQLException{
		delete(DELETE_BY_SECOBJID, secObjId);
	}
}