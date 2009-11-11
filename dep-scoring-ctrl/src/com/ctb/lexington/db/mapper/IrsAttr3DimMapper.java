package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsAttr3DimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr3DimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_ATTR3ID = "findByAttr3DimID";
	private static final String INSERT_INTO_ATTR3DIM = "insertAttr3Dim";
	private static final String UPDATE_BY_ATTR3ID = "updateAttr3Dim";
    private static final String DELETE_BY_ATTR3ID = "deleteAttr3Dim";
      
	public IrsAttr3DimMapper(Connection conn){
		super(conn);
	}
	
	public IrsAttr3DimData findByAttr3Id(Long attr3Id){
		IrsAttr3DimData template = new IrsAttr3DimData();
		template.setAttr3Id(attr3Id);
		return (IrsAttr3DimData) find(FIND_BY_ATTR3ID,template);    
    }
	
	public IrsAttr3DimData insert(IrsAttr3DimData record)throws SQLException{
		insert(INSERT_INTO_ATTR3DIM, record);
		return findByAttr3Id(record.getAttr3Id());
    }
		
	public void update(IrsAttr3DimData record)throws SQLException{
        update(UPDATE_BY_ATTR3ID, record);
    }
	
	public void delete(Long attr2Id)throws SQLException{
		delete(DELETE_BY_ATTR3ID, attr2Id);
	}
}