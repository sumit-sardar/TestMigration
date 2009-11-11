package com.ctb.lexington.db.mapper;

import java.sql.Connection;
import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsAttr2DimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr2DimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_ATTR2ID = "findByAttr2DimID";
	private static final String INSERT_INTO_ATTR2DIM = "insertAttr2Dim";
    private static final String UPDATE_BY_ATTR2ID = "updateAttr2Dim";
    private static final String DELETE_BY_ATTR2ID = "deleteAttr2Dim";
	
	public IrsAttr2DimMapper(Connection conn){
		super(conn);
	}
	
	public IrsAttr2DimData findByAttr2DimId(Long attr2Id){
		IrsAttr2DimData template = new IrsAttr2DimData();
		template.setAttr2Id(attr2Id);
		return (IrsAttr2DimData) find(FIND_BY_ATTR2ID,template);    
    }
	
	public IrsAttr2DimData insert(IrsAttr2DimData record)throws SQLException{
    	insert(INSERT_INTO_ATTR2DIM, record);
		return findByAttr2DimId(record.getAttr2Id());
	}
	
	public void update(IrsAttr2DimData record)throws SQLException{
        update(UPDATE_BY_ATTR2ID, record);
    }
	
	public void delete(Long attr2Id)throws SQLException{
		delete(DELETE_BY_ATTR2ID, attr2Id);
	}	
}