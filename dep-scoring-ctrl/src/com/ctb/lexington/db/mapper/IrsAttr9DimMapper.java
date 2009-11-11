package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsAttr9DimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr9DimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_ATTR9ID = "findByAttr9DimID";
	private static final String INSERT_INTO_ATTR9DIM = "insertAttr9Dim";
	private static final String UPDATE_BY_ATTR9ID = "updateAttr9Dim";
    private static final String DELETE_BY_ATTR9ID = "deleteAttr9Dim";
  
	public IrsAttr9DimMapper(Connection conn){
		super(conn);
	}
	
	public IrsAttr9DimData findByAttr9Id(Long attr9Id){
		IrsAttr9DimData template = new IrsAttr9DimData();
		template.setAttr9Id(attr9Id);
		return (IrsAttr9DimData) find(FIND_BY_ATTR9ID,template);    
    }
	
	public IrsAttr9DimData insert(IrsAttr9DimData record)throws SQLException{
		insert(INSERT_INTO_ATTR9DIM, record);
		return findByAttr9Id(record.getAttr9Id());
    }	
	  
	public void update(IrsAttr9DimData record)throws SQLException{
        update(UPDATE_BY_ATTR9ID, record);
    }
	
	public void delete(Long attr2Id)throws SQLException{
		delete(DELETE_BY_ATTR9ID, attr2Id);
	}
}