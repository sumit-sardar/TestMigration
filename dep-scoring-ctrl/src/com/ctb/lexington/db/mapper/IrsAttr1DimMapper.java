package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsAttr1DimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr1DimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_ATTR1ID = "findByAttr1DimID";
	private static final String INSERT_BY_ATTR1ID = "insertAttr1Dim";
    private static final String UPDATE_BY_ATTR1ID = "updateAttr1Dim";
    private static final String DELETE_BY_ATTR1ID = "deleteAttr1Dim";
	
	public IrsAttr1DimMapper(Connection conn){
		super(conn);
	}
	
	public IrsAttr1DimData findByAttr1DimId(Long attr1Id){
		IrsAttr1DimData template = new IrsAttr1DimData();
		template.setAttr1Id(attr1Id);
		return (IrsAttr1DimData) find(FIND_BY_ATTR1ID,template);    
    }
	
	public IrsAttr1DimData insert(IrsAttr1DimData record)throws SQLException{
    	insert(INSERT_BY_ATTR1ID, record);
		return findByAttr1DimId(record.getAttr1Id());
	}
	
	public void update(IrsAttr1DimData record)throws SQLException{
		update(UPDATE_BY_ATTR1ID, record);
    }
	
	public void delete(Long attr1Id)throws SQLException{
		delete(DELETE_BY_ATTR1ID, attr1Id);
	}
}