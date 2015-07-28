package com.ctb.lexington.db.mapper.tsmapper;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.irstsdata.IrsTASCItemFactData;
import com.ctb.lexington.db.mapper.AbstractDBMapper;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author Rama_Rao
 *
 */

public class IrsTASCItemFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_ITEM_STUDENT_SESSION = "TSfindByItemStudentSession";
	private static final String INSERT_INTO_TASCITEMFACT = "TSinsertTASCItemFact";
	private static final String UPDATE_BY_FACTID = "TSupdateTABEItemFact";
	private static final String DELETE_BY_FACTID = "TSdeleteByItemFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TSupdateTASCItemFactCurrentResult";
    private static final String MERGE_FT_INTO_TASCITEMFACT = "TSmergeFieldTestItemFact";
    
    public IrsTASCItemFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTASCItemFactData findByItemStudentSession(Long itemId, Long studentId, Long sessionId){
		IrsTASCItemFactData template = new IrsTASCItemFactData();
		template.setItemid(itemId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTASCItemFactData) find(FIND_BY_ITEM_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsTASCItemFactData record)throws SQLException{
		insert(INSERT_INTO_TASCITEMFACT, record);
    }	
	
	public void update(IrsTASCItemFactData record)throws SQLException{
		update(UPDATE_BY_FACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsTASCItemFactData record)throws SQLException{
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
	
	public void delete(IrsTASCItemFactData record)throws SQLException{
		if(record != null)	
			delete(DELETE_BY_FACTID, record);	
    }
	
	public SqlMapClient insertBatch(IrsTASCItemFactData record, SqlMapClient sqlMap)throws SQLException{
		sqlMap = insertBatch(INSERT_INTO_TASCITEMFACT, record, sqlMap);
		return sqlMap;
    }
	
	public SqlMapClient updateFTBatch(IrsTASCItemFactData record, SqlMapClient sqlMap)throws SQLException{
		sqlMap = updateBatch(MERGE_FT_INTO_TASCITEMFACT, record, sqlMap);
		return sqlMap;
    }
	
	
	public SqlMapClient deleteBatch(IrsTASCItemFactData record, SqlMapClient sqlMap)throws SQLException{
		if(record != null)
			sqlMap = deleteBatch(DELETE_BY_FACTID, record, sqlMap);
		return sqlMap;
    }
	
	public void executeItemBatch(SqlMapClient sqlClient) throws SQLException{
		executeBatchProcess(sqlClient);
	}
}