package com.ctb.lexington.db.mapper.trmapper;

import com.ctb.lexington.db.irsdata.irstrdata.IrsTRItemFactData;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.mapper.AbstractDBMapper;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author Somenath_Chakroborti
 *
 */

public class IrsTRItemFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_ITEM_STUDENT_SESSION = "TRfindByItemStudentSession";
	private static final String INSERT_INTO_ITEMFACT = "TRinsertItemFact";
	private static final String UPDATE_BY_FACTID = "TRupdateItemFact";
	private static final String DELETE_BY_FACTID = "TRdeleteByItemFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TRupdateItemFactCurrentResult";
    private static final String MERGE_FT_INTO_ITEMFACT = "TRmergeFieldTestItemFact";
    
    public IrsTRItemFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTRItemFactData findByItemStudentSession(Long itemId, Long studentId, Long sessionId){
		IrsTRItemFactData template = new IrsTRItemFactData();
		template.setItemid(itemId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTRItemFactData) find(FIND_BY_ITEM_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsTRItemFactData record)throws SQLException{
		insert(INSERT_INTO_ITEMFACT, record);
    }	
	
	public void update(IrsTRItemFactData record)throws SQLException{
		update(UPDATE_BY_FACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsTRItemFactData record)throws SQLException{
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
	
	public void delete(IrsTRItemFactData record)throws SQLException{
		if(record != null)	
			delete(DELETE_BY_FACTID, record);	
    }
	
	public SqlMapClient insertBatch(IrsTRItemFactData record, SqlMapClient sqlMap)throws SQLException{
		sqlMap = insertBatch(INSERT_INTO_ITEMFACT, record, sqlMap);
		return sqlMap;
    }
	
	public SqlMapClient upadateFTBatch(IrsTRItemFactData record, SqlMapClient sqlMap)throws SQLException{
		sqlMap = updateBatch(MERGE_FT_INTO_ITEMFACT, record, sqlMap);
		return sqlMap;
    }
	
	public SqlMapClient deleteBatch(IrsTRItemFactData record, SqlMapClient sqlMap)throws SQLException{
		if(record != null)
			sqlMap = deleteBatch(DELETE_BY_FACTID, record, sqlMap);
		return sqlMap;
    }
	
	public void executeItemBatch(SqlMapClient sqlClient) throws SQLException{
		executeBatchProcess(sqlClient);
	}
}