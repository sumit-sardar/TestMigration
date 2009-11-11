package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsAttr6DimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr6DimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_ATTR6ID = "findByAttr6DimID";
	private static final String INSERT_INTO_ATTR6DIM = "insertAttr6Dim";
	private static final String UPDATE_BY_ATTR6ID = "updateAttr6Dim";
    private static final String DELETE_BY_ATTR6ID = "deleteAttr6Dim";
  
	public IrsAttr6DimMapper(Connection conn){
	    super(conn);
	}
	
	public IrsAttr6DimData findByAttr6Id(Long attr6Id){
		IrsAttr6DimData template = new IrsAttr6DimData();
		template.setAttr6Id(attr6Id);
		return (IrsAttr6DimData) find(FIND_BY_ATTR6ID,template);    
    }
	
	public IrsAttr6DimData insert(IrsAttr6DimData record)throws SQLException{
	    insert(INSERT_INTO_ATTR6DIM, record);
		return findByAttr6Id(record.getAttr6Id());
    }
	
	public void update(IrsAttr6DimData record)throws SQLException{
        update(UPDATE_BY_ATTR6ID, record);
    }
	
	public void delete(Long attr2Id)throws SQLException{
		delete(DELETE_BY_ATTR6ID, attr2Id);
	}
}