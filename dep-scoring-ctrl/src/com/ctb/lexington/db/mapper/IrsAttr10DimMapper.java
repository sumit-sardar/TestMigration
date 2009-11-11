package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsAttr10DimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr10DimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_ATTR10ID = "findByAttr10DimID";
	private static final String INSERT_BY_ATTR10ID = "insertAttr10Dim";
	private static final String UPDATE_BY_ATTR10ID = "updateAttr10Dim";
    private static final String DELETE_BY_ATTR10ID = "deleteAttr10Dim";
  
	public IrsAttr10DimMapper(Connection conn){
		super(conn);
	}
	
	public IrsAttr10DimData findByAttr10Id(Long attr10Id){
		IrsAttr10DimData template = new IrsAttr10DimData();
		template.setAttr10Id(attr10Id);
		return (IrsAttr10DimData) find(FIND_BY_ATTR10ID,template);    
    }
	
	public IrsAttr10DimData insert(IrsAttr10DimData record) throws SQLException{
		insert(INSERT_BY_ATTR10ID, record);
		return findByAttr10Id(record.getAttr10Id());
    }
	
	public void update(IrsAttr10DimData record) throws SQLException {
        update(UPDATE_BY_ATTR10ID, record);
    }
	
	public void delete(Long attr2Id)throws SQLException{
		delete(DELETE_BY_ATTR10ID, attr2Id);
	}
}