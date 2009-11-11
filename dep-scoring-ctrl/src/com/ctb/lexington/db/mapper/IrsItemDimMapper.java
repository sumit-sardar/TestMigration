package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;
import com.ctb.lexington.db.irsdata.IrsItemDimData;

/**
 * @author Rama_Rao
 *
 */

public class IrsItemDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_OAS_ITEMID = "findByOasItemID";
	private static final String INSERT_INTO_ITEMDIM = "insertItemDim";
    private static final String UPDATE_BY_ITEMID = "updateItemDim";
    private static final String DELETE_BY_ITEMID = "deleteItemDim";
  
	public IrsItemDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsItemDimData findByOASItemIdAndSecObjId(String oasItemId, Long secObjid){
		IrsItemDimData template = new IrsItemDimData();
		template.setOasItemid(oasItemId);
        template.setSecObjid(secObjid);
		return (IrsItemDimData) find(FIND_BY_OAS_ITEMID,template);    
    }
	
	public IrsItemDimData insert(IrsItemDimData record)throws SQLException{
		insert(INSERT_INTO_ITEMDIM, record);
		return findByOASItemIdAndSecObjId(record.getOasItemid(), record.getSecObjid());
    }	
	
	public void update(IrsItemDimData record)throws SQLException{
        update(UPDATE_BY_ITEMID, record);
    }
	
	public void delete(String oasItemId)throws SQLException{
		delete(DELETE_BY_ITEMID, oasItemId);
	}
}