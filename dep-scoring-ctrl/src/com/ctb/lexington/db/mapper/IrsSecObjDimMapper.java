package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.ctb.lexington.db.irsdata.IrsSecObjDimData;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author Rama_Rao
 *
 */

public class IrsSecObjDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_SECOBJID = "findBySecObjID";
	private static final String INSERT_INTO_SECOBJDIM = "insertSecObjDim";
    private static final String UPDATE_BY_SECOBJID = "updateSecObjDim";
    private static final String DELETE_BY_SECOBJID = "deleteSecObjDim";
    private static final String UPDATE_BY_SECOBJID_ASSESSMENT = "updateSecObjDimAssessment";
    private static final String FIND_BY_SECOBJID_IN_BULK = "findBySecObjIdInBulk";
  
	public IrsSecObjDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsSecObjDimData findBySecObjId(Long secObjId){
		IrsSecObjDimData template = new IrsSecObjDimData();
		template.setSecObjid(secObjId);
		return (IrsSecObjDimData) find(FIND_BY_SECOBJID,template);    
    }
	
	public IrsSecObjDimData insert(IrsSecObjDimData record)throws SQLException{
		insert(INSERT_INTO_SECOBJDIM, record);
		return findBySecObjId(record.getSecObjid());
    }	
	
	public void update(IrsSecObjDimData record)throws SQLException{
        update(UPDATE_BY_SECOBJID, record);
    }
	
	public void delete(Long secObjId)throws SQLException{
		delete(DELETE_BY_SECOBJID, secObjId);
	}
	
	public void updateSec(IrsSecObjDimData record)throws SQLException{
        update(UPDATE_BY_SECOBJID_ASSESSMENT, record);
    }
	
	public SqlMapClient insertBatch(IrsSecObjDimData record, SqlMapClient sqlMap)throws SQLException{
		sqlMap = insertBatch(INSERT_INTO_SECOBJDIM, record, sqlMap);
		return sqlMap;
    }
	
	public SqlMapClient updateBatch(IrsSecObjDimData record, SqlMapClient sqlMap)throws SQLException{
		if(record != null)
			sqlMap = updateBatch(UPDATE_BY_SECOBJID, record, sqlMap);
		return sqlMap;
    }
	
	public void executeObjectiveBatch(SqlMapClient sqlMapClient) throws SQLException{
		executeBatchProcess(sqlMapClient);
	}

	public Map findByOASItemIdAndSecObjId(List objectiveList, String keyProp) {
		HashMap map = new HashMap();
        map.put("objectives", objectiveList);
		return (Map) findManyInMap(FIND_BY_SECOBJID_IN_BULK, map, keyProp);
	}
	
}