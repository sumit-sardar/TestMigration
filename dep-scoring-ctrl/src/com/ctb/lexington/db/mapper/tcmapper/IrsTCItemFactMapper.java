package com.ctb.lexington.db.mapper.tcmapper;

import com.ctb.lexington.db.irsdata.irstcdata.IrsTCItemFactData;

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.mapper.AbstractDBMapper;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author Rama_Rao
 *
 */

public class IrsTCItemFactMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_ITEM_STUDENT_SESSION = "TCfindByItemStudentSession";
	private static final String INSERT_INTO_TABEITEMFACT = "TCinsertTABEItemFact";
	private static final String UPDATE_BY_FACTID = "TCupdateTABEItemFact";
	private static final String DELETE_BY_FACTID = "TCdeleteByItemFactId";
    private static final String UPDATE_CURRENTRESULT_STATUS = "TCupdateTABEItemFactCurrentResult";
    
    public IrsTCItemFactMapper(Connection conn){
		super(conn);
	}
	
	public IrsTCItemFactData findByItemStudentSession(Long itemId, Long studentId, Long sessionId){
		IrsTCItemFactData template = new IrsTCItemFactData();
		template.setItemid(itemId);
		template.setStudentid(studentId);
		template.setSessionid(sessionId);
		return (IrsTCItemFactData) find(FIND_BY_ITEM_STUDENT_SESSION,template);    
    }
	
	public void insert(IrsTCItemFactData record)throws SQLException{
		insert(INSERT_INTO_TABEITEMFACT, record);
    }	
	
	public void update(IrsTCItemFactData record)throws SQLException{
		update(UPDATE_BY_FACTID, record);
    }
    
    public void updateCurrentResultStatus(IrsTCItemFactData record)throws SQLException{
        update(UPDATE_CURRENTRESULT_STATUS, record);
    }
	
	public void delete(IrsTCItemFactData record)throws SQLException{
		if(record != null)	
			delete(DELETE_BY_FACTID, record);	
    }
	public SqlMapClient insertBatch(IrsTCItemFactData record, SqlMapClient sqlMap)throws SQLException{
		sqlMap = insertBatch(INSERT_INTO_TABEITEMFACT, record, sqlMap);
		return sqlMap;
    }
	
	public SqlMapClient deleteBatch(IrsTCItemFactData record, SqlMapClient sqlMap)throws SQLException{
		if(record != null)
			sqlMap = deleteBatch(DELETE_BY_FACTID, record, sqlMap);
		return sqlMap;
    }
	
	public void executeItemBatch(SqlMapClient sqlClient) throws SQLException{
		executeBatchProcess(sqlClient);
	}
}