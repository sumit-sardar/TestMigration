package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ctb.lexington.db.irsdata.IrsItemDimData;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author Rama_Rao
 *
 */

public class IrsItemDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_OAS_ITEMID = "findByOasItemID";
	private static final String INSERT_INTO_ITEMDIM = "insertItemDim";
    private static final String UPDATE_BY_ITEMID = "updateItemDim";
    private static final String DELETE_BY_ITEMID = "deleteItemDim";
    private static final String FIND_OAS_ITEMID_IN_BULK = "findOasItemIDInBulk";
  
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
	
	public SqlMapClient insertBatch(IrsItemDimData record, SqlMapClient sqlMap)throws SQLException{
		sqlMap = insertBatch(INSERT_INTO_ITEMDIM, record, sqlMap);
		return sqlMap;
    }
	
	public SqlMapClient updateBatch(IrsItemDimData record, SqlMapClient sqlMap)throws SQLException{
		if(record != null)
			sqlMap = updateBatch(INSERT_INTO_ITEMDIM, record, sqlMap);
		return sqlMap;
    }
	
	public Map findByOASItemIdAndSecObjId(List itemArrList, String keyProp){
		HashMap map = new HashMap();
        map.put("items", itemArrList);
		return (Map) findManyInMap(FIND_OAS_ITEMID_IN_BULK, map, keyProp);
    }
	
	public void executeItemBatch(SqlMapClient sqlClient) throws SQLException{
		executeBatchProcess(sqlClient);
	}
}