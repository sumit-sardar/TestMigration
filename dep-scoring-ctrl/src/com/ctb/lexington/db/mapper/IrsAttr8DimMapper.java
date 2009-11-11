package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsAttr8DimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr8DimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_ATTR8ID = "findByAttr8DimID";
	private static final String INSERT_INTO_ATTR8DIM = "insertAttr8Dim";
	private static final String UPDATE_BY_ATTR8ID = "updateAttr8Dim";
    private static final String DELETE_BY_ATTR8ID = "deleteAttr8Dim";
      
	public IrsAttr8DimMapper(Connection conn){
		super(conn);
	}
	
	public IrsAttr8DimData findByAttr8Id(Long attr8Id){
		IrsAttr8DimData template = new IrsAttr8DimData();
		template.setAttr8Id(attr8Id);
		return (IrsAttr8DimData) find(FIND_BY_ATTR8ID,template);    
    }
	
	public IrsAttr8DimData insert(IrsAttr8DimData record)throws SQLException{
		insert(INSERT_INTO_ATTR8DIM, record);
		return findByAttr8Id(record.getAttr8Id());
    }
	
	public void update(IrsAttr8DimData record)throws SQLException{
        update(UPDATE_BY_ATTR8ID, record);
    }  
	
	public void delete(Long attr2Id)throws SQLException{
		delete(DELETE_BY_ATTR8ID, attr2Id);
	}
}