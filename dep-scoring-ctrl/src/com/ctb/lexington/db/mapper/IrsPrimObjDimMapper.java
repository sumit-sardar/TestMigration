package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ctb.lexington.db.irsdata.IrsPrimObjDimData;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author Rama_Rao
 *
 */

public class IrsPrimObjDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_PRIMOBJID = "findByPrimObjID";
	private static final String INSERT_INTO_PRIMOBJDIM = "insertPrimObjDim";
    private static final String UPDATE_BY_PRMIOBJID = "updatePrimObjDim";
    private static final String DELETE_BY_PRIMOBJID = "deletePrimObjDim";
    private static final String FIND_BY_PRIMOBJID_IN_BULK = "findByPrimObjIdInBulk";
  
	public IrsPrimObjDimMapper(Connection conn){
		super(conn);
	}
	
	public IrsPrimObjDimData findByPrimObjId(Long primObjId){
		IrsPrimObjDimData template = new IrsPrimObjDimData();
		template.setPrimObjid(primObjId);
		return (IrsPrimObjDimData) find(FIND_BY_PRIMOBJID,template);    
    }
	
	public IrsPrimObjDimData insert(IrsPrimObjDimData record)throws SQLException{
		insert(INSERT_INTO_PRIMOBJDIM, record);
	    return findByPrimObjId(record.getPrimObjid());
    }	
	
	public void update(IrsPrimObjDimData record)throws SQLException{
        update(UPDATE_BY_PRMIOBJID, record);
    }
	
	public void delete(Long primObjId)throws SQLException{
		delete(DELETE_BY_PRIMOBJID, primObjId);
	}
	
	public SqlMapClient insertBatch(IrsPrimObjDimData record, SqlMapClient sqlMap)throws SQLException{
		sqlMap = insertBatch(INSERT_INTO_PRIMOBJDIM, record, sqlMap);
		return sqlMap;
    }
	
	public SqlMapClient updateBatch(IrsPrimObjDimData record, SqlMapClient sqlMap)throws SQLException{
		if(record != null)
			sqlMap = updateBatch(UPDATE_BY_PRMIOBJID, record, sqlMap);
		return sqlMap;
    }
	
	public void executeObjectiveBatch(SqlMapClient sqlMapClient) throws SQLException{
		executeBatchProcess(sqlMapClient);
	}

	public Map findByOASItemIdAndSecObjId(List objectiveList, String keyProp) {
		HashMap map = new HashMap();
        map.put("primObjectives", objectiveList);
		return (Map) findManyInMap(FIND_BY_PRIMOBJID_IN_BULK, map, keyProp);
	}
}