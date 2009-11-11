package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsAttr5DimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr5DimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_ATTR5ID = "findByAttr5DimID";
	private static final String INSERT_INTO_ATTR5DIM = "insertAttr5Dim";
	private static final String UPDATE_BY_ATTR5ID = "updateAttr5Dim";
    private static final String DELETE_BY_ATTR5ID = "deleteAttr5Dim";
    
	public IrsAttr5DimMapper(Connection conn){
		super(conn);
	}
	
	public IrsAttr5DimData findByAttr5Id(Long attr5Id){
		IrsAttr5DimData template = new IrsAttr5DimData();
		template.setAttr5Id(attr5Id);
		return (IrsAttr5DimData) find(FIND_BY_ATTR5ID,template);    
    }
	
	public IrsAttr5DimData insert(IrsAttr5DimData record)throws SQLException{
	    insert(INSERT_INTO_ATTR5DIM, record);
	    return findByAttr5Id(record.getAttr5Id());
    }
	
	public void update(IrsAttr5DimData record)throws SQLException{
        update(UPDATE_BY_ATTR5ID, record);
    }
	
	public void delete(Long attr2Id)throws SQLException{
		delete(DELETE_BY_ATTR5ID, attr2Id);
	}
}