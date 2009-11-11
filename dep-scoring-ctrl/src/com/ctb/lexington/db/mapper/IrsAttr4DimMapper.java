package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsAttr4DimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsAttr4DimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_ATTR4ID = "findByAttr4DimID";
	private static final String INSERT_INTO_ATTR4DIM = "insertAttr4Dim";
	private static final String UPDATE_BY_ATTR4ID = "updateAttr4Dim";
    private static final String DELETE_BY_ATTR4ID = "deleteAttr4Dim";
  
	public IrsAttr4DimMapper(Connection conn){
	    super(conn);
	}
	
	public IrsAttr4DimData findByAttr4Id(Long attr4Id){
		IrsAttr4DimData template = new IrsAttr4DimData();
		template.setAttr4Id(attr4Id);
		return (IrsAttr4DimData) find(FIND_BY_ATTR4ID,template);    
    }
	
	public IrsAttr4DimData insert(IrsAttr4DimData record)throws SQLException{
	    insert(INSERT_INTO_ATTR4DIM, record);
		return findByAttr4Id(record.getAttr4Id());
    }
	
	public void update(IrsAttr4DimData record)throws SQLException{
        update(UPDATE_BY_ATTR4ID, record);
    }
	
	public void delete(Long attr2Id)throws SQLException{
	    delete(DELETE_BY_ATTR4ID, attr2Id);
	}
}