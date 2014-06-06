package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.ctb.lexington.db.irsdata.IrsContentAreaDimData;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author Rama_Rao
 *
 */

public class IrsContentAreaDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_CONTENTAREAID = "findByContentAreaID";
	private static final String INSERT_INTO_CONTENTAREADIM = "insertContentAreaDim";
    private static final String UPDATE_BY_CONTENTAREAID = "updateContentAreaDim";
    private static final String DELETE_BY_CONTENTAREAID = "deleteContentAreaDim";
    private static final String FIND_BY_CONTENTAREAID_IN_BULK = "findByContentAreaIDInBulk";
    
	public IrsContentAreaDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsContentAreaDimData findByContentAreaId(Long contentAreaId){
		IrsContentAreaDimData template = new IrsContentAreaDimData();
		template.setContentAreaid(contentAreaId);
		return (IrsContentAreaDimData) find(FIND_BY_CONTENTAREAID,template);    
    }
	
	public IrsContentAreaDimData insert(IrsContentAreaDimData record)throws SQLException{
		insert(INSERT_INTO_CONTENTAREADIM, record);
		return findByContentAreaId(record.getContentAreaid());
    }	
	
	public void update(IrsContentAreaDimData record)throws SQLException{
		update(UPDATE_BY_CONTENTAREAID, record);
    }
	
	public void delete(Long contentAreaId)throws SQLException{
		delete(DELETE_BY_CONTENTAREAID, contentAreaId);
	}

	public Map findByContentAreaIdInBulk(List contentAreaList, String keyProp) {
		HashMap map = new HashMap();
        map.put("contentAreas", contentAreaList);
		return (Map) findManyInMap(FIND_BY_CONTENTAREAID_IN_BULK, map, keyProp);
	}

	public SqlMapClient insertBatch(IrsContentAreaDimData record, SqlMapClient sqlMap)throws SQLException{
		sqlMap = insertBatch(INSERT_INTO_CONTENTAREADIM, record, sqlMap);
		return sqlMap;
    }
	
	public SqlMapClient updateBatch(IrsContentAreaDimData record, SqlMapClient sqlMap)throws SQLException{
		if(record != null)
			sqlMap = updateBatch(UPDATE_BY_CONTENTAREAID, record, sqlMap);
		return sqlMap;
    }
	
	public void executeBatch(SqlMapClient sqlMapClient) throws SQLException{
		executeBatchProcess(sqlMapClient);
	}
}