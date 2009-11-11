package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsAttr7DimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr7DimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_ATTR7ID = "findByAttr7DimID";
	private static final String INSERT_INTO_ATTR7DIM = "insertAttr7Dim";
	private static final String UPDATE_BY_ATTR7ID = "updateAttr7Dim";
    private static final String DELETE_BY_ATTR7ID = "deleteAttr7Dim";
  
	public IrsAttr7DimMapper(Connection conn){
		super(conn);
	}
	
	public IrsAttr7DimData findByAttr7Id(Long attr7Id){
		IrsAttr7DimData template = new IrsAttr7DimData();
		template.setAttr7Id(attr7Id);
		return (IrsAttr7DimData) find(FIND_BY_ATTR7ID,template);    
    }
	
	public IrsAttr7DimData insert(IrsAttr7DimData record)throws SQLException{
		insert(INSERT_INTO_ATTR7DIM, record);
		return findByAttr7Id(record.getAttr7Id());
    }

	public void update(IrsAttr7DimData record)throws SQLException{
        update(UPDATE_BY_ATTR7ID, record);
    }
	  
	public void delete(Long attr2Id)throws SQLException{
		delete(DELETE_BY_ATTR7ID, attr2Id);
	}
}